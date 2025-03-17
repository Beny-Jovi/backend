package com.marketplace.Store;

import java.time.LocalTime;

import org.hibernate.annotations.GenericGenerator;

import com.marketplace.Util.Auditable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "Store")
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

    @Column(name = "name", length = 80, nullable = false)
    private String name;

    @ManyToOne
    @JoinColumn(name = "seller_id", nullable = false, referencedColumnName = "id")
    private Account account;

    @Column(name = "rate", nullable = false)
    private double rate = 0;

    @Column(name = "operating_hours_start", nullable = false)
    private LocalTime operatingHoursStart;

    @Column(name = "operating_hours_end", nullable = false)
    private LocalTime operatingHoursEnd;

    @Column(name = "status", length = 20, nullable = false)
    @Enumerated(EnumType.STRING)
    private StoreStatusEnum storeStatus = StoreStatusEnum.BRONZE;

    @Column(name = "number_of_sales", nullable = false)
    private int numberOfSales = 0;

    @Column(name = "logo_path", length = 300)
    private String logoPath = "";

    public Store(String name, LocalTime operatingHoursStart, LocalTime operatingHoursEnd, String logoPath) {
        this.setName(name);
        this.setOperatingHoursStart(operatingHoursStart);
        this.setOperatingHoursEnd(operatingHoursEnd);
        this.setLogoPath(logoPath);
    }

}
