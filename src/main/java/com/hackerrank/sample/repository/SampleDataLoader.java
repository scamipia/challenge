package com.hackerrank.sample.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hackerrank.sample.model.Item;
import com.hackerrank.sample.model.Question;
import com.hackerrank.sample.model.RelatedItem;
import com.hackerrank.sample.model.Review;
import com.hackerrank.sample.model.SellerDetail;
import com.hackerrank.sample.model.ShippingOption;
import jakarta.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

@Component
public class SampleDataLoader {

    private final ObjectMapper objectMapper;

    private final Map<String, Item> items = new ConcurrentHashMap<>();
    private final Map<String, SellerDetail> sellers = new ConcurrentHashMap<>();
    private final Map<String, List<RelatedItem>> relatedItems = new ConcurrentHashMap<>();
    private final Map<String, List<Question>> questions = new ConcurrentHashMap<>();
    private final Map<String, List<Review>> reviews = new ConcurrentHashMap<>();
    private final Map<String, List<ShippingOption>> shippingOptions = new ConcurrentHashMap<>();

    public SampleDataLoader(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @PostConstruct
    public void loadSampleData() {
        Resource resource = new ClassPathResource("data/catalog.json");
        if (!resource.exists()) {
            throw new IllegalStateException("No se encontró el archivo data/catalog.json");
        }

        try (InputStream inputStream = resource.getInputStream()) {
            CatalogData catalog = objectMapper.readValue(inputStream, CatalogData.class);
            Item item = toItem(catalog.item());
            items.put(item.getId(), item);

            SellerDetail seller = toSeller(catalog.seller());
            sellers.put(seller.id(), seller);

            relatedItems.put(item.getId(), catalog.relatedItems()
                    .stream()
                    .map(this::toRelatedItem)
                    .toList());

            questions.put(item.getId(), catalog.questions()
                    .stream()
                    .map(this::toQuestion)
                    .toList());

            reviews.put(item.getId(), catalog.reviews()
                    .stream()
                    .map(this::toReview)
                    .toList());

            shippingOptions.put(item.getId(), catalog.shippingOptions()
                    .stream()
                    .map(this::toShippingOption)
                    .toList());
        } catch (IOException ex) {
            throw new IllegalStateException("No se pudieron cargar los datos de ejemplo", ex);
        }
    }

    public Optional<Item> findItemById(String id) {
        return Optional.ofNullable(items.get(id));
    }

    public List<Item> findAllItems() {
        return List.copyOf(items.values());
    }

    public Optional<SellerDetail> findSellerById(String id) {
        return Optional.ofNullable(sellers.get(id));
    }

    public List<RelatedItem> findRelatedItems(String itemId) {
        return relatedItems.getOrDefault(itemId, List.of());
    }

    public List<Question> findQuestions(String itemId) {
        return questions.getOrDefault(itemId, List.of());
    }

    public List<Review> findReviews(String itemId) {
        return reviews.getOrDefault(itemId, List.of());
    }

    public List<ShippingOption> findShippingOptions(String itemId) {
        return shippingOptions.getOrDefault(itemId, List.of());
    }

    private Item toItem(ItemData data) {
        return Item.builder()
            .id(data.id())
            .title(data.title())
            .permalink(data.permalink())
            .condition(data.condition())
            .warranty(data.warranty())
            .description(data.description())
            .price(new Item.Price(new BigDecimal(String.valueOf(data.price().amount())), data.price().currency()))
            .availableQuantity(data.availableQuantity())
            .soldQuantity(data.soldQuantity())
            .lastUpdated(OffsetDateTime.parse(data.lastUpdated()))
            .seller(new Item.Seller(
                data.seller().id(),
                data.seller().nickname(),
                data.seller().reputation()
            ))
            .shipping(new Item.Shipping(
                data.shipping().freeShipping(),
                data.shipping().logisticType(),
                List.copyOf(data.shipping().tags())
            ))
            .pictures(data.pictures().stream()
                .map(p -> new Item.Picture(
                    p.url(),
                    p.secureUrl(),
                    p.type(),
                    p.order()
                ))
                .toList())
            .attributes(data.attributes().stream()
                .map(a -> new Item.Attribute(
                    a.id(),
                    a.name(),
                    a.valueName()
                ))
                .toList())
            .build();
    }

    private SellerDetail toSeller(SellerData data) {
        return new SellerDetail(
            data.id(),
            data.nickname(),
            data.reputation(),
            data.reputationLevel(),
            data.totalSales(),
            data.responseRate(),
            data.responseTimeHours(),
            List.copyOf(data.policies())
        );
    }

    private RelatedItem toRelatedItem(RelatedItemData data) {
        return new RelatedItem(
            data.id(),
            data.title(),
            new BigDecimal(String.valueOf(data.price())),
            data.thumbnail()
        );
    }

    private Question toQuestion(QuestionData data) {
        return new Question(
            data.id(),
            data.itemId(),
            data.text(),
            OffsetDateTime.parse(data.askedAt()),
            data.answer(),
            data.answeredAt() == null ? null : OffsetDateTime.parse(data.answeredAt())
        );
    }

    private Review toReview(ReviewData data) {
        return new Review(
            data.id(),
            data.itemId(),
            data.author(),
            data.rating(),
            data.title(),
            data.comment(),
            OffsetDateTime.parse(data.createdAt())
        );
    }

    private ShippingOption toShippingOption(ShippingOptionData data) {
        return new ShippingOption(
            data.type(),
            data.carrier(),
            new BigDecimal(String.valueOf(data.cost())),
            data.estimatedDeliveryDays(),
            data.pickupAvailable()
        );
    }

    private record CatalogData(
        ItemData item,
        SellerData seller,
        List<RelatedItemData> relatedItems,
        List<QuestionData> questions,
        List<ReviewData> reviews,
        List<ShippingOptionData> shippingOptions
    ) {
    }

    private record ItemData(
        String id,
        String title,
        String permalink,
        String condition,
        String warranty,
        String description,
        PriceData price,
        int availableQuantity,
        int soldQuantity,
        String lastUpdated,
        SellerSummary seller,
        ShippingData shipping,
        List<PictureData> pictures,
        List<AttributeData> attributes
    ) {
    }

    private record PriceData(double amount, String currency) {
    }

    private record SellerSummary(String id, String nickname, double reputation) {
    }

    private record ShippingData(boolean freeShipping, String logisticType, List<String> tags) {
    }

    private record PictureData(String url, String secureUrl, String type, int order) {
    }

    private record AttributeData(String id, String name, String valueName) {
    }

    private record SellerData(
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

    private record RelatedItemData(String id, String title, double price, String thumbnail) {
    }

    private record QuestionData(
        String id,
        String itemId,
        String text,
        String askedAt,
        String answer,
        String answeredAt
    ) {
    }

    private record ReviewData(
        String id,
        String itemId,
        String author,
        int rating,
        String title,
        String comment,
        String createdAt
    ) {
    }

    private record ShippingOptionData(
        String type,
        String carrier,
        double cost,
        int estimatedDeliveryDays,
        boolean pickupAvailable
    ) {
    }
}


