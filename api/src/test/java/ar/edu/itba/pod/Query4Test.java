package ar.edu.itba.pod;

import ar.edu.itba.pod.models.CHITickets.TicketCHIQuery4;
import ar.edu.itba.pod.models.StringLongPair;
import ar.edu.itba.pod.query4.PlatesMostInfractionsByCountyCollator;
import ar.edu.itba.pod.query4.PlatesMostInfractionsByCountyMapper;
import ar.edu.itba.pod.query4.PlatesMostInfractionsByCountyNoCombinerReducerFactory;
import ar.edu.itba.pod.query4.PlatesMostInfractionsByCountyReducerFactory;
import com.hazelcast.mapreduce.Collator;
import com.hazelcast.mapreduce.Context;
import com.hazelcast.mapreduce.Reducer;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

@SuppressWarnings("deprecation")
@RunWith(MockitoJUnitRunner.class)
public class Query4Test {
    private static final String COUNTY = "COUNTY1";
    private static final String LICENSE_PLATE = "A";

    private static final List<List<StringLongPair>> COMBINED_LIST = List.of(
            List.of(StringLongPair.of(LICENSE_PLATE, 1L), StringLongPair.of(LICENSE_PLATE, 1L))
    );
    private static final List<String> UNCOMBINED_LIST = List.of(LICENSE_PLATE, LICENSE_PLATE);

    private static final Map<String, StringLongPair> MAP = Map.of(
            "A", StringLongPair.of(LICENSE_PLATE, 1L),
            "B", StringLongPair.of(LICENSE_PLATE, 2L),
            "C", StringLongPair.of(LICENSE_PLATE, 3L)
    );

    private static class PlatesMostInfractionsByCountyContext implements Context<String, String> {
        private String s1;
        private String s2;

        @Override
        public void emit(String s1, String s2) {
            this.s1 = s1;
            this.s2 = s2;
        }
    }

    @Test
    public void MapperTest() {
        PlatesMostInfractionsByCountyMapper mapper = new PlatesMostInfractionsByCountyMapper(LocalDateTime.MIN, LocalDateTime.MAX);
        PlatesMostInfractionsByCountyContext context = new PlatesMostInfractionsByCountyContext();

        mapper.map(1L, new TicketCHIQuery4(LICENSE_PLATE, COUNTY, LocalDateTime.now()), context);

        assertEquals(COUNTY, context.s1);
        assertEquals(LICENSE_PLATE, context.s2);
    }

    @Test
    public void ReducerTest() {
        Reducer<List<StringLongPair>, StringLongPair> reducer = new PlatesMostInfractionsByCountyReducerFactory().newReducer(COUNTY);

        reducer.beginReduce();
        COMBINED_LIST.forEach(reducer::reduce);

        StringLongPair pair = reducer.finalizeReduce();
        assertEquals(LICENSE_PLATE, pair.getPlate());
        assertEquals(2L, pair.getNum());
    }

    @Test
    public void NoCombinerReducerTest() {
        Reducer<String, StringLongPair> reducer = new PlatesMostInfractionsByCountyNoCombinerReducerFactory().newReducer(COUNTY);

        reducer.beginReduce();
        UNCOMBINED_LIST.forEach(reducer::reduce);

        StringLongPair result = reducer.finalizeReduce();
        assertEquals(LICENSE_PLATE, result.getPlate());
        assertEquals(UNCOMBINED_LIST.size(), result.getNum());
    }

    @Test
    public void CollatorTest() {
        Collator<Map.Entry<String, StringLongPair>, Map<String, StringLongPair>> collator = new PlatesMostInfractionsByCountyCollator();

        Map<String, StringLongPair> output = collator.collate(MAP.entrySet());

        Map.Entry<String, StringLongPair> prevEntry = null;

        for (Map.Entry<String, StringLongPair> entry : output.entrySet()) {
            if (prevEntry == null) {
                prevEntry = entry;
                continue;
            }
            if (prevEntry.getKey().compareTo(entry.getKey()) > 0) {
                Assert.fail("Ordering incorrect");
            }
            prevEntry = entry;
        }

    }
}
