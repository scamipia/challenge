package com.hackerrank.sample.service.mapper;

import com.hackerrank.sample.dto.ItemDetailDto;
import com.hackerrank.sample.model.Item;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class ItemMapper {

    public ItemDetailDto toDto(Item item) {
        return new ItemDetailDto(
            item.getId(),
            item.getTitle(),
            item.getCondition(),
            item.getPrice().amount(),
            item.getPrice().currency(),
            item.getPermalink(),
            item.getWarranty(),
            item.getDescription(),
            item.getAvailableQuantity(),
            item.getSoldQuantity(),
            item.getLastUpdated(),
            toSellerDto(item.getSeller()),
            toShippingDto(item.getShipping()),
            toPictureDtos(item.getPictures()),
            toAttributeDtos(item.getAttributes())
        );
    }

    private ItemDetailDto.SellerDto toSellerDto(Item.Seller seller) {
        return new ItemDetailDto.SellerDto(
            seller.id(),
            seller.nickname(),
            seller.reputation()
        );
    }

    private ItemDetailDto.ShippingDto toShippingDto(Item.Shipping shipping) {
        List<String> tags = shipping.tags();
        return new ItemDetailDto.ShippingDto(
            shipping.freeShipping(),
            shipping.logisticType(),
            tags == null ? List.of() : List.copyOf(tags)
        );
    }

    private List<ItemDetailDto.PictureDto> toPictureDtos(List<Item.Picture> pictures) {
        return pictures.stream()
            .map(picture -> new ItemDetailDto.PictureDto(
                picture.url(),
                picture.secureUrl(),
                picture.type(),
                picture.order()
            ))
            .toList();
    }

    private List<ItemDetailDto.AttributeDto> toAttributeDtos(List<Item.Attribute> attributes) {
        return attributes.stream()
            .map(attribute -> new ItemDetailDto.AttributeDto(
                attribute.id(),
                attribute.name(),
                attribute.valueName()
            ))
            .toList();
    }
}

