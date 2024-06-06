package ar.edu.itba.pod.models.NYCTickets;

import ar.edu.itba.pod.models.abstractClasses.Ticket;
import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;
import com.hazelcast.nio.serialization.DataSerializable;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Objects;

public class TicketNYCQuery3 extends Ticket implements DataSerializable {

    private LocalDate issueDate;

    private float fineAmount;

    private String issuingAgency;

    public TicketNYCQuery3(){
        // Necesario para hazelcast
    }

    public TicketNYCQuery3(LocalDate issueDate, float fineAmount, String issuingAgency) {
        this.issueDate = issueDate;
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
    public void writeData(ObjectDataOutput objectDataOutput) throws IOException {
        objectDataOutput.writeLong(issueDate.toEpochDay());
        objectDataOutput.writeFloat(fineAmount);
        objectDataOutput.writeUTF(issuingAgency);
    }

    @Override
    public void readData(ObjectDataInput objectDataInput) throws IOException {
        issueDate = LocalDate.ofEpochDay(objectDataInput.readLong());
        fineAmount = objectDataInput.readFloat();
        issuingAgency = objectDataInput.readUTF();
    }

    public LocalDate getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(LocalDate issueDate) {
        this.issueDate = issueDate;
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
        return Float.compare(fineAmount, that.fineAmount) == 0 && Objects.equals(issueDate, that.issueDate) && Objects.equals(issuingAgency, that.issuingAgency);
    }

    @Override
    public int hashCode() {
        return Objects.hash(issueDate, fineAmount, issuingAgency);
    }
}
