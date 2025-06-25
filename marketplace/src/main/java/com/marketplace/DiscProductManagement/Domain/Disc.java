package com.marketplace.DiscProductManagement.Domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "`Disc`")
@Setter @Getter @NoArgsConstructor @AllArgsConstructor
@Builder
public class Disc {
    @Id
    @Column(name = "product_id")
    private String id;

    @Column(name = "title", length = 200, nullable = false)
    private String title;

    public enum ProductConditionEnum {
        NEW,
        USED
    }

    @Column(name = "product_condition", nullable = false)
    @Enumerated(EnumType.STRING)
    private ProductConditionEnum productCondition;

    @Column(name = "description", nullable = false)
    private String description;

    public enum UnitOfProductEnum {
        KG,
        GR
    }

    @Column(name = "unit_of_product", nullable = false)
    @Enumerated(EnumType.STRING)
    private UnitOfProductEnum unitOfProduct;

    @Column(name = "publisher", nullable = false)
    private String publisher;

    @OneToOne
    @MapsId
    @JoinColumn(name = "product_id", referencedColumnName = "id")
    private Product product;
}
