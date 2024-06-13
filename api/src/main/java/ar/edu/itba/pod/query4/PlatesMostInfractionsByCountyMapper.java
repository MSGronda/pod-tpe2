package ar.edu.itba.pod.query4;

import ar.edu.itba.pod.models.abstractClasses.Ticket;
import com.hazelcast.mapreduce.Context;
import com.hazelcast.mapreduce.Mapper;

import java.time.LocalDateTime;

/*This is assuming that Tickets are loaded on a MultiMap using the date as the key, which
 * allows us to use a KeyPredicate to prefilter the data without lifting the whole value.
 *
 * We receive a (LocalDateTime, Ticket) entry, and emit (String County, String Patent)
 */
@SuppressWarnings("deprecation")
public class PlatesMostInfractionsByCountyMapper implements Mapper<Long, Ticket, String, String> {
    private final LocalDateTime from;
    private final LocalDateTime to;

    public PlatesMostInfractionsByCountyMapper(LocalDateTime from, LocalDateTime to) {
        this.from = from;
        this.to = to;
    }

    @Override
    public void map(Long epoch, Ticket ticket, Context<String, String> context) {
        LocalDateTime time = ticket.getDate();
        if ((from.isBefore(time) || from.isEqual(time)) && (to.isAfter(time) || to.isEqual(time))) {
            context.emit(ticket.getCounty(), ticket.getPlate());
        }
    }
}
