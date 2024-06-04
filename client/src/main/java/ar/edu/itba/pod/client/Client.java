package ar.edu.itba.pod.client;

import ar.edu.itba.pod.Constants;
import ar.edu.itba.pod.client.Util.Argument;
import ar.edu.itba.pod.client.Util.ArgumentCollector;
import ar.edu.itba.pod.models.abstractClasses.Infraction;
import ar.edu.itba.pod.models.abstractClasses.Ticket;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

public class Client {
    private static final Logger logger = LoggerFactory.getLogger(Client.class);

    public static void main(String[] args) {
        Argument arguments = ArgumentCollector.obtainArguments();

        HazelcastInstance hz = startHazelcastClient(arguments.getAddresses());

        IMap<String, Infraction> infractions = hz.getMap(Constants.INFRACTION_MAP);
        MultiMap<Long, Ticket> tickets = hz.getMultiMap(Constants.TICKET_LIST);

        logger.info("Loading data");
        arguments.getCity().loadData(arguments, infractions, tickets);
        logger.info("Loading data finished");


        JobTracker jobTracker = hz.getJobTracker(Constants.HZ_NAMESPACE);

        KeyValueSource<Long, Ticket> source = KeyValueSource.fromMultiMap(hz.getMultiMap(Constants.TICKET_LIST));

        Job<Long, Ticket> job = jobTracker.newJob(source);


        try {
            logger.info("Starting MapReduce");
            arguments.getQuery().realizeMapReduce(job, arguments);
            logger.info("MapReduce finished");
        } catch (InterruptedException | ExecutionException | IOException e) {
            logger.error("Oops! Something went wrong", e);
        } finally {
            infractions.clear();
            tickets.clear();
            hz.shutdown();
        }
    }

    private static HazelcastInstance startHazelcastClient(String[] addresses) {
        ClientConfig clientConfig = new ClientConfig();

        clientConfig.setGroupConfig(new GroupConfig().setName(Constants.HZ_NAME).setPassword(Constants.HZ_PASSWORD));

        ClientNetworkConfig clientNetworkConfig = new ClientNetworkConfig();

        clientNetworkConfig.addAddress(addresses);

        clientConfig.setNetworkConfig(clientNetworkConfig);

        clientConfig.setProperty("hazelcast.logging.type", "none");

        return HazelcastClient.newHazelcastClient(clientConfig);
    }

}
