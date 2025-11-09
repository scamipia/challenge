package com.hackerrank.sample.dto;

import java.util.List;

public record ReviewPageDto(
    List<ReviewDto> reviews,
    ReviewSummaryDto summary,
    int page,
    int size,
    int totalPages
) {
}


