package com.hackerrank.sample.dto;

import java.time.OffsetDateTime;

public record QuestionDto(
    String id,
    String text,
    OffsetDateTime askedAt,
    String answer,
    OffsetDateTime answeredAt
) {
}


