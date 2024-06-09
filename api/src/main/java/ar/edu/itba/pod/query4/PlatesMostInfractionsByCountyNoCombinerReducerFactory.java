package ar.edu.itba.pod.query4;

import ar.edu.itba.pod.models.StringLongPair;
import com.hazelcast.mapreduce.Reducer;
import com.hazelcast.mapreduce.ReducerFactory;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("deprecation")
public class PlatesMostInfractionsByCountyNoCombinerReducerFactory implements ReducerFactory<String, String, StringLongPair> {
    @Override
    public Reducer<String, StringLongPair> newReducer(String s) {
        return new PlatesMostInfractionsByCountyReducer();
    }

    private static class PlatesMostInfractionsByCountyReducer extends Reducer<String, StringLongPair> {
        private static final Long ZERO = 0L;
        private final Map<String, Long> map = new HashMap<>();

        @Override
        public void reduce(String s) {
            map.put(s, map.getOrDefault(s, ZERO) + 1);
        }

        @Override
        public StringLongPair finalizeReduce() {
            long max = 0;
            String maxPlate = null;
            for (Map.Entry<String, Long> entry : map.entrySet()) {
                if (entry.getValue() > max) {
                    max = entry.getValue();
                    maxPlate = entry.getKey();
                }
            }
            return StringLongPair.of(maxPlate, max);
        }
    }
}
