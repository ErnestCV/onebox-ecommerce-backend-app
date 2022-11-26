package com.onebox.ecommercebackend.model;

import lombok.Data;

@Data
public class CartCondition {

    private int id;
    private String description;
    private String target; //Can be a product ID (gets applied per item) or '-1' to apply it to the cart total
    private String value; //Can be positive or negative, a number or a percentage
}
