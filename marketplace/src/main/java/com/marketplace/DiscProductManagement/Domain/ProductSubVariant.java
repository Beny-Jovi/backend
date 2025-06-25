package com.marketplace.DiscProductManagement.Domain;

import jakarta.persistence.*;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "`Product_Sub_Variants`")
public class ProductSubVariant {

    @Id
    @GeneratedValue(generator = "custom-generator")
    @GenericGenerator(name = "custom-generator",
            strategy = "com.marketplace.Util.CustomGenerator"
    )
    private String id;

    @Column(name = "variant_specific_name_type", nullable = false, length = 25)
    private String variantSpecificNameType;

    @Column(name = "price", nullable = false)
    private int price;


}
