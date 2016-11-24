package com.enclave.acton.VertxService;

import java.io.File;
import java.util.ArrayList;

import com.hazelcast.config.Config;
import com.hazelcast.core.Hazelcast;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.spi.cluster.ClusterManager;
import io.vertx.spi.cluster.hazelcast.HazelcastClusterManager;

/**
 * Hello world!
 *
 */
public class MainRun extends AbstractVerticle {
    public static void main(String[] args) {
        Config hazelcastConfig = new Config();
        //hazelcastConfig.setConfigurationFile(new File("src/main/resources/config/cluster.xml"));
        hazelcastConfig.getNetworkConfig().getJoin().getTcpIpConfig()
            .addMember("127.0.0.1").setEnabled(true);
        hazelcastConfig.getNetworkConfig().getJoin().getMulticastConfig().setEnabled(false);
        //hazelcastConfig.getNetworkConfig().setPort(9696);
        //hazelcastConfig.getGroupConfig().setName("dev").setPassword("dev-pass");
        
        ClusterManager mgr = new HazelcastClusterManager(hazelcastConfig);
        VertxOptions options = new VertxOptions().setClusterManager(mgr);
        Vertx.clusteredVertx(options, resultHandler -> {
            if(resultHandler.succeeded()){
                CalculateServicePing calculateServicePing = new CalculateServicePing();
                ReceiveMessageService messageService = new ReceiveMessageService();
                Vertx vertx = resultHandler.result();
                vertx.deployVerticle(calculateServicePing);
                vertx.deployVerticle(messageService);
            }
        });
        System.out.println("Receive Service starting....");
        
        
//        Vertx vertx = Vertx.vertx();
//        vertx.deployVerticle(ReceiveMessageService.class.getName());
//        vertx.deployVerticle(CalculateService.class.getName());
//        System.out.println("VertxService running....!");
    }
    
    public void start(){
        vertx.deployVerticle(ReceiveMessageService.class.getName());
        vertx.deployVerticle(CalculateServicePing.class.getName());
    }
}
