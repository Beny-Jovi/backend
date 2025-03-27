package com.marketplace.Store.api;

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
import org.springframework.web.bind.annotation.RestController;

import com.marketplace.Exception.ResourceDuplicationException;
import com.marketplace.Exception.ResourceNotFoundException;
import com.marketplace.Store.domain.Account;
import com.marketplace.Store.domain.AccountService;
import com.marketplace.Store.domain.Store;
import com.marketplace.Store.domain.StoreService;
import com.marketplace.Util.ResponseHandler;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api")
public class StoreAccountController {
    
    private final StoreService storeService;
    private final AccountService accountService;
    private final StoreMapper mapper;

    @Autowired
    public StoreAccountController(StoreService storeService, AccountService accountService, StoreMapper mapper) {
        this.storeService = storeService;
        this.accountService = accountService;
        this.mapper = mapper;
    }

    @GetMapping("/sellers/stores")
    public ResponseEntity<List<StoreDto>> getAllStores() {
        List<StoreDto> stores = storeService.getAllStores()
            .stream()
            .map(mapper::toStoreDto)
            .collect(Collectors.toList());
        log.info("Admin get stores object: {}", stores);
        return ResponseEntity.ok().body(stores);
    }

    @GetMapping("/sellers/{seller_id}/stores")
    public ResponseEntity<Object> getAllStoresWithAnAccount(@PathVariable("seller_id") String sellerId) {
        log.info("Get initialize /api/sellers/{seller_id}/stores");
        Account accounts = accountService.getAccountById(sellerId)
            .orElseThrow(() -> new ResourceNotFoundException("resource not found"));
        List<StoreDto> storesDtos = accounts.getStores().stream()
            .map(mapper::toStoreDto)
            .collect(Collectors.toList());
        log.info("Get initialize /api/sellers/{seller_id}/stores" + storesDtos);
        // List<StoreProjection> storesDtos = accountService.getStoresByAccountId(sellerId);
        return ResponseHandler.generateResponse("Get the stores from the sellers", HttpStatus.OK, storesDtos);

    }

    // let's keep it to this way first with {sellerId} stuff
    @PostMapping("/sellers/{seller_id}/stores")
    public ResponseEntity<Object> createStore(@PathVariable("seller_id") String sellerId, @RequestBody @Valid StoreRequestDTO storeDto) {
        log.info("Post /api/sellers/sellerId/stores store name doesn't have any conflict: {}", storeDto.storeName());
        Account account = accountService.getAccountById(sellerId)
            .orElseThrow(() -> new ResourceNotFoundException("Seller with this id is not found " + sellerId));
        if (storeService.hasStoreNameSame(storeDto.storeName())) {
            throw new ResourceDuplicationException("Store name has already been taken");
        }
        log.info("Post /api/sellers/sellerId/stores find based on seller id: {}", "the seller is: {}", sellerId, account);
        Store createdStore = mapper.toStore(storeDto);
        log.info("Post /api/sellers/sellerId/stores created store is: {}", createdStore);
        accountService.addNumberOfStores(account);
        account.addStores(createdStore);

        createdStore.setAccount(account);
        log.trace("Post /api/sellers/sellerId/stores save account: {}", account);
        
        accountService.saveAccount(account);
        StoreDto parseToStoreDto = mapper.toStoreDto(createdStore);
        return ResponseHandler.generateResponse("Store created", HttpStatus.CREATED, parseToStoreDto);
    }

    @GetMapping("/sellers/{seller_id}/stores/{store_id}")
    public ResponseEntity<Object> getStore(@PathVariable("seller_id") String sellerId, @PathVariable("store_id") String storeId) {
        log.info("Get /seller/{seller_id}/stores/{store_id} find based on seller id: {}", "find based on store id: {}", sellerId, storeId);
        Boolean isSellerAccountThere = accountService.isAccountThere(sellerId);
        if (!isSellerAccountThere) {
            throw new ResourceNotFoundException("Store Not found with this id");
        }
        Store foundStore = storeService.getStoreById(storeId)
            .orElseThrow(() -> new ResourceNotFoundException("Store with this id not found"+ storeId));
        StoreDto storeOutput = mapper.toStoreDto(foundStore);
        return ResponseHandler.generateResponse("Get Store", HttpStatus.OK, storeOutput);

    }

    @PutMapping("/sellers/{seller_id}/stores/{store_id}")
    public ResponseEntity<Object> updateStore(@PathVariable("seller_id") String sellerId, @PathVariable("store_id") String storeId, @RequestBody @Valid StoreRequestDTO storeDto) {
        log.info("Put /sellers/{seller_id}/stores/{store_id} find based on seller id: {}", "find based on store id: {}", sellerId, storeId);
        Boolean checkAccount = accountService.isAccountThere(sellerId);
        if (!checkAccount) {
            throw new ResourceDuplicationException("there is no account with this id");
        }
        log.info("Put /sellers/{seller_id}/stores/{store_id} passed the checking test");
        Store foundStore = storeService.getStoreById(storeId)
            .orElseThrow(() -> new ResourceNotFoundException("store with this id not found "+ storeId));
        if (storeService.hasStoreNameSame(storeDto.storeName())) {
            throw new ResourceDuplicationException("Store name has already been taken");
        }
        Store updatedStore = storeService.updateStore(foundStore, storeDto);
        StoreDto storeOutput = mapper.toStoreDto(updatedStore);
        log.info("Put /sellers/{seller_id}/stores/{store_id} success for update the store");
        return ResponseHandler.generateResponse("Store created", HttpStatus.CREATED, storeOutput);
        
    }

    @DeleteMapping("/sellers/{seller_id}/stores/{store_id}")
    public ResponseEntity<Object> deleteStore(@PathVariable("seller_id") String sellerId, @PathVariable("store_id") String storeId) {
        Boolean checkAccount = accountService.isAccountThere(sellerId);
        if (!checkAccount) {
            throw new ResourceDuplicationException("there is no account with this id");
        }
        Store foundStore = storeService.getStoreById(storeId)
            .orElseThrow(() -> new ResourceNotFoundException("store with this id not found "+ storeId));
        storeService.deleteStoreById(foundStore);    
        return ResponseHandler.generateResponse("Store Deleted", HttpStatus.CREATED, "store has been deleted");
    }

    // @GetMapping("/stores/test")
    // public ResponseEntity<Object> testAccountQuery() {
    //     List<StoreAccountProjection> output = accountService.getAllAccount();
    //     return ResponseHandler.generateResponse("Store created", HttpStatus.CREATED, output);
    // }


    // @GetMapping("/name")
    // public String getThreadName() {
    //     return Thread.currentThread().toString();
    // }

    // @GetMapping("/thread_check")
    // public void doSomething() throws InterruptedException {
    //     log.info("hey, I'm doing something");
    //     Thread.sleep(1000);
    // }

    // @GetMapping("/spring_version")
    // public String getSpringVersion() {
    //     return "Version" + SpringVersion.getVersion();
    // }

}
