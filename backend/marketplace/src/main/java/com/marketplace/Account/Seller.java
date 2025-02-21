package com.marketplace.Account;

import java.time.LocalDateTime;
import java.util.Set;

import org.hibernate.annotations.GenericGenerator;
import org.springframework.security.crypto.bcrypt.BCrypt;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// CREATE TABLE "Seller" (
//   "seller_id" VARCHAR(30) NOT NULL,
//   "seller_email" VARCHAR(150) NOT NULL,
//   "seller_name" VARCHAR(80) NOT NULL,
//   "seller_password" VARCHAR(20) NOT NULL,
//   "account_created_times" TIMESTAMP NOT NULL,
//   "account_updated_times" TIMESTAMP NOT NULL,
//   "number_of_stores" INTEGER NOT NULL,
//   PRIMARY KEY ("seller_id")
// );

// CREATE TABLE "Role" (
//   "role_id" VARCHAR(30) NOT NULL,
//   "role_name" VARCHAR(50),
//   PRIMARY KEY ("role_id")
// );

// CREATE TABLE "Seller_Role" (
//   "seller_id" VARCHAR(30) NOT NULL,
//   "role_id" VARCHAR(30) NOT NULL,
//   CONSTRAINT "FK_Seller_Role.role_id"
//     FOREIGN KEY ("role_id")
//       REFERENCES "Role"("role_id"),
//   CONSTRAINT "FK_Seller_Role.seller_id"
//     FOREIGN KEY ("seller_id")
//       REFERENCES "Seller"("seller_id")
// );



@Entity
@Table(name = "`Account`")
@Getter @Setter @NoArgsConstructor
public class Seller {
    
    @SuppressWarnings("deprecation")
    @Id
    @GeneratedValue(generator = "custom-generator")
    @GenericGenerator(name = "custom-generator",
        strategy = "com.marketplace.Util.CustomGenerator"
    )
    private String id;

    @Column(name = "email", length = 150, nullable = false)
    private String email;

    @Column(name = "name", length = 80, nullable = false)
    private String name;

    @Column(name = "password", length = 200, nullable = false)
    private String password;

    @Column(name = "account_created_times", nullable = false, updatable = false)
    private LocalDateTime accountCreatedTimes;

    @Column(name = "account_updated_times", nullable = false)
    private LocalDateTime accountUpdatedTimes;

    @Column(name = "number_of_stores", nullable = false)
    private int numberOfStores;

    @ManyToMany(cascade = { CascadeType.PERSIST })
    @JoinTable(
        name = "`Account_Role`",
        joinColumns = @JoinColumn(name = "Account_id"),
        inverseJoinColumns = @JoinColumn(name="role_id")
    )
    private Set<Role> accountRoles;

    public Seller(String email, String name, String password, Set<Role> sellerRoles) {
        this.setEmail(email);
        this.setName(name);
        this.password = this.doEncryptPassword(password);
        this.setNumberOfStores(0);
        this.setAccountRoles(sellerRoles);
    }

    public void addRole(Role role) {
        accountRoles.add(role);
    }

    @PrePersist
    public void beforePersist() {
        LocalDateTime currentTime = LocalDateTime.now();
        this.setAccountCreatedTimes(currentTime);
        this.setAccountUpdatedTimes(currentTime);
    }

    public String doEncryptPassword(String sellerPassword) {
        return BCrypt.hashpw(sellerPassword, BCrypt.gensalt(10));
    }

}
