package com.hackerrank.sample.model;

import java.math.BigDecimal;
import java.util.Objects;

public record RelatedItem(
    String id,
    String title,
    BigDecimal price,
    String thumbnail
) {
    public RelatedItem {
        Objects.requireNonNull(id, "id no puede ser nulo");
        Objects.requireNonNull(title, "title no puede ser nulo");
        Objects.requireNonNull(price, "price no puede ser nulo");
        Objects.requireNonNull(thumbnail, "thumbnail no puede ser nulo");
    }
}


