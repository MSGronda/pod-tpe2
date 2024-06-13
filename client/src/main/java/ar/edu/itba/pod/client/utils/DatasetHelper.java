package ar.edu.itba.pod.client.utils;

import ar.edu.itba.pod.client.Query;
import ar.edu.itba.pod.models.InfractionCHI;
import ar.edu.itba.pod.models.InfractionNYC;
import ar.edu.itba.pod.models.abstractClasses.Infraction;
import ar.edu.itba.pod.models.abstractClasses.Ticket;
import com.hazelcast.core.IMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Consumer;
import java.util.stream.Stream;

public class DatasetHelper {
    private static final Logger logger = LoggerFactory.getLogger(DatasetHelper.class);
    private static final int SKIP_CSV_LINES = 1;
    private static final int NUM_FIELDS_TICKET = 6;
    private static final int NUM_FIELDS_INFRACTION = 2;
    private static final int BATCH_SIZE = 100000;
    public static final DateTimeFormatter nycFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    public static final DateTimeFormatter chiFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static void loadTicketBatched(
            Path infractionsPath,
            IMap<String, Infraction> infractions,
            InfractionCreator infractionCreator,
            Path ticketsPath,
            IMap<Long, Ticket> tickets,
            Query query,
            TicketCreator ticketCreator,
            DateTimeFormatter dateFormatter
    ){
        // Lo usamos para filtrar los tickets que no tienen infraction asociado.
        // Usamos una coleccion local para no tener que acceder al IMap del cluster (lo
        // cual lo haria muy lento). Considerando que el localInfractions y infractions IMap
        // van a tener los mismos datos, no hace ninguna diferencia en terminos de consistencia.
        Map<String, Infraction> localInfractions = new HashMap<>();

        // Cargamos los infractions
        sequentialReader.readCsv(infractionsPath, line -> {
            String[] fields = line.split(";");

            if(fields.length != NUM_FIELDS_INFRACTION){ return; }

            localInfractions.put(fields[0], infractionCreator.createInfraction(fields));
        });

        // Mandamos todos al cluster
        infractions.putAll(localInfractions);

        HashMap<Long, Ticket> batch = new HashMap<>();
        AtomicLong id = new AtomicLong(0);

        sequentialReader.readCsv(ticketsPath, line -> {

            String[] fields = line.split(";");

            // Si la linea del csv esta mal o no tiene un infractionCode valido
            if (fields.length != NUM_FIELDS_TICKET || !localInfractions.containsKey(fields[2])) {
                return;
            }

            batch.put(
                    id.getAndIncrement(),
                    ticketCreator.createTicket(fields, query, dateFormatter)
            );

            // Enviamos el batch de tickets al cluster
            if (batch.size() == BATCH_SIZE) {
                tickets.putAll(batch);
                batch.clear();
            }
        });

    }

    public interface InfractionCreator{
        Infraction createInfraction(String[] fields);
    }
    public static final InfractionCreator nycInfractionCreator = (fields) -> new InfractionNYC(Integer.parseInt(fields[0]), fields[1]);
    public static final InfractionCreator chiInfractionCreator = (fields) -> new InfractionCHI(fields[0], fields[1]);


    public interface TicketCreator{
        Ticket createTicket(String[] fields, Query query, DateTimeFormatter dateFormatter);
    }
    public static final TicketCreator nycTicketCreator = (fields, query, dateFormatter) ->
            query.getNYCTicket(
                fields[0],
                LocalDate.parse(fields[1], dateFormatter),
                Integer.parseInt(fields[2]),
                Float.parseFloat(fields[3]),
                fields[4],
                fields[5]
    );

    public static final TicketCreator chiTicketCreator = (fields, query, dateFormatter) ->
            query.getCHITicket(
                    LocalDateTime.parse(fields[0], dateFormatter),
                    fields[1],
                    fields[2],
                    fields[3],
                    Integer.parseInt(fields[4]),
                    fields[5]
    );

    private interface CsvReader {
        void readCsv(Path filepath, Consumer<String> consumer);
    }
    private static final CsvReader sequentialReader = (filepath, consumer) -> {
        try (Stream<String> reader = Files.lines(filepath) ){
            reader.skip(SKIP_CSV_LINES).forEach(consumer);
        } catch (IOException e) {
            logger.error("Error reading file", e);
        }
    };

}
