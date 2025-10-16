package com.marketplace.UserAccountManagement.api;

import com.marketplace.UserAccountManagement.domain.Address;

public record UserAddressDto(
        String addressId,
        String recipientName,
        String recipientNumber,
        Address.AddressLabelEnum addressLabel,
        String cityAndSubsidiary,
        String completeAddress,
        Boolean isPicked
) {
}
