package com.marketplace.UserAccountManagement.domain;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.*;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.security.crypto.bcrypt.BCrypt;

import com.marketplace.Util.Auditable;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "`Users`")
@Getter @Setter @NoArgsConstructor
public class User extends Auditable {
    
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

    @Column(name = "is_deleted")
    private Boolean isDeleted;

//    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
//    @JoinTable(name = "account_Stores",
//            joinColumns = {
//                    @JoinColumn(name = "account_id", referencedColumnName = "id")
//            }, inverseJoinColumns = {
//            @JoinColumn(name = "store_id", referencedColumnName = "id")
//    })
//    private Store store;
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    Set<Address> addresses = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinTable(
        name = "`Account_Roles`",
        joinColumns = @JoinColumn(name = "Account_id"),
        inverseJoinColumns = @JoinColumn(name="role_id")
    )
    private Set<Role> accountRoles;


    public User(String email, String name, String password, Set<Role> sellerRoles) {
        this.setEmail(email);
        this.setName(name);
        this.password = this.doEncryptPassword(password);
        this.setAccountRoles(sellerRoles);
    }

    public void addAddress(Address address) {
        addresses.add(address);
    }

    public void addRole(Role role) {
        accountRoles.add(role);
    }


    public String doEncryptPassword(String sellerPassword) {
        return BCrypt.hashpw(sellerPassword, BCrypt.gensalt(10));
    }

}
