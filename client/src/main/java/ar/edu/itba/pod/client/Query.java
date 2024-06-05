package ar.edu.itba.pod.client;

import ar.edu.itba.pod.client.utils.Argument;
import ar.edu.itba.pod.models.StringLongPair;
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
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.mapreduce.Job;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.function.BiFunction;

@SuppressWarnings("deprecation")
public enum Query {
    ONE(1, "Infraction;Tickets") {
        @Override
        public void realizeMapReduce(Job<Long, Ticket> job, Argument arguments, HazelcastInstance hzInstance) throws ExecutionException, InterruptedException, IOException {
            Map<String, Integer> results = job
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
    }, TWO(2, "County;InfractionTop1;InfractionTop2;InfractionTop3") {
        @Override
        public void realizeMapReduce(Job<Long, Ticket> job, Argument arguments, HazelcastInstance hzInstance) throws ExecutionException, InterruptedException, IOException {
            Map<String, List<String>> results = job
                    .mapper(new TopInfractionsMapper())
                    .reducer(new TopInfractionsReducer())
                    .submit(new TopInfractionsCollator())
                    .get();

            Query.writeOutput(TWO.getFilePath(arguments.getOutPath()),
                    TWO.csvHeader,
                    results,
                    (key, value) -> {
                        StringBuilder sb = new StringBuilder().append(key);
                        value.forEach(s -> sb.append(";").append(s));
                        return sb.toString();
                    });
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
                    results,
                    (key, value) -> String.format("%s;%.2f%%", key, value)
            );
        }
    }, FOUR(4, "County;Plate;Tickets") {
        @Override
        public void realizeMapReduce(Job<Long, Ticket> job, Argument arguments, HazelcastInstance hzInstance) throws ExecutionException, InterruptedException, IOException {
            Map<String, StringLongPair> results = job
                    .keyPredicate(new PlatesMostInfractionsByCountyKeyPredicate(arguments.getFrom(), arguments.getTo()))
                    .mapper(new PlatesMostInfractionsByCountyMapper())
                    .combiner(new PlatesMostInfractionsByCountyCombinerFactory())
                    .reducer(new PlatesMostInfractionsByCountyReducerFactory())
                    .submit(new PlatesMostInfractionsByCountyCollator())
                    .get();

            Query.writeOutput(
                    FOUR.getFilePath(arguments.getOutPath()),
                    FOUR.csvHeader,
                    results,
                    (key, value) -> String.format("%s;%s;%d", key, value.getPlate(), value.getNum())
            );
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
            // TODO: Implement
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

    public void checkQueryArguments(Argument arguments, StringBuilder errors) {}

    private Path getFilePath(Path outPathDir) {
        return outPathDir.resolve(String.format("query%d.csv", num));
    }

    private static <K, V> void writeOutput(Path filePath, String csvHeader, Map<K, V> map, BiFunction<K, V, String> stringify) throws IOException {
        try (BufferedWriter writer = Files.newBufferedWriter(filePath, StandardOpenOption.WRITE, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING)) {
            writer.write(csvHeader);
            writer.newLine();
            for (Map.Entry<K, V> entry : map.entrySet()) {
                writer.write(stringify.apply(entry.getKey(), entry.getValue()));
                writer.newLine();
            }
        }
    }

    public int getNum() {
        return num;
    }
}
