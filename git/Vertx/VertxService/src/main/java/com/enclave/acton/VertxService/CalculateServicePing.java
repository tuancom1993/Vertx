package com.enclave.acton.VertxService;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;

public class CalculateServicePing extends AbstractVerticle {
    @Override
    public void start() {
        vertx.eventBus().consumer("vertx.calculate.service.cal", message -> {
            JsonObject json = (JsonObject) message.body();
            System.out.println("[CalculateService - Ping] Receive message: "+json);
            int valReturn = Integer.parseInt(json.getString("value")) * 2;
            message.reply(valReturn);
        });
    }
}
