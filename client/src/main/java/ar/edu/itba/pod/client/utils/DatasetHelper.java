package ar.edu.itba.pod.client.utils;

import ar.edu.itba.pod.client.Query;
import ar.edu.itba.pod.models.InfractionCHI;
import ar.edu.itba.pod.models.InfractionNYC;
import ar.edu.itba.pod.models.abstractClasses.Infraction;
import ar.edu.itba.pod.models.abstractClasses.Ticket;
import com.hazelcast.core.IMap;
import com.hazelcast.core.MultiMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.function.Consumer;
import java.util.stream.Stream;

public class DatasetHelper {
    private static final int SKIP_CSV_LINES = 1;
    private static final int NUM_FIELDS_TICKET = 6;
    private static final int NUM_FIELDS_INFRACTION = 2;
    private static final Logger logger = LoggerFactory.getLogger(DatasetHelper.class);

    public static void loadNYCData(
            String infractionsPath,
            IMap<String, Infraction> infractions,
            CsvReaderType infractionsReaderType,
            String ticketsPath,
            MultiMap<Long, Ticket> tickets,
            CsvReaderType ticketReaderType,
            Query query
    ){
        infractionsReaderType.reader.readCsv(infractionsPath, line -> {
            String[] fields = line.split(";");

            if(fields.length != NUM_FIELDS_INFRACTION){ return; }

            infractions.put(
                fields[0],
                new InfractionNYC(Integer.parseInt(fields[0]), fields[1])
            );
        });

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");


        ticketReaderType.reader.readCsv(ticketsPath, line -> {

            String[] fields = line.split(";");

            if(fields.length != NUM_FIELDS_TICKET){ return; }

            LocalDate date = LocalDate.parse(fields[1], dateFormatter);

            tickets.put(
                    date.toEpochSecond(LocalTime.MIDNIGHT, ZoneOffset.UTC),
                    query.getNYCTicket(
                        fields[0],
                        date,
                        Integer.parseInt(fields[2]),
                        Float.parseFloat(fields[3]),
                        fields[4],
                        fields[5]
                    )
            );
        });
    }

    public static void loadCHIData(
            String infractionsPath,
            IMap<String, Infraction> infractions,
            CsvReaderType infractionsReaderType,
            String ticketsPath,
            MultiMap<Long, Ticket> tickets,
            CsvReaderType ticketReaderType,
            Query query
    ){
        infractionsReaderType.reader.readCsv(infractionsPath, line -> {
            String[] fields = line.split(";");

            if(fields.length != NUM_FIELDS_INFRACTION){ return; }

            infractions.put(
                    fields[0],
                    new InfractionCHI(fields[0], fields[1])
            );
        });

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        ticketReaderType.reader.readCsv(ticketsPath, line -> {
            String[] fields = line.split(";");

            if(fields.length != NUM_FIELDS_TICKET){ return; }

            LocalDateTime date = LocalDateTime.parse(fields[0], dateFormatter);
            tickets.put(
                    date.toEpochSecond(ZoneOffset.UTC),
                    query.getCHITicket(
                        date,
                        fields[1],
                        fields[2],
                        fields[3],
                        Integer.parseInt(fields[4]),
                        fields[5]
                    )
            );
        });
    }

    public enum CsvReaderType {
        PARALLEL(parallelReader),
        SEQUENTIAL(sequentialReader);

        CsvReaderType(CsvReader reader){
            this.reader = reader;
        }
        private final CsvReader reader;
    }
    private interface CsvReader {
        void readCsv(String filepath, Consumer<String> consumer);
    }

    private static final CsvReader parallelReader = (filepath, consumer) -> {
        try (Stream<String> reader = Files.lines(Path.of(filepath))) {
            reader.skip(SKIP_CSV_LINES).parallel().forEach(consumer);
        } catch (IOException e) {
            logger.error("Error reading file", e);
        }
    };

    private static final CsvReader sequentialReader = (filepath, consumer) -> {
        try (Stream<String> reader = Files.lines(Path.of(filepath)) ){
            reader.skip(SKIP_CSV_LINES).forEach(consumer);
        } catch (IOException e) {
            logger.error("Error reading file", e);
        }
    };

}
