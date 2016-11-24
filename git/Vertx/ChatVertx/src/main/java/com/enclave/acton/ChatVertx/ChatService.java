package com.enclave.acton.ChatVertx;

import java.util.Scanner;

import io.vertx.core.AbstractVerticle;

/**
 * Hello world!
 *
 */
public class ChatService extends AbstractVerticle  {
    private String localSMS;

    public void start() {
        System.out.println("ChatService is stating...");
        vertx.eventBus().consumer("service.chat", message -> {
            if (!message.body().toString().equals(localSMS))
                System.out.println(message.body().toString());
            else
                localSMS = null;
        });
    }

    public void sentMessage(String sms) {
        localSMS = sms;
        System.out.println(sms);
        vertx.eventBus().publish("service.chat", sms);
        
    }

    
}
