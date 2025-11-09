package com.hackerrank.sample.model;

import java.util.List;
import java.util.Objects;

public record SellerDetail(
    String id,
    String nickname,
    double reputation,
    String reputationLevel,
    int totalSales,
    double responseRate,
    int responseTimeHours,
    List<String> policies
) {
    public SellerDetail {
        Objects.requireNonNull(id, "id no puede ser nulo");
        Objects.requireNonNull(nickname, "nickname no puede ser nulo");
        Objects.requireNonNull(reputationLevel, "reputationLevel no puede ser nulo");
        Objects.requireNonNull(policies, "policies no puede ser nulo");
        policies = List.copyOf(policies);
    }
}


