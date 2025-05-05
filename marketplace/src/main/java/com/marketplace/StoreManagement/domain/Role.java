package com.marketplace.StoreManagement.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import java.util.Set;

@Entity(name = "Role_inStore")
@Table(name = "`Roles`")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Role {

    @SuppressWarnings("deprecation")
    @Id
    @GeneratedValue(generator = "custom-generator")
    @GenericGenerator(name = "custom-generator",
            strategy = "com.marketplace.Util.CustomGenerator"
    )
    private String id;

    public enum RoleEnum {
        DEVELOPER,
        SELLER,
        ADMIN,
        BUYER
    }

    @Column(name = "role_name", length = 50, nullable = false)
    @Enumerated(EnumType.STRING)
    private RoleEnum roleName;

    @ManyToMany(mappedBy = "accountRoles")
    private Set<Account> sellers;

    public Role(RoleEnum roleName) {
        this.setRoleName(roleName);
    }

}
