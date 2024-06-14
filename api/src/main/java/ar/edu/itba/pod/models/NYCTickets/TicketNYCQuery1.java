package ar.edu.itba.pod.models.NYCTickets;

import ar.edu.itba.pod.models.abstractClasses.Ticket;
import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;
import com.hazelcast.nio.serialization.DataSerializable;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Objects;

public class TicketNYCQuery1 extends Ticket implements DataSerializable {

    private int infractionCode;

    public TicketNYCQuery1() {
        // Necesario para hazelcast
    }

    public TicketNYCQuery1(int infractionCode) {
        this.infractionCode = infractionCode;
    }


    @Override
    public String getInfractionCode() {
        return String.valueOf(infractionCode);
    }

    @Override
    public String getCounty() {
        return null;
    }

    @Override
    public String getAgency() {
        return null;
    }

    @Override
    public String getPlate() {
        return null;
    }

    @Override
    public float getFineAmount() {
        return 0;
    }

    @Override
    public LocalDateTime getDate() {
        return null;
    }

    @Override
    public void writeData(ObjectDataOutput objectDataOutput) throws IOException {
        objectDataOutput.writeInt(infractionCode);
    }

    @Override
    public void readData(ObjectDataInput objectDataInput) throws IOException {
        infractionCode = objectDataInput.readInt();
    }

    public void setInfractionCode(int infractionCode) {
        this.infractionCode = infractionCode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TicketNYCQuery1 that)) return false;
        return infractionCode == that.infractionCode;
    }

    @Override
    public int hashCode() {
        return Objects.hash(infractionCode);
    }
}
