package com.marketplace.UserAccountManagement.api;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.marketplace.UserAccountManagement.domain.Address;
import jakarta.validation.constraints.NotBlank;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record UserAddressCreationDto(
        @NotBlank(message = "this can't be empty")
        String recipientName,
        @NotBlank(message = "this can't be empty")
        String recipientNumber,
        Address.AddressLabelEnum addressLabel,
        @NotBlank(message = "this can't be empty")
        String cityAndSubsidiary,
        @NotBlank(message = "this can't be empty")
        String completeAddress,
        Boolean isPicked
) {
}
