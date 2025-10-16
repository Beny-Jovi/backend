package com.marketplace.DiscProductManagement.Domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import java.util.HashSet;
import java.util.Set;

@Entity(name = "Product_Category")
@Table(name = "`Categories`")
@Getter
@Setter
@AllArgsConstructor @NoArgsConstructor
@Builder
public class Category {

    @Id
    @GeneratedValue(generator = "custom-generator")
    @GenericGenerator(name = "custom-generator",
            strategy = "com.marketplace.Util.CustomGenerator"
    )
    private String id;

    @Column(name = "name", length = 80, nullable = false)
    private String name;

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<SubCategory> subCategories = new HashSet<SubCategory>();

    public Category(String name) {
        this.setName(name);
    }
}
