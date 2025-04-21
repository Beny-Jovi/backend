package com.marketplace.Account;

import com.marketplace.Account.api.AccountProjection;
import com.marketplace.Account.api.RoleEnum;
import com.marketplace.Account.domain.Role;
import com.marketplace.Account.domain.Seller;
import com.marketplace.Account.domain.SellerRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class AccountRepositoryUnitTest {
    @Autowired
    private SellerRepository sellerRepository;

    @Test
    @Rollback(value = false)
    public void testSaveAccount_ThenReturnAccountId() {
        Role role = Role.builder()
                .roleName(RoleEnum.SELLER)
                .build();
        Set<Role> sellerRoles = new HashSet<>();
        sellerRoles.add(role);
        Seller seller = new Seller("test@test", "test", "test_password", sellerRoles);
        seller.setCreatedAt(LocalDateTime.now());
        seller.setUpdatedTimes(LocalDateTime.now());
//        sellerInRoles.add(seller);
//        seller.setAccountRoles(sellerRoles);
        Seller savedSeller = sellerRepository.save(seller);

        assertThat(savedSeller.getId()).isNotNull();
        assertThat(savedSeller.getEmail()).isEqualTo("test@test");
        assertThat(savedSeller.getAccountRoles()).isNotNull();
    }

    @Test
    @Rollback(value = false)
    public void testFindAccountEmailAndName_thenReturnEmailAndName() {
        Role role = Role.builder()
                .roleName(RoleEnum.SELLER)
                .build();
        Set<Role> sellerRoles = new HashSet<>();
        sellerRoles.add(role);
        Seller seller = new Seller("test@test", "test", "test_password", sellerRoles);
        seller.setCreatedAt(LocalDateTime.now());
        seller.setUpdatedTimes(LocalDateTime.now());
        Seller savedSeller = sellerRepository.save(seller);

        AccountProjection result = sellerRepository.findAccountEmailAndName(savedSeller.getId());

        assertThat(result).isNotNull();
        assertThat(result.getAccountName()).isEqualTo("test");
        assertThat(result.getAccountEmail()).isEqualTo("test@test");
    }

}
