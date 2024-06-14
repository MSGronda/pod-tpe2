package ar.edu.itba.pod.query5;

import ar.edu.itba.pod.models.abstractClasses.Ticket;
import com.hazelcast.mapreduce.Context;
import com.hazelcast.mapreduce.Mapper;

@SuppressWarnings("deprecation")
public class AverageFineMapper implements Mapper<Long, Ticket, String, Float> {

    public AverageFineMapper() {
        // Necessary for hazelcast
    }

    @Override
    public void map(Long i, Ticket t, Context<String, Float> context) {
        context.emit(t.getInfractionCode(), t.getFineAmount());
    }
}
