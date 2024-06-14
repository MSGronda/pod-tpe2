package ar.edu.itba.pod.models.abstractClasses;

import java.time.LocalDateTime;

public abstract class Ticket {

    public abstract String getInfractionCode();

    public abstract String getCounty();

    public abstract String getAgency();

    public abstract String getPlate();

    public abstract float getFineAmount();

    public abstract LocalDateTime getDate();

}
