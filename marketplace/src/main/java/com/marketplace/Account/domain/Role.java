package com.marketplace.Account.domain;

import java.util.Set;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import com.marketplace.Account.api.RoleEnum;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "`Roles`")
@Builder
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Role {
    
    @SuppressWarnings("deprecation")
    @Id
    @GeneratedValue(generator = "custom-generator")
    @GenericGenerator(name = "custom-generator",
        strategy = "com.marketplace.Util.CustomGenerator"
    )
    private String id;

    @Column(name = "role_name", length = 50, nullable = false)
    @Enumerated(EnumType.STRING)
    private RoleEnum roleName;

    @ManyToMany(mappedBy = "accountRoles")
    private Set<Seller> sellers;

    public Role(RoleEnum roleName) {
        this.setRoleName(roleName);
    }

    public void addSellerToRole(Seller seller) {
        sellers.add(seller);
    }

}
