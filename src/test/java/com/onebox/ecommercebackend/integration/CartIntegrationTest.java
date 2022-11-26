package com.onebox.ecommercebackend.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.onebox.ecommercebackend.model.Cart;
import com.onebox.ecommercebackend.model.CartCondition;
import com.onebox.ecommercebackend.model.Product;
import com.onebox.ecommercebackend.model.dto.AddConditionRequest;
import com.onebox.ecommercebackend.model.dto.AddProductRequest;
import com.onebox.ecommercebackend.model.dto.RemoveProductRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.HashSet;
import java.util.Set;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class CartIntegrationTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    final String CONTROLLER_BASE_URL = "/api/v1/carts";

    Product product;
    String cartId;
    AddProductRequest addProductRequest;

    @BeforeEach
    void setup() throws Exception {

        product = new Product(1, "Apple", 3);

        addProductRequest = new AddProductRequest();
        Set<Product> products = new HashSet<>();
        products.add(product);
        addProductRequest.setProducts(products);

        MvcResult result = mockMvc.perform(post(CONTROLLER_BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(addProductRequest)))
                .andReturn();

        String contentAsString = result.getResponse().getContentAsString();
        Cart cart = objectMapper.readValue(contentAsString, Cart.class);
        cartId = cart.getId();
    }

    @Test
    void givenCartExists_whenGetCart_thenReturnCart() throws Exception {

        mockMvc.perform(get(CONTROLLER_BASE_URL + "/" + cartId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(cartId))
                .andExpect(jsonPath("$.datetime").hasJsonPath())
                .andExpect(jsonPath("$.products.1.id").value(1))
                .andExpect(jsonPath("$.products.1.description").value("Apple"))
                .andExpect(jsonPath("$.products.1.amount").value(3))
                .andExpect(jsonPath("$.conditions").isEmpty());
    }

    @Test
    void givenCartNotExists_whenGetCart_thenThrowException() throws Exception {

        mockMvc.perform(get(CONTROLLER_BASE_URL + "/" + "random-non-valid-UUID"))
                .andExpect(status().isNotFound());
    }

    @Test
    void whenCreateCart_thenReturnNewCart() throws Exception {

        mockMvc.perform(post(CONTROLLER_BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(addProductRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").hasJsonPath())
                .andExpect(jsonPath("$.datetime").hasJsonPath())
                .andExpect(jsonPath("$.products.1.id").value(1))
                .andExpect(jsonPath("$.products.1.description").value("Apple"))
                .andExpect(jsonPath("$.products.1.amount").value(3))
                .andExpect(jsonPath("$.conditions").isEmpty());
    }

    @Test
    void givenCartExists_whenAddNewProducts_thenReturnCart() throws Exception {

        Product product = new Product(3, "Banana", 7);
        Set<Product> products = new HashSet<>();
        products.add(product);
        addProductRequest.setProducts(products);

        mockMvc.perform(post(CONTROLLER_BASE_URL + "/" + cartId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(addProductRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(cartId))
                .andExpect(jsonPath("$.datetime").hasJsonPath())
                .andExpect(jsonPath("$.products.3.id").value(3))
                .andExpect(jsonPath("$.products.3.description").value("Banana"))
                .andExpect(jsonPath("$.products.3.amount").value(7))
                .andExpect(jsonPath("$.conditions").isEmpty());
    }

    @Test
    void givenCartExists_whenRemoveProducts_thenReturnCart() throws Exception {

        RemoveProductRequest removeProductRequest = new RemoveProductRequest();
        Set<Integer> productSet = new HashSet<>();
        productSet.add(1);
        productSet.add(2);
        removeProductRequest.setIds(productSet);

        mockMvc.perform(put(CONTROLLER_BASE_URL + "/" + cartId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(removeProductRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(cartId))
                .andExpect(jsonPath("$.datetime").hasJsonPath())
                .andExpect(jsonPath("$.products").isEmpty())
                .andExpect(jsonPath("$.conditions").isEmpty());
    }

    @Test
    void whenDeleteCart_thenVerifyDelete() throws Exception {

        mockMvc.perform(delete(CONTROLLER_BASE_URL + "/" + cartId))
                .andExpect(status().isNoContent())
                .andExpect(jsonPath("$").doesNotExist());
    }

    @Test
    void givenCartExists_whenAddCartCondition_thenReturnCart() throws Exception {

        CartCondition cartCondition = new CartCondition();
        cartCondition.setId(2);
        cartCondition.setDescription("Description");
        cartCondition.setTarget("1");
        cartCondition.setValue("-20%");

        Set<CartCondition> conditionSet = new HashSet<>();
        conditionSet.add(cartCondition);

        AddConditionRequest addConditionRequest = new AddConditionRequest();
        addConditionRequest.setConditions(conditionSet);

        mockMvc.perform(post(CONTROLLER_BASE_URL + "/" + cartId + "/conditions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(addConditionRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(cartId))
                .andExpect(jsonPath("$.datetime").hasJsonPath())
                .andExpect(jsonPath("$.conditions.2.id").value(2))
                .andExpect(jsonPath("$.conditions.2.description").value("Description"))
                .andExpect(jsonPath("$.conditions.2.target").value("1"))
                .andExpect(jsonPath("$.conditions.2.value").value("-20%"));
    }
}
