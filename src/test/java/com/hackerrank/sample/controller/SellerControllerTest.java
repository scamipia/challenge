package com.hackerrank.sample.controller;

import com.hackerrank.sample.dto.SellerDetailDto;
import com.hackerrank.sample.service.ItemExtrasService;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SellerController.class)
class SellerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ItemExtrasService itemExtrasService;

    @Test
    @DisplayName("GET /api/sellers/{sellerId} devuelve el detalle del vendedor")
    void getSellerDetail_devuelveDetalle() throws Exception {
        SellerDetailDto seller = new SellerDetailDto(
            "S123",
            "TechStoreOficial",
            4.8,
            "oro",
            1200,
            0.95,
            6,
            List.of("Devoluciones gratis", "Atención 24/7")
        );

        when(itemExtrasService.getSellerDetail("S123")).thenReturn(seller);

        mockMvc.perform(get("/api/sellers/{sellerId}", "S123"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value("S123"))
            .andExpect(jsonPath("$.totalSales").value(1200))
            .andExpect(jsonPath("$.policies[0]").value("Devoluciones gratis"));

        verify(itemExtrasService).getSellerDetail("S123");
    }
}


