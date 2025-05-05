package com.marketplace.StoreManagement.domain;

import com.marketplace.Util.Auditable;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "`Profiles`")
@Getter @Setter @NoArgsConstructor
public class Profile extends Auditable {

    @SuppressWarnings("deprecation")
    @Id
    @GeneratedValue(generator = "custom-generator")
    @GenericGenerator(name = "custom-generator",
            strategy = "com.marketplace.Util.CustomGenerator"
    )
    private String id;

    @Column(name = "logo_path", length = 300)
    private String logoPath;

    @OneToOne(mappedBy = "storeProfile")
    private Store store;

    public Profile(Store store) {
        this.setStore(store);
    }

    public Profile(String logoPath, Store store) {
        this.setLogoPath(logoPath);
        this.setStore(store);
    }

}
