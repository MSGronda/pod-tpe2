package ar.edu.itba.pod.client;

import ar.edu.itba.pod.Constants;
import ar.edu.itba.pod.client.utils.DatasetHelper;
import ar.edu.itba.pod.models.StringPair;
import ar.edu.itba.pod.models.abstractClasses.Infraction;
import ar.edu.itba.pod.models.abstractClasses.Ticket;
import ar.edu.itba.pod.query1.TotalInfractionsMapper;
import ar.edu.itba.pod.query1.TotalInfractionsReducer;
import ar.edu.itba.pod.query5.AverageFineMapper;
import ar.edu.itba.pod.query5.AverageFineReducer;
import ar.edu.itba.pod.query5.GroupingByFineMapper;
import ar.edu.itba.pod.query5.GroupingByFineReducer;
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

import java.util.List;
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
                "./ticketsNYCSubset.csv",
                tickets,
                DatasetHelper.CsvReaderType.PARALLEL,
                Query.FIVE
        );

        // CORRER EL JOB
        JobTracker jobTracker = hazelcastInstance.getJobTracker(Constants.HZ_NAMESPACE);
        KeyValueSource<Long, Ticket> source = KeyValueSource.fromMultiMap(hazelcastInstance.getMultiMap(Constants.TICKET_LIST));
        Job<Long, Ticket> job = jobTracker.newJob(source);
        Map<String, Float> partialResult = job
                .mapper(new AverageFineMapper())
                .reducer(new AverageFineReducer())
                .submit()
                .get();

        IMap<String, Float> averageFines = hazelcastInstance.getMap("average-fine-results");
        averageFines.putAll(partialResult);

        partialResult.forEach((s, f) -> System.out.println(s + " "  + f));


        // CORRER EL JOB
        JobTracker jobTracker2 = hazelcastInstance.getJobTracker(Constants.HZ_NAMESPACE);
        KeyValueSource<String, Float> source2 = KeyValueSource.fromMap(averageFines);
        Job<String, Float> job2 = jobTracker2.newJob(source2);
        Map<Integer, List<StringPair>> reducedData = job2
                .mapper(new GroupingByFineMapper())
                .reducer(new GroupingByFineReducer())
                .submit()
                .get();

        reducedData.forEach((integer, stringPairs) -> System.out.println(integer + " " + stringPairs.toString()));
    }
}
