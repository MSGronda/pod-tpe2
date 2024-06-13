package ar.edu.itba.pod.query1;

import ar.edu.itba.pod.utils.Constants;
import ar.edu.itba.pod.models.abstractClasses.Infraction;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import com.hazelcast.mapreduce.Collator;

import java.util.*;

@SuppressWarnings("deprecation")
public class TotalInfractionsCollator implements Collator<Map.Entry<String, Integer>, Set<Map.Entry<String, Integer>>>  {

    private final transient IMap<String, Infraction> infractions;

    private static final Comparator<Map.Entry<String, Integer>> cmp = Comparator.comparingInt((Map.Entry<String, Integer> e) -> e.getValue()).reversed().thenComparing(Map.Entry::getKey);

    public TotalInfractionsCollator(HazelcastInstance hazelcastInstance){
        this.infractions = hazelcastInstance.getMap(Constants.INFRACTION_MAP);
    }

    @Override
    public Set<Map.Entry<String, Integer>> collate(Iterable<Map.Entry<String, Integer>> iterable) {
        Set<Map.Entry<String, Integer>> resp = new TreeSet<>(cmp);

        iterable.forEach(entry -> resp.add(new AbstractMap.SimpleEntry<>(infractions.get(entry.getKey()).getDescription(), entry.getValue())));

        return resp;
    }
}
