package com.marketplace.Store.domain;

import jakarta.persistence.*;
import org.hibernate.annotations.GenericGenerator;

import com.marketplace.Util.Auditable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Table(name = "`Stores`")
@Builder
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Store extends Auditable {

    @SuppressWarnings("deprecation")
    @Id
    @GeneratedValue(generator = "custom-generator")
    @GenericGenerator(name = "custom-generator", 
        strategy = "com.marketplace.Util.CustomGenerator"
    )
    private String id;

    @Column(name = "name", length = 80, nullable = false)
    private String name;

    @OneToOne(mappedBy = "store")
    private Account account;

    @OneToOne(mappedBy = "store", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @PrimaryKeyJoinColumn
    private StoreDetail storeDetail;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinTable(name = "Store_Profiles",
        joinColumns = {
            @JoinColumn(name = "store_id", referencedColumnName = "id")
        }, inverseJoinColumns = {
            @JoinColumn(name = "profile_id", referencedColumnName = "id")
        }
    )
    private Profile storeProfile;

    public Store(String name) {
        this.setName(name);
    }

    public Store(String name, Account account) {
        this.setName(name);
        this.setAccount(account);
    }

}
