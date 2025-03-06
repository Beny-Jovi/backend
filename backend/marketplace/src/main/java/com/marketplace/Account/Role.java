package com.marketplace.Account;

import java.util.Set;

import org.hibernate.annotations.GenericGenerator;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// CREATE TABLE "Role" (
//   "role_id" VARCHAR(30) NOT NULL,
//   "role_name" VARCHAR(50),
//   PRIMARY KEY ("role_id")
// );

@Entity
@Table(name = "`Role`")
@Getter @Setter @NoArgsConstructor

public class Role {
    
    @SuppressWarnings("deprecation")
    @Id
    @GeneratedValue(generator = "custom-generator")
    @GenericGenerator(name = "custom-generator",
        strategy = "com.marketplace.Util.CustomGenerator"
    )
    private String id;

    @Column(name = "role_name", length = 50, nullable = false)
    private String roleName;

    @ManyToMany(mappedBy = "accountRoles")
    private Set<Seller> sellers;

    public Role(String roleName) {
        this.setRoleName(roleName);
    }

    public void addSellerToRole(Seller seller) {
        sellers.add(seller);
    }

}
