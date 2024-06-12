package ar.edu.itba.pod.utils;

import ar.edu.itba.pod.models.InfractionCHI;
import com.hazelcast.core.IMap;

import java.util.List;

public class Common {
    public static final String VIOLATION_CODE = "A(@#*J#($";
    public static final List<String> VIOLATION_CODES = List.of("awsh", "213i4", "3i492");
    public static final List<String> VIOLATION_DESCRIPTIONS = List.of("Drunk driving", "Driving without sunglasses", "Parked 10 feet from the curve");
    public static final IMap<Object, Object> VIOLATION_CODE_DESC_MAP = setupViolationCodeDescMap();
    private static IMap<Object, Object> setupViolationCodeDescMap(){
        IMap<Object, Object> resp = new DummyIMap<>();

        resp.put(VIOLATION_CODES.get(0), new InfractionCHI(VIOLATION_CODES.get(0), VIOLATION_DESCRIPTIONS.get(0)));
        resp.put(VIOLATION_CODES.get(1), new InfractionCHI(VIOLATION_CODES.get(1), VIOLATION_DESCRIPTIONS.get(1)));
        resp.put(VIOLATION_CODES.get(2), new InfractionCHI(VIOLATION_CODES.get(2), VIOLATION_DESCRIPTIONS.get(2)));

        return resp;
    }
}
