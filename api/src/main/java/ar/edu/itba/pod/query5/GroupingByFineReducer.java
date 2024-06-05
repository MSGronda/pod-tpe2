package ar.edu.itba.pod.query5;

import ar.edu.itba.pod.models.StringPair;
import com.hazelcast.mapreduce.Reducer;
import com.hazelcast.mapreduce.ReducerFactory;

import java.util.*;

@SuppressWarnings("deprecation")
public class GroupingByFineReducer implements ReducerFactory<Integer, String, List<StringPair>> {

    @Override
    public Reducer<String, List<StringPair>> newReducer(Integer fineRange) {
        return new GroupingByFineReducer.GroupReducer();
    }

    private static class GroupReducer extends Reducer<String, List<StringPair>> {
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
        public List<StringPair> finalizeReduce() {
            List<StringPair> resp = new ArrayList<>();

            Object[] infractions = infractionNames.toArray();

            for(int i=0; i<infractions.length; i++){
                for(int j=0; j<i; j++){
                    resp.add(new StringPair((String) infractions[i], (String) infractions[j]));     // TODO: check el casteo
                }
            }

            return resp;
        }
    }

}