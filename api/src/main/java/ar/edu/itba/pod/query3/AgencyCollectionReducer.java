package ar.edu.itba.pod.query3;

import com.hazelcast.mapreduce.Reducer;
import com.hazelcast.mapreduce.ReducerFactory;

@SuppressWarnings("deprecation")
// Recibe <String, Long> (Agencia y monto) y devuelve <String, Long> (Agencia y monto total)
// Se encarga el Collator de sumar toda la recaudacion y calcular el porcentage
public class AgencyCollectionReducer implements ReducerFactory<String, Long, Long> {

    public AgencyCollectionReducer() {
    }

    @Override
    public Reducer<Long, Long> newReducer(String s) {
        return new AgencyReducer();
    }

    private static class AgencyReducer extends Reducer<Long, Long> {
        private long sum;

        @Override
        public void beginReduce() {
            sum = 0;
        }
        @Override
        public void reduce(Long value) {
            sum += value;
        }

        @Override
        public Long finalizeReduce() {
            return sum;
        }
    }
}
