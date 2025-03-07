package com.marketplace.Account;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record SellerAccountUpdatePasswordDTO(
    @NotBlank(message = "The Password have to contain")
    @Size(min = 8, max = 15, message = "The password should be 8 to 15 characters")
    String password, 
    @NotBlank(message = "The repeat password not the same as password")
    @Size(min = 8, max = 15, message = "The password should be 8 to 15 characters")
    String repeatPassword) {
        
        public SellerAccountUpdatePasswordDTO {
            if (!password.equals(repeatPassword)) {
                throw new IllegalArgumentException("the password should be the same");
            }
        }
}