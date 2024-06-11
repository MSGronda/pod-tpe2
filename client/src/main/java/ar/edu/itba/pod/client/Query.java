package ar.edu.itba.pod.client;

import ar.edu.itba.pod.Constants;
import ar.edu.itba.pod.client.utils.Argument;
import ar.edu.itba.pod.models.CHITickets.*;
import ar.edu.itba.pod.models.NYCTickets.*;
import ar.edu.itba.pod.models.StringLongPair;
import ar.edu.itba.pod.models.StringPair;
import ar.edu.itba.pod.models.abstractClasses.Ticket;
import ar.edu.itba.pod.query1.TotalInfractionsCollator;
import ar.edu.itba.pod.query1.TotalInfractionsMapper;
import ar.edu.itba.pod.query1.TotalInfractionsReducer;
import ar.edu.itba.pod.query2.TopInfractionsCollator;
import ar.edu.itba.pod.query2.TopInfractionsMapper;
import ar.edu.itba.pod.query2.TopInfractionsReducer;
import ar.edu.itba.pod.query3.AgencyCollectionCollator;
import ar.edu.itba.pod.query3.AgencyCollectionMapper;
import ar.edu.itba.pod.query3.AgencyCollectionReducer;
import ar.edu.itba.pod.query4.*;
import ar.edu.itba.pod.query5.*;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import com.hazelcast.mapreduce.Job;
import com.hazelcast.mapreduce.JobTracker;
import com.hazelcast.mapreduce.KeyValueSource;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.function.BiFunction;

@SuppressWarnings("deprecation")
public enum Query {
    ONE(1, "Infraction;Tickets") {
        @Override
        public void realizeMapReduce(Job<Long, Ticket> job, Argument arguments, HazelcastInstance hzInstance) throws ExecutionException, InterruptedException, IOException {
            Set<Map.Entry<String, Integer>> results = job
                    .mapper(new TotalInfractionsMapper())
                    .reducer(new TotalInfractionsReducer())
                    .submit(new TotalInfractionsCollator(hzInstance))
                    .get();

            Query.writeOutput(
                    ONE.getFilePath(arguments.getOutPath()),
                    ONE.csvHeader,
                    results,
                    (key, value) -> String.format("%s;%s", key, value)
            );
        }

        @Override
        public Ticket getCHITicket(LocalDateTime issueDate, String licensePlateNumber, String violationCode, String unitDescription, int fine, String communityArea) {
            return new TicketCHIQuery1(violationCode);
        }

        @Override
        public Ticket getNYCTicket(String plate, LocalDate issueDate, int infractionCode, float fineAmount, String countyName, String issuingAgency) {
            return new TicketNYCQuery1(infractionCode);
        }
    }, TWO(2, "County;InfractionTop1;InfractionTop2;InfractionTop3") {
        @Override
        public void realizeMapReduce(Job<Long, Ticket> job, Argument arguments, HazelcastInstance hzInstance) throws ExecutionException, InterruptedException, IOException {
            Map<String, List<String>> results = job
                    .mapper(new TopInfractionsMapper())
                    .reducer(new TopInfractionsReducer())
                    .submit(new TopInfractionsCollator(hzInstance))
                    .get();

            Query.writeOutput(TWO.getFilePath(arguments.getOutPath()),
                    TWO.csvHeader,
                    results.entrySet(),
                    (key, value) -> {
                        StringBuilder sb = new StringBuilder().append(key);
                        value.forEach(s -> sb.append(";").append(s));
                        return sb.toString();
                    });
        }

        @Override
        public Ticket getCHITicket(LocalDateTime issueDate, String licensePlateNumber, String violationCode, String unitDescription, int fine, String communityArea) {
            return new TicketCHIQuery2(violationCode, communityArea);
        }

        @Override
        public Ticket getNYCTicket(String plate, LocalDate issueDate, int infractionCode, float fineAmount, String countyName, String issuingAgency) {
            return new TicketNYCQuery2(infractionCode, countyName);
        }
    }, THREE(3, "Issuing Agency;Percentage") {
        @Override
        public void realizeMapReduce(Job<Long, Ticket> job, Argument arguments, HazelcastInstance hzInstance) throws ExecutionException, InterruptedException, IOException {
            Map<String, Double> results = job
                    .mapper(new AgencyCollectionMapper())
                    .reducer(new AgencyCollectionReducer())
                    .submit(new AgencyCollectionCollator(arguments.getN()))
                    .get();

            Query.writeOutput(THREE.getFilePath(arguments.getOutPath()),
                    THREE.csvHeader,
                    results.entrySet(),
                    (key, value) -> String.format("%s;%.2f%%", key, value)
            );
        }

        @Override
        public Ticket getCHITicket(LocalDateTime issueDate, String licensePlateNumber, String violationCode, String unitDescription, int fine, String communityArea) {
            return new TicketCHIQuery3(unitDescription, fine);
        }

        @Override
        public Ticket getNYCTicket(String plate, LocalDate issueDate, int infractionCode, float fineAmount, String countyName, String issuingAgency) {
            return new TicketNYCQuery3(fineAmount, issuingAgency);
        }
    }, FOUR(4, "County;Plate;Tickets") {
        @Override
        public void realizeMapReduce(Job<Long, Ticket> job, Argument arguments, HazelcastInstance hzInstance) throws ExecutionException, InterruptedException, IOException {
            Map<String, StringLongPair> results = job
                    .keyPredicate(new PlatesMostInfractionsByCountyKeyPredicate(arguments.getFrom(), arguments.getTo()))
                    .mapper(new PlatesMostInfractionsByCountyMapper())
                    .combiner(new PlatesMostInfractionsByCountyCombinerFactory())
                    .reducer(new PlatesMostInfractionsByCountyReducerFactory())
                    // Testing on only one node, no combiner is faster
//                    .reducer(new PlatesMostInfractionsByCountyNoCombinerReducerFactory())
                    .submit(new PlatesMostInfractionsByCountyCollator())
                    .get();

            Query.writeOutput(
                    FOUR.getFilePath(arguments.getOutPath()),
                    FOUR.csvHeader,
                    results.entrySet(),
                    (key, value) -> String.format("%s;%s;%d", key, value.getPlate(), value.getNum())
            );
        }

        @Override
        public Ticket getCHITicket(LocalDateTime issueDate, String licensePlateNumber, String violationCode, String unitDescription, int fine, String communityArea) {
            return new TicketCHIQuery4(licensePlateNumber, communityArea);
        }

        @Override
        public Ticket getNYCTicket(String plate, LocalDate issueDate, int infractionCode, float fineAmount, String countyName, String issuingAgency) {
            return new TicketNYCQuery4(plate, countyName);
        }


        @Override
        public void checkQueryArguments(Argument arguments, StringBuilder errors) {
            super.checkQueryArguments(arguments, errors);

            if (arguments.getFrom() == null) {
                errors.append("-Dfrom=DD/MM/YYYY is a required parameter for this query\n");
            }

            if (arguments.getTo() == null) {
                errors.append("-Dto=DD/MM/YYYY is a required parameter for this query\n");
            }
        }
    }, FIVE(5, "Group;Infraction A;Infraction B") {
        @Override
        public void realizeMapReduce(Job<Long, Ticket> job, Argument arguments, HazelcastInstance hzInstance) throws ExecutionException, InterruptedException, IOException {
            Map<String, Float> partialResults = job
                    .mapper(new AverageFineMapper())
                    .reducer(new AverageFineReducer())
                    .submit()
                    .get();

            IMap<String, Float> partialResultMap = hzInstance.getMap("query5-partial-result");
            partialResultMap.putAll(partialResults);

            JobTracker jobTracker = hzInstance.getJobTracker(Constants.HZ_NAMESPACE);
            KeyValueSource<String, Float> source = KeyValueSource.fromMap(partialResultMap);
            Job<String, Float> job2 = jobTracker.newJob(source);


            Map<Integer, Set<StringPair>> results = job2
                    .mapper(new GroupingByFineMapper())
                    .reducer(new GroupingByFineReducer())
                    .submit(new GroupingByFineCollator(hzInstance))
                    .get();


            Query.writeOutput(
                    FIVE.getFilePath(arguments.getOutPath()),
                    FIVE.csvHeader,
                    results.entrySet(),
                    (key, set) -> {
                        StringBuilder sb = new StringBuilder();
                        Iterator<StringPair> iterator = set.iterator();
                        while (iterator.hasNext()){
                            StringPair sp = iterator.next();
                            sb.append(key).append(';').append(sp.getValue1()).append(';').append(sp.getValue2());
                            if(iterator.hasNext()){
                                sb.append('\n');
                            }
                        }
                        return sb.toString();
                    }
            );
        }

        @Override
        public Ticket getCHITicket(LocalDateTime issueDate, String licensePlateNumber, String violationCode, String unitDescription, int fine, String communityArea) {
            return new TicketCHIQuery5(violationCode, fine);
        }

        @Override
        public Ticket getNYCTicket(String plate, LocalDate issueDate, int infractionCode, float fineAmount, String countyName, String issuingAgency) {
            return new TicketNYCQuery5(infractionCode, fineAmount);
        }

    };

