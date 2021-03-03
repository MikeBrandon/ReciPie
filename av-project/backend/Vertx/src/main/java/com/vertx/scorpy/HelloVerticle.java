package com.vertx.scorpy;

import io.vertx.core.AbstractVerticle;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;

public class HelloVerticle extends AbstractVerticle{

    @Override
    public void start() {
        vertx.eventBus().consumer("hello.vertx.addr", msg -> {
            msg.reply("Hello World");
        });
        
        vertx.eventBus().consumer("hello.named.addr", msg -> {
            String name = msg.body();
            msg.reply(String.format("Hello %s!", name));
        });
    }
}