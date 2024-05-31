package ar.edu.itba.pod.models;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.UUID;

import ar.edu.itba.pod.models.abstractClasses.Ticket;
import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;
import com.hazelcast.nio.serialization.DataSerializable;

public class TicketCHI extends Ticket implements DataSerializable{
    private LocalDateTime issueDate;
    private UUID licensePlateNumber;
    private String violationCode;
    private String unitDescription;
    private int fine;
    private String communityArea;

    public TicketCHI(LocalDateTime issueDate, UUID licensePlateNumber, String violationCode, String unitDescription, int fine, String communityArea){
        this.issueDate = issueDate;
        this.licensePlateNumber = licensePlateNumber;
        this.violationCode = violationCode;
        this.unitDescription = unitDescription;
        this.fine = fine;
        this.communityArea = communityArea;
    }


    @Override
    public void writeData(ObjectDataOutput objectDataOutput) throws IOException {
        objectDataOutput.writeLong(issueDate.toEpochSecond(ZoneOffset.UTC));
        objectDataOutput.writeUTF(licensePlateNumber.toString());
        objectDataOutput.writeUTF(violationCode);
        objectDataOutput.writeUTF(unitDescription);
        objectDataOutput.writeInt(fine);
        objectDataOutput.writeUTF(communityArea);
    }

    @Override
    public void readData(ObjectDataInput objectDataInput) throws IOException {
        issueDate = LocalDateTime.ofEpochSecond(objectDataInput.readLong(), 0, ZoneOffset.UTC);
        licensePlateNumber = UUID.fromString(objectDataInput.readUTF());
        violationCode = objectDataInput.readUTF();
        unitDescription = objectDataInput.readUTF();
        fine = objectDataInput.readInt();
        communityArea = objectDataInput.readUTF();
    }

    @Override
    public String getInfractionCode(){
        return violationCode;
    }

    @Override
    public String getCounty() {
        return communityArea;
    }
}
