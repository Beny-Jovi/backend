package com.marketplace.Banner.domain;

import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;

import org.hibernate.annotations.GenericGenerator;

import com.marketplace.Util.Auditable;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "BannerStore")
@Table(name = "`Stores`")
@Getter @Setter @NoArgsConstructor
public class Store extends Auditable {
    
    @SuppressWarnings("deprecation")
    @Id
    @GeneratedValue(generator = "custom-generator")
    @GenericGenerator(name = "custom-generator",
        strategy = "com.marketplace.Util.CustomGenerator"
    )
    private String id;

    @Column(name = "seller_id", length = 26, nullable = false)
    private String sellerId;

    @Column(name = "name", length = 30, nullable = false)
    private String name;

    @Column(name = "rate", nullable = false)
    private double rate;

    @Column(name = "operating_hours_start", nullable = false)
    private LocalTime operatingHoursStart;

    @Column(name = "operating_hours_end", nullable = false)
    private LocalTime operatingHoursEnd;

    private enum StoreStatusEnum {
        BRONZE,
        GOLD,
        PLATINUM
    }

    @Column(name = "status", length = 20, nullable = false)
    @Enumerated(EnumType.STRING)
    private StoreStatusEnum storeStatus;

    @Column(name = "number_of_sales", nullable = false)
    private int numberOfSales;

    @Column(name = "logo_path", length = 300)
    private String logoPath;

    @OneToMany(mappedBy = "store", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private Set<Etalation> storeEtalations;

    @OneToMany(mappedBy = "store", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private Set<Banner> storeBanners = new HashSet<Banner>();

    public void addStoreBanners(Banner banner) {
        storeBanners.add(banner);
    }

}
