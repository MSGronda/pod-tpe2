package ar.edu.itba.pod.client;

import ar.edu.itba.pod.Constants;
import ar.edu.itba.pod.client.utils.Argument;
import ar.edu.itba.pod.client.utils.ArgumentCollector;
import ar.edu.itba.pod.models.abstractClasses.Infraction;
import ar.edu.itba.pod.models.abstractClasses.Ticket;
import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.client.config.ClientNetworkConfig;
import com.hazelcast.config.GroupConfig;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import com.hazelcast.mapreduce.Job;
import com.hazelcast.mapreduce.JobTracker;
import com.hazelcast.mapreduce.KeyValueSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

@SuppressWarnings("deprecation")
public class Client {
    private static final Logger logger = LoggerFactory.getLogger(Client.class);
    private static final Logger timingsLogger = LoggerFactory.getLogger("TimingsLogger");

    public static void main(String[] args) {
        Argument arguments = ArgumentCollector.obtainArguments();

        logger.info("Starting Hazelcast client");
        HazelcastInstance hz = startHazelcastClient(arguments.getAddresses());
        logger.info("Hazelcast client started");

        IMap<String, Infraction> infractions = hz.getMap(Constants.INFRACTION_MAP);
        IMap<Long, Ticket> tickets = hz.getMap(Constants.TICKET_LIST);

        logger.info("Loading data");
        timingsLogger.info("Loading data");
        arguments.getCity().loadData(arguments, infractions, tickets);
        timingsLogger.info("Loading data finished");
        logger.info("Loading data finished");

        JobTracker jobTracker = hz.getJobTracker(Constants.HZ_NAMESPACE);

        KeyValueSource<Long, Ticket> source = KeyValueSource.fromMap(hz.getMap(Constants.TICKET_LIST));

        Job<Long, Ticket> job = jobTracker.newJob(source);

        try {
            logger.info("Starting MapReduce");
            timingsLogger.info("Starting MapReduce");
            arguments.getQuery().realizeMapReduce(job, arguments, hz);
            timingsLogger.info("MapReduce finished");
            logger.info("MapReduce finished");
        } catch (InterruptedException | ExecutionException | IOException e) {
            logger.error("Oops! Something went wrong", e);
        } finally {
            logger.info("Clearing maps used");
            infractions.clear();
            tickets.clear();
            logger.info("Shutting down Hazelcast client");
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
