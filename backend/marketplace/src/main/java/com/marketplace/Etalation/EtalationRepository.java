package com.marketplace.Etalation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EtalationRepository extends JpaRepository<Etalation, String> {
    
}
