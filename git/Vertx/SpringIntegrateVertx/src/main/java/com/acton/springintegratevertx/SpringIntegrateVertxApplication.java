package com.acton.springintegratevertx;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.acton.springintegratevertx.vertx.AppConfig;
import com.acton.springintegratevertx.vertx.VertxService;
import com.hazelcast.config.Config;

import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.spi.cluster.ClusterManager;
import io.vertx.spi.cluster.hazelcast.HazelcastClusterManager;

@SpringBootApplication
public class SpringIntegrateVertxApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringIntegrateVertxApplication.class, args);
		ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
		
		Config hazelcastConfig = new Config();
        hazelcastConfig.getNetworkConfig().getJoin().getTcpIpConfig()
            .addMember("127.0.0.1").setEnabled(true);
        hazelcastConfig.getNetworkConfig().getJoin().getMulticastConfig().setEnabled(false);
        
        ClusterManager mgr = new HazelcastClusterManager(hazelcastConfig);
        VertxOptions options = new VertxOptions().setClusterManager(mgr);
        Vertx.clusteredVertx(options, resultHandler -> {
            if(resultHandler.succeeded()){
                VertxService vertxService = new VertxService(context);
                Vertx vertx = resultHandler.result();
                vertx.deployVerticle(vertxService);
                System.out.println("Deploy successful...");
            }
        });
	}
}
