package com.ebsolutions.config

import java.time.LocalDateTime

class TestConstants {
    public static String adminServiceUrl = "http://localhost:8080"

    // Client Constants
    public static String getClientId = "get-mock-client-id"
    public static String deleteClientId = "delete-mock-client-id"
    public static String updateClientId = "update-mock-client-id"
    public static String nonExistentClientId = "non-existent-client-id"

    // Location Constants
    public static String getLocationClientId = "get-mock-location-client-id"
    public static String getLocationId = "get-mock-location-id"
    public static String getAllLocationClientId = "get-all-location-mock-client-id"
    public static String createLocationClientId = "create-location-mock-client-id"
    public static String deleteLocationClientId = "delete-location-mock-client-id"
    public static String deleteLocationId = "delete-location-mock-location-id"
    public static String updateLocationClientId = "update-location-mock-client-id"
    public static String updateLocationId = "update-location-mock-location-id"

    public static LocalDateTime createdOn = LocalDateTime.of(2023, 7, 04, 1, 2, 34)
    public static LocalDateTime lastUpdatedOn = LocalDateTime.of(2023, 7, 04, 12, 34, 56)
}
