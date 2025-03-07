package com.marketplace.Store;

import java.time.LocalTime;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

// all of dto need to be patternize 

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record StoreRequestDTO(
    @NotBlank(message = "Invalid store name: Empty/Null") 
    @Size(min = 3, max = 20, message = "Store name must be 3-20 characters")
    String storeName,

    @Min(value = 0, message = "Operating hour start must be ≥0") 
    @Max(value = 23, message = "Operating hour start must be ≤23")
    int storeOperatingHourStart,

    @Min(value = 0, message = "Operating minute start must be ≥0") 
    @Max(value = 59, message = "Operating minute start must be ≤59")
    int storeOperatingMinutesStart,

    @Min(value = 0, message = "Operating hour end must be ≥0") 
    @Max(value = 23, message = "Operating hour end must be ≤23")
    int storeOperatingHoursEnd,

    @Min(value = 0, message = "Operating minute end must be ≥0") 
    @Max(value = 59, message = "Operating minute end must be ≤59")
    int storeOperatingMinutesEnd

) {
    // Compact constructor for validation
    public StoreRequestDTO {
        if (toLocalTime(storeOperatingHourStart, storeOperatingMinutesStart)
                .isAfter(toLocalTime(storeOperatingHoursEnd, storeOperatingMinutesEnd))) {
            throw new IllegalArgumentException("Start time must be before end time");
        }
    }

    // Derived fields (no need to store them explicitly)
    public LocalTime storeOperatingTimeStart() {
        return toLocalTime(storeOperatingHourStart, storeOperatingMinutesStart);
    }

    public LocalTime storeOperatingTimeEnd() {
        return toLocalTime(storeOperatingHoursEnd, storeOperatingMinutesEnd);
    }

    private static LocalTime toLocalTime(int hour, int minute) {
        return LocalTime.of(hour, minute);
    }

}