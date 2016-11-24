package com.acton.springintegratevertx.vertx;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import com.acton.springintegratevertx.service.MessageService;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;

public class VertxService extends AbstractVerticle {
	ApplicationContext context;
	MessageService messageService; 
	
	
	public VertxService(ApplicationContext context) {
		messageService = (MessageService) context.getBean("messageService");
	}

	@Override
	public void start() {
		Router router = Router.router(vertx);
		router.get("/cal/:value").handler(this::handlerGetValue);
		
		vertx.createHttpServer().requestHandler(router::accept).listen(8083);
	}

	public void handlerGetValue(RoutingContext context) {
		System.out.println("---"+messageService.getMessage()+"---");
		
		String val = context.request().getParam("value");
		vertx.eventBus().send("calculate.service", new JsonObject().put("value", val), reply -> {
			if (reply.succeeded()) {
				context.response().end("Your results (" + val + " * 2) = " + reply.result().body());
			} else {
				System.out.println(reply.cause());
			}
		});
	}
}
