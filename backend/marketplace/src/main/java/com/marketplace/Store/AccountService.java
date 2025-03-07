package com.marketplace.Store;

import java.util.Objects;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccountService {
    
    @Autowired
    private AccountRepostiory accountRepostiory;

    public Boolean isAccountThere(String id) {
        return accountRepostiory.findAll().stream()
            .anyMatch(account -> account.getId().contains(id));
    }

    public Optional<Account> getAccountById(String id) {
        return accountRepostiory.findById(id);
    }

    public void addNumberOfStores(Account foundAccount) {
        int currentNumberOfStores = foundAccount.getNumberOfStores() + 1;
        foundAccount.setNumberOfStores(currentNumberOfStores);
        accountRepostiory.save(foundAccount);
    }

    public void saveAccount(Account account) {
        Objects.requireNonNull(account);
        accountRepostiory.save(account);
    } 

}
