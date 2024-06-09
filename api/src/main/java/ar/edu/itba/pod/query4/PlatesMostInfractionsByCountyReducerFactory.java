package ar.edu.itba.pod.query4;

import ar.edu.itba.pod.models.StringLongPair;
import com.hazelcast.mapreduce.Reducer;
import com.hazelcast.mapreduce.ReducerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("deprecation")
public class PlatesMostInfractionsByCountyReducerFactory implements ReducerFactory<String, List<StringLongPair>, StringLongPair> {

    @Override
    public Reducer<List<StringLongPair>, StringLongPair> newReducer(String s) {
        return new PlatesMostInfractionsByCountyReducer();
    }

    private static class PlatesMostInfractionsByCountyReducer extends Reducer<List<StringLongPair>, StringLongPair> {
        private final Map<String, Long> map = new HashMap<>();
        private static final Long ZERO = 0L;

        @Override
        public void reduce(List<StringLongPair> s) {
            for (StringLongPair pair : s) {
                map.put(pair.getPlate(), map.getOrDefault(pair.getPlate(), ZERO) + pair.getNum());
            }
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
