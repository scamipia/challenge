package com.hackerrank.sample.dto;

import java.time.OffsetDateTime;

public record ReviewDto(
    String id,
    String author,
    int rating,
    String title,
    String comment,
    OffsetDateTime createdAt
) {
}


