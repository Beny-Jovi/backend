package com.marketplace.Account.api;

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

import com.marketplace.Account.domain.Role;
import com.marketplace.Account.domain.RoleService;
import com.marketplace.Account.domain.Seller;
import com.marketplace.Account.domain.SellerService;
import com.marketplace.Exception.ResourceDuplicationException;
import com.marketplace.Exception.ResourceNotFoundException;
import com.marketplace.Util.ResponseHandler;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

// add idempotent post operation
// add explaination in swagger
// consider to use projection in each controller to reduce unretrive value and boost performance
// eliminate all "" or `` which is no need in database

@Slf4j
@RestController
@RequestMapping("/api")
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

    @GetMapping("/sellers")
    public ResponseEntity<Object> getAllSellerAccount() {
        log.info("Get /seller - Fetching all sellers ");
        List<SellerAccountDTO> sellers = sellerService.getAllSeller()
            .stream()
            .map(mapper::toAccountDto)
            .collect(Collectors.toList());
        return ResponseHandler.generateResponse("get Successfully", HttpStatus.OK, sellers);
        
    }

    @Operation(summary = "Seller can be register in here")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Successfully create the store",
            content = { @Content(mediaType = "application/json", schema = @Schema(implementation = SellerAccountDTO.class)) }
        ),
        @ApiResponse(responseCode = "302", description = "the email has already been taken",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(
                    example = "{\"message\": \"Email already registered\", \"cause\": \"Duplicate resource\"}"
                )
            )
        ),
        @ApiResponse(responseCode = "400", description = "Invalid inserted value",
            content = { @Content(mediaType = "application/json", schema = @Schema(
                example = "Invalid inserted value"
            )) }
        )
    })
    @PostMapping("/sellers") // the plan this url redirect into /{id}
    public ResponseEntity<Object> createAccount(@RequestBody @Valid SellerAccountCreationDTO accountDTO) {
        log.info("{Post /seller - Creating User: {} - User name is : {} }", accountDTO.email(), accountDTO.name());
        Boolean isSellerCreated = sellerService.checkSellerByEmail(accountDTO.email());
        if (isSellerCreated) {
            throw new ResourceDuplicationException("the name of the account has already been taken");
        }
        if (!accountDTO.repeatPassword().equals(accountDTO.password())) {
            throw new IllegalArgumentException("repeat password should match the password");
        }
        Role addOrGetRole = roleService.getOrCreateRoleAccount(RoleEnum.SELLER);
        Seller createdAccount = mapper.toSellerAccount(accountDTO);
        createdAccount.addRole(addOrGetRole);
        sellerService.saveSeller(createdAccount);
        SellerAccountDTO data = mapper.toAccountDto(createdAccount);
        return ResponseHandler.generateResponse("Success to create", HttpStatus.CREATED, data);
    }

    @GetMapping("/sellers/{seller_id}")
    public ResponseEntity<Object> getSellerById(@PathVariable("seller_id") String id) {
        // logger.info("Get /seller/{} - Fetching seller by id ", id);
        log.info("{Get /seller/{} - Fetching seller by id }", id);
        Seller foundSeller = sellerService.getSellerById(id)
            .orElseThrow(() -> new ResourceNotFoundException("User not found with id " + id ));
        SellerAccountDTO data = mapper.toAccountDto(foundSeller);
        return new ResponseEntity<Object>(data, HttpStatus.OK);
    }

    @GetMapping("/sellers/email")
    public ResponseEntity<Object> getSellerByEmail(@RequestParam String email) {
        // logger.info("Get /seller/email - Fetching seller by email ", email);
        log.info("{Get /seller/email - Fetching seller by email }", email);
        Seller foundSeller = sellerService.getSellerByEmail(email)
            .orElseThrow(() -> new ResourceNotFoundException("User not found with email " + email));
        SellerAccountDTO data = mapper.toAccountDto(foundSeller);
        return ResponseHandler.generateResponse("Successfully retrieve the account", HttpStatus.OK, data);
    }

    @PutMapping("/sellers/{seller_id}") // the plan this url redirect into /{id}
    public ResponseEntity<Object> updateAccount(@PathVariable("seller_id") String id, @RequestBody @Valid SellerAccountUpdateDTO accountdDto) {
        log.info("{Put /seller/{} - updating seller: {} }", id, accountdDto);
        log.info("{Put /seller/{} - updating seller name: {} - updating seller email }", accountdDto.name(), accountdDto.email());
        Seller foundSeller = sellerService.getSellerById(id)
            .orElseThrow(() -> new ResourceNotFoundException("User not found with id " + id));
        Seller newSellerData = sellerService.updateSeller(foundSeller, accountdDto);
        SellerAccountDTO data = mapper.toAccountDto(newSellerData);
        return ResponseHandler.generateResponse("the result of modified account ", HttpStatus.OK, data);
    }

    @PutMapping(path = "/sellers/change-password/{seller_id}") // the plan this url redirect into /{id}
    public ResponseEntity<Object> updatePassword(@PathVariable("seller_id") String id, @RequestBody @Valid SellerAccountUpdatePasswordDTO accountDto) {
        Seller foundSeller = sellerService.getSellerById(id)
            .orElseThrow(() -> new ResourceNotFoundException("User not found with id "+id));
        if (!accountDto.repeatPassword().equals(accountDto.password())) {
            throw new IllegalArgumentException("repeat password should match the password");
        }
        sellerService.updateSellerPassword(foundSeller, accountDto);
        return ResponseHandler.generateResponse("Password successfully changed", HttpStatus.ACCEPTED, "");
    }

    @DeleteMapping("/sellers/{seller_id}")
    public ResponseEntity<Object> deleteAccount(@PathVariable("seller_id") String id) {
        // logger.info("Delete /seller/{} - Deleting seller ", id);
        log.info("{Delete /seller/{} - Deleting seller }", id);
        Seller founSeller = sellerService.getSellerById(id)
            .orElseThrow(() -> new ResourceNotFoundException("User not found with id "+ id));
        sellerService.deleteSellerById(founSeller);
        return ResponseHandler.generateResponse("delete successfully", HttpStatus.OK, "");

    }

    // @GetMapping("/sellers/test/{seller_id}")
    // public ResponseEntity<Object> testQuery(@PathVariable("seller_id") String id) {
    //     AccountProjection outputs = sellerService.getEmailAndName(id);
    //     return ResponseHandler.generateResponse("Test succesfully", HttpStatus.OK, outputs);
    // }

}
