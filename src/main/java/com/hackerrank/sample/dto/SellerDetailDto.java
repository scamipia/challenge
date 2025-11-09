package com.hackerrank.sample.dto;

import java.util.List;

public record SellerDetailDto(
    String id,
    String nickname,
    double reputation,
    String reputationLevel,
    int totalSales,
    double responseRate,
    int responseTimeHours,
    List<String> policies
) {
}


