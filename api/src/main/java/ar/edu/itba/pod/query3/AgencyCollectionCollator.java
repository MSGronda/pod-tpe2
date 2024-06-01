package ar.edu.itba.pod.query3;

import com.hazelcast.mapreduce.Collator;

import java.util.HashMap;
import java.util.Map;

public class AgencyCollectionCollator implements Collator<Map.Entry<String, Long>, Map<String, Double>> {
    @Override
    public Map<String, Double> collate(Iterable<Map.Entry<String, Long>> iterable) {
        long total = 0;
        Map<String, Double> toRet = new HashMap<>();
        for (Map.Entry<String, Long> entry : iterable) {
            total += entry.getValue();
        }
        for (Map.Entry<String, Long> entry : iterable) {
            double percentage = (double) entry.getValue() / total * 100;
            percentage = Math.floor(percentage * 100) / 100; // Truncar a 2 decimales
            toRet.put(entry.getKey(), percentage);
        }
        return toRet;
    }
}
