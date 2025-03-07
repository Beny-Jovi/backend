package com.marketplace.Account;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.marketplace.Exception.ResourceFoundException;
import com.marketplace.Exception.ResourceNotFoundException;
import com.marketplace.Util.ResponseHandler;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

// add idempotent operation
// remove the response handler custom message "count" part

@Slf4j
@RestController
@RequestMapping("/api/seller")
public class SellerAccountController {

    private SellerService sellerService;
    private RoleService roleService;
    private SellerMapper mapper;

    @Autowired
    public SellerAccountController(SellerService sellerService, RoleService roleService, SellerMapper mapper) {
        this.sellerService = sellerService;
        this.roleService = roleService;
        this.mapper = mapper;
    }

    @GetMapping
    public ResponseEntity<Object> getAllSellerAccount() {
        log.info("Get /seller - Fetching all sellers ");
        List<SellerAccountDTO> sellers = sellerService.getAllSeller()
            .stream()
            .map(mapper::toAccountDto)
            .collect(Collectors.toList());
        return ResponseHandler.generateResponse("get Successfully", HttpStatus.OK, sellers);
        
    }

    @PostMapping // the plan this url redirect into /{id}
    public ResponseEntity<Object> createAccount(@RequestBody @Valid SellerAccountCreationDTO accountDTO) {
        log.info("{Post /seller - Creating User: {} - User name is : {} }", accountDTO.email(), accountDTO.name());
        Boolean isSellerCreated = sellerService.checkSellerByEmail(accountDTO.email());
        if (isSellerCreated) {
            throw new ResourceFoundException("the name of the account has already been taken");
        }
        Role addOrGetRole = roleService.getOrCreateRoleAccount(RoleEnum.SELLER);
        Seller createdAccount = mapper.toSellerAccount(accountDTO);
        createdAccount.addRole(addOrGetRole);
        sellerService.saveSeller(createdAccount);
        SellerAccountDTO data = mapper.toAccountDto(createdAccount);
        return ResponseHandler.generateResponse("Success to create", HttpStatus.CREATED, data);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getSellerById(@PathVariable String id) {
        // logger.info("Get /seller/{} - Fetching seller by id ", id);
        log.info("{Get /seller/{} - Fetching seller by id }", id);
        Seller foundSeller = sellerService.getSellerById(id)
            .orElseThrow(() -> new ResourceNotFoundException("User not found with id " + id ));
        SellerAccountDTO data = mapper.toAccountDto(foundSeller);
        return new ResponseEntity<Object>(data, HttpStatus.OK);
    }

    @GetMapping("/email")
    public ResponseEntity<Object> getSellerByEmail(@RequestParam String email) {
        // logger.info("Get /seller/email - Fetching seller by email ", email);
        log.info("{Get /seller/email - Fetching seller by email }", email);
        Seller foundSeller = sellerService.getSellerByEmail(email)
            .orElseThrow(() -> new ResourceNotFoundException("User not found with email " + email));
        SellerAccountDTO data = mapper.toAccountDto(foundSeller);
        return ResponseHandler.generateResponse("Successfully retrieve the account", HttpStatus.OK, data);
    }

    @PutMapping("/{id}") // the plan this url redirect into /{id}
    public ResponseEntity<Object> updateAccount(@PathVariable String id, @RequestBody @Valid SellerAccountUpdateDTO accountdDto) {
        log.info("{Put /seller/{} - updating seller: {} }", id, accountdDto);
        log.info("{Put /seller/{} - updating seller name: {} - updating seller email }", accountdDto.name(), accountdDto.email());
        Seller foundSeller = sellerService.getSellerById(id)
            .orElseThrow(() -> new ResourceNotFoundException("User not found with id " + id));
        Seller newSellerData = sellerService.updateSeller(foundSeller, accountdDto);
        SellerAccountDTO data = mapper.toAccountDto(newSellerData);
        return ResponseHandler.generateResponse("the result of modified account ", HttpStatus.OK, data);
    }

    @PutMapping("/change-password/{id}") // the plan this url redirect into /{id}
    public ResponseEntity<Object> updatePassword(@PathVariable String id, @RequestBody @Valid SellerAccountUpdatePasswordDTO accountDto) {
        Seller foundSeller = sellerService.getSellerById(id)
            .orElseThrow(() -> new ResourceNotFoundException("User not found with id "+id));
        sellerService.updateSellerPassword(foundSeller, accountDto);
        return ResponseHandler.generateResponse("Password successfully changed", HttpStatus.ACCEPTED, "");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteAccount(@PathVariable String id) {
        // logger.info("Delete /seller/{} - Deleting seller ", id);
        log.info("{Delete /seller/{} - Deleting seller }", id);
        Seller founSeller = sellerService.getSellerById(id)
            .orElseThrow(() -> new ResourceNotFoundException("User not found with id "+ id));
        sellerService.deleteSellerById(founSeller);
        return ResponseHandler.generateResponse("delete successfully", HttpStatus.OK, "");

    }

}
