package com.marketplace.StoreOperatingHours.domain;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import java.time.DayOfWeek;
import java.util.Set;

@Entity
@Table(name = "`Days`")
@Data
public class Day {

    @SuppressWarnings("deprecation")
    @Id
    @GeneratedValue(generator = "custom-generator")
    @GenericGenerator(name = "custom-generator",
            strategy = "com.marketplace.Util.CustomGenerator"
    )
    private String id;

    @Enumerated(EnumType.STRING)
    @Column(name = "day_name", length = 15, nullable = false, updatable = false)
    private DayOfWeek dayName;

    @OneToMany(mappedBy = "day", fetch = FetchType.LAZY)
    Set<StoreDayOperatingHours> storeDayOperatingHours;

}
