package com.marketplace.Account;

import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;

public class SellerService {
    // Save seller
    // get all seller
    // update seller
    // delete seller

    @Autowired
    private SellerRepository sellerRepository;

    public List<Seller> getAllSeller() {
        return sellerRepository.findAll();
    }

    public Seller getSellerById(String sellereId) {
        return getAllSeller()
            .stream()
            .filter(seller -> seller.getId().equals(sellereId))
            .findAny()
            .orElse(null);
    }

    public Boolean isTheSellerThere(String id) {
        return getAllSeller().stream()
            .anyMatch(seller -> seller.getId().contains(id));
    }

    public void saveSeller(Seller seller) {
        Objects.requireNonNull(seller);
        sellerRepository.save(seller);
    }

    public Seller updateSeller(String id, AccountUpdateDTO accountDto) {
        Seller foundSellerById = getSellerById(id);
        foundSellerById.setEmail(accountDto.getEmail());
        foundSellerById.setName(accountDto.getName());
        sellerRepository.save(foundSellerById);
        return foundSellerById;
    }

    public void updateSellerPassword(String id, AccountUpdatePasswordDTO accountDto) {
        Seller foundSellerById = getSellerById(id);
        foundSellerById.setPassword(foundSellerById.doEncryptPassword(accountDto.getPassword()));
        sellerRepository.save(foundSellerById);
    }

    public void deleteSellerById(String id) {
        Seller founSellerById = getSellerById(id);
        sellerRepository.delete(founSellerById);
    }

    public Boolean checkSellerByEmail(String email) {
        return getAllSeller().stream()
            .anyMatch(seller -> seller.getEmail().contains(email));
    }

    public Seller getSellerByEmail(String email) {
        return getAllSeller().stream()
            .filter(seller -> seller.getEmail().equals(email))
            .findAny()
            .orElse(null);
    }

    
}
