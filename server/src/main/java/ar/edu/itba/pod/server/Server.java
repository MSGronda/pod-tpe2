package ar.edu.itba.pod.server;

import ar.edu.itba.pod.Constants;
import com.hazelcast.config.*;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.Optional;

public class Server {
    private static Logger logger = LoggerFactory.getLogger(Server.class);

    public static void main(String[] args) {
        logger.info("Initializing Hazelcast node...");

        Config hzConfig = new Config();

        // Deshabilitamos el logging molesto
        hzConfig.setProperty("hazelcast.logging.type", "none");

        hzConfig.setGroupConfig(new GroupConfig().setName(Constants.HZ_NAME).setPassword(Constants.HZ_PASSWORD));

        NetworkConfig networkConfig = new NetworkConfig().setJoin(new JoinConfig().setMulticastConfig(new MulticastConfig()));

        // Opcional: especificar el interface
        //TODO check
        Optional<String> networkInterface = Optional.ofNullable(System.getProperty("interface"));
        networkInterface.ifPresent(s -> networkConfig.setInterfaces(new InterfacesConfig().setInterfaces(Collections.singletonList(s)).setEnabled(true)));

        hzConfig.setNetworkConfig(networkConfig);

        HazelcastInstance instance = Hazelcast.newHazelcastInstance(hzConfig);
        logger.info("New Hazelcast node @ " + instance.getCluster().getLocalMember().getAddress());
    }
}
