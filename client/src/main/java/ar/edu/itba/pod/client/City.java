package ar.edu.itba.pod.client;

import ar.edu.itba.pod.client.utils.Argument;
import ar.edu.itba.pod.client.utils.Constants;
import ar.edu.itba.pod.client.utils.DatasetHelper;
import ar.edu.itba.pod.models.abstractClasses.Infraction;
import ar.edu.itba.pod.models.abstractClasses.Ticket;
import com.hazelcast.core.IMap;


public enum City {
    CHI {
        @Override
        public void loadData(Argument arguments, IMap<String, Infraction> infractions, IMap<Long, Ticket> tickets) {
                DatasetHelper.loadTicketBatched(
                        arguments.getInPath().resolve(String.format(Constants.INFRACTIONS_FILENAME_FMT, this.name())),
                        infractions,
                        DatasetHelper.chiInfractionCreator,
                        arguments.getInPath().resolve(String.format(Constants.TICKETS_FILENAME_FMT, this.name())),
                        tickets,
                        arguments.getQuery(),
                        DatasetHelper.chiTicketCreator,
                        DatasetHelper.chiFormatter
                );
        }
    }, NYC {
        @Override
        public void loadData(Argument arguments, IMap<String, Infraction> infractions, IMap<Long, Ticket> tickets) {
            DatasetHelper.loadTicketBatched(
                    arguments.getInPath().resolve(String.format(Constants.INFRACTIONS_FILENAME_FMT, this.name())),
                    infractions,
                    DatasetHelper.nycInfractionCreator,
                    arguments.getInPath().resolve(String.format(Constants.TICKETS_FILENAME_FMT, this.name())),
                    tickets,
                    arguments.getQuery(),
                    DatasetHelper.nycTicketCreator,
                    DatasetHelper.nycFormatter
            );
        }
    };

    public abstract void loadData(Argument arguments, IMap<String, Infraction> infractions, IMap<Long, Ticket> tickets);
}
