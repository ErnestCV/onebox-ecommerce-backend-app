package com.onebox.ecommercebackend.model.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Set;

@Data
public class RemoveProductRequest {

    @NotNull
    Set<Integer> ids;
}
