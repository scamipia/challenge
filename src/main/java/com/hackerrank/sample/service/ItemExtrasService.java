package com.hackerrank.sample.service;

import com.hackerrank.sample.dto.QuestionDto;
import com.hackerrank.sample.dto.RelatedItemDto;
import com.hackerrank.sample.dto.ReviewPageDto;
import com.hackerrank.sample.dto.SellerDetailDto;
import com.hackerrank.sample.dto.ShippingOptionDto;
import java.util.List;

public interface ItemExtrasService {

    List<RelatedItemDto> getRelatedItems(String itemId);

    SellerDetailDto getSellerDetail(String sellerId);

    List<QuestionDto> getQuestions(String itemId);

    ReviewPageDto getReviews(String itemId, int page, int size);

    List<ShippingOptionDto> getShippingOptions(String itemId, String zipcode);
}


