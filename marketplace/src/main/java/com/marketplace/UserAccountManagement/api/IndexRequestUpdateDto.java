package com.marketplace.UserAccountManagement.api;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import javax.annotation.Nullable;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record IndexRequestUpdateDto(
        Integer previousIndex,
        Integer currentIndex
) {
}
