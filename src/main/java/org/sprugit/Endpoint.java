package org.sprugit;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.infrastructure.Infrastructure;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import java.time.Duration;
import java.util.Random;

@ApplicationScoped
@Path("/sse/{notif-target}")
public class Endpoint {

    static Random r = new Random();

    private Multi<Event> submitKeepAlive() {
        return Multi.createFrom().ticks().every(Duration.ofSeconds(5))
                .onItem().transform(aLong -> new Event("keep_alive"));
    }

    private Multi<Event> submitUserEvent(String target) {
        return Multi.createBy().repeating()
                .uni(() -> Uni.createFrom()
                            .item(() -> new Event(String.format("Event to %s",target)))
                            .onItem().delayIt().by(Duration.ofSeconds(1 + r.nextInt(14))))
                .indefinitely();
    }

    @GET
    @Produces(MediaType.SERVER_SENT_EVENTS)
    public Multi<Event> sse(@PathParam("notif-target") String target) {
        return Multi.createBy().merging()
                .streams(submitKeepAlive(),submitUserEvent(target))
                .runSubscriptionOn(Infrastructure.getDefaultWorkerPool());
    }
}
