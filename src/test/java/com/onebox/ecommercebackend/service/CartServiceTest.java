package com.onebox.ecommercebackend.service;

import com.onebox.ecommercebackend.exception.CartNorFoundException;
import com.onebox.ecommercebackend.model.Cart;
import com.onebox.ecommercebackend.model.CartCondition;
import com.onebox.ecommercebackend.model.Product;
import com.onebox.ecommercebackend.model.dto.AddConditionRequest;
import com.onebox.ecommercebackend.model.dto.AddProductRequest;
import com.onebox.ecommercebackend.model.dto.RemoveProductRequest;
import com.onebox.ecommercebackend.repository.CartCache;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class CartServiceTest {

    @Mock
    CartCache cartCache;

    @InjectMocks
    CartService cartService;

    Product product;
    String cartId;
    Cart cart;
    CartCondition cartCondition;
    AddProductRequest addProductRequest;

    @BeforeEach
    void setup() {

        product = new Product(1, "Apple", 3);

        addProductRequest = new AddProductRequest();
        Set<Product> products = new HashSet<>();
        products.add(product);
        addProductRequest.setProducts(products);

        Map<Integer, Product> productMap = new HashMap<>();
        productMap.put(product.getId(), product);

        cartCondition = new CartCondition();
        cartCondition.setId(1);
        cartCondition.setDescription("Description");
        cartCondition.setTarget("-1");
        cartCondition.setValue("-10%");

        Map<Integer, CartCondition> conditionMap = new HashMap<>();
        conditionMap.put(cartCondition.getId(), cartCondition);

        cartId = "1";
        cart = new Cart();
        cart.setId(cartId);
        cart.setProducts(productMap);
        cart.setConditions(conditionMap);
    }

    @Test
    void givenCartExists_whenGetCart_thenReturnCart() {

        given(cartCache.get(cartId)).willReturn(Optional.of(cart));

        Cart actual = cartService.getCart(cartId);

        assertThat(actual).isNotNull()
                .hasFieldOrPropertyWithValue("id", cartId)
                .hasFieldOrProperty("datetime")
                .hasFieldOrProperty("conditions");
        assertThat(actual.getProducts()).containsEntry(1, product);
        assertThat(actual.getConditions()).containsEntry(1, cartCondition);
    }

    @Test
    void givenCartNotExists_whenGetCart_thenThrowException() {

        given(cartCache.get(cartId)).willReturn(Optional.empty());

        assertThatExceptionOfType(CartNorFoundException.class)
                .isThrownBy(() -> cartService.getCart(cartId))
                .withMessage("Cart with id " + cartId + " not found");
    }

    @Test
    void whenCreateCart_thenReturnNewCart() {

        Cart actual = cartService.newCart(addProductRequest);

        assertThat(actual).isNotNull()
                .hasFieldOrProperty("id")
                .hasFieldOrProperty("products")
                .hasFieldOrProperty("datetime")
                .hasFieldOrProperty("conditions");
    }

    @Test
    void givenCartExists_whenAddNewProducts_thenReturnCart() {

        Product product = new Product(3, "Banana", 7);
        Set<Product> products = new HashSet<>();
        products.add(product);
        addProductRequest.setProducts(products);

        given(cartCache.get(cartId)).willReturn(Optional.of(cart));

        Cart actual = cartService.addProducts(addProductRequest, cartId);

        assertThat(actual).isNotNull()
                .hasFieldOrPropertyWithValue("id", cartId)
                .hasFieldOrProperty("datetime")
                .hasFieldOrProperty("conditions");
        assertThat(actual.getProducts()).containsEntry(3, product);
        assertThat(actual.getConditions()).containsEntry(1, cartCondition);
    }

    @Test
    void givenCartExists_whenRemoveProducts_thenReturnCart() {

        RemoveProductRequest removeProductRequest = new RemoveProductRequest();
        Set<Integer> productSet = new HashSet<>();
        productSet.add(1);
        productSet.add(2);
        removeProductRequest.setIds(productSet);

        given(cartCache.get(cartId)).willReturn(Optional.of(cart));

        Cart actual = cartService.removeProducts(removeProductRequest, cartId);

        assertThat(actual).isNotNull()
                .hasFieldOrPropertyWithValue("id", cartId)
                .hasFieldOrProperty("datetime")
                .hasFieldOrProperty("conditions");
        assertThat(actual.getProducts()).isEmpty();
        assertThat(actual.getConditions()).containsEntry(1, cartCondition);
    }

    @Test
    void whenDeleteCart_thenVerifyDelete() {

        cartService.deleteCart(cartId);

        verify(cartCache, times(1)).delete(cartId);
    }

    @Test
    void givenCartExists_whenAddCartCondition_thenReturnCart() {

        cartCondition = new CartCondition();
        cartCondition.setId(2);
        cartCondition.setDescription("Description");
        cartCondition.setTarget("1");
        cartCondition.setValue("-20%");

        Set<CartCondition> conditionSet = new HashSet<>();
        conditionSet.add(cartCondition);

        AddConditionRequest addConditionRequest = new AddConditionRequest();
        addConditionRequest.setConditions(conditionSet);

        given(cartCache.get(cartId)).willReturn(Optional.of(cart));

        Cart actual = cartService.addCondition(addConditionRequest, cartId);

        assertThat(actual).isNotNull()
                .hasFieldOrPropertyWithValue("id", cartId)
                .hasFieldOrProperty("datetime")
                .hasFieldOrProperty("conditions");
        assertThat(actual.getProducts()).containsEntry(1, product);
        assertThat(actual.getConditions()).containsEntry(2, cartCondition);
    }
}