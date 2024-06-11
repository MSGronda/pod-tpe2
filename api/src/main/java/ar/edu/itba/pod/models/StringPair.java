package ar.edu.itba.pod.models;

import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;
import com.hazelcast.nio.serialization.DataSerializable;

import java.io.IOException;
import java.util.Objects;

// Hacemos un stringPair (en vez de una Pair generico) por la serializacion. Necesitamos saber bien
// el tipo de dato y que se pueda serializar.
public class StringPair implements DataSerializable {
    private String value1;
    private String value2;

    public StringPair(){
        // Necessary for hazelcast
    }

    public StringPair(String value1, String value2){
        this.value1 = value1;
        this.value2 = value2;
    }

    public String getValue1() {
        return value1;
    }

    public String getValue2() {
        return value2;
    }


    @Override
    public void writeData(ObjectDataOutput objectDataOutput) throws IOException {
        objectDataOutput.writeUTF(value1);
        objectDataOutput.writeUTF(value2);
    }

    @Override
    public void readData(ObjectDataInput objectDataInput) throws IOException {
        value1 = objectDataInput.readUTF();
        value2 = objectDataInput.readUTF();
    }

    @Override
    public String toString() {
        return "<" + value1 + " & " + value2 + ">";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StringPair that = (StringPair) o;
        return Objects.equals(value1, that.value1) && Objects.equals(value2, that.value2);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value1, value2);
    }
}
