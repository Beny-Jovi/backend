package com.marketplace.Store.api;

import java.io.IOException;
import java.util.List;

import jakarta.servlet.annotation.MultipartConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
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
import org.springframework.web.multipart.MultipartFile;

import com.marketplace.Exception.ResourceNotFoundException;
import com.marketplace.Store.domain.Account;
import com.marketplace.Store.domain.AccountService;
import com.marketplace.Store.domain.Store;
import com.marketplace.Store.domain.StoreService;
import com.marketplace.Util.ResponseHandler;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@MultipartConfig(maxFileSize = 3 * 1024 * 1024, maxRequestSize = 3 * 1024 * 1024)
@RestController
@RequestMapping("/api")
public class StoreAccountController {
    
    private final StoreService storeService;
    private final AccountService accountService;
    private final StoreMapper mapper;

    @Value("${file.upload-profile}")
    private String uploadDir;

    @Autowired
    public StoreAccountController(StoreService storeService, AccountService accountService, StoreMapper mapper) {
        this.storeService = storeService;
        this.accountService = accountService;
        this.mapper = mapper;
    }

    @GetMapping("/stores")
    public ResponseEntity<Object> getAllStores() {
        List<StoreProjection> outputs = storeService.getAllIdAndNammeStores();
        log.info("Admin get stores object: {}", outputs);
        return ResponseHandler.generateResponse("Successfully to retrieve the stores", HttpStatus.OK, outputs);
    }

    @GetMapping("/sellers/{seller_id}/stores/store")
    public ResponseEntity<Object> getStoreWithAnAccount(@PathVariable("seller_id") String sellerId) {
        log.info("Get initialize /api/sellers/{seller_id}/stores");
        Account account = accountService.getAccountById(sellerId)
            .orElseThrow(() -> new ResourceNotFoundException("resource not found"));
        log.info("get account by id method has executed");
        if (account.getStore() == null) {
            throw new IllegalArgumentException("You need to create store first");
        }
        Store store = account.getStore();
        StoreDto output = mapper.toStoreDto(store);
        return ResponseHandler.generateResponse("Get the stores from the sellers", HttpStatus.OK, output);

    }

//    @GetMapping("/stores/{store_id}/store_operating_time")
//    public ResponseEntity<Object> getAllStoreOperatingTime(@PathVariable("store_id") String storeId) {
//
//    }

//    @PutMapping("/stores/{store_id}/store_operating_time")
//    public ResponseEntity<Object> createStoreOperatingTime(@PathVariable("store_id") String storeId, @RequestBody @Valid List<StoreRequestOperatingHoursDto> storeDto) {
//        storeService.updateStoreOperatingHours(storeId, storeDto);
//        return ResponseHandler.generateResponse("Store name successfully changes", HttpStatus.CREATED, storeDto);
//    }

    @GetMapping("/stores/{store_id}/profile")
    public ResponseEntity<Object> getStoreLogo(@PathVariable("store_id") String storeId) {
        Store store = storeService.getStoreById(storeId)
                .orElseThrow(() -> new ResourceNotFoundException("The store with this id not found"));
        try {
            Resource storePath = storeService.getStoreLogo(store);
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(storePath);

        } catch (IOException ex) {
            log.error(ex.getMessage());
            return ResponseHandler.generateResponse("Failed to get file", HttpStatus.NOT_FOUND, "");
        }

    }

    @PutMapping("/stores/{store_id}/upload_image/image")
    public ResponseEntity<Object> updateStoreLogo(@PathVariable("store_id") String storeId, @RequestParam("image") MultipartFile file) {
        Store store = storeService.getStoreById(storeId)
                .orElseThrow(() -> new ResourceNotFoundException("The store with this id not found"));
        try {
            String uploadedFile = storeService.uploadStoreProfile(store, file, uploadDir);
            return ResponseHandler.generateResponse("Image successfully to uploaded", HttpStatus.CREATED, uploadedFile);
        } catch(IOException ex) {
            log.error("file upload error" + ex.getMessage());
            throw new IllegalArgumentException("File upload error");
        }
    }

    @DeleteMapping("/stores/{store_id}/uploaded_image")
    public ResponseEntity<Object> deleteStoreLogo(@PathVariable("store_id") String storeId) {
        try {
            storeService.deleteStoreLogo(storeId);
            return ResponseHandler.generateResponse("Image successfully to deleted", HttpStatus.CREATED, "");
        } catch (IOException ex) {
            log.error("/api/stores/{store_id}/uploaded_images", "delete store logo error is: {}", ex.getMessage());
            throw new IllegalArgumentException("Can't read the file");
        }
    }
    
    // let's keep it to this way first with {sellerId} stuff
    @PostMapping("/sellers/{seller_id}/stores")
    public ResponseEntity<Object> createStore(@PathVariable("seller_id") String sellerId, @RequestBody @Valid StoreRequestDTO storeDto) {
        Store createdStore = accountService.createStore(sellerId, storeService.hasStoreNameSame(storeDto.storeName()), storeDto);
        StoreDto parseToStoreDto = mapper.toStoreDto(createdStore);
        return ResponseHandler.generateResponse("Store created", HttpStatus.CREATED, parseToStoreDto);
    }

    @PutMapping("/sellers/{seller_id}/stores/store")
    public ResponseEntity<Object> updateStoreName(@PathVariable("seller_id") String sellerId, @RequestBody @Valid StoreRequestDTO storeDto) {
        String storeUpdatedName = accountService.updateStoreName(sellerId, storeService.hasStoreNameSame(storeDto.storeName()), storeDto);
        return ResponseHandler.generateResponse("Store name successfully changes", HttpStatus.CREATED, storeUpdatedName);
    }

    @DeleteMapping("/sellers/{seller_id}/stores/store")
    public ResponseEntity<Object> deleteStore(@PathVariable("seller_id") String sellerId) {
        accountService.deleteStoreFromAccount(sellerId);
        return ResponseHandler.generateResponse("Store name successfully changes", HttpStatus.CREATED, "");
    }

}
