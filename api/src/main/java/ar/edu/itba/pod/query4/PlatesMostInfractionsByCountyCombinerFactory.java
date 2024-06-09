package ar.edu.itba.pod.query4;

import ar.edu.itba.pod.models.StringLongPair;
import com.hazelcast.mapreduce.Combiner;
import com.hazelcast.mapreduce.CombinerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("deprecation")
public class PlatesMostInfractionsByCountyCombinerFactory implements CombinerFactory<String, String, List<StringLongPair>> {
    @Override
    public Combiner<String, List<StringLongPair>> newCombiner(String s) {
        return new PlatesMostInfractionsByCountyCombiner();
    }

    private static class PlatesMostInfractionsByCountyCombiner extends Combiner<String, List<StringLongPair>> {
        private final Map<String, Long> map = new HashMap<>();
        private static final Long ZERO = 0L;

        @Override
        public void combine(String s) {
            map.put(s, map.getOrDefault(s, ZERO) + 1);
        }

        @Override
        public List<StringLongPair> finalizeChunk() {
            final List<StringLongPair> list = map.entrySet().stream().map(x -> StringLongPair.of(x.getKey(), x.getValue())).toList();
            map.clear();
            return list;
        }
    }
}


