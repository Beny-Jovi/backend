package com.marketplace.UserAccountManagement.api;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
// import com.marketplace.Exception.PasswordNotMatchException;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record UserAccountUpdatePasswordDTO(
    @NotBlank(message = "The Password have to contain")
    @Size(min = 8, max = 15, message = "The password should be 8 to 15 characters")
    String password, 
    @NotBlank(message = "The repeat password not the same as password")
    @Size(min = 8, max = 15, message = "The password should be 8 to 15 characters")
    String repeatPassword) { 
}
