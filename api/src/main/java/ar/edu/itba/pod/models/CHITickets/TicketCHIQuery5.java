package ar.edu.itba.pod.models.CHITickets;

import ar.edu.itba.pod.models.abstractClasses.Ticket;
import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;
import com.hazelcast.nio.serialization.DataSerializable;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Objects;

public class TicketCHIQuery5 extends Ticket implements DataSerializable {
    private String violationCode;

    private int fine;

    public TicketCHIQuery5() {
        // Necesario para hazelcast
    }

    public TicketCHIQuery5(String violationCode, int fine) {
        this.violationCode = violationCode;
        this.fine = fine;
    }

    @Override
    public LocalDateTime getDate() {
        return null;
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
        return fine;
    }

    @Override
    public void writeData(ObjectDataOutput objectDataOutput) throws IOException {
        objectDataOutput.writeUTF(violationCode);
        objectDataOutput.writeInt(fine);
    }

    @Override
    public void readData(ObjectDataInput objectDataInput) throws IOException {
        violationCode = objectDataInput.readUTF();
        fine = objectDataInput.readInt();
    }

    public String getViolationCode() {
        return violationCode;
    }

    public void setViolationCode(String violationCode) {
        this.violationCode = violationCode;
    }

    public int getFine() {
        return fine;
    }

    public void setFine(int fine) {
        this.fine = fine;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TicketCHIQuery5 that)) return false;
        return fine == that.fine && Objects.equals(violationCode, that.violationCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(violationCode, fine);
    }
}
