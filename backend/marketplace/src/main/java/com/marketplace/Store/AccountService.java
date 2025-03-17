package com.marketplace.Store;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccountService {
    
    @Autowired
    private AccountRepository accountRepository;

    public Boolean isAccountThere(String id) {
        return accountRepository.findAll().stream()
            .anyMatch(account -> account.getId().contains(id));
    }

    public Optional<Account> getAccountById(String id) {
        return accountRepository.findById(id);
    }

    public void addNumberOfStores(Account foundAccount) {
        int currentNumberOfStores = foundAccount.getNumberOfStores() + 1;
        foundAccount.setNumberOfStores(currentNumberOfStores);
        accountRepository.save(foundAccount);
    }

    public void saveAccount(Account account) {
        Objects.requireNonNull(account);
        accountRepository.save(account);
    }

    // public Accounts getAccountByName(String accountName) {
    //     return accountRepository.findAccountByName(accountName);
    // }
    
    // public List<StoreProjection> getStoresByAccountId(String id) {
    //     return accountRepository.findStoresByAccountId(id);
    // }

    public List<StoreAccountProjection> getAllAccount() {
        return accountRepository.findAllProjected();
    }

}
