package com.marketplace.DiscProductManagement.Domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "`Product_View`")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
public class ProductView {

    @Id
    @Column(name = "product_id")
    private String id;

    @Column(name = "number_of_view", nullable = false)
    private int numberOfView;

    @Column(name = "product_rate", nullable = false)
    private int productRate;

    @OneToOne
    @MapsId
    @JoinColumn(name = "product_id", referencedColumnName = "id")
    private Product product;
}
