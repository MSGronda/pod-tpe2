package ar.edu.itba.pod.models.CHITickets;

import ar.edu.itba.pod.models.abstractClasses.Ticket;
import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;
import com.hazelcast.nio.serialization.DataSerializable;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Objects;

public class TicketCHIQuery3 extends Ticket implements DataSerializable {
    private String unitDescription;
    private int fine;

    public TicketCHIQuery3() {
        // Necessary for hazelcast
    }

    public TicketCHIQuery3(String unitDescription, int fine) {
        this.unitDescription = unitDescription;
        this.fine = fine;
    }

    @Override
    public LocalDateTime getDate() {
        return null;
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
        objectDataOutput.writeUTF(unitDescription);
        objectDataOutput.writeInt(fine);
    }

    @Override
    public void readData(ObjectDataInput objectDataInput) throws IOException {
        unitDescription = objectDataInput.readUTF();
        fine = objectDataInput.readInt();
    }

    public void setFine(int fine) {
        this.fine = fine;
    }

    public void setUnitDescription(String unitDescription) {
        this.unitDescription = unitDescription;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TicketCHIQuery3 that = (TicketCHIQuery3) o;
        return fine == that.fine && Objects.equals(unitDescription, that.unitDescription);
    }

    @Override
    public int hashCode() {
        return Objects.hash(unitDescription, fine);
    }
}