    private final int num;
    private final String csvHeader;

    Query(int num, String csvHeader) {
        this.num = num;
        this.csvHeader = csvHeader;
    }

    public static Query fromNum(int value) {
        for (Query q : values()) {
            if (q.num == value)
                return q;
        }
        throw new IllegalArgumentException();
    }

    public abstract void realizeMapReduce(Job<Long, Ticket> job, Argument arguments, HazelcastInstance hzInstance) throws ExecutionException, InterruptedException, IOException;

    public abstract Ticket getCHITicket(LocalDateTime issueDate, String licensePlateNumber, String violationCode, String unitDescription, int fine, String communityArea);
    public abstract Ticket getNYCTicket(String plate, LocalDate issueDate, int infractionCode, float fineAmount, String countyName, String issuingAgency);

    public void checkQueryArguments(Argument arguments, StringBuilder errors) {}

    private Path getFilePath(Path outPathDir) {
        return outPathDir.resolve(String.format("query%d.csv", num));
    }

    private static <K, V> void writeOutput(Path filePath, String csvHeader, Set<Map.Entry<K, V>> set, BiFunction<K, V, String> stringify) throws IOException {
        try (BufferedWriter writer = Files.newBufferedWriter(filePath, StandardOpenOption.WRITE, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING)) {
            writer.write(csvHeader);
            writer.newLine();
            for (Map.Entry<K, V> entry : set) {
                writer.write(stringify.apply(entry.getKey(), entry.getValue()));
                writer.newLine();
            }
        }
    }

    public int getNum() {
        return num;
    }
}
