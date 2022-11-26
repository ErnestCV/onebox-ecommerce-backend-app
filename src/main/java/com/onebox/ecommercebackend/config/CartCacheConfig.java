package com.onebox.ecommercebackend.config;

import com.onebox.ecommercebackend.repository.CartCache;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
public class CartCacheConfig {

    private static final long TTL = 10L;

    @Bean
    public CartCache cartCache() {
        return new CartCache(Duration.ofMinutes(TTL));
    }
}
