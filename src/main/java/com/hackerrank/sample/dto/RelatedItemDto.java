package com.hackerrank.sample.dto;

import java.math.BigDecimal;

public record RelatedItemDto(
    String id,
    String title,
    BigDecimal price,
    String thumbnail
) {
}


