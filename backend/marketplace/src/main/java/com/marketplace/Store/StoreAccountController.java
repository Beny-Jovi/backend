package com.marketplace.Store;

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

import com.marketplace.Exception.ResourceFoundException;
import com.marketplace.Exception.ResourceNotFoundException;
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

    @GetMapping("/seller/store")
    public ResponseEntity<List<StoreDto>> getAllStores() {
        List<StoreDto> stores = storeService.getAllStores()
            .stream()
            .map(mapper::toStoreDto)
            .collect(Collectors.toList());
        log.info("Admin get stores object: {}", stores);
        return ResponseEntity.ok().body(stores);
    }

    // let's keep it to this way first with {sellerId} stuff
    @PostMapping("/{seller_id}/store")
    public ResponseEntity<Object> createStore(@PathVariable("seller_id") String sellerId, @RequestBody @Valid StoreRequestDTO storeDto) {
        log.info("Post /api/sellerId store name doesn't have any conflict: {}", storeDto.storeName());
        Account account = accountService.getAccountById(sellerId)
            .orElseThrow(() -> new ResourceNotFoundException("Seller with this id is not found " + sellerId));
        if (storeService.hasStoreNameSame(storeDto.storeName())) {
            throw new ResourceFoundException("Store name has already been taken");
        }
        log.info("Post /api/sellerId find based on seller id: {}", "the seller is: {}", sellerId, account);
        Store createdStore = mapper.toStore(storeDto);
        log.info("Post /api/sellerId created store is: {}", createdStore);
        accountService.addNumberOfStores(account);
        account.addStores(createdStore);

        createdStore.setAccount(account);
        log.trace("Post /api/sellerId save account: {}", account);

        // storeService.saveStore(createdStore);
        accountService.saveAccount(account);
        StoreDto parseToStoreDto = mapper.toStoreDto(createdStore);
        // return ResponseEntity.ok().body(parseToStoreDto);
        return ResponseHandler.generateResponse("Store created", HttpStatus.CREATED, parseToStoreDto);
    }

    @PutMapping("/{seller_id}/{store_id}")
    public ResponseEntity<Object> updateStore(@PathVariable("seller_id") String sellerId, @PathVariable("store_id") String storeId, @RequestBody @Valid StoreRequestDTO storeDto) {
        log.info("Put /{seller_id}/{store_id} find based on seller id: {}", "find based on store id: {}", sellerId, storeId);
        Boolean checkAccount = accountService.isAccountThere(sellerId);
        if (!checkAccount) {
            throw new ResourceFoundException("there is no account with this id");
        }
        log.info("Put /{seller_id}/{store_id} passed the checking test");
        Store foundStore = storeService.getStoreById(storeId)
            .orElseThrow(() -> new ResourceNotFoundException("store with this id not found "+ storeId));
        if (storeService.hasStoreNameSame(storeDto.storeName())) {
            throw new ResourceFoundException("Store name has already been taken");
        }
        Store updatedStore = storeService.updateStore(foundStore, storeDto);
        StoreDto storeOutput = mapper.toStoreDto(updatedStore);
        log.info("Put /{seller_id}/{store_id} success for update the store");
        return ResponseHandler.generateResponse("Store created", HttpStatus.CREATED, storeOutput);
        
    }

    @DeleteMapping("/{seller_id}/{store_id}")
    public ResponseEntity<Object> deleteStore(@PathVariable("seller_id") String sellerId, @PathVariable("store_id") String storeId) {
        Boolean checkAccount = accountService.isAccountThere(sellerId);
        if (!checkAccount) {
            throw new ResourceFoundException("there is no account with this id");
        }
        Store foundStore = storeService.getStoreById(storeId)
            .orElseThrow(() -> new ResourceNotFoundException("store with this id not found "+ storeId));
        storeService.deleteStoreById(foundStore);    
        return ResponseHandler.generateResponse("Store created", HttpStatus.CREATED, "store has been deleted");
    }


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
