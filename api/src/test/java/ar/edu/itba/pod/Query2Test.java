package ar.edu.itba.pod;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.mapreduce.Context;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;

@SuppressWarnings("deprecation")
@RunWith(MockitoJUnitRunner.class)
public class Query2Test {

    private static final List<String> counties = List.of("Albany Park", "Archer Heights", "Armour Square");
    private static final List<String> infractions =
            List.of("STREET CLEANING OR SPECIAL EVENT",
                    "RESIDENTIAL PERMIT PARKING",
                    "EXPIRED PLATES OR TEMPORARY REGISTRATION",
                    "EXP. METER NON-CENTRAL BUSINESS DISTRICT",
                    "EXPIRED METER OR OVERSTAY",
                    "RUSH HOUR PARKING",
                    "NO CITY STICKER OR IMPROPER DISPLAY",
                    "REAR AND FRONT PLATE REQUIRED");

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
        // TODO
    }

}
