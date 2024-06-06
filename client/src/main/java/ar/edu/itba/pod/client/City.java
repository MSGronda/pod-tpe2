package ar.edu.itba.pod.client;

import ar.edu.itba.pod.client.utils.Argument;
import ar.edu.itba.pod.client.utils.Constants;
import ar.edu.itba.pod.client.utils.DatasetHelper;
import ar.edu.itba.pod.models.abstractClasses.Infraction;
import ar.edu.itba.pod.models.abstractClasses.Ticket;
import com.hazelcast.core.IMap;
import com.hazelcast.core.MultiMap;


public enum City {
    CHI {
        @Override
        public void loadData(Argument arguments, IMap<String, Infraction> infractions, IMap<Long, Ticket> tickets) {
                DatasetHelper.loadCHIData(
                        String.format(Constants.INFRACTIONS_FILENAME_FMT, this.name()),
                        infractions,
                        DatasetHelper.CsvReaderType.SEQUENTIAL,
                        String.format(Constants.TICKETS_FILENAME_FMT, this.name()),
                        tickets,
                        DatasetHelper.CsvReaderType.PARALLEL,
                        arguments.getQuery()
                );
        }
    }, NYC {
        @Override
        public void loadData(Argument arguments, IMap<String, Infraction> infractions, IMap<Long, Ticket> tickets) {
            DatasetHelper.loadNYCData(
                    String.format(Constants.INFRACTIONS_FILENAME_FMT, this.name()),
                    infractions,
                    DatasetHelper.CsvReaderType.SEQUENTIAL,
                    String.format(Constants.TICKETS_FILENAME_FMT, this.name()),
                    tickets,
                    DatasetHelper.CsvReaderType.PARALLEL,
                    arguments.getQuery()
            );
        }
    };

    public abstract void loadData(Argument arguments, IMap<String, Infraction> infractions, IMap<Long, Ticket> tickets);
}
