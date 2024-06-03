package ar.edu.itba.pod.query1;

import ar.edu.itba.pod.Constants;
import ar.edu.itba.pod.models.abstractClasses.Infraction;
import ar.edu.itba.pod.models.abstractClasses.Ticket;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.HazelcastInstanceAware;
import com.hazelcast.core.IMap;
import com.hazelcast.mapreduce.Context;
import com.hazelcast.mapreduce.Mapper;

import java.io.Serializable;

@SuppressWarnings("deprecation")
public class TotalInfractionsMapper implements Mapper<Long, Ticket, String, Long>, HazelcastInstanceAware {
    private static final Long ONE = 1L;

    private transient IMap<String, Infraction> infractions;

    public TotalInfractionsMapper(){

    }

    @Override
    public void map(Long i, Ticket t, Context<String, Long> context) {
        context.emit(infractions.get(t.getInfractionCode()).getDescription(), ONE);
    }

    @Override
    public void setHazelcastInstance(HazelcastInstance hazelcastInstance) {
        this.infractions = hazelcastInstance.getMap(Constants.INFRACTION_MAP);
    }
}
