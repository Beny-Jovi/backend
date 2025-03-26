package com.marketplace.Store_pages.domain;

import java.util.List;

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
import com.marketplace.Store_pages.api.PageDto;
import com.marketplace.Store_pages.api.PageMapper;
import com.marketplace.Store_pages.api.StorePageProjection;
import com.marketplace.Store_pages.api.PageRequestDto;
import com.marketplace.Util.ResponseHandler;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api")
public class StorePageController {
    
    private final StoreService storeService;
    private final PageMapper mapper;
    private final StorePageService pageService;

    @Autowired
    public StorePageController(StoreService storeService, PageMapper mapper, StorePageService pageService) {
        this.storeService = storeService;
        this.mapper = mapper;
        this.pageService = pageService;
    }

    @GetMapping("/stores/{store_id}/pages")
    public ResponseEntity<Object> getAllPagesInAStore(@PathVariable("store_id") String storeId) {
        List<StorePageProjection> foundStore = storeService.findPagesWithStoreId(storeId);
        if (foundStore.isEmpty()) {
            throw new ResourceNotFoundException("the store with this id not found");
        }
        return ResponseHandler.generateResponse("Successfully retrive the store pages", HttpStatus.OK, foundStore);
    }

    @PostMapping("/stores/{store_id}/pages")
    public ResponseEntity<Object> createPage(@PathVariable("store_id") String storeId, @RequestBody @Valid PageRequestDto pageDto) {
        Store foundStore = storeService.getStoreById(storeId)
            .orElseThrow(() -> new ResourceNotFoundException("Store with this id not found"));
        Boolean checkPageName = foundStore.getPages().stream()
            .anyMatch(page -> page.getName().equals(pageDto.pageName()));
        if (checkPageName) {
            throw new ResourceDuplicationException("this kind of name has been listed");
        }
        Page page = mapper.toPage(pageDto);
        page.setStore(foundStore);
        foundStore.addPages(page);
        storeService.saveStoreWithPages(foundStore);
        PageDto output = mapper.toDto(page);
        return ResponseHandler.generateResponse("Success for create the store page", HttpStatus.CREATED, output);
    }

    @GetMapping("/stores/{store_id}/pages/{page_id}")
    public ResponseEntity<Object> getCertainPage(@PathVariable("store_id") String storeId, @PathVariable("page_id") String pageId) {
        List<StorePageProjection> pages = storeService.findPagesWithStoreId(storeId);
        if (pages.isEmpty()) {
            throw new ResourceNotFoundException("the store with this id not found");
        }
        StorePageProjection output = pages.stream()
        .filter(storePageProjection -> storePageProjection.pageId().equals(pageId))
        .findAny()
        .orElseThrow(() -> new ResourceNotFoundException("The page with this id not found"));
        return ResponseHandler.generateResponse("Successfully to get page", HttpStatus.OK, output);
    }

    @PutMapping("/stores/{store_id}/pages/{page_id}")
    public ResponseEntity<Object> updatePage(@PathVariable("store_id") String storeId, @PathVariable("page_id") String pageId, @RequestBody @Valid PageRequestDto pageDto) {
        Store foundStore = storeService.getStoreById(storeId)
            .orElseThrow(() -> new ResourceNotFoundException("the store with this id is not found"));
        Page foundPage = foundStore.getPages().stream()
            .filter(storePage -> storePage.getId().equals(pageId))
            .findAny()
            .orElseThrow(() -> new ResourceNotFoundException("the page with this id not found"));
        pageService.updatePage(foundPage, pageDto);
        storeService.saveStoreWithPages(foundStore);
        PageDto output = mapper.toDto(foundPage);
        return ResponseHandler.generateResponse("Successfully to store id", HttpStatus.OK, output);

    }

    @DeleteMapping("/stores/{store_id}/pages/{page_id}")
    public ResponseEntity<Object> deletePage(@PathVariable("store_id") String storeId, @PathVariable("page_id") String pageId) {
        Store foundStore = storeService.getStoreById(storeId)
            .orElseThrow(() -> new ResourceNotFoundException("store with this id not found"));
        // foundStore.getPages().removeIf(page -> page.getId().equals(pageId));
        Boolean checkPageId = foundStore.getPages().stream()
            .anyMatch(page -> page.getId().equals(pageId));
        if (!checkPageId) {
            throw new ResourceNotFoundException("page with this id not found");
        }
        foundStore.getPages().removeIf(page -> page.getId().equals(pageId));
        // foundStore.deletePageWithId(pageId);
        storeService.saveStoreWithPages(foundStore);
        return ResponseHandler.generateResponse("Successfully to delete", HttpStatus.OK, "");
        
    }

    // @GetMapping("/pages/test")
    // public ResponseEntity<Object> pageTestId() {
    //     List<StoreView> stores = storeService.getAllStores();
    //     return ResponseHandler.generateResponse("test id", HttpStatus.OK, stores);
    // }

}
