package com.hackerrank.sample.dto;

import java.util.Map;

public record ReviewSummaryDto(
    double averageRating,
    int totalReviews,
    Map<Integer, Long> ratingDistribution
) {
}


