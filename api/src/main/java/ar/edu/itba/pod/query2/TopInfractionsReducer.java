package ar.edu.itba.pod.query2;

import com.hazelcast.mapreduce.Reducer;
import com.hazelcast.mapreduce.ReducerFactory;

import java.util.*;


@SuppressWarnings("deprecation")
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

        // Creamos una nueva lista y la llenamos con las 3 infracciones mas comunes del county
        // Para ello ordenamos nuestro mapa por valor y tomamos los 3 primeros
        // En caso de que no haya 3 infracciones, completamos con "-"
        @Override
        public List<String> finalizeReduce() {
            List<String> topInfractions = new ArrayList<>();
            infractions.entrySet().stream()
                    .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                    .limit(3)
                    .forEach(entry -> topInfractions.add(entry.getKey()));

            while (topInfractions.size() < 3) {
                topInfractions.add("-");
            }

            return topInfractions;
        }
    }


}
