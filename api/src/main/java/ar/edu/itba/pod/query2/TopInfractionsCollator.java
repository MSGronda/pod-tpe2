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

        // Creamos un mapa ordenado por key value (County alfabetico crecientemente)
        Map<String, List<String>> map = new java.util.TreeMap<>();

        // Iteramos sobre los codigos de infracciones y obtenemos sus descripciones
        // En caso de ver el codigo "-" repetimos dicho valor
        for (Map.Entry<String, List<String>> entry : values) {
            List<String> topInfractions = entry.getValue();
            //List<String> topInfractionsDescription = topInfractions.stream().filter(i -> !i.equals("-")).map(infractionCode -> infractions.get(infractionCode).getDescription()).toList();

            List<String> topInfractionsDescription = new java.util.ArrayList<>();
            for (String infractionCode : topInfractions) {
                if (!infractionCode.equals("-") || !infractions.containsKey(infractionCode)){
                    topInfractionsDescription.add(infractions.get(infractionCode).getDescription());
                }else{
                    topInfractionsDescription.add("-");
                }
            }

            map.put(entry.getKey(), topInfractionsDescription);
        }
        return map;
    }
}


