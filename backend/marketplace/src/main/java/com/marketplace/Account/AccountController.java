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

@RestController
@RequestMapping("/api/seller")
public class AccountController {

    private SellerService sellerService;
    private RoleService roleService;
    private Mapper mapper;

    @Autowired
    public AccountController(SellerService sellerService, RoleService roleService, Mapper mapper) {
        this.sellerService = sellerService;
        this.roleService = roleService;
        this.mapper = mapper;
    }

    @GetMapping
    public ResponseEntity<Object> getAllSellerAccount() {
        List<AccountDTO> sellers = sellerService.getAllSeller()
            .stream()
            .map(mapper::toAccountDto)
            .collect(Collectors.toList());
        return ResponseHandler.generateResponse("get Successfully", HttpStatus.OK, sellers, 0);
        
    }

    @PostMapping
    public ResponseEntity<Object> createAccount(@RequestBody AccountCreationDTO accountDTO) {
        Boolean isSellerCreated = sellerService.checkSellerByEmail(accountDTO.getSellerEmail());
        if (isSellerCreated) {
            return ResponseHandler.generateResponse("Seller has already been created", HttpStatus.FOUND, "", 0);
        }
        Role addOrGetRole = roleService.getOrCreateRoleAccount(RoleEnum.SELLER);
        Seller createdAccount = mapper.toSellerAccount(accountDTO);
        createdAccount.addRole(addOrGetRole);
        sellerService.saveSeller(createdAccount);
        AccountDTO data = mapper.toAccountDto(createdAccount);
        return ResponseHandler.generateResponse("Success to create", HttpStatus.CREATED, data, 0);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getSellerById(@PathVariable String id) {
        Boolean isThisTypeOfAccountThere = sellerService.isTheSellerThere(id);
        if (!isThisTypeOfAccountThere) {
            return ResponseHandler.generateResponse("this type of account is not here", HttpStatus.NOT_FOUND, "", 0);
        }
        Seller seller = sellerService.getSellerById(id.toString());
        AccountDTO data = mapper.toAccountDto(seller);
        return new ResponseEntity<Object>(data, HttpStatus.OK);
    }

    @GetMapping("/email")
    public ResponseEntity<Object> getSellerByEmail(@RequestParam String email) {
        Boolean isSellerCreated = sellerService.checkSellerByEmail(email);
        if (!isSellerCreated) {
            return ResponseHandler.generateResponse("This type of seller not found", HttpStatus.NOT_FOUND, "", 0);
        }
        Seller getByEmail = sellerService.getSellerByEmail(email);
        AccountDTO data = mapper.toAccountDto(getByEmail);
        return ResponseHandler.generateResponse("Successfully retrieve the account", HttpStatus.OK, data, 0);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateAccount(@PathVariable String id, @RequestBody AccountUpdateDTO accountdDto) {
        Boolean isThisTypeOfAccountThere = sellerService.isTheSellerThere(id);
        if (!isThisTypeOfAccountThere) {
            return ResponseHandler.generateResponse("this type of account is not here", HttpStatus.NOT_FOUND, "", 0);
        }
        Seller newSellerData = sellerService.updateSeller(id, accountdDto);
        AccountDTO data = mapper.toAccountDto(newSellerData);
        return ResponseHandler.generateResponse("the result of modified account ", HttpStatus.OK, data, 0);
    }

    @PutMapping("/change-password/{id}")
    public ResponseEntity<Object> updatePassword(@PathVariable String id, @RequestBody AccountUpdatePasswordDTO accountDto) {
        Boolean isThisTypeOfAccountThere = sellerService.isTheSellerThere(id);
        if (!isThisTypeOfAccountThere) {
            return ResponseHandler.generateResponse("this type of account is not here", HttpStatus.NOT_FOUND, "", 0);
        }
        if (!accountDto.getPassword().equals(accountDto.getRepeatPassword())) {
            return ResponseHandler.generateResponse("Please check the password again", HttpStatus.BAD_REQUEST, "", 0);
        } else if(accountDto.getPassword().length() < 8) {
            return ResponseHandler.generateResponse("Password still few character, add more character", HttpStatus.NOT_ACCEPTABLE, isThisTypeOfAccountThere, 0);
        }
        sellerService.updateSellerPassword(id, accountDto);
        return ResponseHandler.generateResponse("Password successfully changed", HttpStatus.ACCEPTED, "", 0);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteAccount(@PathVariable String id) {
        Boolean isThisTypeOfAccountThere = sellerService.isTheSellerThere(id);
        if (!isThisTypeOfAccountThere) {
            return ResponseHandler.generateResponse("this type of account is not here", HttpStatus.NOT_FOUND, "", 0);
        }
        sellerService.deleteSellerById(id);
        return ResponseHandler.generateResponse("delete successfully", HttpStatus.OK, "", 0);

    }

}
