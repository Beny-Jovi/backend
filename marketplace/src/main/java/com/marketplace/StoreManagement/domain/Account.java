package com.marketplace.StoreManagement.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import com.marketplace.Util.Auditable;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "`Users`")
@Builder
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Account extends Auditable {
    
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

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinTable(
            name = "`Account_Roles`",
            joinColumns = @JoinColumn(name = "Account_id"),
            inverseJoinColumns = @JoinColumn(name="role_id")
    )
    private Set<Role> accountRoles = new HashSet<Role>();

    //for this thing, in the future, consider to use one-to-one join table
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JoinTable(name = "Account_Stores",
        joinColumns = {
            @JoinColumn(name = "account_id", referencedColumnName = "id")
        }, inverseJoinColumns = {
            @JoinColumn(name = "store_id", referencedColumnName = "id")
        }
    )
    private Store store;

}
