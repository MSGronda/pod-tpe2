package ar.edu.itba.pod.query4;

import ar.edu.itba.pod.models.StringLongPair;
import com.hazelcast.mapreduce.Collator;

import java.util.Map;

public class PlatesMostInfractionsByCountyCollator implements Collator<Map.Entry<String, StringLongPair>, Map<String, StringLongPair>> {
    @Override
    public Map<String, StringLongPair> collate(Iterable<Map.Entry<String, StringLongPair>> values) {
        // map sorted by keys
        Map<String, StringLongPair> map = new java.util.TreeMap<>();
        for (Map.Entry<String, StringLongPair> entry : values) {
            map.put(entry.getKey(), entry.getValue());
        }
        return map;
    }
}
