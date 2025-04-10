package com.marketplace.Store.domain;

import jakarta.persistence.*;
import org.hibernate.annotations.GenericGenerator;

import com.marketplace.Util.Auditable;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "`Accounts`")
@Getter @Setter @NoArgsConstructor
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
