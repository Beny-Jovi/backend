package com.marketplace.StoreManagement.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("StoreAccountRepository")
public interface AccountRepository extends JpaRepository<Account, String> {
    
}
