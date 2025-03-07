package com.marketplace.Store;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepostiory extends JpaRepository<Account, String> {}
