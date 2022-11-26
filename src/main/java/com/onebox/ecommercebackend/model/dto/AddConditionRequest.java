package com.onebox.ecommercebackend.model.dto;

import com.onebox.ecommercebackend.model.CartCondition;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Set;

@Data
public class AddConditionRequest {

    @NotNull
    private Set<CartCondition> conditions;
}
