package com.hackerrank.sample.service;

import com.hackerrank.sample.dto.QuestionDto;
import com.hackerrank.sample.dto.RelatedItemDto;
import com.hackerrank.sample.dto.ReviewDto;
import com.hackerrank.sample.dto.ReviewPageDto;
import com.hackerrank.sample.dto.ReviewSummaryDto;
import com.hackerrank.sample.dto.SellerDetailDto;
import com.hackerrank.sample.dto.ShippingOptionDto;
import com.hackerrank.sample.exception.NoSuchResourceFoundException;
import com.hackerrank.sample.model.Review;
import com.hackerrank.sample.repository.ItemRepository;
import com.hackerrank.sample.repository.QuestionRepository;
import com.hackerrank.sample.repository.RelatedItemRepository;
import com.hackerrank.sample.repository.ReviewRepository;
import com.hackerrank.sample.repository.SellerDetailRepository;
import com.hackerrank.sample.repository.ShippingOptionRepository;
import com.hackerrank.sample.service.mapper.ItemExtrasMapper;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ItemExtrasServiceImpl implements ItemExtrasService {

    private static final int DEFAULT_PAGE_SIZE = 10;

    private final ItemRepository itemRepository;
    private final RelatedItemRepository relatedItemRepository;
    private final SellerDetailRepository sellerDetailRepository;
    private final QuestionRepository questionRepository;
    private final ReviewRepository reviewRepository;
    private final ShippingOptionRepository shippingOptionRepository;
    private final ItemExtrasMapper mapper;

    @Autowired
    public ItemExtrasServiceImpl(
            ItemRepository itemRepository,
            RelatedItemRepository relatedItemRepository,
            SellerDetailRepository sellerDetailRepository,
            QuestionRepository questionRepository,
            ReviewRepository reviewRepository,
            ShippingOptionRepository shippingOptionRepository,
            ItemExtrasMapper mapper
    ) {
        this.itemRepository = itemRepository;
        this.relatedItemRepository = relatedItemRepository;
        this.sellerDetailRepository = sellerDetailRepository;
        this.questionRepository = questionRepository;
        this.reviewRepository = reviewRepository;
        this.shippingOptionRepository = shippingOptionRepository;
        this.mapper = mapper;
    }

    @Override
    public List<RelatedItemDto> getRelatedItems(String itemId) {
        ensureItemExists(itemId);
        return mapper.toRelatedDtos(relatedItemRepository.findRelatedByItemId(itemId));
    }

    @Override
    public SellerDetailDto getSellerDetail(String sellerId) {
        return sellerDetailRepository.findDetailById(sellerId)
            .map(mapper::toSellerDetailDto)
            .orElseThrow(() -> new NoSuchResourceFoundException("No se encontró el vendedor con id " + sellerId));
    }

    @Override
    public List<QuestionDto> getQuestions(String itemId) {
        ensureItemExists(itemId);
        return mapper.toQuestionDtos(questionRepository.findQuestionsByItemId(itemId));
    }

    @Override
    public ReviewPageDto getReviews(String itemId, int page, int size) {
        ensureItemExists(itemId);
        int resolvedSize = size <= 0 ? DEFAULT_PAGE_SIZE : size;
        int resolvedPage = Math.max(page, 0);

        List<Review> allReviews = reviewRepository.findReviewsByItemId(itemId)
            .stream()
            .sorted(Comparator.comparing(Review::createdAt).reversed())
            .toList();

        int totalReviews = allReviews.size();
        int fromIndex = Math.min(resolvedPage * resolvedSize, totalReviews);
        int toIndex = Math.min(fromIndex + resolvedSize, totalReviews);
        List<ReviewDto> reviewsPage = mapper.toReviewDtos(allReviews.subList(fromIndex, toIndex));

        double averageRating = allReviews.stream()
            .mapToInt(Review::rating)
            .average()
            .orElse(0.0);

        Map<Integer, Long> distribution = allReviews.stream()
            .collect(Collectors.groupingBy(Review::rating, Collectors.counting()));

        Map<Integer, Long> normalizedDistribution = new LinkedHashMap<>();
        for (int rating = 5; rating >= 1; rating--) {
            normalizedDistribution.put(rating, distribution.getOrDefault(rating, 0L));
        }

        int totalPages = (int) Math.ceil(totalReviews / (double) resolvedSize);
        ReviewSummaryDto summaryDto = mapper.toReviewSummaryDto(averageRating, totalReviews, normalizedDistribution);

        return new ReviewPageDto(
            reviewsPage,
            summaryDto,
            resolvedPage,
            resolvedSize,
            totalPages
        );
    }

    @Override
    public List<ShippingOptionDto> getShippingOptions(String itemId, String zipcode) {
        ensureItemExists(itemId);
        return mapper.toShippingOptionDtos(
            shippingOptionRepository.findByItemIdAndZipcode(itemId, zipcode)
        );
    }

    private void ensureItemExists(String itemId) {
        itemRepository.findById(itemId)
            .orElseThrow(() -> new NoSuchResourceFoundException("No se encontró el item con id " + itemId));
    }
}


