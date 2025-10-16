package com.marketplace.StoreManagement.api;


import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

// all of dto need to be patternize 

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record StoreRequestDTO(
    @NotBlank(message = "Invalid store name: Empty/Null")
    @Size(min = 3, max = 30, message = "Store name must be 3-20 characters")
    String storeName

) {
}