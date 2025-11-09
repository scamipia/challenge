package com.hackerrank.sample.service;

import com.hackerrank.sample.dto.QuestionDto;
import com.hackerrank.sample.dto.RelatedItemDto;
import com.hackerrank.sample.dto.ReviewDto;
import com.hackerrank.sample.dto.ReviewPageDto;
import com.hackerrank.sample.dto.ReviewSummaryDto;
import com.hackerrank.sample.dto.SellerDetailDto;
import com.hackerrank.sample.dto.ShippingOptionDto;
import com.hackerrank.sample.exception.NoSuchResourceFoundException;
import com.hackerrank.sample.model.Item;
import com.hackerrank.sample.model.Question;
import com.hackerrank.sample.model.RelatedItem;
import com.hackerrank.sample.model.Review;
import com.hackerrank.sample.model.SellerDetail;
import com.hackerrank.sample.model.ShippingOption;
import com.hackerrank.sample.repository.ItemRepository;
import com.hackerrank.sample.repository.QuestionRepository;
import com.hackerrank.sample.repository.RelatedItemRepository;
import com.hackerrank.sample.repository.ReviewRepository;
import com.hackerrank.sample.repository.SellerDetailRepository;
import com.hackerrank.sample.repository.ShippingOptionRepository;
import com.hackerrank.sample.service.mapper.ItemExtrasMapper;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ItemExtrasServiceImplTest {

    private static final String ITEM_ID = "MLA123456";
    private static final String SELLER_ID = "S123";

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private RelatedItemRepository relatedItemRepository;

    @Mock
    private SellerDetailRepository sellerDetailRepository;

    @Mock
    private QuestionRepository questionRepository;

    @Mock
    private ReviewRepository reviewRepository;

    @Mock
    private ShippingOptionRepository shippingOptionRepository;

    @Mock
    private ItemExtrasMapper mapper;

    @InjectMocks
    private ItemExtrasServiceImpl itemExtrasService;

    private Item sampleItem;

    @BeforeEach
    void setUp() {
        sampleItem = Item.builder()
            .id(ITEM_ID)
            .title("Auriculares")
            .permalink("https://example.com")
            .condition("new")
            .warranty("12 meses")
            .description("descripcion")
            .price(new Item.Price(new BigDecimal("100.00"), "ARS"))
            .availableQuantity(10)
            .soldQuantity(5)
            .lastUpdated(OffsetDateTime.now(ZoneOffset.UTC))
            .seller(new Item.Seller(SELLER_ID, "Seller", 4.5))
            .shipping(new Item.Shipping(true, "fulfillment", List.of("envios_gratis")))
            .pictures(List.of(new Item.Picture("http://img", null, "principal", 1)))
            .attributes(List.of(new Item.Attribute("COLOR", "Color", "Negro")))
            .build();
    }

    @Test
    @DisplayName("getRelatedItems retorna los relacionados mapeados cuando el item existe")
    void getRelatedItems_itemExiste() {
        given(itemRepository.findById(ITEM_ID)).willReturn(Optional.of(sampleItem));
        List<RelatedItem> related = List.of(
            new RelatedItem("MLA2", "Item", new BigDecimal("200"), "thumb")
        );
        given(relatedItemRepository.findRelatedByItemId(ITEM_ID)).willReturn(related);

        List<RelatedItemDto> mapped = List.of(new RelatedItemDto("MLA2", "Item", new BigDecimal("200"), "thumb"));
        given(mapper.toRelatedDtos(related)).willReturn(mapped);

        List<RelatedItemDto> result = itemExtrasService.getRelatedItems(ITEM_ID);

        assertThat(result).isEqualTo(mapped);
        verify(mapper).toRelatedDtos(related);
    }

    @Test
    @DisplayName("getSellerDetail lanza excepción cuando no existe el vendedor")
    void getSellerDetail_vendedorInexistente() {
        given(sellerDetailRepository.findDetailById(SELLER_ID)).willReturn(Optional.empty());

        assertThatThrownBy(() -> itemExtrasService.getSellerDetail(SELLER_ID))
            .isInstanceOf(NoSuchResourceFoundException.class);
    }

    @Test
    @DisplayName("getQuestions retorna lista de preguntas mapeada")
    void getQuestions_devuelvePreguntas() {
        List<Question> questionList = List.of(
            new Question("Q1", ITEM_ID, "¿Hay stock?", OffsetDateTime.now(ZoneOffset.UTC), "Sí", OffsetDateTime.now(ZoneOffset.UTC))
        );
        List<QuestionDto> dtoList = List.of(
            new QuestionDto("Q1", "¿Hay stock?", OffsetDateTime.now(ZoneOffset.UTC), "Sí", OffsetDateTime.now(ZoneOffset.UTC))
        );

        given(itemRepository.findById(ITEM_ID)).willReturn(Optional.of(sampleItem));
        given(questionRepository.findQuestionsByItemId(ITEM_ID)).willReturn(questionList);
        given(mapper.toQuestionDtos(questionList)).willReturn(dtoList);

        List<QuestionDto> result = itemExtrasService.getQuestions(ITEM_ID);

        assertThat(result).isEqualTo(dtoList);
    }

    @Test
    @DisplayName("getReviews calcula métricas y devuelve paginación")
    void getReviews_calculaMetricas() {
        Review review5 = new Review("R1", ITEM_ID, "Autor 1", 5, "Excelente", "Muy bueno",
            OffsetDateTime.now(ZoneOffset.UTC));
        Review review3 = new Review("R2", ITEM_ID, "Autor 2", 3, "Normal", "Está bien",
            OffsetDateTime.now(ZoneOffset.UTC).minusDays(1));

        List<Review> reviews = List.of(review5, review3);
        List<ReviewDto> reviewDtos = List.of(
            new ReviewDto("R1", "Autor 1", 5, "Excelente", "Muy bueno", review5.createdAt()),
            new ReviewDto("R2", "Autor 2", 3, "Normal", "Está bien", review3.createdAt())
        );
        given(itemRepository.findById(ITEM_ID)).willReturn(Optional.of(sampleItem));
        given(reviewRepository.findReviewsByItemId(ITEM_ID)).willReturn(reviews);
        given(mapper.toReviewDtos(reviews)).willReturn(reviewDtos);
        given(mapper.toReviewSummaryDto(4.0, 2, Map.of(5, 1L, 4, 0L, 3, 1L, 2, 0L, 1, 0L)))
            .willReturn(new ReviewSummaryDto(4.0, 2, Map.of(5, 1L, 4, 0L, 3, 1L, 2, 0L, 1, 0L)));

        ReviewPageDto result = itemExtrasService.getReviews(ITEM_ID, 0, 10);

        assertThat(result.reviews()).hasSize(2);
        assertThat(result.summary().averageRating()).isEqualTo(4.0);
        assertThat(result.summary().ratingDistribution()).containsEntry(5, 1L);
    }

    @Test
    @DisplayName("getShippingOptions delega en el repositorio")
    void getShippingOptions_devuelveOpciones() {
        List<ShippingOption> options = List.of(
            new ShippingOption("standard", "Correo", new BigDecimal("1000"), 3, false)
        );
        List<ShippingOptionDto> optionDtos = List.of(
            new ShippingOptionDto("standard", "Correo", new BigDecimal("1000"), 3, false)
        );
        given(itemRepository.findById(ITEM_ID)).willReturn(Optional.of(sampleItem));
        given(shippingOptionRepository.findByItemIdAndZipcode(ITEM_ID, "1000")).willReturn(options);
        given(mapper.toShippingOptionDtos(options)).willReturn(optionDtos);

        List<ShippingOptionDto> result = itemExtrasService.getShippingOptions(ITEM_ID, "1000");

        assertThat(result).isEqualTo(optionDtos);
    }
}


