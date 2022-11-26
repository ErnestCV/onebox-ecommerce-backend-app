package com.onebox.ecommercebackend.controller;

import com.onebox.ecommercebackend.model.Cart;
import com.onebox.ecommercebackend.model.dto.AddConditionRequest;
import com.onebox.ecommercebackend.model.dto.AddProductRequest;
import com.onebox.ecommercebackend.model.dto.RemoveProductRequest;
import com.onebox.ecommercebackend.service.CartService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/carts")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping("/{id}")
    public Cart getCarts(@PathVariable String id) {

        return cartService.getCart(id);
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public Cart createCart(@Valid @RequestBody AddProductRequest request) {

        return cartService.newCart(request);
    }

    @PostMapping("/{id}")
    public Cart addToCart(@Valid @RequestBody AddProductRequest request, @PathVariable String id) {

        return cartService.addProducts(request, id);
    }

    @PutMapping("/{id}")
    public Cart removeProducts(@Valid @RequestBody RemoveProductRequest request, @PathVariable String id) {

        return cartService.removeProducts(request, id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCart(@PathVariable String id) {

        cartService.deleteCart(id);
    }

    @PostMapping("/{id}/conditions")
    public Cart addCondition(@Valid @RequestBody AddConditionRequest request, @PathVariable String id) {

        return cartService.addCondition(request, id);
    }
}
