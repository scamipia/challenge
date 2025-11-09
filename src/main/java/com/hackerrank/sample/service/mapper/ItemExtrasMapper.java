package com.hackerrank.sample.service.mapper;

import com.hackerrank.sample.dto.QuestionDto;
import com.hackerrank.sample.dto.RelatedItemDto;
import com.hackerrank.sample.dto.ReviewDto;
import com.hackerrank.sample.dto.ReviewSummaryDto;
import com.hackerrank.sample.dto.SellerDetailDto;
import com.hackerrank.sample.dto.ShippingOptionDto;
import com.hackerrank.sample.model.Question;
import com.hackerrank.sample.model.RelatedItem;
import com.hackerrank.sample.model.Review;
import com.hackerrank.sample.model.SellerDetail;
import com.hackerrank.sample.model.ShippingOption;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class ItemExtrasMapper {

    public List<RelatedItemDto> toRelatedDtos(List<RelatedItem> items) {
        return items.stream()
            .map(item -> new RelatedItemDto(item.id(), item.title(), item.price(), item.thumbnail()))
            .toList();
    }

    public SellerDetailDto toSellerDetailDto(SellerDetail sellerDetail) {
        return new SellerDetailDto(
            sellerDetail.id(),
            sellerDetail.nickname(),
            sellerDetail.reputation(),
            sellerDetail.reputationLevel(),
            sellerDetail.totalSales(),
            sellerDetail.responseRate(),
            sellerDetail.responseTimeHours(),
            sellerDetail.policies()
        );
    }

    public List<QuestionDto> toQuestionDtos(List<Question> questionList) {
        return questionList.stream()
            .map(question -> new QuestionDto(
                question.id(),
                question.text(),
                question.askedAt(),
                question.answer(),
                question.answeredAt()
            ))
            .toList();
    }

    public List<ReviewDto> toReviewDtos(List<Review> reviewList) {
        return reviewList.stream()
            .map(review -> new ReviewDto(
                review.id(),
                review.author(),
                review.rating(),
                review.title(),
                review.comment(),
                review.createdAt()
            ))
            .toList();
    }

    public ReviewSummaryDto toReviewSummaryDto(double averageRating, int totalReviews,
            Map<Integer, Long> distribution) {
        return new ReviewSummaryDto(averageRating, totalReviews, distribution);
    }

    public List<ShippingOptionDto> toShippingOptionDtos(List<ShippingOption> options) {
        return options.stream()
            .map(option -> new ShippingOptionDto(
                option.type(),
                option.carrier(),
                option.cost(),
                option.estimatedDeliveryDays(),
                option.pickupAvailable()
            ))
            .collect(Collectors.toList());
    }
}


