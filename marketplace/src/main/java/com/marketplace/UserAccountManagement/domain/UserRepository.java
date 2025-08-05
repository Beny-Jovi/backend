package com.marketplace.UserAccountManagement.domain;

import com.marketplace.UserAccountManagement.api.UserAccountDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository("UserRepository")
public interface UserRepository extends JpaRepository<User, String> {
    @Query("SELECT new com.marketplace.UserAccountManagement.api.UserAccountDTO(u.id, u.email, u.name) FROM User u WHERE u.id = :id")
    UserAccountDTO findAccountEmailAndName(@Param("id") String id);

}
