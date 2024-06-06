package ar.edu.itba.pod.models.CHITickets;

import ar.edu.itba.pod.models.abstractClasses.Ticket;
import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;
import com.hazelcast.nio.serialization.DataSerializable;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Objects;


public class TicketCHIQuery1 extends Ticket implements DataSerializable {
    private LocalDateTime issueDate;

    private String violationCode;

    public TicketCHIQuery1(){
        // Necesario para hazelcast
    }

    public TicketCHIQuery1(LocalDateTime issueDate, String violationCode) {
        this.issueDate = issueDate;
        this.violationCode = violationCode;
    }


    @Override
    public String getInfractionCode() {
        return violationCode;
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
    public void writeData(ObjectDataOutput objectDataOutput) throws IOException {
        objectDataOutput.writeLong(issueDate.toEpochSecond(ZoneOffset.UTC));
        objectDataOutput.writeUTF(violationCode);
    }

    @Override
    public void readData(ObjectDataInput objectDataInput) throws IOException {
        issueDate = LocalDateTime.ofEpochSecond(objectDataInput.readLong(), 0, ZoneOffset.UTC);
        violationCode = objectDataInput.readUTF();
    }

    public LocalDateTime getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(LocalDateTime issueDate) {
        this.issueDate = issueDate;
    }

    public String getViolationCode() {
        return violationCode;
    }

    public void setViolationCode(String violationCode) {
        this.violationCode = violationCode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TicketCHIQuery1 that)) return false;
        return Objects.equals(issueDate, that.issueDate) && Objects.equals(violationCode, that.violationCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(issueDate, violationCode);
    }
}
