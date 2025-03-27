package com.marketplace.Store.domain;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
// import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.marketplace.Store.api.StoreProjection;

@Repository
public interface StoreRepository extends JpaRepository<Store, String> {
    @Query("SELECT s.name AS storeName, s.rate AS storeRate FROM Store s")
    List<StoreProjection> findStoresNameAndRate();
}
