package ar.edu.itba.pod.models;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Objects;

import ar.edu.itba.pod.models.abstractClasses.Ticket;
import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;
import com.hazelcast.nio.serialization.DataSerializable;

public class TicketCHI extends Ticket implements DataSerializable{
    private LocalDateTime issueDate;
    private String licensePlateNumber;
    private String violationCode;
    private String unitDescription;
    private int fine;
    private String communityArea;

    public TicketCHI(){
        // Necessary for hazelcast
    }

    public TicketCHI(LocalDateTime issueDate, String licensePlateNumber, String violationCode, String unitDescription, int fine, String communityArea){
        this.issueDate = issueDate;
        this.licensePlateNumber = licensePlateNumber;
        this.violationCode = violationCode;
        this.unitDescription = unitDescription;
        this.fine = fine;
        this.communityArea = communityArea;
    }


    @Override
    public void writeData(ObjectDataOutput objectDataOutput) throws IOException {
        objectDataOutput.writeLong(issueDate.toEpochSecond(ZoneOffset.UTC));
        objectDataOutput.writeUTF(licensePlateNumber);
        objectDataOutput.writeUTF(violationCode);
        objectDataOutput.writeUTF(unitDescription);
        objectDataOutput.writeInt(fine);
        objectDataOutput.writeUTF(communityArea);
    }

    @Override
    public void readData(ObjectDataInput objectDataInput) throws IOException {
        issueDate = LocalDateTime.ofEpochSecond(objectDataInput.readLong(), 0, ZoneOffset.UTC);
        licensePlateNumber = objectDataInput.readUTF();
        violationCode = objectDataInput.readUTF();
        unitDescription = objectDataInput.readUTF();
        fine = objectDataInput.readInt();
        communityArea = objectDataInput.readUTF();
    }

    @Override
    public String getInfractionCode(){
        return violationCode;
    }

    @Override
    public String getCounty() {
        return communityArea;
    }

    @Override
    public String getAgency(){
        return unitDescription;
    }

    @Override
    public float getFineAmount() {
        return fine;
    }

    @Override
    public String getPlate() {
        return licensePlateNumber;
    }

    public LocalDateTime getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(LocalDateTime issueDate) {
        this.issueDate = issueDate;
    }

    public String getLicensePlateNumber() {
        return licensePlateNumber;
    }

    public void setLicensePlateNumber(String licensePlateNumber) {
        this.licensePlateNumber = licensePlateNumber;
    }

    public String getViolationCode() {
        return violationCode;
    }

    public void setViolationCode(String violationCode) {
        this.violationCode = violationCode;
    }

    public String getUnitDescription() {
        return unitDescription;
    }

    public void setUnitDescription(String unitDescription) {
        this.unitDescription = unitDescription;
    }

    public int getFine() {
        return fine;
    }

    public void setFine(int fine) {
        this.fine = fine;
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
        if (o == null || getClass() != o.getClass()) return false;
        TicketCHI ticketCHI = (TicketCHI) o;
        return fine == ticketCHI.fine && Objects.equals(issueDate, ticketCHI.issueDate) && Objects.equals(licensePlateNumber, ticketCHI.licensePlateNumber) && Objects.equals(violationCode, ticketCHI.violationCode) && Objects.equals(unitDescription, ticketCHI.unitDescription) && Objects.equals(communityArea, ticketCHI.communityArea);
    }

    @Override
    public int hashCode() {
        return Objects.hash(issueDate, licensePlateNumber, violationCode, unitDescription, fine, communityArea);
    }
}
