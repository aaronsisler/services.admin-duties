package com.ebsolutions.config

import java.time.LocalDateTime

class TestConstants {
    public static String adminServiceUrl = "http://localhost:8080"
    public static String clientId = "mock-client-id"
    public static String deleteClientId = "delete-mock-client-id"
    public static String updateClientId = "update-mock-client-id"

    public static LocalDateTime createdOn = LocalDateTime.of(2023, 7, 04, 1, 2, 34)
    public static LocalDateTime lastUpdatedOn = LocalDateTime.of(2023, 7, 04, 12, 34, 56)
}
