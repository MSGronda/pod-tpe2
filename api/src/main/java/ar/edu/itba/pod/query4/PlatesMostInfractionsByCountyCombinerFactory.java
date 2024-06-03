package ar.edu.itba.pod.query4;

import ar.edu.itba.pod.models.StringLongPair;
import com.hazelcast.mapreduce.Combiner;
import com.hazelcast.mapreduce.CombinerFactory;

import java.util.HashMap;
import java.util.Map;

public class PlatesMostInfractionsByCountyCombinerFactory implements CombinerFactory<String, String, StringLongPair> {
    @Override
    public Combiner<String, StringLongPair> newCombiner(String s) {
        return new PlatesMostInfractionsByCountyCombiner();
    }

    private static class PlatesMostInfractionsByCountyCombiner extends Combiner<String, StringLongPair> {
        private final Map<String, Long> map = new HashMap<>();
        private static final Long ZERO = 0L;

        @Override
        public void combine(String s) {
            map.put(s, map.getOrDefault(s, ZERO) + 1);
        }

        @Override
        public StringLongPair finalizeChunk() {
            long max = 0;
            String maxPlate = null;
            for (Map.Entry<String, Long> entry : map.entrySet()) {
                if (entry.getValue() > max) {
                    max = entry.getValue();
                    maxPlate = entry.getKey();
                }
            }
            map.clear();
            return StringLongPair.of(maxPlate, max);
        }
    }
}


