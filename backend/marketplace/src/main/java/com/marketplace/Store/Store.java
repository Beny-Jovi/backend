package com.marketplace.Store;

import java.time.LocalDateTime;
import java.time.LocalTime;

import org.hibernate.annotations.GenericGenerator;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "`Store`")
@Getter @Setter @NoArgsConstructor
public class Store {

    @SuppressWarnings("deprecation")
    @Id
    @GeneratedValue(generator = "custom-generator")
    @GenericGenerator(name = "custom-generator", 
        strategy = "com.marketplace.Util.CustomGenerator"
    )
    private String id;

    @Column(name = "store_name", length = 80, nullable = false)
    private String storeName;

    @ManyToOne
    @JoinColumn(name = "seller_id", nullable = false, referencedColumnName = "id")
    private Account account;

    @Column(name = "store_rate", nullable = false)
    private double storeRate = 0;

    @Column(name = "store_operating_hours_start", nullable = false)
    private LocalTime storeOperatingHoursStart;

    @Column(name = "store_operating_hours_end", nullable = false)
    private LocalTime storeOperatingHoursEnd;

    @Column(name = "store_status", length = 20, nullable = false)
    @Enumerated(EnumType.STRING)
    private StoreStatusEnum storeStatus = StoreStatusEnum.BRONZE;

    @Column(name = "number_of_sales", nullable = false)
    private int numberOfSales = 0;

    @Column(name = "store_logo_path", length = 300)
    private String storeLogoPath = "";

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_times", nullable = false)
    private LocalDateTime updatedTimes;

    public Store(String storeName, LocalTime storeOperatingHoursStart, LocalTime storeOperatingHoursEnd, String storeLogoPath) {
        this.setStoreName(storeName);
        this.setStoreOperatingHoursStart(storeOperatingHoursStart);
        this.setStoreOperatingHoursEnd(storeOperatingHoursEnd);
        this.setStoreLogoPath(storeLogoPath);
        // this.setAccount(account);
    }

    @PrePersist
    public void beforePersist() {
        this.setUpdatedTimes(LocalDateTime.now());
        this.setCreatedAt(LocalDateTime.now());
    }

    @PreUpdate
    public void beforeUpdate() {
        this.setUpdatedTimes(LocalDateTime.now());
    }

}
