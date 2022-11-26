package com.onebox.ecommercebackend.model.dto;

import com.onebox.ecommercebackend.model.Product;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Set;

@Data
public class AddProductRequest {

    @NotNull
    private Set<Product> products;
}
