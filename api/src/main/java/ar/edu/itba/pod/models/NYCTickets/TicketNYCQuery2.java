package ar.edu.itba.pod.models.NYCTickets;

import ar.edu.itba.pod.models.abstractClasses.Ticket;
import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;
import com.hazelcast.nio.serialization.DataSerializable;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Objects;

public class TicketNYCQuery2 extends Ticket implements DataSerializable {
    private LocalDate issueDate;

    private int infractionCode;

    private String countyName;

    public TicketNYCQuery2(){
        // Necesario para hazelcast
    }

    public TicketNYCQuery2(LocalDate issueDate, int infractionCode, String countyName) {
        this.issueDate = issueDate;
        this.infractionCode = infractionCode;
        this.countyName = countyName;
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
    public void writeData(ObjectDataOutput objectDataOutput) throws IOException {
        objectDataOutput.writeLong(issueDate.toEpochDay());
        objectDataOutput.writeInt(infractionCode);
        objectDataOutput.writeUTF(countyName);
    }

    @Override
    public void readData(ObjectDataInput objectDataInput) throws IOException {
        issueDate = LocalDate.ofEpochDay(objectDataInput.readLong());
        infractionCode = objectDataInput.readInt();
        countyName = objectDataInput.readUTF();
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

    public String getCountyName() {
        return countyName;
    }

    public void setCountyName(String countyName) {
        this.countyName = countyName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TicketNYCQuery2 that)) return false;
        return infractionCode == that.infractionCode && Objects.equals(issueDate, that.issueDate) && Objects.equals(countyName, that.countyName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(issueDate, infractionCode, countyName);
    }
}
