package ar.edu.itba.pod.client.utils;

import ar.edu.itba.pod.models.InfractionCHI;
import ar.edu.itba.pod.models.InfractionNYC;
import ar.edu.itba.pod.models.TicketCHI;
import ar.edu.itba.pod.models.TicketNYC;
import ar.edu.itba.pod.models.abstractClasses.Infraction;
import ar.edu.itba.pod.models.abstractClasses.Ticket;
import com.hazelcast.core.IList;
import com.hazelcast.core.IMap;
import com.hazelcast.core.MultiMap;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.UUID;
import java.util.function.Consumer;

public class DatasetHelper {
    private static final int SKIP_CSV_LINES = 1;

    public static void loadNYCData(
            String infractionsPath,
            IMap<String, Infraction> infractions,
            CsvReaderType infractionsReaderType,
            String ticketsPath,
            MultiMap<Long, Ticket> tickets,
            CsvReaderType ticketReaderType
    ){
        infractionsReaderType.reader.readCsv(infractionsPath, line -> {
            String[] fields = line.split(";");
            infractions.put(
                fields[0],
                new InfractionNYC(Integer.parseInt(fields[0]), fields[1])
            );
        });

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");


        ticketReaderType.reader.readCsv(ticketsPath, line -> {

            String[] fields = line.split(";");

            LocalDate date = LocalDate.parse(fields[1], dateFormatter);

            tickets.put(
                    date.toEpochDay(),
                    new TicketNYC(
                        fields[0],
                        date,
                        Integer.parseInt(fields[2]),
                        Float.parseFloat(fields[3]),
                        fields[4],
                        fields[5])
            );
        });
    }

    public static void loadCHIData(
            String infractionsPath,
           IMap<String, Infraction> infractions,
           CsvReaderType infractionsReaderType,
           String ticketsPath,
            MultiMap<Long, Ticket> tickets,
           CsvReaderType ticketReaderType
    ){
        infractionsReaderType.reader.readCsv(infractionsPath, line -> {
            String[] fields = line.split(";");
            infractions.put(
                    fields[0],
                    new InfractionCHI(fields[0], fields[1])
            );
        });

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        ticketReaderType.reader.readCsv(ticketsPath, line -> {
            String[] fields = line.split(";");

            LocalDateTime date = LocalDateTime.parse(fields[0], dateFormatter);

            tickets.put(
                    date.toEpochSecond(ZoneOffset.UTC),
                    new TicketCHI(
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
        try (BufferedReader reader = new BufferedReader(new FileReader(filepath))) {
            reader.lines().skip(SKIP_CSV_LINES).parallel().forEach(consumer);
        } catch (IOException e) {
            System.out.println(e); //TODO: handle properly
        }
    };

    private static final CsvReader sequentialReader = (filepath, consumer) -> {
        try (BufferedReader reader = new BufferedReader(new FileReader(filepath))) {
            reader.lines().skip(SKIP_CSV_LINES).forEach(consumer);
        } catch (IOException e) {
            System.out.println(e); //TODO: handle properly
        }
    };



}
