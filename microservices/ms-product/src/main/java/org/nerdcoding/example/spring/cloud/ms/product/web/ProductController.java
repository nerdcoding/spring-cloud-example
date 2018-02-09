/*
 * ProductController.java
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

package org.nerdcoding.example.spring.cloud.ms.product.web;

import org.nerdcoding.example.spring.cloud.ms.product.model.Product;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/product")
public class ProductController {

    private static final List<Product> IN_MEMORY_DB = new ArrayList<>(Arrays.asList(
            new Product.Builder()
                    .withId(System.nanoTime())
                    .withName("Product 1")
                    .withPrice(47.11)
                    .withAvailableSince(LocalDate.of(1986, 4, 22)).build(),
            new Product.Builder()
                    .withId(System.nanoTime())
                    .withName("Product 2")
                    .withPrice(123.45)
                    .withAvailableSince(LocalDate.of(2001, 7, 18)).build(),
            new Product.Builder()
                    .withId(System.nanoTime())
                    .withName("Product 3")
                    .withPrice(9.99)
                    .withAvailableSince(LocalDate.of(1974, 11, 4)).build()
    ));

    @GetMapping
    public ResponseEntity<List<Product>> getProducts() {
        return new ResponseEntity<>(IN_MEMORY_DB, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable("id") final Long id) {
        if (id == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        return IN_MEMORY_DB.stream()
                .filter(product -> product.getId().equals(id))
                .findFirst()
                .map(foundProduct -> new ResponseEntity<>(foundProduct, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    public ResponseEntity<Void> createPorduct(@RequestBody final Product product) {
        if (product == null
                || product.getName() == null
                || product.getPrice() == null
                || product.getAvailableSince() == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        IN_MEMORY_DB.add(new Product.Builder()
                .withId(System.nanoTime())
                .withName(product.getName())
                .withPrice(product.getPrice())
                .withAvailableSince(product.getAvailableSince()).build()
        );
        return new ResponseEntity<>(HttpStatus.CREATED);
    }


    @PutMapping("/{id}")
    public ResponseEntity<Void> updateProduct(@PathVariable("id") final Long id, @RequestBody final Product updatedProduct) {
        if (id == null
                || updatedProduct == null
                || updatedProduct.getName() == null
                || updatedProduct.getPrice() == null
                || updatedProduct.getAvailableSince() == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        if (IN_MEMORY_DB.removeIf(product -> product.getId().equals(id))) {
            IN_MEMORY_DB.add(new Product.Builder()
                    .withId(id)
                    .withName(updatedProduct.getName())
                    .withPrice(updatedProduct.getPrice())
                    .withAvailableSince(updatedProduct.getAvailableSince()).build()
            );
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Product> deleteProductById(@PathVariable("id") final Long id) {
        if (IN_MEMORY_DB.removeIf(product -> product.getId().equals(id))) {
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
