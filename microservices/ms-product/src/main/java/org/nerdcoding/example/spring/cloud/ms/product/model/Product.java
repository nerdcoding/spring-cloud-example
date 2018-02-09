/*
 * Product.java
 *
 * Copyright (c) 2018, Tobias Koltsch. All rights reserved.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 2 and
 * only version 2 as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/gpl-2.0.html>.
 */

package org.nerdcoding.example.spring.cloud.ms.product.model;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.time.LocalDate;
import java.util.Objects;

@JsonDeserialize(builder = Product.Builder.class)
public class Product {

    private final Long id;
    private final String name;
    private final Double price;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private final LocalDate availableSince;

    private Product(final Builder builder) {
        this.id = builder.id;
        this.name = builder.name;
        this.price = builder.price;
        this.availableSince = builder.availableSince;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Double getPrice() {
        return price;
    }

    public LocalDate getAvailableSince() {
        return availableSince;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final Product product = (Product) o;
        return Objects.equals(id, product.id) &&
                Objects.equals(name, product.name) &&
                Objects.equals(price, product.price) &&
                Objects.equals(availableSince, product.availableSince);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, name, price, availableSince);
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", availableSince=" + availableSince +
                '}';
    }

    public static final class Builder {
        private Long id;
        private String name;
        private Double price;
        private LocalDate availableSince;

        public Builder() {
        }

        public Builder withId(final Long val) {
            id = val;
            return this;
        }

        public Builder withName(final String val) {
            name = val;
            return this;
        }

        public Builder withPrice(final Double val) {
            price = val;
            return this;
        }

        public Builder withAvailableSince(final LocalDate val) {
            availableSince = val;
            return this;
        }

        public Product build() {
            return new Product(this);
        }
    }
}
