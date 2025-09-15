package com.marketplace.StoreManagement.api;

import java.io.IOException;
import java.util.List;

import com.marketplace.StoreManagement.domain.*;
import jakarta.servlet.annotation.MultipartConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.marketplace.Exception.ResourceNotFoundException;
import com.marketplace.Util.ResponseHandler;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@MultipartConfig(maxFileSize = 3 * 1024 * 1024, maxRequestSize = 3 * 1024 * 1024)
@RestController
@RequestMapping("/api")
public class StoreManagementController {
    
    private final StoreService storeService;
    private final AccountService accountService;
    private final StoreMapper mapper;
    private final RoleService roleService;

    @Value("${file.upload-profile}")
    private String uploadDir;

    @Autowired
    public StoreManagementController(StoreService storeService, AccountService accountService, StoreMapper mapper, RoleService roleService) {
        this.storeService = storeService;
        this.accountService = accountService;
        this.mapper = mapper;
        this.roleService = roleService;
    }

    @GetMapping("/stores")
    public ResponseEntity<Object> getAllStores() {
        List<StoreProjection> outputs = storeService.getAllIdAndNameStores();
        log.info("Admin get stores object: {}", outputs);
        return ResponseHandler.generateResponse("Successfully to retrieve the stores", HttpStatus.OK, outputs);
    }

    @GetMapping("/sellers/{account_id}/stores/store")
    public ResponseEntity<Object> getStoreWithAnAccount(@PathVariable("account_id") String accountId) {
        log.info("Get initialize /api/sellers/{account_id}/stores");
        Account account = accountService.getAccountById(accountId)
            .orElseThrow(() -> new ResourceNotFoundException("resource not found"));
        log.info("get account by id method has executed");
        if (account.getStore() == null) {
            throw new IllegalArgumentException("You need to create store first");
        }
        Store store = account.getStore();
        StoreDto output = mapper.toStoreDto(store);
        return ResponseHandler.generateResponse("Get the stores from the sellers", HttpStatus.OK, output);

    }

    @GetMapping("/sellers/{account_id}/check")
    public ResponseEntity<Boolean> doesAccountHaveStore(@PathVariable("account_id") String accountId) {
        Boolean checkAccountHasStore = accountService.doesAccountHaveStore(accountId);
        return ResponseEntity.ok(checkAccountHasStore);
    }


    @GetMapping("/stores/{store_id}/profile")
    public ResponseEntity<Object> getStoreLogo(@PathVariable("store_id") String storeId) {
        
        String storeLogo = storeService.getStoreLogo((storeId));
        System.out.println("storeLogo = " + storeLogo);
        return ResponseHandler.generateResponse("Successfully retrive logo", HttpStatus.OK, storeLogo);
    }

    @PostMapping("/stores/{store_id}/upload_image/image")
    public ResponseEntity<Object> updateStoreLogo(@PathVariable("store_id") String storeId, @RequestPart("image") MultipartFile file) {
        Store store = storeService.getStoreById(storeId)
                .orElseThrow(() -> new ResourceNotFoundException("The store with this id not found"));
        if (!file.getContentType().equals("image/png") &&
                !file.getContentType().equals("image/jpeg") &&
                !file.getContentType().equals("image/jpg")
        ) {
            throw new IllegalArgumentException("Invalid file type. Only PNG or JPEG or JPG files are allowed");
        }
        try {
            String uploadedFile = storeService.uploadStoreProfile(store, file, uploadDir);
            return ResponseHandler.generateResponse("Image successfully to uploaded", HttpStatus.CREATED, uploadedFile);

        } catch (IOException e) {
            log.error("file upload error" + e.getMessage());
            throw new IllegalArgumentException(e.getMessage());

        }

    }

    @DeleteMapping("/stores/{store_id}/uploaded_image")
    public ResponseEntity<Object> deleteStoreLogo(@PathVariable("store_id") String storeId) {
        Store store = storeService.getStoreById(storeId)
                .orElseThrow(() -> new ResourceNotFoundException("Store with this id not found"));
        storeService.deleteStoreLogo(store);
        return ResponseHandler.generateResponse("Image successfully to deleted", HttpStatus.OK, "");

    }
    
    // let's keep it to this way first with {accountId} stuff
//    and then check the testting for this
    @PostMapping("/sellers/{account_id}/stores")
    public ResponseEntity<Object> createStore(@PathVariable("account_id") String accountId, @RequestBody @Valid StoreRequestDTO storeDto) {
        System.out.println("store dto is: " + storeDto);
        Boolean checkIntersectionStoreName = storeService.hasStoreNameSame(storeDto.storeName());
        Role role = roleService.getOrCreateRoleAccount(Role.RoleEnum.SELLER);
        accountService.createStore(accountId, role, checkIntersectionStoreName, storeDto);
//        StoreDto parseToStoreDto = mapper.toStoreDto(createdStore);
        return ResponseHandler.generateResponse("Store created", HttpStatus.CREATED, "");
    }

    @PutMapping("/sellers/{account_id}/stores/store")
    public ResponseEntity<Object> updateStoreName(@PathVariable("account_id") String accountId, @RequestBody @Valid StoreRequestDTO storeDto) {
        String storeUpdatedName = accountService.updateStoreName(accountId, storeService.hasStoreNameSame(storeDto.storeName()), storeDto);
        return ResponseHandler.generateResponse("Store name successfully changes", HttpStatus.CREATED, storeUpdatedName);
    }

    @DeleteMapping("/sellers/{account_id}/stores/store")
    public ResponseEntity<Object> deleteStore(@PathVariable("account_id") String accountId) {
        accountService.deleteStoreFromAccount(accountId);
        return ResponseHandler.generateResponse("Store name successfully changes", HttpStatus.CREATED, "");
    }

    @DeleteMapping("/sellers/delete/{account_id}")
    public ResponseEntity<String> deleteAccountAndStore(@PathVariable("account_id") String accountId) {
        accountService.deleteAccountAndStore(accountId);
        return ResponseEntity.ok("account and store has been deleted");
    }

}
