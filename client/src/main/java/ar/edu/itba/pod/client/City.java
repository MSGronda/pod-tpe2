package ar.edu.itba.pod.client;

import ar.edu.itba.pod.client.utils.Argument;
import ar.edu.itba.pod.client.utils.Constants;
import ar.edu.itba.pod.models.InfractionCHI;
import ar.edu.itba.pod.models.InfractionNYC;
import ar.edu.itba.pod.models.TicketCHI;
import ar.edu.itba.pod.models.TicketNYC;
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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Stream;

public enum City {
    CHI {
        @Override
        public void loadData(Argument arguments,
                             IMap<String, Infraction> infractions,
                             MultiMap<Long, Ticket> tickets) {
            City.loadDataSkeleton(() -> {
                try (Stream<String> lines = Files.lines(getInfractionsPath(arguments.getInPath())).skip(1).parallel()) {
                    lines.forEach(line -> {
                        String[] fields = line.split(";");
                        if (fields.length != NUM_FIELDS_INFRACTION) {
                            return;
                        }

                        infractions.put(fields[0], new InfractionCHI(fields[0], fields[1]));
                    });
                } catch (IOException e) {
                    logger.error("Error loading data", e);
                }
            }, () -> {
                try (Stream<String> lines = Files.lines(getTicketsPath(arguments.getInPath())).skip(1).parallel()) {
                    lines.forEach(line -> {
                        String[] fields = line.split(";");
                        if (fields.length != NUM_FIELDS_TICKET) {
                            return;
                        }

                        LocalDateTime date = LocalDateTime.parse(fields[0], ticketDateFormatterCHI);
                        tickets.put(
                                date.toEpochSecond(ZoneOffset.UTC),
                                new TicketCHI(date, fields[1], fields[2], fields[3], Integer.parseInt(fields[4]), fields[5])
                        );
                    });
                } catch (IOException e) {
                    logger.error("Error loading data", e);
                }
            });
        }
    }, NYC {
        @Override
        public void loadData(Argument arguments, IMap<String, Infraction> infractions, MultiMap<Long, Ticket> tickets) {
            City.loadDataSkeleton(() -> {
                try (Stream<String> lines = Files.lines(getInfractionsPath(arguments.getInPath())).skip(1).parallel()) {
                    lines.forEach(line -> {
                        String[] fields = line.split(";");

                        if (fields.length != NUM_FIELDS_INFRACTION) {
                            return;
                        }

                        infractions.put(fields[0], new InfractionNYC(Integer.parseInt(fields[0]), fields[1]));
                    });
                } catch (IOException e) {
                    logger.error("Error loading data", e);
                }
            }, () -> {
                try (Stream<String> lines = Files.lines(getTicketsPath(arguments.getInPath())).skip(1).parallel()) {
                    lines.forEach(line -> {
                        String[] fields = line.split(";");

                        if (fields.length != NUM_FIELDS_TICKET) {
                            return;
                        }

                        LocalDate date = LocalDate.parse(fields[1], ticketDateFormatterNYC);

                        tickets.put(date.toEpochSecond(LocalTime.MIDNIGHT, ZoneOffset.UTC),
                                new TicketNYC(fields[0],
                                        date,
                                        Integer.parseInt(fields[2]),
                                        Float.parseFloat(fields[3]),
                                        fields[4],
                                        fields[5])
                        );
                    });
                } catch (IOException e) {
                    logger.error("Error loading data", e);
                }
            });
        }
    };

    private static final DateTimeFormatter ticketDateFormatterNYC = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter ticketDateFormatterCHI = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final Logger logger = LoggerFactory.getLogger(City.class);
    private static final int NUM_FIELDS_TICKET = 6;
    private static final int NUM_FIELDS_INFRACTION = 2;


    public Path getInfractionsPath(Path path) {
        return path.resolve(String.format(Constants.INFRACTIONS_FILENAME_FMT, this.name()));
    }

    public Path getTicketsPath(Path path) {
        return path.resolve(String.format(Constants.TICKETS_FILENAME_FMT, this.name()));
    }

    public abstract void loadData(Argument arguments, IMap<String, Infraction> infractions, MultiMap<Long, Ticket> tickets);

    private static void loadDataSkeleton(Runnable infractionsTask, Runnable ticketsTask) {
        // We will use an executor to upload both csv files at the same time
        try {
            ExecutorService service = Executors.newCachedThreadPool();
            service.submit(infractionsTask);
            service.submit(ticketsTask);

            service.shutdown();
            service.awaitTermination(Constants.TIMEOUT_AWAIT, Constants.TIMEOUT_AWAIT_UNIT); // TODO: Check awaitTermination output to decide if all data was loaded
        } catch (InterruptedException e) {
            logger.error("Interrupted while loading data", e);
        }
    }
}
