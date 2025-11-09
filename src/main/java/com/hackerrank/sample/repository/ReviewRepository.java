package com.hackerrank.sample.repository;

import com.hackerrank.sample.model.Review;
import java.util.List;

public interface ReviewRepository {
    List<Review> findReviewsByItemId(String itemId);
}


