package ar.edu.itba.pod.models.NYCTickets;

import ar.edu.itba.pod.models.abstractClasses.Ticket;
import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;
import com.hazelcast.nio.serialization.DataSerializable;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Objects;

public class TicketNYCQuery5 extends Ticket implements DataSerializable {
    private int infractionCode;
    private float fineAmount;

    public TicketNYCQuery5(){
        // Necesario para hazelcast
    }

    public TicketNYCQuery5(int infractionCode, float fineAmount) {
        this.infractionCode = infractionCode;
        this.fineAmount = fineAmount;
    }

    @Override
    public LocalDateTime getDate() {
        return null;
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
        return fineAmount;
    }

    @Override
    public void writeData(ObjectDataOutput objectDataOutput) throws IOException {
        objectDataOutput.writeInt(infractionCode);
        objectDataOutput.writeFloat(fineAmount);
    }

    @Override
    public void readData(ObjectDataInput objectDataInput) throws IOException {
        infractionCode = objectDataInput.readInt();
        fineAmount = objectDataInput.readFloat();
    }

    public void setInfractionCode(int infractionCode) {
        this.infractionCode = infractionCode;
    }

    public void setFineAmount(float fineAmount) {
        this.fineAmount = fineAmount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TicketNYCQuery5 that)) return false;
        return infractionCode == that.infractionCode && Float.compare(fineAmount, that.fineAmount) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(infractionCode, fineAmount);
    }
}
