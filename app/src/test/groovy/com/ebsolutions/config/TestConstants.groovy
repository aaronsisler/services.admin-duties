package com.ebsolutions.config

import java.time.LocalDateTime

class TestConstants {
    public static String adminServiceUrl = "http://localhost:8080"
    public static String clientsUrl = TestConstants.adminServiceUrl + "/data/clients"

    // Client Constants
    public static String getClientId = "get-mock-client-id"
    public static String deleteClientId = "delete-mock-client-id"
    public static String updateClientId = "update-mock-client-id"
    public static String nonExistentClientId = "non-existent-client-id"

    // Location Constants
    // Leading Dashes on location ids are to help with the DB Setup
    public static String getLocationClientId = "get-mock-location-client-id"
    public static String getLocationId = "-get-mock-location-id"
    public static String getAllLocationClientId = "get-all-location-mock-client-id"
    public static String getAllLocationIdOne = "-get-all-location-mock-location-1-id"
    public static String getAllLocationIdTwo = "-get-all-location-mock-location-2-id"
    public static String createLocationClientId = "create-location-mock-client-id"
    public static String deleteLocationClientId = "delete-location-mock-client-id"
    public static String deleteLocationId = "-delete-location-mock-location-id"
    public static String updateLocationClientId = "update-location-mock-client-id"
    public static String updateLocationId = "-update-location-mock-location-id"

    // Organizer Constants
    // Leading Dashes on organizer ids are to help with the DB Setup
    public static String getOrganizerClientId = "get-mock-organizer-client-id"
    public static String getOrganizerId = "-get-mock-organizer-id"
    public static String getAllOrganizerClientId = "get-all-organizer-mock-client-id"
    public static String getAllOrganizerIdOne = "-get-all-organizer-mock-organizer-1-id"
    public static String getAllOrganizerIdTwo = "-get-all-organizer-mock-organizer-2-id"
    public static String createOrganizerClientId = "create-organizer-mock-client-id"
    public static String deleteOrganizerClientId = "delete-organizer-mock-client-id"
    public static String deleteOrganizerId = "-delete-organizer-mock-organizer-id"
    public static String updateOrganizerClientId = "update-organizer-mock-client-id"
    public static String updateOrganizerId = "-update-organizer-mock-organizer-id"

    public static LocalDateTime createdOn = LocalDateTime.of(2023, 7, 04, 1, 2, 34)
    public static LocalDateTime lastUpdatedOn = LocalDateTime.of(2023, 7, 04, 12, 34, 56)
}
