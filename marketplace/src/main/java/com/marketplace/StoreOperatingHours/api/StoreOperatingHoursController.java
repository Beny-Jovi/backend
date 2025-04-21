package com.marketplace.StoreOperatingHours.api;

import com.marketplace.StoreOperatingHours.domain.DayService;
import com.marketplace.StoreOperatingHours.domain.StoreDayOperatingHoursService;
import com.marketplace.StoreOperatingHours.domain.StoreService;
import com.marketplace.Util.ResponseHandler;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/stores")
@AllArgsConstructor
public class StoreOperatingHoursController {

//    private final StoreService storeService;
    private final StoreDayOperatingHoursService storeDayOperatingHoursService;
    private final DayService dayService;

    @PostMapping("/{store_id}/day_operating_hours")
    public ResponseEntity<Object> createStoreOperatingHours(@PathVariable("store_id") String storeId) {
        storeDayOperatingHoursService.createStoreOperatingHours(storeId, dayService.getOrCreateDays());
        return ResponseHandler.generateResponse("successfully to create store operating hours", HttpStatus.CREATED, "");
    }

    @PutMapping("/{store_id}/day_operating_hours")
    public ResponseEntity<Object> updateStoreOperatingHours(@PathVariable("store_id") String storeId, @RequestBody @Valid List<StoreRequestOperatingHoursDto> storeDtos) {
//        storeService.updateStoreOperatingHours(storeId, storeDtos);
        storeDayOperatingHoursService.updateStoreOperatingHours(storeId, storeDtos);
        return ResponseEntity.status(HttpStatus.OK).body("store operating hours has updated");
    }
//
   @GetMapping("/{store_id}/day_operating_hours")
   public ResponseEntity<List<StoreOperatingHoursDto>> getAllStoreSchedule(@PathVariable("store_id") String storeId) {
       return ResponseEntity
               .ok(storeDayOperatingHoursService.getAllStoreOperatingHoursSchedule(storeId)) ;
   }
//
   @GetMapping("/{store_id}/day_operating_hours/today")
   public ResponseEntity<StoreOperatingHoursDto> getStoreScheduleToday(@PathVariable("store_id") String storeId) {
       return ResponseEntity
               .ok(storeDayOperatingHoursService.getStoreOperatingHoursToday(storeId, dayService.getAllDays()));
   }

}
