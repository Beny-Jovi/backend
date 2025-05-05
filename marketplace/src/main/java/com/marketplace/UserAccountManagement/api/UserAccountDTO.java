package com.marketplace.UserAccountManagement.api;

import java.util.Set;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.marketplace.UserAccountManagement.domain.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
//public record SellerAccountDTO(String sellerEmail, String sellerName, Set<RoleEnum> roles) {
//}
@Data @AllArgsConstructor
@Builder
public class UserAccountDTO {
    private String sellerEmail;
    private String sellerName;
    private Set<Role.RoleEnum> roles;

    public UserAccountDTO(String sellerEmail, String sellerName) {
        this.setSellerEmail(sellerEmail);
        this.setSellerName(sellerName);
    }

}
