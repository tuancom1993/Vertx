package com.enclave.acton.VertxService;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;

public class ReceiveMessageService extends AbstractVerticle {
    
    @Override
    public void start() {
        Router router = Router.router(vertx);
        
        router.route("/hello").handler(han -> {
            han.response().end("Hello, Wellcome to Vert.x!!!");
        });
        
        router.get("/cal/:value").handler(this::handlerCalculate);
        
        router.route("/calbypost").handler(BodyHandler.create());
        router.get("/calbypost").handler(this::handlerGoToIndexPage);
        router.post("/calbypost").handler(this::handlerExeCalByPost);
        
        router.route("/springtovertx").handler(BodyHandler.create());
        router.post("/springtovertx").handler(this::handlerSpringToVertx);
        
        vertx.createHttpServer().requestHandler(router::accept).listen(8081);
        
        
    }
    
    public void handlerCalculate(RoutingContext context){
        String val = context.request().getParam("value");
        vertx.eventBus().send("calculate.service", 
                               new JsonObject().put("value", val),
                               reply -> {
            if (reply.succeeded()) {
                context.response().end("Your results ("+val+" * 2) = " +reply.result().body());
            } else {
                System.out.println(reply.cause());
            }
        });
    }
    
    public void handlerGoToIndexPage(RoutingContext context){
        context.request().response().sendFile("web/index.html");
    }
    
    public void handlerExeCalByPost(RoutingContext context){
        String val = context.request().getFormAttribute("value");
        System.out.println("[ReceiveMessageService] Sent: "+val);
        vertx.eventBus().send("vertx.calculate.service.cal", 
                new JsonObject().put("value", val),
                reply -> {
                    if (reply.succeeded()) {
                        context.response().setChunked(true);
                     //context.response().end("Your results ("+val+" * 2) = " +reply.result().body());
                        context.response().setStatusCode(200)
                            .write("Your results ("+val+" * 2) = " +reply.result().body()).end();
                    } else {
                     System.out.println(reply.cause());
                    }
                });
     }
    
    public void handlerSpringToVertx(RoutingContext context){
        //System.out.println(context.request().getHeader("By"));
        String val = context.getBodyAsString();
        System.out.println("Value receeve from SpringMVC: "+val);
        vertx.eventBus().send("vertx.calculate.service.cal", 
                new JsonObject().put("value", val),
                reply -> {
                    if (reply.succeeded()) {
                     context.response().end(reply.result().body().toString());
                    } else {
                     System.out.println(reply.cause());
                    }
                });
    }
}
