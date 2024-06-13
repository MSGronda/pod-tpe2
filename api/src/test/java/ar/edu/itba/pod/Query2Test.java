package ar.edu.itba.pod;

import ar.edu.itba.pod.models.CHITickets.TicketCHIQuery2;
import ar.edu.itba.pod.query2.TopInfractionsCollator;
import ar.edu.itba.pod.query2.TopInfractionsMapper;
import ar.edu.itba.pod.query2.TopInfractionsReducer;
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

import static ar.edu.itba.pod.utils.Common.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

@SuppressWarnings("deprecation")
@RunWith(MockitoJUnitRunner.class)
public class Query2Test {

    private static final List<String> counties = List.of("Albany Park", "Archer Heights", "Armour Square");
    private static final List<String> infractions = List.of(
            VIOLATION_CODES.get(0),
            VIOLATION_CODES.get(0),
            VIOLATION_CODES.get(1)
    );

    private static final Map<String, List<String>> COUNTY_MAP = Map.of(
            counties.get(2), List.of(VIOLATION_CODES.get(0), "-", "-"),
            counties.get(0), List.of(VIOLATION_CODES.get(0), VIOLATION_CODES.get(1), "-"),
            counties.get(1), List.of(VIOLATION_CODES.get(0), VIOLATION_CODES.get(1), VIOLATION_CODES.get(2))
    );

    private static final Map<String, List<String>> DESCRIPTION_MAP = Map.of(
            counties.get(0), List.of("Drunk driving", "Driving without sunglasses", "-"),
            counties.get(1), List.of("Drunk driving", "Driving without sunglasses","Parked 10 feet from the curve"),
            counties.get(2), List.of("Drunk driving", "-", "-")
    );


    @Mock
    private HazelcastInstance hazelcastInstance;

    private static class TopInfractionsContext implements Context<String, String> {
        private String s1;
        private String s2;

        @Override
        public void emit(String s1, String s2) {
            this.s1 = s1;
            this.s2 = s2;
        }
    }

    @Test
    public void topInfractionsMapperTest() {
        TopInfractionsMapper mapper = new TopInfractionsMapper();
        TopInfractionsContext context = new TopInfractionsContext();

        mapper.map(1L, new TicketCHIQuery2(VIOLATION_CODES.get(0), counties.get(0)), context);

        assertEquals(counties.get(0), context.s1);
        assertEquals(VIOLATION_CODES.get(0), context.s2);
    }

    @Test
    public void topInfractionsReducerTest(){
        Reducer<String, List<String>> reducer = new TopInfractionsReducer().newReducer(counties.get(0));

        reducer.beginReduce();
        infractions.forEach(reducer::reduce);

        assertEquals(List.of(VIOLATION_CODES.get(0), VIOLATION_CODES.get(1), "-"), reducer.finalizeReduce());
    }

    @Test
    public void topInfractionsCollatorTest(){
        when(hazelcastInstance.getMap(Constants.INFRACTION_MAP)).thenReturn(VIOLATION_CODE_DESC_MAP);

        Collator<Map.Entry<String, List<String>>, Map<String, List<String>>> collator = new TopInfractionsCollator(hazelcastInstance);
        Map<String, List<String>> collatedMap = collator.collate(COUNTY_MAP.entrySet());

        assertEquals(COUNTY_MAP.size(), collatedMap.size());
        assertEquals(DESCRIPTION_MAP.get(counties.get(0)), collatedMap.get(counties.get(0)));
        assertEquals(DESCRIPTION_MAP.get(counties.get(1)), collatedMap.get(counties.get(1)));
        assertEquals(DESCRIPTION_MAP.get(counties.get(2)), collatedMap.get(counties.get(2)));
    }

}
