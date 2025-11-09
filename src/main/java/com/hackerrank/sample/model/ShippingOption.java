package com.hackerrank.sample.model;

import java.math.BigDecimal;
import java.util.Objects;

public record ShippingOption(
    String type,
    String carrier,
    BigDecimal cost,
    int estimatedDeliveryDays,
    boolean pickupAvailable
) {
    public ShippingOption {
        Objects.requireNonNull(type, "type no puede ser nulo");
        Objects.requireNonNull(carrier, "carrier no puede ser nulo");
        Objects.requireNonNull(cost, "cost no puede ser nulo");
        if (estimatedDeliveryDays < 0) {
            throw new IllegalArgumentException("estimatedDeliveryDays no puede ser negativo");
        }
    }
}


