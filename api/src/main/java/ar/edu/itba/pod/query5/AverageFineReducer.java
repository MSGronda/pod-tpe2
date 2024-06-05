package ar.edu.itba.pod.query5;

import com.hazelcast.mapreduce.Reducer;
import com.hazelcast.mapreduce.ReducerFactory;

@SuppressWarnings("deprecation")
public class AverageFineReducer implements ReducerFactory<String, Float, Float> {

    @Override
    public Reducer<Float, Float> newReducer(String s) {
        return new AverageFineReducer.FineReducer();
    }

    private static class FineReducer extends Reducer<Float, Float>{
        private double sum;
        private long count;

        @Override
        public void beginReduce() {
            sum = 0;
            count = 0;
        }

        @Override
        public void reduce(Float fineValue) {
            sum += fineValue;
            count++;
        }

        @Override
        public Float finalizeReduce() {
            return (float) (sum / count);
        }
    }

}
