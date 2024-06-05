package ar.edu.itba.pod.client;

import ar.edu.itba.pod.client.utils.Argument;
import ar.edu.itba.pod.client.utils.Constants;
import ar.edu.itba.pod.client.utils.DatasetHelper;
import ar.edu.itba.pod.models.InfractionCHI;
import ar.edu.itba.pod.models.InfractionNYC;
import ar.edu.itba.pod.models.TicketCHI;
import ar.edu.itba.pod.models.TicketNYC;
import ar.edu.itba.pod.models.abstractClasses.Infraction;
import ar.edu.itba.pod.models.abstractClasses.Ticket;
import com.hazelcast.core.IMap;
import com.hazelcast.core.MultiMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Stream;

public enum City {
    CHI {
        @Override
        public void loadData(Argument arguments,
                             IMap<String, Infraction> infractions,
                             MultiMap<Long, Ticket> tickets) {
                DatasetHelper.loadCHIData(
                        String.format(Constants.INFRACTIONS_FILENAME_FMT, this.name()),
                        infractions,
                        DatasetHelper.CsvReaderType.SEQUENTIAL,
                        String.format(Constants.TICKETS_FILENAME_FMT, this.name()),
                        tickets,
                        DatasetHelper.CsvReaderType.PARALLEL
                );
        }
    }, NYC {
        @Override
        public void loadData(Argument arguments, IMap<String, Infraction> infractions, MultiMap<Long, Ticket> tickets) {
            DatasetHelper.loadNYCData(
                    String.format(Constants.INFRACTIONS_FILENAME_FMT, this.name()),
                    infractions,
                    DatasetHelper.CsvReaderType.SEQUENTIAL,
                    String.format(Constants.TICKETS_FILENAME_FMT, this.name()),
                    tickets,
                    DatasetHelper.CsvReaderType.PARALLEL
            );
        }
    };

    public abstract void loadData(Argument arguments, IMap<String, Infraction> infractions, MultiMap<Long, Ticket> tickets);
}
