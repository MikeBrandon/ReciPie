package com.vertx.scorpy;

import io.vertx.core.AbstractVerticle;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;

public class MainVerticle extends AbstractVerticle {

    @Override
    public void start() {
        Router router = Router.router(vertx);
        router.get("/api/v1/hello").handler(this::helloAll);
        router.get("/api/v1/hello/:name").handler(this::helloUser);

        vertx.createHttpServer().requestHandler(router).listen(8080);
    }

    private void helloAll(RoutingContext ctx) {
        vertx.eventBus().request(ctx -> {
            ctx.request().response().end("Hello World");
        });
    }

    private void helloUser(RoutingContext ctx) {
        String name = ctx.pathParam("name");
        vertx.eventBus().request(ctx -> {
            ctx.request().response().end("Hello %s!", name);
        });
    }
}
