package com.marketplace.DiscProductManagement.Domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import java.util.HashSet;
import java.util.Set;

@Entity
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

    public enum CategoryEnum {
        DIGITAL_WORK
    }

    @Column(name = "name", length = 80, nullable = false)
    @Enumerated(EnumType.STRING)
    private CategoryEnum name;

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<SubCategory> subCategories = new HashSet<SubCategory>();

    public void addSubCategory(SubCategory subCategory) {
        System.out.println("this.getSubCategories() = " + this.getSubCategories());
        subCategories.add(subCategory);
    }

    public Category(CategoryEnum name) {
        this.setName(name);
    }
}
