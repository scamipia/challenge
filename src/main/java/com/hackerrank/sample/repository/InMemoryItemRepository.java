package com.hackerrank.sample.repository;

import com.hackerrank.sample.model.Item;
import com.hackerrank.sample.model.Question;
import com.hackerrank.sample.model.RelatedItem;
import com.hackerrank.sample.model.Review;
import com.hackerrank.sample.model.SellerDetail;
import com.hackerrank.sample.model.ShippingOption;
import jakarta.annotation.PostConstruct;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import org.springframework.stereotype.Repository;

@Repository("itemRepository")
public class InMemoryItemRepository implements ItemRepository, RelatedItemRepository,
        SellerDetailRepository, QuestionRepository, ReviewRepository, ShippingOptionRepository {

    private final Map<String, Item> items = new ConcurrentHashMap<>();
    private final Map<String, List<RelatedItem>> relatedItems = new ConcurrentHashMap<>();
    private final Map<String, SellerDetail> sellers = new ConcurrentHashMap<>();
    private final Map<String, List<Question>> questions = new ConcurrentHashMap<>();
    private final Map<String, List<Review>> reviews = new ConcurrentHashMap<>();
    private final Map<String, List<ShippingOption>> shippingOptions = new ConcurrentHashMap<>();

    @PostConstruct
    public void preloadData() {
        SellerDetail sellerDetail = new SellerDetail(
            "S123",
            "TechStoreOficial",
            4.8,
            "oro",
            12540,
            0.97,
            6,
            List.of(
                "Devolución gratis dentro de los 30 días",
                "Atención personalizada 24/7",
                "Facturación para empresas"
            )
        );

        Item demoItem = Item.builder()
            .id("MLA123456")
            .title("Auriculares Bluetooth Noise Cancelling")
            .permalink("https://www.mercadolibre.com.ar/MLA123456")
            .condition("new")
            .warranty("Garantía oficial 12 meses")
            .description("Auriculares inalámbricos con cancelación activa de ruido y hasta 30 horas de batería.")
            .price(new Item.Price(new BigDecimal("79999.90"), "ARS"))
            .availableQuantity(120)
            .soldQuantity(532)
            .lastUpdated(OffsetDateTime.of(2024, 5, 20, 10, 30, 0, 0, ZoneOffset.UTC))
            .seller(new Item.Seller(sellerDetail.id(), sellerDetail.nickname(), sellerDetail.reputation()))
            .shipping(new Item.Shipping(true, "fulfillment", List.of("envios_gratis", "retiro_en_sucursal")))
            .pictures(List.of(
                new Item.Picture(
                    "http://http2.mlstatic.com/D_1.jpg",
                    "https://http2.mlstatic.com/D_1.jpg",
                    "principal",
                    1
                ),
                new Item.Picture(
                    "http://http2.mlstatic.com/D_2.jpg",
                    "https://http2.mlstatic.com/D_2.jpg",
                    "detalle",
                    2
                )
            ))
            .attributes(List.of(
                new Item.Attribute("BRAND", "Marca", "SoundMagic"),
                new Item.Attribute("MODEL", "Modelo", "NoisePro X200"),
                new Item.Attribute("COLOR", "Color", "Negro")
            ))
            .build();

        items.put(demoItem.getId(), demoItem);
        sellers.put(sellerDetail.id(), sellerDetail);

        relatedItems.put(demoItem.getId(), List.of(
            new RelatedItem("MLA654321", "Auriculares In-Ear Inalámbricos", new BigDecimal("42999.00"),
                "https://http2.mlstatic.com/R_1.jpg"),
            new RelatedItem("MLA987654", "Auriculares Over-Ear Studio Pro", new BigDecimal("129999.99"),
                "https://http2.mlstatic.com/R_2.jpg"),
            new RelatedItem("MLA135790", "Soporte de Auriculares RGB", new BigDecimal("18999.50"),
                "https://http2.mlstatic.com/R_3.jpg")
        ));

        questions.put(demoItem.getId(), List.of(
            new Question("Q1", demoItem.getId(), "¿Incluye cable auxiliar?",
                OffsetDateTime.of(2024, 6, 1, 14, 5, 0, 0, ZoneOffset.UTC),
                "Sí, incluye cable auxiliar de 1,5 metros.",
                OffsetDateTime.of(2024, 6, 1, 15, 12, 0, 0, ZoneOffset.UTC)),
            new Question("Q2", demoItem.getId(), "¿Se pueden plegar para transportar?",
                OffsetDateTime.of(2024, 5, 29, 10, 30, 0, 0, ZoneOffset.UTC),
                "Se pliegan parcialmente y traen funda rígida.",
                OffsetDateTime.of(2024, 5, 29, 12, 45, 0, 0, ZoneOffset.UTC))
        ));

        reviews.put(demoItem.getId(), List.of(
            new Review("R1", demoItem.getId(), "María G.", 5, "Excelente compra",
                "Cancelación de ruido increíble y la batería dura muchísimo.",
                OffsetDateTime.of(2024, 6, 2, 9, 20, 0, 0, ZoneOffset.UTC)),
            new Review("R2", demoItem.getId(), "Carlos P.", 4, "Muy buenos",
                "Cómodos y buen sonido. El micrófono podría ser mejor.",
                OffsetDateTime.of(2024, 5, 28, 18, 5, 0, 0, ZoneOffset.UTC)),
            new Review("R3", demoItem.getId(), "Lucía M.", 3, "Cumplen",
                "Esperaba más graves, pero aún así están bien por el precio.",
                OffsetDateTime.of(2024, 5, 20, 21, 10, 0, 0, ZoneOffset.UTC)),
            new Review("R4", demoItem.getId(), "Federico L.", 5, "Los volvería a comprar",
                "Son cómodos para usar todo el día y la app móvil es muy completa.",
                OffsetDateTime.of(2024, 4, 30, 11, 0, 0, 0, ZoneOffset.UTC))
        ));

        shippingOptions.put(demoItem.getId(), List.of(
            new ShippingOption("express", "Mercado Envíos Flex", new BigDecimal("0.00"), 1, true),
            new ShippingOption("standard", "Correo Argentino", new BigDecimal("1999.99"), 3, false),
            new ShippingOption("pickup", "Retiro en sucursal", new BigDecimal("0.00"), 2, true)
        ));
    }

    @Override
    public Optional<Item> findById(String id) {
        return Optional.ofNullable(items.get(id));
    }

    @Override
    public List<Item> findAll() {
        return Collections.unmodifiableList(new ArrayList<>(items.values()));
    }

    @Override
    public List<RelatedItem> findRelatedByItemId(String itemId) {
        return relatedItems.getOrDefault(itemId, List.of());
    }

    @Override
    public Optional<SellerDetail> findDetailById(String sellerId) {
        return Optional.ofNullable(sellers.get(sellerId));
    }

    @Override
    public List<Question> findQuestionsByItemId(String itemId) {
        return questions.getOrDefault(itemId, List.of());
    }

    @Override
    public List<Review> findReviewsByItemId(String itemId) {
        return reviews.getOrDefault(itemId, List.of());
    }

    @Override
    public List<ShippingOption> findByItemIdAndZipcode(String itemId, String zipcode) {
        List<ShippingOption> baseOptions = shippingOptions.getOrDefault(itemId, List.of());
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

