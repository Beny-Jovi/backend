package com.marketplace.UserAccountManagement.domain;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import com.marketplace.UserAccountManagement.api.*;
import com.marketplace.Exception.ResourceDuplicationException;
import com.marketplace.Exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class UserService {

//  check is user have the default address

    @Autowired
    private UserRepository sellerRepository;

    public List<User> getAllUser() {
        return sellerRepository.findAll();
    }

    public Optional<User> getUserById(String sellerId) {
        return sellerRepository.findById(sellerId);
    }

    // public Boolean isTheUserThere(String id) {
    //     return getAllUser().stream()
    //         .anyMatch(seller -> seller.getId().contains(id));
    // }

    public List<UserAccountDTO> getAllUserIsDeletedFalse() {
        return getAllUser().stream()
                .filter(user -> user.getIsDeleted() == null || !user.getIsDeleted())
                .map(user -> new UserMapper().toAccountDto(user))
                .toList();
    }

//    public List<AccountProjection> getAllUserIsDeletedFalse() {
//        return sellerRepository.getUsers();
//    }

    public User saveUser(User seller) {
        Objects.requireNonNull(seller);
        return sellerRepository.save(seller);
    }

    public User updateUser(User foundUser, UserAccountUpdateDTO accountDto) {
        log.info("The email is: {} - the name is: {}", accountDto.email(), accountDto.name());
        foundUser.setEmail(accountDto.email());
        foundUser.setName(accountDto.name());
        sellerRepository.save(foundUser);
        return foundUser;
    }

    public void updateUserPassword(User foundUser, UserAccountUpdatePasswordDTO accountDto) {
        foundUser.setPassword(foundUser.doEncryptPassword(accountDto.password()));
        sellerRepository.save(foundUser);
    }

    public void deleteUserById(User foundUser) {
//        sellerRepository.delete(foundUser);
        foundUser.setIsDeleted(true);
        sellerRepository.save(foundUser);
    }

    public Boolean checkUserByEmail(String email) {
        return getAllUser().stream()
            .anyMatch(seller -> seller.getEmail().contains(email));
    }

    public Optional<User> getUserByEmail(String email) {
        return getAllUser().stream()
            .filter(seller -> seller.getEmail().equals(email))
            .findAny();
    }

    public UserAccountDTO getEmailAndName(String id) {
        return sellerRepository.findAccountEmailAndName(id);
    }

    public UserAccountDTO getUserDataByEmail(String email, UserMapper mapper) {
        User foundUser = getUserByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email " + email));
        if (foundUser.getIsDeleted()!=null) {
            throw new IllegalArgumentException("User has already been deleted");
        }
        return mapper.toAccountDto(foundUser);
    }


    @Transactional
    public void addUserAddress(String id, UserAddressCreationDto addressDto) {
        User foundUser = getUserById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id "+id));
        if (foundUser.getIsDeleted()!=null) {
            throw new IllegalArgumentException("User has already been deleted");
        }

        Address address = Address.builder()
                .recipientName(addressDto.recipientName())
                .recipientNumber(addressDto.recipientNumber())
                .addressLabel(addressDto.addressLabel())
                .isDeleted(false)
                .cityAndSubsidiary(addressDto.cityAndSubsidiary())
                .completeAddress(addressDto.completeAddress())
                .isPicked(false)
                .user(foundUser)
                .build();

        foundUser.addAddress(address);
        sellerRepository.save(foundUser);

    }

    @Transactional(readOnly = true)
    public List<UserAddressDto> getAllUserAddresses(String id) {
        User foundUser = getUserById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id "+ id));
        return foundUser.getAddresses().stream()
                .filter(address -> address.getIsDeleted() ==null || address.getIsDeleted().equals(false))
                .map(address -> new UserAddressDto(address.getRecipientName(), address.getRecipientNumber(), address.getAddressLabel(), address.getCityAndSubsidiary(), address.getCompleteAddress(), address.getIsPicked()))
                .toList();
    }

    @Transactional
    public void updateUserAddress(String id, String addressId, UserAddressCreationDto addressDto) {
        User foundUser = getUserById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id "+ id));
        if (foundUser.getIsDeleted() != null) {
            throw new IllegalArgumentException("User has already been deleted");
        }
        Address address = foundUser.getAddresses().stream()
                .filter(existingAddress -> existingAddress.getId().equals(addressId))
                .findAny()
                .orElseThrow(() -> new ResourceNotFoundException("Address not found with id " + id));
        address.setCompleteAddress(addressDto.completeAddress());
        address.setAddressLabel(addressDto.addressLabel());
        address.setRecipientName(addressDto.recipientName());
        address.setRecipientNumber(addressDto.recipientNumber());
        address.setCityAndSubsidiary(addressDto.cityAndSubsidiary());
        saveUser(foundUser);
    }

    public UserAccountDTO getUserDataById(String id, UserMapper mapper) {
        User foundSeller = getUserById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id " + id ));
        if (foundSeller.getIsDeleted()!=null) {
            throw new IllegalArgumentException("Seller has already been deleted");
        }
        return mapper.toAccountDto(foundSeller);
    }

    public void deleteAccountById(String id) {
        User foundUser = getUserById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id "+ id));
        if (foundUser.getIsDeleted()!=null) {
            throw new IllegalArgumentException("User has already been deleted");
        }
        deleteUserById(foundUser);
    }
    
}
