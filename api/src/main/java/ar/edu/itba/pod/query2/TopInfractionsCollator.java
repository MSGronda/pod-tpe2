package ar.edu.itba.pod.query2;


import ar.edu.itba.pod.Constants;
import ar.edu.itba.pod.models.abstractClasses.Infraction;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import com.hazelcast.mapreduce.Collator;
import java.util.List;
import java.util.Map;

@SuppressWarnings("deprecation")
public class TopInfractionsCollator implements Collator<Map.Entry<String, List<String>>, Map<String, List<String>>> {

    private final transient IMap<String, Infraction> infractions;

    public TopInfractionsCollator(HazelcastInstance hazelcastInstance){
        this.infractions = hazelcastInstance.getMap(Constants.INFRACTION_MAP);
    }

    @Override
    public Map<String, List<String>> collate(Iterable<Map.Entry<String, List<String>>> values) {
        // map sorted by keys
        Map<String, List<String>> map = new java.util.TreeMap<>();
        for (Map.Entry<String, List<String>> entry : values) {
            List<String> topInfractions = entry.getValue();
            List<String> topInfractionsDescription = topInfractions.stream().map(infractionCode -> infractions.get(infractionCode).getDescription()).toList();
            map.put(entry.getKey(), topInfractionsDescription);
        }
        return map;
    }
}


