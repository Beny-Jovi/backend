package com.marketplace.Etalation.api;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import jakarta.validation.constraints.NotBlank;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record EtalationRequestDto(
    @NotBlank(message = "Fill the etalation name in the field")
    String etalationName
) {
}
