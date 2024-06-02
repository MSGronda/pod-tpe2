package ar.edu.itba.pod.client;

import ar.edu.itba.pod.Constants;
import ar.edu.itba.pod.client.utils.DatasetHelper;
import ar.edu.itba.pod.models.abstractClasses.Infraction;
import ar.edu.itba.pod.models.abstractClasses.Ticket;
import ar.edu.itba.pod.query1.TotalInfractionsMapper;
import ar.edu.itba.pod.query1.TotalInfractionsReducer;
import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.client.config.ClientNetworkConfig;
import com.hazelcast.config.GroupConfig;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import com.hazelcast.core.MultiMap;
import com.hazelcast.mapreduce.Job;
import com.hazelcast.mapreduce.JobTracker;
import com.hazelcast.mapreduce.KeyValueSource;

import java.util.Map;
import java.util.concurrent.ExecutionException;

public class TestClient {


    public static void main(String[] args) throws ExecutionException, InterruptedException {

        // CONFIGURACION
        ClientConfig clientConfig = new ClientConfig();

        clientConfig.setGroupConfig(new GroupConfig().setName(Constants.HZ_NAME).setPassword(Constants.HZ_PASSWORD));

        clientConfig.setNetworkConfig(new ClientNetworkConfig().addAddress("192.168.0.139:5701"));

        clientConfig.setProperty("hazelcast.logging.type", "none");

        HazelcastInstance hazelcastInstance = HazelcastClient.newHazelcastClient(clientConfig);     // Creamos el cliente

        // CARGADO DE DATOS

        IMap<String, Infraction> infractions = hazelcastInstance.getMap(Constants.INFRACTION_MAP);
        MultiMap<Long, Ticket> tickets = hazelcastInstance.getMultiMap(Constants.TICKET_LIST);

        DatasetHelper.loadNYCData(
                "./infractionsNYC.csv",
                infractions,
                DatasetHelper.CsvReaderType.SEQUENTIAL,
                "./ticketsNYC.csv",
                tickets,
                DatasetHelper.CsvReaderType.PARALLEL
        );

        // CORRER EL JOB

        JobTracker jobTracker = hazelcastInstance.getJobTracker(Constants.HZ_NAMESPACE);

        KeyValueSource<Long, Ticket> source = KeyValueSource.fromMultiMap(hazelcastInstance.getMultiMap(Constants.TICKET_LIST));

        Job<Long, Ticket> job = jobTracker.newJob(source);

        Map<String, Long> reducedData = job
                .mapper(new TotalInfractionsMapper())
                .reducer(new TotalInfractionsReducer())
                .submit()
                .get();

        System.out.println(reducedData);
    }
}
