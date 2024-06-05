package ar.edu.itba.pod.query1;

import ar.edu.itba.pod.Constants;
import ar.edu.itba.pod.models.abstractClasses.Infraction;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import com.hazelcast.mapreduce.Collator;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("deprecation")
public class TotalInfractionsCollator implements Collator<Map.Entry<String, Integer>, Map<String, Integer>>  {

    private final transient IMap<String, Infraction> infractions;

    public TotalInfractionsCollator(HazelcastInstance hazelcastInstance){
        this.infractions = hazelcastInstance.getMap(Constants.INFRACTION_MAP);
    }

    @Override
    public Map<String, Integer> collate(Iterable<Map.Entry<String, Integer>> iterable) {
        Map<String, Integer> resp = new HashMap<>();

        iterable.forEach(entry -> resp.put(infractions.get(entry.getKey()).getDescription(), entry.getValue()));

        return resp;
    }
}
