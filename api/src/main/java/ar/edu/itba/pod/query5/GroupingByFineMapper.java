package ar.edu.itba.pod.query5;

import com.hazelcast.mapreduce.Context;
import com.hazelcast.mapreduce.Mapper;

public class GroupingByFineMapper implements Mapper<String, Float, Integer, String> {
    public GroupingByFineMapper(){
        // Necessary for hazelcast
    }

    @Override
    public void map(String infractionName, Float fineAverage, Context<Integer, String> context) {
        context.emit(((int) Math.floor(fineAverage / 100)) * 100, infractionName);
    }

}