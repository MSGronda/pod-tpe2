package ar.edu.itba.pod.query1;


import ar.edu.itba.pod.models.abstractClasses.Ticket;
import com.hazelcast.mapreduce.Context;
import com.hazelcast.mapreduce.Mapper;

@SuppressWarnings("deprecation")
public class TotalInfractionsMapper implements Mapper<Long, Ticket, String, Integer> {
    private static final Integer ONE = 1;

    public TotalInfractionsMapper() {
        // Necessary for hazelcast
    }

    @Override
    public void map(Long i, Ticket t, Context<String, Integer> context) {
        context.emit(t.getInfractionCode(), ONE);
    }
}
