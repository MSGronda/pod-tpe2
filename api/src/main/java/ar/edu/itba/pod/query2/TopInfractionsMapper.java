package ar.edu.itba.pod.query2;


import ar.edu.itba.pod.models.abstractClasses.Ticket;
import com.hazelcast.mapreduce.Context;
import com.hazelcast.mapreduce.Mapper;

@SuppressWarnings("deprecation")
public class TopInfractionsMapper implements Mapper<Long, Ticket, String, String> {

    public TopInfractionsMapper(){
        // Necessary for hazelcast
    }

    @Override
    public void map(Long i, Ticket t, Context<String, String> context) {
        context.emit(t.getCounty(),  t.getInfractionCode());
    }

}
