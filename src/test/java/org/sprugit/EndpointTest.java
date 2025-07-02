package org.sprugit;

import io.quarkus.test.junit.QuarkusTest;
import io.smallrye.mutiny.helpers.test.AssertSubscriber;
import jakarta.inject.Inject;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.sprugit.client.EndpointClient;

import java.time.Duration;


@QuarkusTest
public class EndpointTest {

    @Inject
    @RestClient
    EndpointClient ec;

    @Test
    void testReceivesData() {

        String message = "Event to %s";
        String identifier = "event-tester";
        AssertSubscriber<Event> event_stream = ec.getEventsFor(identifier).subscribe().withSubscriber(AssertSubscriber.create());

        Assertions.assertEquals(String.format(message, identifier),
                event_stream.awaitNextItem(Duration.ofSeconds(15L)).getLastItem().getPayload());

    }

}
