package com.marketplace.Account;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface SellerRepository extends JpaRepository<Seller, String> {
    @Query("SELECT s.email AS accountEmail, s.name AS accountName FROM Seller s WHERE s.id = :id")
    AccountProjection findAccountEmailAndName(@Param("id") String id);
}
