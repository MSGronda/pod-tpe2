package ar.edu.itba.pod.query2;

import com.hazelcast.mapreduce.Reducer;
import com.hazelcast.mapreduce.ReducerFactory;


import java.util.*;

public class TopInfractionsReducer implements ReducerFactory<String, String, List<String>> {

    @Override
    public Reducer<String, List<String>> newReducer(String s) {
        return new InfractionReducer();
    }

    private static class InfractionReducer extends Reducer<String, List<String>> {
        private Map<String, Integer> infractions;

        @Override
        public void beginReduce() {
            infractions = new HashMap<>();
        }

        @Override
        public void reduce(String value) {
            infractions.merge(value, 1, Integer::sum);
        }

        @Override
        public List<String> finalizeReduce() {
            List<String> topInfractions = new ArrayList<>();
            for (int i = 0; i < 3; i++){
                if (infractions.isEmpty()){
                    topInfractions.add("-");
                }else {
                    Map.Entry<String, Integer> maxEntry = null;
                    for (Map.Entry<String, Integer> entry : infractions.entrySet()) { // O(n)
                        if (maxEntry == null || entry.getValue().compareTo(maxEntry.getValue()) > 0) {
                            maxEntry = entry;
                        }
                    }
                    topInfractions.add(maxEntry.getKey()); // O(1)
                    infractions.remove(maxEntry.getKey()); // O(1)
                }
            }
            return topInfractions;

            // esta es otra que se me ocurrio pero como sortea es O(n logn)
            // TODO: podemos testearla mas adelante
            //infractions.entrySet().stream()
                    //.sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                    //.limit(3)
                    //.forEach(e -> topInfractions.add(e.getKey()));

        }
    }


}
