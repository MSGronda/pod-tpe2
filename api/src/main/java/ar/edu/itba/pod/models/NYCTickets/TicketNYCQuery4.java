package ar.edu.itba.pod.models.NYCTickets;

import ar.edu.itba.pod.models.abstractClasses.Ticket;
import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;
import com.hazelcast.nio.serialization.DataSerializable;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.util.Objects;

public class TicketNYCQuery4 extends Ticket implements DataSerializable {
    private String plate;
    private LocalDate date;
    private String countyName;

    public TicketNYCQuery4() {
        // Necesario para hazelcast
    }

    public TicketNYCQuery4(String plate, LocalDate date, String countyName) {
        this.plate = plate;
        this.date = date;
        this.countyName = countyName;
    }


    @Override
    public String getInfractionCode() {
        return null;
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
        return plate;
    }

    @Override
    public float getFineAmount() {
        return 0;
    }

    @Override
    public void writeData(ObjectDataOutput objectDataOutput) throws IOException {
        objectDataOutput.writeUTF(plate);
        objectDataOutput.writeUTF(countyName);
        objectDataOutput.writeLong(date.toEpochSecond(LocalTime.MIDNIGHT, ZoneOffset.UTC));
    }

    @Override
    public LocalDateTime getDate() {
        return date.atStartOfDay();
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TicketNYCQuery4 that = (TicketNYCQuery4) o;
        return Objects.equals(plate, that.plate) && Objects.equals(date, that.date) && Objects.equals(countyName, that.countyName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(plate, date, countyName);
    }

    @Override
    public void readData(ObjectDataInput objectDataInput) throws IOException {
        plate = objectDataInput.readUTF();
        countyName = objectDataInput.readUTF();
        date = LocalDateTime.ofEpochSecond(objectDataInput.readLong(), 0, ZoneOffset.UTC).toLocalDate();
    }

    public void setPlate(String plate) {
        this.plate = plate;
    }

    public String getCountyName() {
        return countyName;
    }

    public void setCountyName(String countyName) {
        this.countyName = countyName;
    }

}
