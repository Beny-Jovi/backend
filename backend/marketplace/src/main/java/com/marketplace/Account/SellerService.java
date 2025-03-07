package com.marketplace.Account;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
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

    public Optional<Seller> getSellerById(String sellerId) {
        return sellerRepository.findById(sellerId);
    }

    // public Boolean isTheSellerThere(String id) {
    //     return getAllSeller().stream()
    //         .anyMatch(seller -> seller.getId().contains(id));
    // }

    public void saveSeller(Seller seller) {
        Objects.requireNonNull(seller);
        sellerRepository.save(seller);
    }

    public Seller updateSeller(Seller foundSeller, SellerAccountUpdateDTO accountDto) {
        log.info("The email is: {} - the name is: {}", accountDto.email(), accountDto.name());
        foundSeller.setEmail(accountDto.email());
        foundSeller.setName(accountDto.name());
        sellerRepository.save(foundSeller);
        return foundSeller;
    }

    public void updateSellerPassword(Seller foundSeller, SellerAccountUpdatePasswordDTO accountDto) {
        foundSeller.setPassword(foundSeller.doEncryptPassword(accountDto.password()));
        sellerRepository.save(foundSeller);
    }

    public void deleteSellerById(Seller foundSeller) {
        sellerRepository.delete(foundSeller);
    }

    public Boolean checkSellerByEmail(String email) {
        return getAllSeller().stream()
            .anyMatch(seller -> seller.getEmail().contains(email));
    }

    public Optional<Seller> getSellerByEmail(String email) {
        return getAllSeller().stream()
            .filter(seller -> seller.getEmail().equals(email))
            .findAny();
    }

    
}
