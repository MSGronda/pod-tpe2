package ar.edu.itba.pod.query5;

import ar.edu.itba.pod.models.Pair;
import com.hazelcast.mapreduce.Reducer;
import com.hazelcast.mapreduce.ReducerFactory;

import java.util.*;

public class GroupingByFineReducer implements ReducerFactory<Integer, String, List<Pair<String, String>>> {

    @Override
    public Reducer<String, List<Pair<String, String>>> newReducer(Integer fineRange) {
        return new GroupingByFineReducer.GroupReducer();
    }

    private static class GroupReducer extends Reducer<String, List<Pair<String, String>>>{
        private Set<String> infractionNames;
        @Override
        public void beginReduce() {
            infractionNames = new HashSet<>();
        }

        @Override
        public void reduce(String infractionName) {
            infractionNames.add(infractionName);
        }

        @Override
        public List<Pair<String, String>> finalizeReduce() {
            List<Pair<String, String>> resp = new ArrayList<>();

            Object[] infractions = infractionNames.toArray();

            for(int i=0; i<infractions.length; i++){
                for(int j=0; j<i; j++){
                    resp.add(new Pair<>((String) infractions[i], (String) infractions[j]));     // TODO: check el casteo
                }
            }

            return resp;
        }
    }

}