package ar.edu.itba.pod.models.NYCTickets;

import ar.edu.itba.pod.models.abstractClasses.Ticket;
import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;
import com.hazelcast.nio.serialization.DataSerializable;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Objects;

public class TicketNYCQuery3 extends Ticket implements DataSerializable {
    private float fineAmount;

    private String issuingAgency;

    public TicketNYCQuery3() {
        // Necesario para hazelcast
    }

    public TicketNYCQuery3(float fineAmount, String issuingAgency) {
        this.fineAmount = fineAmount;
        this.issuingAgency = issuingAgency;
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
        return issuingAgency;
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
    public LocalDateTime getDate() {
        return null;
    }

    @Override
    public void writeData(ObjectDataOutput objectDataOutput) throws IOException {
        objectDataOutput.writeFloat(fineAmount);
        objectDataOutput.writeUTF(issuingAgency);
    }

    @Override
    public void readData(ObjectDataInput objectDataInput) throws IOException {
        fineAmount = objectDataInput.readFloat();
        issuingAgency = objectDataInput.readUTF();
    }

    public void setFineAmount(float fineAmount) {
        this.fineAmount = fineAmount;
    }

    public String getIssuingAgency() {
        return issuingAgency;
    }

    public void setIssuingAgency(String issuingAgency) {
        this.issuingAgency = issuingAgency;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TicketNYCQuery3 that)) return false;
        return Float.compare(fineAmount, that.fineAmount) == 0 && Objects.equals(issuingAgency, that.issuingAgency);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fineAmount, issuingAgency);
    }
}
