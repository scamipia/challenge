package com.hackerrank.sample.model;

import java.time.OffsetDateTime;
import java.util.Objects;

public record Question(
    String id,
    String itemId,
    String text,
    OffsetDateTime askedAt,
    String answer,
    OffsetDateTime answeredAt
) {
    public Question {
        Objects.requireNonNull(id, "id no puede ser nulo");
        Objects.requireNonNull(itemId, "itemId no puede ser nulo");
        Objects.requireNonNull(text, "text no puede ser nulo");
        Objects.requireNonNull(askedAt, "askedAt no puede ser nulo");
    }
}


