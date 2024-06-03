package ar.edu.itba.pod.models;

import ar.edu.itba.pod.models.abstractClasses.Infraction;
import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;
import com.hazelcast.nio.serialization.DataSerializable;

import java.io.IOException;
import java.util.Objects;

public class InfractionNYC extends Infraction implements DataSerializable {
    private int code;
    private String definition;

    public InfractionNYC() {
        // Necessary for hazelcast
    }

    public InfractionNYC(final int code, final String definition) {
        this.code = code;
        this.definition = definition;
    }

    @Override
    public void writeData(ObjectDataOutput objectDataOutput) throws IOException {
        objectDataOutput.writeInt(code);
        objectDataOutput.writeUTF(definition);
    }

    @Override
    public void readData(ObjectDataInput objectDataInput) throws IOException {
        code = objectDataInput.readInt();
        definition = objectDataInput.readUTF();
    }

    @Override
    public String getDescription() {
        return definition;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InfractionNYC that = (InfractionNYC) o;
        return code == that.code && Objects.equals(definition, that.definition);
    }

    @Override
    public int hashCode() {
        return Objects.hash(code, definition);
    }

    public int getCode() {
        return code;
    }

    public String getDefinition() {
        return definition;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public void setDefinition(String definition) {
        this.definition = definition;
    }
}
