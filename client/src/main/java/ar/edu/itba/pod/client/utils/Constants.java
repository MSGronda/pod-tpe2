package ar.edu.itba.pod.client.utils;

import java.util.concurrent.TimeUnit;

public class Constants {

    public static final String INFRACTIONS_FILENAME_FMT = "infractions%s.csv";
    public static final String TICKETS_FILENAME_FMT = "tickets%s.csv";

    public static final Long TIMEOUT_AWAIT = 2L;
    public static final TimeUnit TIMEOUT_AWAIT_UNIT = TimeUnit.MINUTES;

}
