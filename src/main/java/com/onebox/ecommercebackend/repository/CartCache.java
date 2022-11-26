package com.onebox.ecommercebackend.repository;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.onebox.ecommercebackend.model.Cart;
import lombok.extern.slf4j.Slf4j;

import java.time.Duration;
import java.util.Optional;

@Slf4j
public class CartCache {

    private final Cache<String, Cart> cache;

    public CartCache(Duration duration) {

        cache = CacheBuilder.newBuilder()
                .expireAfterWrite(duration) //Sets a TTL for a given cart
                .build();
    }

    public Optional<Cart> get(String key) {

        Cart cart = cache.getIfPresent(key);
        return Optional.ofNullable(cart);
    }

    public void add(String key, Cart value) {

        if (key != null && value != null) {
            cache.put(key, value);
            log.info("Added cart with id: " + key);
        }
    }

    public void delete(String key) {

        cache.invalidate(key);
        log.info("Deleted cart with id: " + key);
    }
}
