package com.hackerrank.sample.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "id",
    "title",
    "condition",
    "price",
    "currency",
    "permalink",
    "warranty",
    "description",
    "available_quantity",
    "sold_quantity",
    "last_updated",
    "seller",
    "shipping",
    "pictures",
    "attributes"
})
public record ItemDetailDto(
    String id,
    String title,
    String condition,
    BigDecimal price,
    String currency,
    String permalink,
    String warranty,
    String description,
    int availableQuantity,
    int soldQuantity,
    OffsetDateTime lastUpdated,
    SellerDto seller,
    ShippingDto shipping,
    List<PictureDto> pictures,
    List<AttributeDto> attributes
) {
    public record SellerDto(String id, String nickname, double reputation) {
    }

    public record ShippingDto(boolean freeShipping, String logisticType, List<String> tags) {
    }

    public record PictureDto(String url, String secureUrl, String type, int order) {
    }

    public record AttributeDto(String id, String name, String valueName) {
    }
}

