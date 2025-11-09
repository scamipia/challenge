package com.hackerrank.sample.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Representa la información necesaria para renderizar la página de detalle de un item
 * al estilo Mercado Libre. Se modelan los atributos más habituales que requiere un
 * frontend: datos básicos, precio, vendedor, logística, atributos y recursos multimedia.
 *
 * Esta clase es inmutable para facilitar su uso en servicios de solo lectura y permite
 * la serialización directa a JSON mediante Jackson.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public final class Item implements Serializable {
    private final String id;
    private final String title;
    private final String permalink;
    private final String condition;
    private final String warranty;
    private final String description;
    private final Price price;
    private final int availableQuantity;
    private final int soldQuantity;
    private final OffsetDateTime lastUpdated;
    private final Seller seller;
    private final Shipping shipping;
    private final List<Picture> pictures;
    private final List<Attribute> attributes;

    private Item(Builder builder) {
        this.id = builder.id;
        this.title = builder.title;
        this.permalink = builder.permalink;
        this.condition = builder.condition;
        this.warranty = builder.warranty;
        this.description = builder.description;
        this.price = builder.price;
        this.availableQuantity = builder.availableQuantity;
        this.soldQuantity = builder.soldQuantity;
        this.lastUpdated = builder.lastUpdated;
        this.seller = builder.seller;
        this.shipping = builder.shipping;
        this.pictures = Collections.unmodifiableList(new ArrayList<>(builder.pictures));
        this.attributes = Collections.unmodifiableList(new ArrayList<>(builder.attributes));
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getPermalink() {
        return permalink;
    }

    public String getCondition() {
        return condition;
    }

    public String getWarranty() {
        return warranty;
    }

    public String getDescription() {
        return description;
    }

    public Price getPrice() {
        return price;
    }

    @JsonProperty("available_quantity")
    public int getAvailableQuantity() {
        return availableQuantity;
    }

    @JsonProperty("sold_quantity")
    public int getSoldQuantity() {
        return soldQuantity;
    }

    @JsonProperty("last_updated")
    public OffsetDateTime getLastUpdated() {
        return lastUpdated;
    }

    public Seller getSeller() {
        return seller;
    }

    public Shipping getShipping() {
        return shipping;
    }

    public List<Picture> getPictures() {
        return pictures;
    }

    public List<Attribute> getAttributes() {
        return attributes;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private String id;
        private String title;
        private String permalink;
        private String condition;
        private String warranty;
        private String description;
        private Price price;
        private int availableQuantity;
        private int soldQuantity;
        private OffsetDateTime lastUpdated;
        private Seller seller;
        private Shipping shipping;
        private List<Picture> pictures = new ArrayList<>();
        private List<Attribute> attributes = new ArrayList<>();

        private Builder() {
        }

        public Builder id(String id) {
            this.id = id;
            return this;
        }

        public Builder title(String title) {
            this.title = title;
            return this;
        }

        public Builder permalink(String permalink) {
            this.permalink = permalink;
            return this;
        }

        public Builder condition(String condition) {
            this.condition = condition;
            return this;
        }

        public Builder warranty(String warranty) {
            this.warranty = warranty;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder price(Price price) {
            this.price = price;
            return this;
        }

        public Builder availableQuantity(int availableQuantity) {
            this.availableQuantity = availableQuantity;
            return this;
        }

        public Builder soldQuantity(int soldQuantity) {
            this.soldQuantity = soldQuantity;
            return this;
        }

        public Builder lastUpdated(OffsetDateTime lastUpdated) {
            this.lastUpdated = lastUpdated;
            return this;
        }

        public Builder seller(Seller seller) {
            this.seller = seller;
            return this;
        }

        public Builder shipping(Shipping shipping) {
            Objects.requireNonNull(shipping, "shipping no puede ser nulo");
            this.shipping = new Shipping(
                shipping.freeShipping(),
                shipping.logisticType(),
                List.copyOf(shipping.tags())
            );
            return this;
        }

        public Builder pictures(List<Picture> pictures) {
            this.pictures = new ArrayList<>(Objects.requireNonNull(pictures));
            return this;
        }

        public Builder attributes(List<Attribute> attributes) {
            this.attributes = new ArrayList<>(Objects.requireNonNull(attributes));
            return this;
        }

        public Item build() {
            Objects.requireNonNull(id, "id no puede ser nulo");
            Objects.requireNonNull(title, "title no puede ser nulo");
            Objects.requireNonNull(price, "price no puede ser nulo");
            Objects.requireNonNull(seller, "seller no puede ser nulo");
            Objects.requireNonNull(shipping, "shipping no puede ser nulo");
            return new Item(this);
        }
    }

    public record Price(BigDecimal amount, String currency) implements Serializable {
        public Price {
            Objects.requireNonNull(amount, "amount no puede ser nulo");
            Objects.requireNonNull(currency, "currency no puede ser nulo");
        }
    }

    public record Seller(String id, String nickname, double reputation) implements Serializable {
        public Seller {
            Objects.requireNonNull(id, "id no puede ser nulo");
            Objects.requireNonNull(nickname, "nickname no puede ser nulo");
        }
    }

    public record Shipping(boolean freeShipping, String logisticType, List<String> tags) implements Serializable {
        public Shipping {
            Objects.requireNonNull(tags, "tags no puede ser nulo");
        }
    }

    public record Picture(String url, String secureUrl, String type, int order) implements Serializable {
        public Picture {
            Objects.requireNonNull(url, "url no puede ser nulo");
        }
    }

    public record Attribute(String id, String name, String valueName) implements Serializable {
        public Attribute {
            Objects.requireNonNull(id, "id no puede ser nulo");
            Objects.requireNonNull(name, "name no puede ser nulo");
        }
    }
}

