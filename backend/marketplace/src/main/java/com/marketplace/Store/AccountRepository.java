package com.marketplace.Store;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
// import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends JpaRepository<Account, String> {
    @Query("SELECT a.email AS accountEmail, a.name AS accountName FROM Account a")
    List<StoreAccountProjection> findAllProjected();
}
    // @Query("SELECT a.email AS accountEmail, a.name AS accountName FROM Account a")
    // List<StoreAccountProjection> findStoresEmailAndName();
