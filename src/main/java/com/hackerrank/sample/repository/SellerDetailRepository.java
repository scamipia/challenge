package com.hackerrank.sample.repository;

import com.hackerrank.sample.model.SellerDetail;
import java.util.Optional;

public interface SellerDetailRepository {
    Optional<SellerDetail> findDetailById(String sellerId);
}


