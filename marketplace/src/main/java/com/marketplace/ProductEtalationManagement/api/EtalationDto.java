package com.marketplace.ProductEtalationManagement.api;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record EtalationDto(String etalationId, String etalationName) {
    
}
