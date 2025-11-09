package com.hackerrank.sample.dto;

import java.math.BigDecimal;

public record ShippingOptionDto(
    String type,
    String carrier,
    BigDecimal cost,
    int estimatedDeliveryDays,
    boolean pickupAvailable
) {
}


