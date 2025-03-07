package com.marketplace.Account;

import java.util.Set;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record SellerAccountDTO(String sellerEmail, String sellerName, Set<RoleEnum> roles) {
}
