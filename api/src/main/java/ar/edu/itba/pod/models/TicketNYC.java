package ar.edu.itba.pod.models;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Objects;

import ar.edu.itba.pod.models.abstractClasses.Ticket;
import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;
import com.hazelcast.nio.serialization.DataSerializable;

public class TicketNYC extends Ticket implements DataSerializable {

    private String plate;
    private LocalDate issueDate;
    private int infractionCode;
    private float fineAmount;
    private String countyName;
    private String issuingAgency;

    public TicketNYC(){
        // Necessary for hazelcast
    }

    public TicketNYC(
        String plate,
        LocalDate issueDate,
        int infractionCode,
        float fineAmount,
        String countyName,
        String issuingAgency
    ){
        this.plate = plate;
        this.issueDate = issueDate;
        this.infractionCode = infractionCode;
        this.fineAmount = fineAmount;
        this.countyName = countyName;
        this.issuingAgency = issuingAgency;
    }

    @Override
    public void writeData(ObjectDataOutput objectDataOutput) throws IOException {
        objectDataOutput.writeUTF(plate);
        objectDataOutput.writeLong(issueDate.toEpochDay());
        objectDataOutput.writeInt(infractionCode);
        objectDataOutput.writeFloat(fineAmount);
        objectDataOutput.writeUTF(countyName);
        objectDataOutput.writeUTF(issuingAgency);

    }

    @Override
    public void readData(ObjectDataInput objectDataInput) throws IOException {
        plate = objectDataInput.readUTF();
        issueDate = LocalDate.ofEpochDay(objectDataInput.readLong());
        infractionCode = objectDataInput.readInt();
        fineAmount = objectDataInput.readFloat();
        countyName = objectDataInput.readUTF();
        issuingAgency = objectDataInput.readUTF();
    }

    @Override
    public String getInfractionCode() {
        return String.valueOf(infractionCode);
    }
    @Override
    public String getCounty() {
        return countyName;
    }
    @Override
    public float getFineAmount() {
        return fineAmount;
    }

    @Override
    public String getAgency() {
        return issuingAgency;
    }

    public String getPlate() {
        return plate;
    }

    public void setPlate(String plate) {
        this.plate = plate;
    }

    public LocalDate getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(LocalDate issueDate) {
        this.issueDate = issueDate;
    }

    public void setInfractionCode(int infractionCode) {
        this.infractionCode = infractionCode;
    }

    public void setFineAmount(float fineAmount) {
        this.fineAmount = fineAmount;
    }

    public String getCountyName() {
        return countyName;
    }

    public void setCountyName(String countyName) {
        this.countyName = countyName;
    }

    public String getIssuingAgency() {
        return issuingAgency;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TicketNYC ticketNYC = (TicketNYC) o;
        return infractionCode == ticketNYC.infractionCode && Float.compare(fineAmount, ticketNYC.fineAmount) == 0 && Objects.equals(plate, ticketNYC.plate) && Objects.equals(issueDate, ticketNYC.issueDate) && Objects.equals(countyName, ticketNYC.countyName) && Objects.equals(issuingAgency, ticketNYC.issuingAgency);
    }

    @Override
    public int hashCode() {
        return Objects.hash(plate, issueDate, infractionCode, fineAmount, countyName, issuingAgency);
    }

    public void setIssuingAgency(String issuingAgency) {
        this.issuingAgency = issuingAgency;
    }
}
