package com.fullcycle.catalogo.infrastructure.kafka.models.connect;

import com.fasterxml.jackson.annotation.JsonProperty;

public record MessageValue<T>(
        @JsonProperty("payload") ValuePayload<T> payload
) {
}
