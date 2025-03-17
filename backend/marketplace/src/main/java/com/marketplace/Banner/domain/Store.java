package com.marketplace.Banner.domain;

import org.hibernate.annotations.GenericGenerator;

import com.marketplace.Util.Auditable;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "`Stores`")
public class Store extends Auditable {
    
    @SuppressWarnings("deprecation")
    @Id
    @GeneratedValue(generator = "custom-generator")
    @GenericGenerator(name = "custom-genertator", 
        strategy = "com.marketplace.Util.CustomGenerator"
    )
    private String id;

}
