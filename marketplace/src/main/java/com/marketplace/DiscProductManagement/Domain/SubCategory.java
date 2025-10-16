package com.marketplace.DiscProductManagement.Domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import java.util.HashSet;
import java.util.Set;

@Entity(name = "SubCategory_ProductManagement")
@Table(name = "`Sub_Categories`")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
public class SubCategory {

    @Id
    @GeneratedValue(generator = "custom-generator")
    @GenericGenerator(name = "custom-generator",
            strategy = "com.marketplace.Util.CustomGenerator"
    )
    private String id;

    @Column(name = "sub_category_name", length = 80, nullable = false)
    private String subCategoryName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false, referencedColumnName = "id")
    private Category category;

    @OneToMany(mappedBy = "subCategory", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Product> products = new HashSet<>();

    public SubCategory(String subCategoryName, Category category) {
        setSubCategoryName(subCategoryName);
        setCategory(category);
    }
}
