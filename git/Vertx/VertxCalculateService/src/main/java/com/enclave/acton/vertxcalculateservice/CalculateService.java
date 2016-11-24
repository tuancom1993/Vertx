package com.enclave.acton.vertxcalculateservice;

import com.hazelcast.config.Config;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.eventbus.EventBusOptions;
import io.vertx.core.json.JsonObject;
import io.vertx.core.spi.cluster.ClusterManager;
import io.vertx.spi.cluster.hazelcast.HazelcastClusterManager;

/**
 * Hello world!
 *
 */
public class CalculateService extends AbstractVerticle {
    @Override
    public void start() {
        vertx.eventBus().consumer("calculate.service", message -> {
            JsonObject json = (JsonObject) message.body();
            System.out.println("[CalculateService - Pong] Receive message: " + json.toString());
            int valReturn = Integer.parseInt(json.getString("value")) * 2;
            message.reply(valReturn);

        });
    }

    public static void main(String args[]) {
        Config hazelcastConfig = new Config();
        hazelcastConfig.getNetworkConfig().getJoin().getTcpIpConfig().addMember("127.0.0.1").setEnabled(true);
        hazelcastConfig.getNetworkConfig().getJoin().getMulticastConfig().setEnabled(false);
        // hazelcastConfig.getNetworkConfig().setPort(9696);

        ClusterManager mgr = new HazelcastClusterManager(hazelcastConfig);
        VertxOptions options = new VertxOptions().setClusterManager(mgr);
        Vertx.clusteredVertx(options, resultHandler -> {
            if (resultHandler.succeeded()) {
                CalculateService calculateService = new CalculateService();
                Vertx vertx = resultHandler.result();
                vertx.deployVerticle(calculateService);
            }
        });

        System.out.println("Calculate Service starting....");
    }
}
