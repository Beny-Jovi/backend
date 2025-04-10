package com.marketplace.Store.domain;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;

import com.marketplace.Util.ImageName;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.marketplace.Exception.ResourceNotFoundException;
import com.marketplace.Store.api.StoreProjection;

@Slf4j
@Service
public class StoreService {
    
    @Autowired
    private StoreRepository storeRepository;
    
    public List<Store> getAllStores() {
        return storeRepository.findAll();
    }

    public List<StoreProjection> getAllIdAndNammeStores() {
        return storeRepository.getAllStore();
    }

    public void saveStore(Store store) {
        Objects.requireNonNull(store);
        storeRepository.save(store);
    }

    public Optional<Store> getStoreById(String storeId) {
        return storeRepository.findById(storeId);
    } 

    public Boolean hasStoreNameSame(String storeName) {
        return getAllStores().stream()
            .anyMatch(store -> store.getName().contains(storeName));
    }


    public String uploadStoreProfile(Store store, MultipartFile file, String uploadDir) throws IOException {
        if (!file.getContentType().equals("image/png") &&
                !file.getContentType().equals("image/jpeg") &&
                !file.getContentType().equals("image/jpg")
        ) {
            throw new IllegalArgumentException("Invalid file type. Only PNG or JPEG or JPG files are allowed");
        }

        if (file.getSize() > 3_000_000) {
            throw  new IllegalArgumentException("File is too large. The size limit is 2 MB.");
        }

        String fileName = file.getOriginalFilename().replace(file.getOriginalFilename(), "profile_" + store.getId() + "." + ImageName.getImageFileExtensions(file.getOriginalFilename()));
        Path path = Paths.get(uploadDir.toString()).resolve(store.getId()+"/profile/"+fileName);
        if (!Files.exists(path)) {
            Files.createDirectories(path);
        }
        Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
        Profile pictureProfile = new Profile(path.toString(), store);
        store.setStoreProfile(pictureProfile);
        saveStore(store);
        return path.toString();
    }

    public Resource getStoreLogo(Store store) throws IOException {

        String storeLogoPath = store.getStoreProfile().getLogoPath();

        Path filePath = Paths.get(storeLogoPath);
        Resource resource = new UrlResource(filePath.toUri());
        if (!resource.exists() || !resource.isReadable()) {
            throw new ResourceNotFoundException("The file is not found");
        }

        return resource;
    }

    public void deleteStoreLogo(String storeId) throws IOException {
        Store store = getStoreById(storeId)
                .orElseThrow(() -> new ResourceNotFoundException("Store with this id not found"));
        Profile profile = store.getStoreProfile();
        Path filePath = Paths.get(profile.getLogoPath()).normalize();
        if (!Files.exists(filePath)) {
            throw new ResourceNotFoundException("The file is not found");
        }
        Files.delete(filePath);
        log.info("store picture profile is: {}" + profile);
        profile.setStore(null);
        store.setStoreProfile(null);
        log.info("store picture profile is: {}" + profile, "second time invoke");
        saveStore(store);

    }

//    public void deleteStoreLogo(String storeId) {
//        Store store = getStoreById(storeId)
//                .orElseThrow(() -> new ResourceNotFoundException("Store with this id not found"));
//        Profile storeLogoProfile = store.getStoreProfile();
//        store.removeStoreProfile(store.getStorePictureProfile());
//        if (storeLogoProfile == null) {
//            throw new IllegalArgumentException("You haven't create store profile yet");
//        } else {
//            store.setStorePictureProfile(null);
//            storeRepository.save(store);
//        }
//    }

//    @Transactional
//    public Set<OperatingHours> updateStoreOperatingHours(String storeId, List<StoreRequestOperatingHoursDto> storeDto) {
//        Store store = getStoreById(storeId)
//            .orElseThrow(() -> new ResourceNotFoundException("Store with this id not found"));
//        if(storeDto.size() != 7) {
//            throw new IndexOutOfBoundsException("there just 7 days in a week");
//        }
//
//        Set<OperatingHours> operatingHoursList = store.getStoreOperatingHours();
//        Set<OperatingHours> operatingHoursSet = IntStream.of(operatingHoursList)
//                .filter();
//        Set<OperatingHours> operatingHoursSet = IntStream.range(0, operatingHoursList.size())
//                .mapToObj(i -> {
//                    StoreRequestOperatingHoursDto newOperatingHours = storeDto.get(i);
//                    OperatingHours operatingHours = new OperatingHours();
//                    operatingHours.setOperatingHoursStart(newOperatingHours.operatingTimeStart());
//                    operatingHours.setOperatingHoursEnd(newOperatingHours.operatingTimeEnd());
//                    operatingHours.setStore(store);
//                    return
//                });

//                .map(operatingHours -> {
//                    StoreRequestOperatingHoursDto newOperatingHours = storeDto.get()
//                   int dayNumber = operatingHours.setDay(storeDto.get(i));
//                });
//        Set<OperatingHours> storeOperatingHoursList = IntStream.range(0, operatingHoursList.size())
//                .mapToObj(i -> {
//                    OperatingHours operatingHoursDto = operatingHoursList;
//                    LocalTime operatingStart = operatingHoursDto.operatingTimeStart();
//                    LocalTime operatingEnd = operatingHoursDto.operatingTimeEnd();
//                    operatingHours.setOperatingHoursStart(operatingStart);
//                    operatingHours.setOperatingHoursEnd(operatingEnd);
//                    operatingHours.setStore(store);
//                    return operatingHours;
//                })
//                .collect(Collectors.toSet());
//
//        store.addOperatingHours(storeOperatingHoursList);
//        saveStore(store);
//        return storeOperatingHoursList;
//        store.setStoreOperatingHours();

//    }


    // public List<StoreProjection> getStoresNameAndRate() {
    //     return storeRepository.findStoresNameAndRate();
    // }

    // public List<StoreProjection> findStoresById(String accountId) {
    //     return storeRepository.findStoresByAccountId(accountId);
    // }

    // public Boolean isThereAStore(String id) {
    //     return getAllStores().stream()
    //         .anyMatch(store -> store.getId().contains(id));
    // }

}
