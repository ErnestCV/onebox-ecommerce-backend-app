package com.onebox.ecommercebackend.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
public class Cart {

    private String id;
    private LocalDateTime datetime;
    private Map<Integer, Product> products;
    private Map<Integer, CartCondition> conditions;

    public Cart(Set<Product> products) {
        this.id = UUID.randomUUID().toString();
        this.datetime = LocalDateTime.now();
        this.products = products.stream().collect(Collectors.toMap(Product::getId, p -> p));
        this.conditions = new HashMap<>();
    }

    public void addProducts(Set<Product> productsToAdd) {

        productsToAdd.forEach(product -> {
            boolean exists = products.containsKey(product.getId());

            if (exists) {
                Product existingProduct = products.get(product.getId());
                existingProduct.setAmount(existingProduct.getAmount() + product.getAmount());
            } else {
                products.put(product.getId(), product);
            }
        });
    }

    public void addConditions(Set<CartCondition> conditionsToAdd) {
        conditionsToAdd.forEach(cartCondition -> conditions.put(cartCondition.getId(), cartCondition));
    }
}
