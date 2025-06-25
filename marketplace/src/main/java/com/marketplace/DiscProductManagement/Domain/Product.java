package com.marketplace.DiscProductManagement.Domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDateTime;

@Entity(name = "Product_Management")
@Table(name = "`Products`")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Product {
    @Id
    @GeneratedValue(generator = "custom-generator")
    @GenericGenerator(name = "custom-generator",
            strategy = "com.marketplace.Util.CustomGenerator"
    )
    private String id;

    @Column(name = "order_minimum", nullable = false)
    private Integer orderMinimum;

    @Column(name = "created_at", nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "is_deleted")
    private Boolean isDeleted;

    @ManyToOne(optional = false)
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;

    @OneToOne(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @PrimaryKeyJoinColumn
    private ProductView productView;

    @OneToOne(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @PrimaryKeyJoinColumn
    private Disc disc;

    @ManyToOne(optional = false)
    @JoinColumn(name = "sub_category_id", nullable = false)
    private SubCategory subCategory;

    public Product(int orderMinimum, Store store, ProductView productView, Disc disc, SubCategory subCategory) {
        this.setOrderMinimum(orderMinimum);
        this.setStore(store);
        this.setProductView(productView);
        this.setDisc(disc);
        this.setSubCategory(subCategory);
    }
}

//    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//    private Set<ProductVariant> productVariants = new HashSet<>();
//
//    public void addProductVariants(ProductVariant productVariant) {
//        productVariants.add(productVariant);
//    }
