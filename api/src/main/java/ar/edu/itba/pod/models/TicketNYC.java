package ar.edu.itba.pod.models;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import ar.edu.itba.pod.models.abstractClasses.Ticket;
import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;
import com.hazelcast.nio.serialization.DataSerializable;

public class TicketNYC extends Ticket implements DataSerializable {

    private String plate;
    private LocalDateTime issueDate;
    private int infractionCode;
    private float fineAmount;
    private String countyName;
    private String issuingAgency;

    public TicketNYC(
        String plate,
        LocalDateTime issueDate,
        int infractionCode,
        float fineAmount,
        String countyName,
        String issuingAgency
    ){

        this.issueDate = issueDate;
        this.infractionCode = infractionCode;
        this.fineAmount = fineAmount;
        this.countyName = countyName;
        this.issuingAgency = issuingAgency;

    }

    @Override
    public void writeData(ObjectDataOutput objectDataOutput) throws IOException {
        objectDataOutput.writeUTF(plate);
        objectDataOutput.writeObject(issueDate.toEpochSecond(ZoneOffset.UTC));
        objectDataOutput.writeInt(infractionCode);
        objectDataOutput.writeFloat(fineAmount);
        objectDataOutput.writeUTF(countyName);
        objectDataOutput.writeUTF(issuingAgency);

    }

    @Override
    public void readData(ObjectDataInput objectDataInput) throws IOException {
        plate = objectDataInput.readUTF();
        issueDate = LocalDateTime.ofEpochSecond(objectDataInput.readLong(), 0, ZoneOffset.UTC);
        infractionCode = objectDataInput.readInt();
        fineAmount = objectDataInput.readFloat();
        countyName = objectDataInput.readUTF();
        issuingAgency = objectDataInput.readUTF();
    }

    @Override
    public String getInfractionCode() {
        return String.valueOf(infractionCode);
    }
}
