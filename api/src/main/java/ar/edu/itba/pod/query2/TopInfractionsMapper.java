package ar.edu.itba.pod.query2;

import ar.edu.itba.pod.models.abstractClasses.Infraction;
import ar.edu.itba.pod.models.abstractClasses.Ticket;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.HazelcastInstanceAware;
import com.hazelcast.core.IMap;
import com.hazelcast.mapreduce.Context;
import com.hazelcast.mapreduce.Mapper;

public class TopInfractionsMapper implements Mapper<Long, Ticket, String, String>, HazelcastInstanceAware {

    private IMap<String, Infraction> infractions;

    @Override
    public void map(Long i, Ticket t, Context<String, String> context) {
        context.emit(t.getCounty(),  infractions.get(t.getInfractionCode()).getDescription());
    }

    @Override
    public void setHazelcastInstance(HazelcastInstance hazelcastInstance) {
        this.infractions = hazelcastInstance.getMap("infractions");
    }


}
