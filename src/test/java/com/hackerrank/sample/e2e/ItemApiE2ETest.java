package com.hackerrank.sample.e2e;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ItemApiE2ETest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    @DisplayName("GET /api/items/{id} devuelve el detalle completo (E2E)")
    void getItemDetail_endToEnd() {
        ResponseEntity<Map> response = restTemplate.getForEntity(
            url("/api/items/MLA123456"),
            Map.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        Map<String, Object> body = response.getBody();
        assertThat(body).isNotNull();
        assertThat(body.get("id")).isEqualTo("MLA123456");
        assertThat(body.get("title")).isEqualTo("Auriculares Bluetooth Noise Cancelling");
        assertThat(body.get("availableQuantity")).isEqualTo(120);

        Map<String, Object> seller = (Map<String, Object>) body.get("seller");
        assertThat(seller).isNotNull();
        assertThat(seller.get("nickname")).isEqualTo("TechStoreOficial");

        Map<String, Object> shipping = (Map<String, Object>) body.get("shipping");
        assertThat(shipping).isNotNull();
        List<String> tags = (List<String>) shipping.get("tags");
        assertThat(tags).contains("envios_gratis");
    }

    @Test
    @DisplayName("GET /api/items/{id} devuelve 404 cuando no existe (E2E)")
    void getItemDetail_endToEndNotFound() {
        ResponseEntity<String> response = restTemplate.getForEntity(
            url("/api/items/MLA000000"),
            String.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    private String url(String path) {
        return "http://localhost:" + port + path;
    }
}


