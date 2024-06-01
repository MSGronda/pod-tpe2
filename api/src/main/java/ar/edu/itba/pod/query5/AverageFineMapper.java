package ar.edu.itba.pod.query5;

import ar.edu.itba.pod.models.abstractClasses.Infraction;
import ar.edu.itba.pod.models.abstractClasses.Ticket;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.HazelcastInstanceAware;
import com.hazelcast.core.IMap;
import com.hazelcast.mapreduce.Context;
import com.hazelcast.mapreduce.Mapper;

public class AverageFineMapper implements Mapper<Integer, Ticket, String, Float>, HazelcastInstanceAware {

    private IMap<String, Infraction> infractions;

    @Override
    public void map(Integer i, Ticket t, Context<String, Float> context) {
        context.emit(infractions.get(t.getInfractionCode()).getDescription(), t.getFineAmount());
    }

    @Override
    public void setHazelcastInstance(HazelcastInstance hazelcastInstance) {
        this.infractions = hazelcastInstance.getMap("infractions");
    }
}
