package com.marketplace.Auth.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import java.util.HashSet;
import java.util.Set;

@Entity(name = "Role_Auth")
@Table(name = "`Roles`")
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Cacheable
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

    @ManyToMany(mappedBy = "accountRoles", fetch = FetchType.LAZY)
    private Set<User> users = new HashSet<>();

    public Role(RoleEnum roleName) {
        this.setRoleName(roleName);
    }

    public void addSellerToRole(User user) {
        users.add(user);
    }

}
