package com.ebsolutions.config


import java.time.LocalDateTime

class TestConstants {
    // Generic Test Constants
    public static String adminServiceUrl = "http://localhost:8080"
    public static String clientsUrl = TestConstants.adminServiceUrl + "/data/clients"
    // Does Not Exist Client
    public static String nonExistentClientId = "non-existent-client-id"
    public static LocalDateTime createdOn = LocalDateTime.of(2023, 7, 04, 1, 2, 34)
    public static LocalDateTime lastUpdatedOn = LocalDateTime.of(2023, 7, 04, 12, 34, 56)
    // This is Saturday, September 30, 2028 12:00:00 AM in epoch seconds
    public static long expiryTime = (long) 1853884800
}
