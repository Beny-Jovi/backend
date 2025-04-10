package com.marketplace.StoreOperatingHours.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("Store_Operating_Hours_Repository")
public interface StoreRepository extends JpaRepository<Store, String> {
}
