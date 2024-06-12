package ar.edu.itba.pod;

import ar.edu.itba.pod.models.CHITickets.TicketCHIQuery1;
import ar.edu.itba.pod.query1.TotalInfractionsCollator;
import ar.edu.itba.pod.query1.TotalInfractionsMapper;
import ar.edu.itba.pod.query1.TotalInfractionsReducer;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.mapreduce.Collator;
import com.hazelcast.mapreduce.Context;
import com.hazelcast.mapreduce.Reducer;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.*;

import static ar.edu.itba.pod.utils.Common.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

@SuppressWarnings("deprecation")
@RunWith(MockitoJUnitRunner.class)
public class Query1Test {
    private static final List<Integer> ONES = List.of(1,1,1,1,1,1);
    private static final Map<String, Integer> INFRACTION_COUNT_MAP = Map.of(
            VIOLATION_CODES.get(0), 5,
            VIOLATION_CODES.get(1), 34,
            VIOLATION_CODES.get(2), 12
    );

    @Mock
    private HazelcastInstance hazelcastInstance;

    private static class TotalInfractionsContext implements Context<String, Integer> {
        private String s;
        private int i;
        @Override
        public void emit(String s, Integer i) {
            this.s = s;
            this.i = i;
        }
    }
    @Test
    public void TotalInfractionsMapperTest(){
        TotalInfractionsMapper mapper = new TotalInfractionsMapper();
        TotalInfractionsContext context = new TotalInfractionsContext();

        mapper.map(1L, new TicketCHIQuery1(VIOLATION_CODE), context);

        assertEquals(VIOLATION_CODE, context.s);
        assertEquals(1, context.i);
    }

    @Test
    public void TotalInfractionsReducerTest(){
        Reducer<Integer, Integer> reducer = new TotalInfractionsReducer().newReducer(VIOLATION_CODE);

        reducer.beginReduce();
        ONES.forEach(reducer::reduce);

        assertEquals(ONES.size(), reducer.finalizeReduce().intValue());
    }

    @Test
    public void TotalInfractionsCollatorTest(){
        when(hazelcastInstance.getMap(Constants.INFRACTION_MAP)).thenReturn(VIOLATION_CODE_DESC_MAP);

        Collator<Map.Entry<String, Integer>, Set<Map.Entry<String, Integer>>> collator = new TotalInfractionsCollator(hazelcastInstance);

        Set<Map.Entry<String, Integer>> output = collator.collate(INFRACTION_COUNT_MAP.entrySet());

        assertTrue(output.containsAll(List.of(
                new AbstractMap.SimpleEntry(VIOLATION_DESCRIPTIONS.get(0), INFRACTION_COUNT_MAP.get(VIOLATION_CODES.get(0))),
                new AbstractMap.SimpleEntry(VIOLATION_DESCRIPTIONS.get(1), INFRACTION_COUNT_MAP.get(VIOLATION_CODES.get(1))),
                new AbstractMap.SimpleEntry(VIOLATION_DESCRIPTIONS.get(2), INFRACTION_COUNT_MAP.get(VIOLATION_CODES.get(2)))
        )));
        Integer prevInt = output.iterator().next().getValue();
        String prevString = output.iterator().next().getKey();

        for(Map.Entry<String, Integer> entry : output){
            if(prevInt < entry.getValue()){
                Assert.fail("Infraction count ordering incorrect");
            }
            if(prevInt.equals(entry.getValue()) && prevString.compareTo(entry.getKey()) > 0){
                Assert.fail("Infraction name ordering incorrect");
            }
            prevInt = entry.getValue();
            prevString = entry.getKey();
        }
    }

}
