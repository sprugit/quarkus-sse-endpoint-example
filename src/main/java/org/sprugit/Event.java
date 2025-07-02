package org.sprugit;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Event {

    @JsonProperty String payload;

    public Event(String payload) {
        this.payload = payload;
    }

    public String getPayload() { return payload; }
}
