package ar.edu.itba.pod.query4;

import com.hazelcast.mapreduce.KeyPredicate;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

@SuppressWarnings("deprecation")
/* We are assuming that the key is an epoch */
public class PlatesMostInfractionsByCountyKeyPredicate implements KeyPredicate<Long> {
    private final LocalDateTime from;
    private final LocalDateTime to;

    public PlatesMostInfractionsByCountyKeyPredicate(LocalDateTime from, LocalDateTime to) {
        this.from = from;
        this.to = to;
    }

    @Override
    public boolean evaluate(Long key) {
        LocalDateTime date = LocalDateTime.ofEpochSecond(key, 0, ZoneOffset.UTC);
        return (from.isBefore(date) || from.isEqual(date)) && (to.isAfter(date) || to.isEqual(date));
    }
}
