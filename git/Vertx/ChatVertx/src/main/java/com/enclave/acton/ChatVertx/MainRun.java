package com.enclave.acton.ChatVertx;

import com.hazelcast.config.Config;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.spi.cluster.ClusterManager;
import io.vertx.spi.cluster.hazelcast.HazelcastClusterManager;

public class MainRun extends AbstractVerticle {
    public static void main(String[] args) {
        Config hazelcastConfig = new Config();
        hazelcastConfig.getNetworkConfig().getJoin().getTcpIpConfig()
            .addMember("127.0.0.1").setEnabled(true);
        hazelcastConfig.getNetworkConfig().getJoin().getMulticastConfig().setEnabled(false);
        
        ClusterManager mgr = new HazelcastClusterManager(hazelcastConfig);
        VertxOptions options = new VertxOptions().setClusterManager(mgr);
        Vertx.clusteredVertx(options, resultHandler -> {
            if(resultHandler.succeeded()){
                ChatService chatService = new ChatService();
                Vertx vertx = resultHandler.result();
                vertx.deployVerticle(chatService);
                SentMessageThread thread = new SentMessageThread(chatService);
                thread.start();
            }
        });
    }
    
    public void start(){
        ChatService chatService = new ChatService();
        vertx.deployVerticle(chatService);
        SentMessageThread thread = new SentMessageThread(chatService);
        thread.start();
    }
}
