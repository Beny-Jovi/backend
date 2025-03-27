package com.marketplace.Store.domain;

import java.util.HashSet;
import java.util.Set;

import org.hibernate.annotations.GenericGenerator;
// import org.springframework.security.crypto.bcrypt.BCrypt;

import com.marketplace.Util.Auditable;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
// import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
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

    @Column(name = "number_of_stores", nullable = false)
    private int numberOfStores = 0;

    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private Set<Store> stores = new HashSet<Store>();

    public void addStores(Store store) {
        stores.add(store);
    }

}
