package com.marketplace.Auth.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("UserRepositoryInAuth")
public interface UserRepository extends JpaRepository<User, String> {
}
