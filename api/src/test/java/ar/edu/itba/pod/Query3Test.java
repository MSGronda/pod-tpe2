package ar.edu.itba.pod;

import ar.edu.itba.pod.models.CHITickets.TicketCHIQuery3;
import ar.edu.itba.pod.query3.AgencyCollectionCollator;
import ar.edu.itba.pod.query3.AgencyCollectionMapper;
import ar.edu.itba.pod.query3.AgencyCollectionReducer;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.mapreduce.Collator;
import com.hazelcast.mapreduce.Context;
import com.hazelcast.mapreduce.Reducer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@RunWith(MockitoJUnitRunner.class)
public class Query3Test {

    private static final List<String> agencies = List.of("Traffic", "Parking", "Police", "Sanitation");

    private static final List<Integer> fines = List.of(50, 30, 40);

    private static final Map<String, Long> AGENCY_COLLECTION_MAP = Map.of(
            agencies.get(0), 70L,
            agencies.get(1), 30L,
            agencies.get(2), 40L,
            agencies.get(3), 10L
    );

    private static final int SUM = 70 + 30 + 40 + 10;
    private static final Map<String, Double> AGENCY_PERCENTAGE_MAP = Map.of(
            agencies.get(0), Math.floor(70 / (double) SUM * 100 * 100) / 100,
            agencies.get(1), Math.floor(30 / (double) SUM * 100 * 100) / 100,
            agencies.get(2), Math.floor(40 / (double) SUM * 100 * 100) / 100,
            agencies.get(3), Math.floor(10 / (double) SUM * 100 * 100) / 100
    );

    @Mock
    private HazelcastInstance hazelcastInstance;

    private static class AgencyCollectionContext implements Context<String, Long> {
        private String s;

        private Long l;

        @Override
        public void emit(String s, Long l) {
            this.s = s;
            this.l = l;
        }
    }

    @Test
    public void agencyCollectionMapperTest() {
        AgencyCollectionMapper mapper = new AgencyCollectionMapper();
        AgencyCollectionContext context = new AgencyCollectionContext();

        mapper.map(1L, new TicketCHIQuery3(agencies.get(0), fines.get(0)), context);

        assertEquals(agencies.get(0), context.s);
        assertEquals((long) fines.get(0), context.l);
    }

    @Test
    public void agencyCollectionReducerTest() {
        Reducer<Long, Long> reducer = new AgencyCollectionReducer().newReducer(agencies.get(0));

        reducer.beginReduce();
        for (Integer fine : fines) {
            reducer.reduce((long) fine);
        }

        assertEquals((long) fines.stream().reduce(0, Integer::sum), reducer.finalizeReduce());
    }

    @Test
    public void agencyCollectionCollatorTest() {
        int n = 3;
        Collator<Map.Entry<String, Long>, Set<Map.Entry<String, Double>>> collator = new AgencyCollectionCollator(n);

        Set<Map.Entry<String, Double>> result = collator.collate(AGENCY_COLLECTION_MAP.entrySet());
        assertEquals(n, result.size());

        assertTrue(result.containsAll(List.of(
                Map.entry(agencies.get(0), AGENCY_PERCENTAGE_MAP.get(agencies.get(0))),
                Map.entry(agencies.get(1), AGENCY_PERCENTAGE_MAP.get(agencies.get(1))),
                Map.entry(agencies.get(2), AGENCY_PERCENTAGE_MAP.get(agencies.get(2))
                ))));

        Double value = null;
        for (Map.Entry<String, Double> entry : result) {
            if (value == null) {
                value = entry.getValue();
            } else {
                assertTrue(value >= entry.getValue());
            }
        }
    }
}
