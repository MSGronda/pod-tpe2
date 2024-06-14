package ar.edu.itba.pod.query5;

import ar.edu.itba.pod.models.StringPair;
import ar.edu.itba.pod.models.abstractClasses.Infraction;
import ar.edu.itba.pod.utils.Constants;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import com.hazelcast.mapreduce.Collator;

import java.util.*;

@SuppressWarnings("deprecation")
public class GroupingByFineCollator implements Collator<Map.Entry<Integer, List<StringPair>>, Map<Integer, Set<StringPair>>> {
    private final transient IMap<String, Infraction> infractions;
    private static final Comparator<StringPair> cmp = Comparator.comparing(StringPair::getValue1).thenComparing(StringPair::getValue2);

    public GroupingByFineCollator(HazelcastInstance hazelcastInstance) {
        this.infractions = hazelcastInstance.getMap(Constants.INFRACTION_MAP);
    }

    @Override
    public Map<Integer, Set<StringPair>> collate(Iterable<Map.Entry<Integer, List<StringPair>>> iterable) {
        Map<Integer, Set<StringPair>> resp = new TreeMap<>(Comparator.reverseOrder());

        iterable.forEach(entry -> {
            Set<StringPair> infractionsCodes = new TreeSet<>(cmp);

            entry.getValue().forEach(sp -> {
                String s1 = infractions.get(sp.getValue1()).getDescription();
                String s2 = infractions.get(sp.getValue2()).getDescription();

                if (s1.compareTo(s2) < 0) {
                    infractionsCodes.add(new StringPair(s1, s2));
                } else {
                    infractionsCodes.add(new StringPair(s2, s1));
                }
            });
            if (!infractionsCodes.isEmpty()) {
                resp.put(entry.getKey(), infractionsCodes);
            }

        });


        return resp;
    }
}
