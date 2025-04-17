package com.marketplace.StoreOperatingHours.api;

import java.time.LocalTime;

public record StoreOperatingHoursDto(
        String dayName,
        LocalTime storeOperatingTimeStart,
        LocalTime storeOperatingTimeEnd
) {
}
