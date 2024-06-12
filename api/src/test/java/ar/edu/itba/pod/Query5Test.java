package ar.edu.itba.pod;

import ar.edu.itba.pod.models.CHITickets.TicketCHIQuery5;
import ar.edu.itba.pod.models.StringPair;
import ar.edu.itba.pod.query5.*;
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
public class Query5Test {
    private static final int FINE_AMOUNT = 50;
    private static final float AVERAGE_FINE = 40f;
    private static final int FINE_GROUP = 0;
    private static final List<Float> FINES = List.of(50f,30f,40f,40f,30f,50f);

    @Mock
    private HazelcastInstance hazelcastInstance;


    private static final Map<Integer, List<StringPair>> GROUPED_BY_FINE_MAP = Map.of(
            FINE_GROUP, List.of(
                    new StringPair(VIOLATION_CODES.get(1),VIOLATION_CODES.get(0)),
                    new StringPair(VIOLATION_CODES.get(2),VIOLATION_CODES.get(0)),
                    new StringPair(VIOLATION_CODES.get(2),VIOLATION_CODES.get(1))
            )
    );
    private static final Map<Integer, List<StringPair>> GROUPED_BY_FINE_WITH_DESC_MAP = Map.of(
            FINE_GROUP, List.of(
                    new StringPair(VIOLATION_DESCRIPTIONS.get(1),VIOLATION_DESCRIPTIONS.get(0)),
                    new StringPair(VIOLATION_DESCRIPTIONS.get(2),VIOLATION_DESCRIPTIONS.get(0)),
                    new StringPair(VIOLATION_DESCRIPTIONS.get(2),VIOLATION_DESCRIPTIONS.get(1))
            )
    );

    private static class AverageFineContext implements Context<String, Float> {
        private String s;
        private float f;
        @Override
        public void emit(String s, Float f) {
            this.s = s;
            this.f = f;
        }
    }
    @Test
    public void averageFineMapperTest(){
        AverageFineMapper mapper = new AverageFineMapper();
        AverageFineContext context = new AverageFineContext();

        mapper.map(1L, new TicketCHIQuery5(VIOLATION_CODE, FINE_AMOUNT), context);

        assertEquals(VIOLATION_CODE, context.s);
        assertEquals(FINE_AMOUNT, (int) context.f);
    }

    @Test
    public void averageFineReducerTest(){
        Reducer<Float, Float> reducer = new AverageFineReducer().newReducer(VIOLATION_CODE);

        reducer.beginReduce();
        FINES.forEach(reducer::reduce);

        assertEquals(AVERAGE_FINE, reducer.finalizeReduce(), 0.0001);
    }

    private static class GroupingByFineContext implements Context<Integer, String> {
        private int i;
        private String s;
        @Override
        public void emit(Integer i, String s) {
            this.s = s;
            this.i = i;
        }
    }

    @Test
    public void groupingByNameMapperTest(){
        GroupingByFineMapper mapper = new GroupingByFineMapper();
        GroupingByFineContext context = new GroupingByFineContext();

        mapper.map(VIOLATION_CODE, AVERAGE_FINE, context);

        assertEquals(VIOLATION_CODE, context.s);
        assertEquals(FINE_GROUP, context.i);
    }

    @Test
    public void groupingByNameReducerTest(){
        Reducer<String, List<StringPair>> reducer = new GroupingByFineReducer().newReducer(FINE_GROUP);

        reducer.beginReduce();
        VIOLATION_CODES.forEach(reducer::reduce);

        assertTrue(reducer.finalizeReduce().containsAll(GROUPED_BY_FINE_MAP.get(FINE_GROUP)))
        ;
    }

    @Test
    public void groupingByNameCollatorTest(){
        when(hazelcastInstance.getMap(Constants.INFRACTION_MAP)).thenReturn(VIOLATION_CODE_DESC_MAP);

        Collator<Map.Entry<Integer, List<StringPair>>, Map<Integer, Set<StringPair>>> collator = new GroupingByFineCollator(hazelcastInstance);
        Map<Integer, Set<StringPair>> output = collator.collate(GROUPED_BY_FINE_MAP.entrySet());
        assertTrue(output.get(FINE_GROUP).containsAll(List.of(
                new StringPair(VIOLATION_DESCRIPTIONS.get(1), VIOLATION_DESCRIPTIONS.get(0)),
                new StringPair(VIOLATION_DESCRIPTIONS.get(1), VIOLATION_DESCRIPTIONS.get(2)),
                new StringPair(VIOLATION_DESCRIPTIONS.get(0), VIOLATION_DESCRIPTIONS.get(2))
        )));
    }

}
