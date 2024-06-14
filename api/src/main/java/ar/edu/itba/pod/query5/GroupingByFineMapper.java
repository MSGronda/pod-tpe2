package ar.edu.itba.pod.query5;

import com.hazelcast.mapreduce.Context;
import com.hazelcast.mapreduce.Mapper;

@SuppressWarnings("deprecation")
public class GroupingByFineMapper implements Mapper<String, Float, Integer, String> {
    public GroupingByFineMapper() {
        // Necessary for hazelcast
    }

    @Override
    public void map(String infractionName, Float fineAverage, Context<Integer, String> context) {
        int group = ((int) Math.floor(fineAverage / 100)) * 100;

        if (group > 0) {
            context.emit(group, infractionName);
        }
    }
}