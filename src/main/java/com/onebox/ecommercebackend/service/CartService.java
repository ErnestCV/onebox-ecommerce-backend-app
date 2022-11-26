package com.onebox.ecommercebackend.service;

import com.onebox.ecommercebackend.exception.CartNorFoundException;
import com.onebox.ecommercebackend.model.Cart;
import com.onebox.ecommercebackend.model.dto.AddConditionRequest;
import com.onebox.ecommercebackend.model.dto.AddProductRequest;
import com.onebox.ecommercebackend.model.dto.RemoveProductRequest;
import com.onebox.ecommercebackend.repository.CartCache;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class CartService {

    private final CartCache cartCache;

    public CartService(CartCache cartCache) {
        this.cartCache = cartCache;
    }

    public Cart getCart(String id) {

        return cartCache.get(id).orElseThrow(() -> new CartNorFoundException("Cart with id " + id + " not found"));
    }

    public Cart newCart(AddProductRequest request) {

        Cart cart = new Cart(request.getProducts());
        cartCache.add(cart.getId(), cart);
        return cart;
    }

    public Cart addProducts(AddProductRequest request, String id) {

        Cart cart = this.getCart(id);
        cart.addProducts(request.getProducts());

        log.info("Added {} products to cart with id: {}", request.getProducts().size(), id);
        return cart;
    }

    public Cart removeProducts(RemoveProductRequest request, String id) {

        Cart cart = this.getCart(id);
        request.getIds().forEach(productId -> cart.getProducts().remove(productId));

        log.info("Removed {} products to cart with id: {}", request.getIds().size(), id);
        return cart;
    }

    public void deleteCart(String id) {

        cartCache.delete(id);
    }

    public Cart addCondition(AddConditionRequest request, String id) {

        Cart cart = this.getCart(id);
        cart.addConditions(request.getConditions());

        log.info("Added {} conditions to cart with id: {}", request.getConditions().size(), id);
        return cart;
    }
}
