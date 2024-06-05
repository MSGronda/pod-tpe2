package ar.edu.itba.pod.query2;


import com.hazelcast.mapreduce.Collator;
import java.util.List;
import java.util.Map;

public class TopInfractionsCollator implements Collator<Map.Entry<String, List<String>>, Map<String, List<String>>> {
    @Override
    public Map<String, List<String>> collate(Iterable<Map.Entry<String, List<String>>> values) {
        // map sorted by keys
        Map<String, List<String>> map = new java.util.TreeMap<>();
        for (Map.Entry<String, List<String>> entry : values) {
            map.put(entry.getKey(), entry.getValue());
        }
        return map;
    }
}


