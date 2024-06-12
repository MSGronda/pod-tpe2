package ar.edu.itba.pod.query4;

import ar.edu.itba.pod.models.abstractClasses.Ticket;
import com.hazelcast.mapreduce.Context;
import com.hazelcast.mapreduce.Mapper;

/*This is assuming that Tickets are loaded on a MultiMap using the date as the key, which
* allows us to use a KeyPredicate to prefilter the data without lifting the whole value.
*
* We receive a (LocalDateTime, Ticket) entry, and emit (String County, String Patent)
*/
@SuppressWarnings("deprecation")
public class PlatesMostInfractionsByCountyMapper implements Mapper<Long, Ticket, String, String> {

    @Override
    public void map(Long epoch, Ticket ticket, Context<String, String> context) {
        context.emit(ticket.getCounty(), ticket.getPlate());
    }
}
