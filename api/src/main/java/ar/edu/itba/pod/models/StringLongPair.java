package ar.edu.itba.pod.models;

import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;
import com.hazelcast.nio.serialization.DataSerializable;

import java.io.IOException;
import java.util.Objects;

public class StringLongPair implements DataSerializable {
    private String plate;
    private long num;

    public StringLongPair() {
        // Necessary for hazelcast
    }

    public StringLongPair(String plate, long num) {
        this.plate = plate;
        this.num = num;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StringLongPair that = (StringLongPair) o;
        return num == that.num && Objects.equals(plate, that.plate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(plate, num);
    }

    @Override
    public void writeData(ObjectDataOutput out) throws IOException {
        out.writeUTF(plate);
        out.writeLong(num);
    }

    @Override
    public void readData(ObjectDataInput in) throws IOException {
        plate = in.readUTF();
        num = in.readLong();
    }

    @Override
    public String toString() {
        return "PlateLongPair{" +
                "plate='" + plate + '\'' +
                ", num=" + num +
                '}';
    }

    public static StringLongPair of(String plate, Long num) {
        return new StringLongPair(plate, num);
    }

    public String getPlate() {
        return plate;
    }

    public long getNum() {
        return num;
    }
}
