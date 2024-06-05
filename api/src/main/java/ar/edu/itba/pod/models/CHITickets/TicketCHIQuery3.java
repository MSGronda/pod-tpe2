package ar.edu.itba.pod.models.CHITickets;

import ar.edu.itba.pod.models.abstractClasses.Ticket;
import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;
import com.hazelcast.nio.serialization.DataSerializable;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

public class TicketCHIQuery3 extends Ticket implements DataSerializable {
    private LocalDateTime issueDate;
    private String unitDescription;
    private int fine;

    public TicketCHIQuery3() {
        // Necessary for hazelcast
    }

    public TicketCHIQuery3(LocalDateTime issueDate, String unitDescription, int fine) {
        this.issueDate = issueDate;
        this.unitDescription = unitDescription;
        this.fine = fine;
    }

    @Override
    public String getInfractionCode() {
        return null;
    }

    @Override
    public String getCounty() {
        return null;
    }

    @Override
    public String getAgency() {
        return unitDescription;
    }

    @Override
    public String getPlate() {
        return null;
    }

    @Override
    public float getFineAmount() {
        return fine;
    }

    @Override
    public void writeData(ObjectDataOutput objectDataOutput) throws IOException {
        objectDataOutput.writeLong(issueDate.toEpochSecond(ZoneOffset.UTC));
        objectDataOutput.writeUTF(unitDescription);
        objectDataOutput.writeInt(fine);
    }

    @Override
    public void readData(ObjectDataInput objectDataInput) throws IOException {
        issueDate = LocalDateTime.ofEpochSecond(objectDataInput.readLong(), 0, ZoneOffset.UTC);
        unitDescription = objectDataInput.readUTF();
        fine = objectDataInput.readInt();
    }
}
