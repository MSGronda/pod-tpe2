package ar.edu.itba.pod.query3;

import ar.edu.itba.pod.models.abstractClasses.Ticket;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.HazelcastInstanceAware;
import com.hazelcast.mapreduce.Context;
import com.hazelcast.mapreduce.Mapper;

public class AgencyCollectionMapper implements Mapper<Long, Ticket, String, Long>{

    public AgencyCollectionMapper() {
    }

    @Override
    public void map(Long value, Ticket ticket, Context<String, Long> context) {
        context.emit(ticket.getAgency(), (long) ticket.getFineAmount());
    }

}
