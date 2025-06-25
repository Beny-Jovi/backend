package com.marketplace.DiscProductManagement.Domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import java.util.HashSet;
import java.util.Set;

@Entity
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

    public enum SubCategoryEnum {
        DISC
    }

    @Column(name = "name", length = 80, nullable = false)
    @Enumerated(EnumType.STRING)
    private SubCategoryEnum name;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @OneToMany(mappedBy = "subCategory", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Product> products = new HashSet<>();

    public void addProduct(Product product) {
        System.out.println("this.getProducts() = " + this.getProducts());
        if (products == null) {
            System.out.println("products is null! Initializing...");
            products = new HashSet<>();
        }
        products.add(product);
    }
}
