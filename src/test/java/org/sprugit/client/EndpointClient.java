package org.sprugit.client;

import io.smallrye.mutiny.Multi;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import org.jboss.resteasy.reactive.client.SseEvent;
import org.jboss.resteasy.reactive.client.SseEventFilter;
import org.sprugit.Event;

import java.util.function.Predicate;

@Path("/sse/{notif-target}")
@RegisterRestClient(baseUri = "http://localhost:8081")
public interface EndpointClient {

    @GET
    @Produces(MediaType.SERVER_SENT_EVENTS)
    @SseEventFilter(EndpointClient.KeepAliveFilter.class)
    Multi<Event> getEventsFor(@PathParam("notif-target") String target);

    class KeepAliveFilter implements Predicate<SseEvent<String>> {
        @Override
        public boolean test(SseEvent<String> event) { return !event.data().contains("keep_alive"); }
    }

}
