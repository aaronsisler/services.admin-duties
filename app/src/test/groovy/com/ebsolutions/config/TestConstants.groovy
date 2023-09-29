package com.ebsolutions.config

import java.time.LocalDateTime

class TestConstants {
    public static String adminServiceUrl = "http://localhost:8080"
    public static String clientsUrl = TestConstants.adminServiceUrl + "/data/clients"

    // Client Constants
    public static String getClientId = "get-client-mock-client-id"
    public static String deleteClientId = "delete-client-mock-client-id"
    public static String updateClientId = "update-client-mock-client-id"
    public static String nonExistentClientId = "non-existent-client-id"

    // Location Constants
    // Leading Dashes on location ids are to help with the DB Setup
    public static String getLocationClientId = "get-location-mock-location-client-id"
    public static String getLocationId = "-get-location-mock-location-id"
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
    public static String getOrganizerClientId = "get-organizer-mock-organizer-client-id"
    public static String getOrganizerId = "-get-organizer-mock-organizer-id"
    public static String getAllOrganizerClientId = "get-all-organizer-mock-client-id"
    public static String getAllOrganizerIdOne = "-get-all-organizer-mock-organizer-1-id"
    public static String getAllOrganizerIdTwo = "-get-all-organizer-mock-organizer-2-id"
    public static String createOrganizerClientId = "create-organizer-mock-client-id"
    public static String deleteOrganizerClientId = "delete-organizer-mock-client-id"
    public static String deleteOrganizerId = "-delete-organizer-mock-organizer-id"
    public static String updateOrganizerClientId = "update-organizer-mock-client-id"
    public static String updateOrganizerId = "-update-organizer-mock-organizer-id"

    // Event Constants
    // Leading Dashes on event ids are to help with the DB Setup
    public static String getEventClientId = "get-event-mock-client-id"
    public static String getEventId = "-get-event-mock-event-id"
    public static String getEventLocationId = "-get-event-mock-location-id"
    public static String getEventOrganizerId = "-get-event-mock-organizer-id"
    public static String getAllEventClientId = "get-all-event-mock-client-id"
    public static String getAllEventIdOne = "-get-all-event-mock-event-1-id"
    public static String getAllEventLocationIdOne = "-get-all-event-mock-location-1-id"
    public static String getAllEventOrganizerIdOne = "-get-all-event-mock-organizer-1-id"
    public static String getAllEventIdTwo = "-get-all-event-mock-event-2-id"
    public static String getAllEventLocationIdTwo = "-get-all-event-mock-location-2-id"
    public static String getAllEventOrganizerIdTwo = "-get-all-event-mock-organizer-2-id"
    public static String createEventClientId = "create-event-mock-client-id"
    public static String createEventLocationId = "-create-event-mock-location-id"
    public static String createEventOrganizerId = "-create-event-mock-organizer-id"
    public static String deleteEventClientId = "delete-event-mock-client-id"
    public static String deleteEventId = "-delete-event-mock-event-id"
    public static String deleteEventLocationId = "-delete-event-mock-location-id"
    public static String deleteEventOrganizerId = "-delete-event-mock-organizer-id"
    public static String updateEventClientId = "update-event-mock-client-id"
    public static String updateEventId = "-update-event-mock-event-id"
    public static String updateEventLocationId = "-update-event-mock-location-id"
    public static String updateEventOrganizerId = "-update-event-mock-organizer-id"

    public static LocalDateTime createdOn = LocalDateTime.of(2023, 7, 04, 1, 2, 34)
    public static LocalDateTime lastUpdatedOn = LocalDateTime.of(2023, 7, 04, 12, 34, 56)
}
