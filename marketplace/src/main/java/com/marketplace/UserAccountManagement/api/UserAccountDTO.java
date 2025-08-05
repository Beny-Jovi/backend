package com.marketplace.UserAccountManagement.api;

import java.util.Set;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Data;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record UserAccountDTO(
        String id,
        String email,
        String userName
) {
}

//@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
//@Data
//@Builder
//public class UserAccountDTO {
//    private String sellerEmail;
//    private String sellerName;
//
//    public UserAccountDTO(String sellerEmail, String sellerName) {
//        this.setSellerEmail(sellerEmail);
//        this.setSellerName(sellerName);
//    }
//
//}
