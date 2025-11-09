package com.hackerrank.sample.model;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ItemTest {

    @Test
    @DisplayName("build crea Item inmutable copiando las listas de entrada")
    void builder_creaItemInmutable() {
        List<Item.Picture> pictures = new ArrayList<>(List.of(
            new Item.Picture("http://mlstatic.com/1.jpg", null, "principal", 1)
        ));
        List<Item.Attribute> attributes = new ArrayList<>(List.of(
            new Item.Attribute("COLOR", "Color", "Negro")
        ));
        List<String> tags = new ArrayList<>(List.of("envios_gratis"));

        Item item = Item.builder()
            .id("MLA0001")
            .title("Tablet 10\"")
            .permalink("https://www.mercadolibre.com.ar/MLA0001")
            .condition("new")
            .warranty("Garantía 12 meses")
            .description("Tablet ideal para entretenimiento.")
            .price(new Item.Price(new BigDecimal("250000.00"), "ARS"))
            .availableQuantity(7)
            .soldQuantity(3)
            .lastUpdated(OffsetDateTime.of(2024, 6, 1, 12, 0, 0, 0, ZoneOffset.UTC))
            .seller(new Item.Seller("S0001", "TabletWorld", 4.6))
            .shipping(new Item.Shipping(true, "fulfillment", tags))
            .pictures(pictures)
            .attributes(attributes)
            .build();

        pictures.add(new Item.Picture("http://mlstatic.com/2.jpg", null, "detalle", 2));
        attributes.add(new Item.Attribute("MEMORY", "Memoria RAM", "8GB"));
        tags.add("retiro_en_sucursal");

        assertThat(item.getPictures()).hasSize(1);
        assertThatThrownBy(() -> item.getPictures().add(
            new Item.Picture("http://mlstatic.com/3.jpg", null, "extra", 3)))
            .isInstanceOf(UnsupportedOperationException.class);

        assertThat(item.getAttributes()).hasSize(1);
        assertThatThrownBy(() -> item.getAttributes().clear())
            .isInstanceOf(UnsupportedOperationException.class);

        assertThat(item.getShipping().tags()).containsExactly("envios_gratis");
        assertThatThrownBy(() -> item.getShipping().tags().add("nuevo_tag"))
            .isInstanceOf(UnsupportedOperationException.class);
    }

    @Test
    @DisplayName("build lanza NullPointerException cuando faltan campos obligatorios")
    void builder_fallaCuandoFaltanCamposObligatorios() {
        Item.Builder builder = Item.builder()
            .title("Notebook Gamer");

        assertThatThrownBy(builder::build)
            .isInstanceOf(NullPointerException.class)
            .hasMessageContaining("id");
    }

    @Test
    @DisplayName("Price exige amount y currency no nulos")
    void price_validaArgumentosNoNulos() {
        assertThatThrownBy(() -> new Item.Price(null, "ARS"))
            .isInstanceOf(NullPointerException.class)
            .hasMessageContaining("amount");
        assertThatThrownBy(() -> new Item.Price(new BigDecimal("1.0"), null))
            .isInstanceOf(NullPointerException.class)
            .hasMessageContaining("currency");
    }

    @Test
    @DisplayName("Seller exige id y nickname no nulos")
    void seller_validaArgumentosNoNulos() {
        assertThatThrownBy(() -> new Item.Seller(null, "Nick", 4.5))
            .isInstanceOf(NullPointerException.class)
            .hasMessageContaining("id");
        assertThatThrownBy(() -> new Item.Seller("S1", null, 4.5))
            .isInstanceOf(NullPointerException.class)
            .hasMessageContaining("nickname");
    }

    @Test
    @DisplayName("Shipping exige lista de tags no nula")
    void shipping_validaArgumentosNoNulos() {
        assertThatThrownBy(() -> new Item.Shipping(true, "custom", null))
            .isInstanceOf(NullPointerException.class)
            .hasMessageContaining("tags");
    }

    @Test
    @DisplayName("Picture exige url no nula")
    void picture_validaUrlNoNula() {
        assertThatThrownBy(() -> new Item.Picture(null, null, "principal", 1))
            .isInstanceOf(NullPointerException.class)
            .hasMessageContaining("url");
    }

    @Test
    @DisplayName("Attribute exige id y name no nulos")
    void attribute_validaArgumentosNoNulos() {
        assertThatThrownBy(() -> new Item.Attribute(null, "Nombre", "Valor"))
            .isInstanceOf(NullPointerException.class)
            .hasMessageContaining("id");
        assertThatThrownBy(() -> new Item.Attribute("ID", null, "Valor"))
            .isInstanceOf(NullPointerException.class)
            .hasMessageContaining("name");
    }
}


