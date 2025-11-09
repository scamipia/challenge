package com.hackerrank.sample.repository;

import com.hackerrank.sample.model.Item;
import com.hackerrank.sample.model.Question;
import com.hackerrank.sample.model.RelatedItem;
import com.hackerrank.sample.model.Review;
import com.hackerrank.sample.model.SellerDetail;
import com.hackerrank.sample.model.ShippingOption;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.stereotype.Repository;

@Repository("itemRepository")
public class InMemoryItemRepository implements ItemRepository, RelatedItemRepository,
        SellerDetailRepository, QuestionRepository, ReviewRepository, ShippingOptionRepository {

    private final SampleDataLoader dataLoader;

    public InMemoryItemRepository(SampleDataLoader dataLoader) {
        this.dataLoader = dataLoader;
    }

    @Override
    public Optional<Item> findById(String id) {
        return dataLoader.findItemById(id);
    }

    @Override
    public List<Item> findAll() {
        return dataLoader.findAllItems();
    }

    @Override
    public List<RelatedItem> findRelatedByItemId(String itemId) {
        return dataLoader.findRelatedItems(itemId);
    }

    @Override
    public Optional<SellerDetail> findDetailById(String sellerId) {
        return dataLoader.findSellerById(sellerId);
    }

    @Override
    public List<Question> findQuestionsByItemId(String itemId) {
        return dataLoader.findQuestions(itemId);
    }

    @Override
    public List<Review> findReviewsByItemId(String itemId) {
        return dataLoader.findReviews(itemId);
    }

    @Override
    public List<ShippingOption> findByItemIdAndZipcode(String itemId, String zipcode) {
        List<ShippingOption> baseOptions = dataLoader.findShippingOptions(itemId);
        if (baseOptions.isEmpty()) {
            return baseOptions;
        }

        String normalizedZipcode = zipcode == null ? "" : zipcode.trim();
        int additionalDays = deliveryDelay(normalizedZipcode);
        BigDecimal surchargeFactor = surchargeFactor(normalizedZipcode);

        return baseOptions.stream()
            .map(option -> new ShippingOption(
                option.type(),
                option.carrier(),
                option.cost().multiply(surchargeFactor).setScale(2, RoundingMode.HALF_UP),
                option.estimatedDeliveryDays() + additionalDays,
                option.pickupAvailable()
            ))
            .collect(Collectors.toUnmodifiableList());
    }

    private int deliveryDelay(String zipcode) {
        if (zipcode.isEmpty()) {
            return 0;
        }

        if (zipcode.startsWith("1")) {
            return 0;
        }
        if (zipcode.startsWith("8") || zipcode.startsWith("9")) {
            return 3;
        }
        if (zipcode.startsWith("7")) {
            return 2;
        }

        return 1;
    }

    private BigDecimal surchargeFactor(String zipcode) {
        if (zipcode.isEmpty() || zipcode.startsWith("1")) {
            return BigDecimal.ONE;
        }
        if (zipcode.startsWith("8") || zipcode.startsWith("9")) {
            return new BigDecimal("1.35");
        }
        if (zipcode.startsWith("7")) {
            return new BigDecimal("1.20");
	}

        return new BigDecimal("1.10");
    }
}

