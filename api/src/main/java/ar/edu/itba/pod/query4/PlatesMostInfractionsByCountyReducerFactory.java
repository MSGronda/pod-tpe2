package ar.edu.itba.pod.query4;

import ar.edu.itba.pod.models.StringLongPair;
import com.hazelcast.mapreduce.Reducer;
import com.hazelcast.mapreduce.ReducerFactory;

import java.util.HashMap;
import java.util.Map;

public class PlatesMostInfractionsByCountyReducerFactory implements ReducerFactory<String, StringLongPair, StringLongPair> {

    @Override
    public Reducer<StringLongPair, StringLongPair> newReducer(String s) {
        return new PlatesMostInfractionsByCountyReducer();
    }

    private static class PlatesMostInfractionsByCountyReducer extends Reducer<StringLongPair, StringLongPair> {
        private final Map<String, Long> map = new HashMap<>();
        private static final Long ZERO = 0L;

        @Override
        public void reduce(StringLongPair s) {
            map.put(s.getPlate(), map.getOrDefault(s.getPlate(), ZERO) + s.getNum());
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
