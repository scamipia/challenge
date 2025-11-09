package com.hackerrank.sample.service;

import com.hackerrank.sample.dto.ItemDetailDto;
import com.hackerrank.sample.exception.NoSuchResourceFoundException;
import com.hackerrank.sample.model.Item;
import com.hackerrank.sample.repository.ItemRepository;
import com.hackerrank.sample.service.mapper.ItemMapper;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ItemServiceImplTest {

    private static final String ITEM_ID = "MLA123456";

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private ItemMapper itemMapper;

    @InjectMocks
    private ItemServiceImpl itemService;

    private Item sampleItem;
    private ItemDetailDto sampleDto;

    @BeforeEach
    void setUp() {
        sampleItem = Item.builder()
            .id(ITEM_ID)
            .title("Auriculares Bluetooth")
            .permalink("https://www.mercadolibre.com.ar/MLA123456")
            .condition("new")
            .warranty("12 meses")
            .description("Auriculares con cancelación de ruido.")
            .price(new Item.Price(new BigDecimal("12345.67"), "ARS"))
            .availableQuantity(10)
            .soldQuantity(5)
            .lastUpdated(OffsetDateTime.of(2024, 5, 20, 10, 30, 0, 0, ZoneOffset.UTC))
            .seller(new Item.Seller("S123", "TechStore", 4.8))
            .shipping(new Item.Shipping(true, "fulfillment", List.of("envios_gratis")))
            .pictures(List.of(
                new Item.Picture("http://mlstatic.com/1.jpg", "https://mlstatic.com/1.jpg", "principal", 1)
            ))
            .attributes(List.of(
                new Item.Attribute("COLOR", "Color", "Negro")
            ))
            .build();

        sampleDto = new ItemDetailDto(
            sampleItem.getId(),
            sampleItem.getTitle(),
            sampleItem.getCondition(),
            sampleItem.getPrice().amount(),
            sampleItem.getPrice().currency(),
            sampleItem.getPermalink(),
            sampleItem.getWarranty(),
            sampleItem.getDescription(),
            sampleItem.getAvailableQuantity(),
            sampleItem.getSoldQuantity(),
            sampleItem.getLastUpdated(),
            new ItemDetailDto.SellerDto(
                sampleItem.getSeller().id(),
                sampleItem.getSeller().nickname(),
                sampleItem.getSeller().reputation()
            ),
            new ItemDetailDto.ShippingDto(
                sampleItem.getShipping().freeShipping(),
                sampleItem.getShipping().logisticType(),
                sampleItem.getShipping().tags()
            ),
            List.of(new ItemDetailDto.PictureDto(
                sampleItem.getPictures().getFirst().url(),
                sampleItem.getPictures().getFirst().secureUrl(),
                sampleItem.getPictures().getFirst().type(),
                sampleItem.getPictures().getFirst().order()
            )),
            List.of(new ItemDetailDto.AttributeDto(
                sampleItem.getAttributes().getFirst().id(),
                sampleItem.getAttributes().getFirst().name(),
                sampleItem.getAttributes().getFirst().valueName()
            ))
        );
    }

    @Test
    void getItemDetail_devuelveDtoCuandoExisteElItem() {
        when(itemRepository.findById(ITEM_ID)).thenReturn(Optional.of(sampleItem));
        when(itemMapper.toDto(sampleItem)).thenReturn(sampleDto);

        ItemDetailDto result = itemService.getItemDetail(ITEM_ID);

        assertThat(result).isEqualTo(sampleDto);

        ArgumentCaptor<String> idCaptor = ArgumentCaptor.forClass(String.class);
        verify(itemRepository).findById(idCaptor.capture());
        assertThat(idCaptor.getValue()).isEqualTo(ITEM_ID);
        verify(itemMapper).toDto(sampleItem);
    }

    @Test
    void getItemDetail_lanzaExcepcionCuandoNoExisteItem() {
        when(itemRepository.findById(ITEM_ID)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> itemService.getItemDetail(ITEM_ID))
            .isInstanceOf(NoSuchResourceFoundException.class)
            .hasMessageContaining(ITEM_ID);
    }
}


