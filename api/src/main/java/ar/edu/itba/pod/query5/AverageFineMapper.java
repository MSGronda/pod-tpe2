package ar.edu.itba.pod.query5;

import ar.edu.itba.pod.Constants;
import ar.edu.itba.pod.models.abstractClasses.Infraction;
import ar.edu.itba.pod.models.abstractClasses.Ticket;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.HazelcastInstanceAware;
import com.hazelcast.core.IMap;
import com.hazelcast.mapreduce.Context;
import com.hazelcast.mapreduce.Mapper;
@SuppressWarnings("deprecation")
public class AverageFineMapper implements Mapper<Long, Ticket, String, Float>, HazelcastInstanceAware {

    public AverageFineMapper(){
        // Necessary for hazelcast
    }

    private transient IMap<String, Infraction> infractions;

    @Override
    public void map(Long i, Ticket t, Context<String, Float> context) {
        context.emit(infractions.get(t.getInfractionCode()).getDescription(), t.getFineAmount());
    }

    @Override
    public void setHazelcastInstance(HazelcastInstance hazelcastInstance) {
        this.infractions = hazelcastInstance.getMap(Constants.INFRACTION_MAP);
    }
}
