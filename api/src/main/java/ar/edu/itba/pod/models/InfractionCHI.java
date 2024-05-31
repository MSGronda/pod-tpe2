package ar.edu.itba.pod.models;

import ar.edu.itba.pod.models.abstractClasses.Infraction;
import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;
import com.hazelcast.nio.serialization.DataSerializable;

import java.io.IOException;

public class InfractionCHI extends Infraction implements DataSerializable {
    private String code;
    private String description;

    public InfractionCHI(String code, String description){
        this.code = code;
        this.description = description;
    }


    @Override
    public void writeData(ObjectDataOutput objectDataOutput) throws IOException {
        objectDataOutput.writeUTF(code);
        objectDataOutput.writeUTF(description);
    }

    @Override
    public void readData(ObjectDataInput objectDataInput) throws IOException {
        code = objectDataInput.readUTF();
        description = objectDataInput.readUTF();
    }

    public String getCode() {
        return code;
    }

    @Override
    public String getDescription() {
        return description;
    }
}
