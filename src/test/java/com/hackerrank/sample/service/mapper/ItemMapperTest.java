package com.hackerrank.sample.service.mapper;

import com.hackerrank.sample.dto.ItemDetailDto;
import com.hackerrank.sample.model.Item;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ItemMapperTest {

    private final ItemMapper mapper = new ItemMapper();

    @Test
    void toDto_mapeaTodosLosCamposDelItem() {
        List<Item.Picture> pictures = List.of(
            new Item.Picture("http://mlstatic.com/1.jpg", "https://mlstatic.com/1.jpg", "principal", 1),
            new Item.Picture("http://mlstatic.com/2.jpg", "https://mlstatic.com/2.jpg", "detalle", 2)
        );
        List<Item.Attribute> attributes = List.of(
            new Item.Attribute("BRAND", "Marca", "SoundMagic"),
            new Item.Attribute("COLOR", "Color", "Negro")
        );
        List<String> tags = new ArrayList<>(List.of("envios_gratis", "retiro_en_sucursal"));

        Item item = Item.builder()
            .id("MLA999999")
            .title("Auriculares Gamer")
            .permalink("https://www.mercadolibre.com.ar/MLA999999")
            .condition("used")
            .warranty("3 meses vendedor")
            .description("Auriculares gamer con luces RGB.")
            .price(new Item.Price(new BigDecimal("45999.99"), "ARS"))
            .availableQuantity(42)
            .soldQuantity(123)
            .lastUpdated(OffsetDateTime.of(2024, 4, 15, 8, 45, 0, 0, ZoneOffset.UTC))
            .seller(new Item.Seller("S999", "GamingStore", 4.5))
            .shipping(new Item.Shipping(true, "fulfillment", tags))
            .pictures(pictures)
            .attributes(attributes)
            .build();

        ItemDetailDto dto = mapper.toDto(item);

        assertThat(dto.id()).isEqualTo(item.getId());
        assertThat(dto.title()).isEqualTo(item.getTitle());
        assertThat(dto.condition()).isEqualTo(item.getCondition());
        assertThat(dto.price()).isEqualTo(item.getPrice().amount());
        assertThat(dto.currency()).isEqualTo(item.getPrice().currency());
        assertThat(dto.permalink()).isEqualTo(item.getPermalink());
        assertThat(dto.warranty()).isEqualTo(item.getWarranty());
        assertThat(dto.description()).isEqualTo(item.getDescription());
        assertThat(dto.availableQuantity()).isEqualTo(item.getAvailableQuantity());
        assertThat(dto.soldQuantity()).isEqualTo(item.getSoldQuantity());
        assertThat(dto.lastUpdated()).isEqualTo(item.getLastUpdated());
        assertThat(dto.seller().id()).isEqualTo(item.getSeller().id());
        assertThat(dto.seller().nickname()).isEqualTo(item.getSeller().nickname());
        assertThat(dto.seller().reputation()).isEqualTo(item.getSeller().reputation());
        assertThat(dto.shipping().freeShipping()).isEqualTo(item.getShipping().freeShipping());
        assertThat(dto.shipping().logisticType()).isEqualTo(item.getShipping().logisticType());
        assertThat(dto.shipping().tags()).containsExactlyElementsOf(tags);
        assertThat(dto.pictures())
            .extracting(ItemDetailDto.PictureDto::url)
            .containsExactly("http://mlstatic.com/1.jpg", "http://mlstatic.com/2.jpg");
        assertThat(dto.attributes())
            .extracting(ItemDetailDto.AttributeDto::id)
            .containsExactly("BRAND", "COLOR");
    }

    @Test
    void toDto_retornaListasInmutables() {
        Item item = Item.builder()
            .id("MLA111111")
            .title("Smartwatch")
            .permalink("https://www.mercadolibre.com.ar/MLA111111")
            .condition("new")
            .warranty("6 meses")
            .description("Smartwatch multideporte")
            .price(new Item.Price(new BigDecimal("99999.50"), "ARS"))
            .availableQuantity(5)
            .soldQuantity(2)
            .lastUpdated(OffsetDateTime.now(ZoneOffset.UTC))
            .seller(new Item.Seller("S321", "WearablesStore", 4.7))
            .shipping(new Item.Shipping(false, "custom", new ArrayList<>(List.of("retiro_local"))))
            .pictures(List.of(new Item.Picture("http://mlstatic.com/watch.jpg", null, "principal", 1)))
            .attributes(List.of(new Item.Attribute("MODEL", "Modelo", "FitPro")))
            .build();

        ItemDetailDto dto = mapper.toDto(item);

        assertThatThrownBy(() -> dto.shipping().tags().add("nuevo_tag"))
            .isInstanceOf(UnsupportedOperationException.class);
        assertThatThrownBy(() -> dto.pictures().add(
            new ItemDetailDto.PictureDto("http://mlstatic.com/extra.jpg", null, "extra", 2)
        ))
            .isInstanceOf(UnsupportedOperationException.class);
        assertThatThrownBy(() -> dto.attributes().clear())
            .isInstanceOf(UnsupportedOperationException.class);
    }
}


