package com.marketplace.Account;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record SellerAccountUpdateDTO(
    @NotBlank(message = "Invalid email: empty null")
    @Email(message = "Invalid Email")
    String email,

    @NotBlank(message = "name can't be empty")
    String name
) {}
