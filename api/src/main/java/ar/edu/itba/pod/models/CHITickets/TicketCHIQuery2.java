package ar.edu.itba.pod.models.CHITickets;

import ar.edu.itba.pod.models.abstractClasses.Ticket;
import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;
import com.hazelcast.nio.serialization.DataSerializable;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Objects;

public class TicketCHIQuery2 extends Ticket implements DataSerializable {
    private LocalDateTime issueDate;

    private String violationCode;

    private String communityArea;

    public TicketCHIQuery2(LocalDateTime issueDate, String violationCode, String communityArea) {
        this.issueDate = issueDate;
        this.violationCode = violationCode;
        this.communityArea = communityArea;
    }


    @Override
    public String getInfractionCode() {
        return violationCode;
    }

    @Override
    public String getCounty() {
        return communityArea;
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
        objectDataOutput.writeUTF(communityArea);
    }

    @Override
    public void readData(ObjectDataInput objectDataInput) throws IOException {
        issueDate = LocalDateTime.ofEpochSecond(objectDataInput.readLong(), 0, ZoneOffset.UTC);
        violationCode = objectDataInput.readUTF();
        communityArea = objectDataInput.readUTF();
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

    public String getCommunityArea() {
        return communityArea;
    }

    public void setCommunityArea(String communityArea) {
        this.communityArea = communityArea;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TicketCHIQuery2 that)) return false;
        return Objects.equals(issueDate, that.issueDate) && Objects.equals(violationCode, that.violationCode) && Objects.equals(communityArea, that.communityArea);
    }

    @Override
    public int hashCode() {
        return Objects.hash(issueDate, violationCode, communityArea);
    }
}
