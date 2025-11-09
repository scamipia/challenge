package com.hackerrank.sample.controller;

import com.hackerrank.sample.dto.ItemDetailDto;
import com.hackerrank.sample.dto.QuestionDto;
import com.hackerrank.sample.dto.RelatedItemDto;
import com.hackerrank.sample.dto.ReviewDto;
import com.hackerrank.sample.dto.ReviewPageDto;
import com.hackerrank.sample.dto.ReviewSummaryDto;
import com.hackerrank.sample.dto.ShippingOptionDto;
import com.hackerrank.sample.exception.NoSuchResourceFoundException;
import com.hackerrank.sample.service.ItemExtrasService;
import com.hackerrank.sample.service.ItemService;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ItemController.class)
class ItemControllerTest {

    private static final String ITEM_ID = "MLA777777";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ItemService itemService;

    @MockBean
    private ItemExtrasService itemExtrasService;

    @Test
    @DisplayName("GET /api/items/{id} devuelve 200 y el cuerpo esperado cuando el item existe")
    void getItemDetail_devuelveItemCuandoExiste() throws Exception {
        ItemDetailDto dto = buildSampleDto();
        when(itemService.getItemDetail(ITEM_ID)).thenReturn(dto);

        mockMvc.perform(get("/api/items/{id}", ITEM_ID))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(dto.id()))
            .andExpect(jsonPath("$.title").value(dto.title()))
            .andExpect(jsonPath("$.seller.id").value(dto.seller().id()))
            .andExpect(jsonPath("$.shipping.tags[0]").value(dto.shipping().tags().getFirst()));

        verify(itemService).getItemDetail(ITEM_ID);
    }

    @Test
    @DisplayName("GET /api/items/{id} devuelve 404 cuando el item no existe")
    void getItemDetail_devuelveNotFoundCuandoNoExiste() throws Exception {
        when(itemService.getItemDetail(ITEM_ID)).thenThrow(new NoSuchResourceFoundException("No existe"));

        mockMvc.perform(get("/api/items/{id}", ITEM_ID))
            .andExpect(status().isNotFound());

        verify(itemService).getItemDetail(ITEM_ID);
    }

    @Test
    @DisplayName("GET /api/items/{id}/related devuelve la lista de relacionados")
    void getRelatedItems_devuelveLista() throws Exception {
        List<RelatedItemDto> related = List.of(
            new RelatedItemDto("MLA1", "Item 1", new BigDecimal("100.00"), "thumb1"),
            new RelatedItemDto("MLA2", "Item 2", new BigDecimal("200.00"), "thumb2")
        );
        when(itemExtrasService.getRelatedItems(ITEM_ID)).thenReturn(related);

        mockMvc.perform(get("/api/items/{id}/related", ITEM_ID))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].id").value("MLA1"))
            .andExpect(jsonPath("$[1].price").value(200.00));

        verify(itemExtrasService).getRelatedItems(ITEM_ID);
    }

    @Test
    @DisplayName("GET /api/items/{id}/questions devuelve las preguntas y respuestas")
    void getQuestions_devuelvePreguntas() throws Exception {
        List<QuestionDto> questions = List.of(
            new QuestionDto("Q1", "¿Listo para retirar?", OffsetDateTime.now(ZoneOffset.UTC), "Sí", OffsetDateTime.now(ZoneOffset.UTC))
        );
        when(itemExtrasService.getQuestions(ITEM_ID)).thenReturn(questions);

        mockMvc.perform(get("/api/items/{id}/questions", ITEM_ID))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].id").value("Q1"))
            .andExpect(jsonPath("$[0].answer").value("Sí"));

        verify(itemExtrasService).getQuestions(ITEM_ID);
    }

    @Test
    @DisplayName("GET /api/items/{id}/reviews retorna la paginación y métricas")
    void getReviews_devuelvePagina() throws Exception {
        ReviewPageDto pageDto = new ReviewPageDto(
            List.of(
                new ReviewDto("R1", "Autor", 5, "Titulo", "Comentario", OffsetDateTime.now(ZoneOffset.UTC))
            ),
            new ReviewSummaryDto(4.5, 10, Map.of(5, 7L, 4, 2L, 3, 1L, 2, 0L, 1, 0L)),
            0,
            10,
            1
        );
        when(itemExtrasService.getReviews(ITEM_ID, 0, 10)).thenReturn(pageDto);

        mockMvc.perform(get("/api/items/{id}/reviews", ITEM_ID))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.reviews[0].id").value("R1"))
            .andExpect(jsonPath("$.summary.averageRating").value(4.5))
            .andExpect(jsonPath("$.totalPages").value(1));

        verify(itemExtrasService).getReviews(ITEM_ID, 0, 10);
    }

    @Test
    @DisplayName("GET /api/items/{id}/shipping-options responde con las opciones calculadas")
    void getShippingOptions_devuelveOpciones() throws Exception {
        List<ShippingOptionDto> options = List.of(
            new ShippingOptionDto("standard", "Correo", new BigDecimal("1000.00"), 3, false)
        );
        when(itemExtrasService.getShippingOptions(ITEM_ID, "1000")).thenReturn(options);

        mockMvc.perform(get("/api/items/{id}/shipping-options", ITEM_ID).param("zipcode", "1000"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].type").value("standard"))
            .andExpect(jsonPath("$[0].cost").value(1000.00));

        verify(itemExtrasService).getShippingOptions(ITEM_ID, "1000");
    }

    private ItemDetailDto buildSampleDto() {
        return new ItemDetailDto(
            ITEM_ID,
            "Auriculares Premium",
            "new",
            new BigDecimal("150000.00"),
            "ARS",
            "https://www.mercadolibre.com.ar/MLA777777",
            "Garantía oficial",
            "Auriculares con cancelación activa",
            20,
            15,
            OffsetDateTime.of(2024, 6, 1, 12, 0, 0, 0, ZoneOffset.UTC),
            new ItemDetailDto.SellerDto("S777", "AudioStore", 4.9),
            new ItemDetailDto.ShippingDto(true, "fulfillment", List.of("envios_gratis", "retiro_en_sucursal")),
            List.of(new ItemDetailDto.PictureDto("http://mlstatic.com/auriculares.jpg", null, "principal", 1)),
            List.of(new ItemDetailDto.AttributeDto("BRAND", "Marca", "AudioMax"))
        );
    }
}
