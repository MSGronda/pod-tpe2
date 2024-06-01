package ar.edu.itba.pod.query3;

import com.hazelcast.mapreduce.Collator;

import java.util.*;

public class AgencyCollectionCollator implements Collator<Map.Entry<String, Long>, Map<String, Double>> {
    @Override
    public Map<String, Double> collate(Iterable<Map.Entry<String, Long>> iterable) {
        long total = 0;
        Map<String, Double> map = new HashMap<>();
        for (Map.Entry<String, Long> entry : iterable) {
            total += entry.getValue();
        }
        for (Map.Entry<String, Long> entry : iterable) {
            double percentage = (double) entry.getValue() / total * 100;
            percentage = Math.floor(percentage * 100) / 100; // Truncar a 2 decimales
            map.put(entry.getKey(), percentage);
        }

        // Se lo ordena por valor
        List<Map.Entry<String, Double>> list = new ArrayList<>(map.entrySet());

        list.sort(new Comparator<Map.Entry<String, Double>>() {
            @Override
            public int compare(Map.Entry<String, Double> entry1, Map.Entry<String, Double> entry2) {
                int comparison = entry2.getValue().compareTo(entry1.getValue());
                if (comparison != 0) {
                    return comparison;
                } else {
                    return entry1.getKey().compareTo(entry2.getKey());
                }
            }
        });

        Map<String, Double> sortedMap = new LinkedHashMap<>();
        for (Map.Entry<String, Double> entry : list) {
            sortedMap.put(entry.getKey(), entry.getValue());
        }
        return sortedMap;
    }
}
