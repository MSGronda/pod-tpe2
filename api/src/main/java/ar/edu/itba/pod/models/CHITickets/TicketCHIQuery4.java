package ar.edu.itba.pod.models.CHITickets;

import ar.edu.itba.pod.models.abstractClasses.Ticket;
import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;
import com.hazelcast.nio.serialization.DataSerializable;

import java.io.IOException;
import java.util.Objects;

public class TicketCHIQuery4 extends Ticket implements DataSerializable {

    private String licensePlateNumber;

    private String communityArea;

    TicketCHIQuery4(){
        // Necesario para hazelcast
    }

    public TicketCHIQuery4(String licensePlateNumber, String communityArea) {
        this.licensePlateNumber = licensePlateNumber;
        this.communityArea = communityArea;
    }

    public String getLicensePlateNumber() {
        return licensePlateNumber;
    }

    public String getCommunityArea() {
        return communityArea;
    }

    public void setLicensePlateNumber(String licensePlateNumber) {
        this.licensePlateNumber = licensePlateNumber;
    }

    public void setCommunityArea(String communityArea) {
        this.communityArea = communityArea;
    }

    @Override
    public String getInfractionCode() {
        return null;
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
        return licensePlateNumber;
    }

    @Override
    public float getFineAmount() {
        return 0;
    }

    @Override
    public void writeData(ObjectDataOutput objectDataOutput) throws IOException {
        objectDataOutput.writeUTF(licensePlateNumber);
        objectDataOutput.writeUTF(communityArea);
    }

    @Override
    public void readData(ObjectDataInput objectDataInput) throws IOException {
        licensePlateNumber = objectDataInput.readUTF();
        communityArea = objectDataInput.readUTF();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TicketCHIQuery4 that)) return false;
        return Objects.equals(licensePlateNumber, that.licensePlateNumber) && Objects.equals(communityArea, that.communityArea);
    }

    @Override
    public int hashCode() {
        return Objects.hash(licensePlateNumber, communityArea);
    }
}
