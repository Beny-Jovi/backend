package com.marketplace.Store;

import com.marketplace.Exception.ResourceNotFoundException;
import com.marketplace.Store.domain.Account;
import com.marketplace.Store.domain.AccountRepository;
import com.marketplace.Store.domain.AccountService;
import de.huxhorn.sulky.ulid.ULID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class AccountServiceUnitTest {

    @InjectMocks
    private AccountService accountService;

    @Mock
    private AccountRepository accountRepository;

    private Account account;

    @BeforeEach
    public void setup() {
        String id = "01JRF117ETQT89GPE3PRZCXDBT";
        String name = "test";
        String email = "test@test";
        String password = "testPassword";
        account = Account.builder()
                .id(id)
                .name(name)
                .email(email)
                .password(password)
                .build();
    }

    @Test
    public void createStoreTest() {
        String id = "01JRF117ETQT89GPE3PRZCXDBT";

        when(accountRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> accountService.getAccountById(id)
                .orElseThrow(() -> new ResourceNotFoundException("account with this id is not found" + id)))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("account with this id is not found" + id);


    }

    @Test
    public void getAccountByIdTest() {
        String id = "01JRF117ETQT89GPE3PRZCXDBT";

        given(accountRepository.findById(id)).willReturn(Optional.ofNullable(account));

        Account existingAccount = accountRepository.findById(account.getId()).get();

        assertThat(existingAccount).isNotNull();
    }
}
