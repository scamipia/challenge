package com.hackerrank.sample.model;

import java.time.OffsetDateTime;
import java.util.Objects;

public record Review(
    String id,
    String itemId,
    String author,
    int rating,
    String title,
    String comment,
    OffsetDateTime createdAt
) {
    public Review {
        Objects.requireNonNull(id, "id no puede ser nulo");
        Objects.requireNonNull(itemId, "itemId no puede ser nulo");
        Objects.requireNonNull(author, "author no puede ser nulo");
        Objects.requireNonNull(title, "title no puede ser nulo");
        Objects.requireNonNull(comment, "comment no puede ser nulo");
        Objects.requireNonNull(createdAt, "createdAt no puede ser nulo");
        if (rating < 1 || rating > 5) {
            throw new IllegalArgumentException("rating debe estar entre 1 y 5");
        }
    }
}


