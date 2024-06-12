package ar.edu.itba.pod.query3;

import com.hazelcast.mapreduce.Collator;
import com.sun.source.tree.Tree;

import java.util.*;
import java.util.stream.Collectors;

@SuppressWarnings("deprecation")
public class AgencyCollectionCollator implements Collator<Map.Entry<String, Long>, Set<Map.Entry<String, Double>>> {
    private final int n;

    public static final Comparator<Map.Entry<String, Double>> CMP = Comparator.comparingDouble((Map.Entry<String, Double> e) -> e.getValue()).reversed().thenComparing(Map.Entry::getKey);
    public AgencyCollectionCollator(final int n) {
        this.n = n;
    }

    @Override
    public Set<Map.Entry<String, Double>> collate(Iterable<Map.Entry<String, Long>> iterable) {
        long total = 0;

        Set<Map.Entry<String, Double>> result = new TreeSet<>(CMP);

        for (Map.Entry<String, Long> entry : iterable) {
            total += entry.getValue();
        }

        for (Map.Entry<String, Long> entry : iterable) {
            double percentage = (double) entry.getValue() / total * 100;
            percentage = Math.floor(percentage * 100) / 100; // Truncar a 2 decimales
            result.add(new AbstractMap.SimpleEntry<>(entry.getKey(), percentage));
        }

        return result.stream().limit(n).collect(Collectors.toCollection(() -> new TreeSet<>(CMP)));
    }
}
