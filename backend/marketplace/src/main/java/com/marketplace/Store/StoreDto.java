package com.marketplace.Store;

import java.time.LocalTime;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record StoreDto(String storeName, Double storeRate, LocalTime storeOperatingHoursStart, LocalTime storeOperatingHoursEnd, StoreStatusEnum storeStatus, int numberOfSales) {
}
