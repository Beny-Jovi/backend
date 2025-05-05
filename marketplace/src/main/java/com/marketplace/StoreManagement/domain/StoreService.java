package com.marketplace.StoreManagement.domain;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;

import com.marketplace.Util.ImageName;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.marketplace.Exception.ResourceNotFoundException;
import com.marketplace.StoreManagement.api.StoreProjection;

@Slf4j
@Service("StoreService")
public class StoreService {
    
    @Autowired
    private StoreRepository storeRepository;
    
    public List<Store> getAllStores() {
        return storeRepository.findAll();
    }

    public List<StoreProjection> getAllIdAndNameStores() {
        return storeRepository.getAllStore();
    }

    public Store saveStore(Store store) {
        Objects.requireNonNull(store);
        return storeRepository.save(store);
    }

    @Cacheable(value = "stores")
    public Optional<Store> getStoreById(String storeId) {
        return storeRepository.findById(storeId);
    } 

    public Boolean hasStoreNameSame(String storeName) {
        return getAllStores().stream()
            .anyMatch(store -> store.getName().contains(storeName));
    }

    public String uploadStoreProfile(String storeId, MultipartFile file, String uploadDir) {
        Store store = getStoreById(storeId)
                .orElseThrow(() -> new ResourceNotFoundException("The store with this id not found"));
        System.out.println("store = " + store);
        if (!file.getContentType().equals("image/png") &&
                !file.getContentType().equals("image/jpeg") &&
                !file.getContentType().equals("image/jpg")
        ) {
            throw new IllegalArgumentException("Invalid file type. Only PNG or JPEG or JPG files are allowed");
        }

        try {
            if (file.getSize() > 2_000_000) {
                throw new IOException("File is too large. The size limit is 2 MB.");
            }

            String fileName = file.getOriginalFilename().replace(file.getOriginalFilename(), "profile_" + store.getId() + "." + ImageName.getImageFileExtensions(file.getOriginalFilename()));
            String storeIdImageDirectory = store.getId();
            String profileDirectory = "/profile/";
            Path path = Paths.get(uploadDir.toString()).resolve(storeIdImageDirectory + profileDirectory + fileName);
            if (!Files.exists(path)) {
                Files.createDirectories(path);
            }
            Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
            Profile pictureProfile = new Profile(path.toString(), store);
            store.setStoreProfile(pictureProfile);
            saveStore(store);
            return path.toString();

        } catch (IOException e) {
            log.error("file upload error" + e.getMessage());
            throw new IllegalArgumentException(e.getMessage());

        }
    }

    @Transactional(readOnly = true)
    public String getStoreLogo(String storeId) {
        Store store = getStoreById(storeId)
                .orElseThrow(() -> new ResourceNotFoundException("The store with this id not found"));
        String storeLogoPath = store.getStoreProfile().getLogoPath();

        Path filePath = Paths.get(storeLogoPath);
        if (!Files.exists(filePath)) {
            throw new ResourceNotFoundException("File is not found");
        }
        return filePath.toString();
    }

    @Transactional
    public void deleteStoreLogo(String storeId) {
        Store store = getStoreById(storeId)
                .orElseThrow(() -> new ResourceNotFoundException("Store with this id not found"));
        Profile profile = store.getStoreProfile();
        Path filePath = Paths.get(profile.getLogoPath()).normalize();
        if (!Files.exists(filePath)) {
            throw new ResourceNotFoundException("The file is not found");
        }
        try {
            Files.delete(filePath);
            log.info("store picture profile is: {}" + profile);
        } catch (IOException e) {
            log.error("/api/stores/{store_id}/uploaded_images", "delete store logo error is: {}", e.getMessage());
            throw new IllegalArgumentException("Can't read the file");
        }
        store.setStoreProfile(null);
//        log.info("store picture profile is: {}" + profile, "second time invoke");
        saveStore(store);
    }

}
