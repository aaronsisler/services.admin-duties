package com.ebsolutions.constants


import java.time.LocalDate
import java.time.LocalTime

class WorkshopTestConstants {
    // Leading Dashes on workshop ids are to help with the DB Setup
    // Get Workshop
    public static String getWorkshopClientId = "get-workshop-mock-client-id"
    public static String getWorkshopId = "-get-workshop-mock-workshop-id"
    public static String getWorkshopLocationId = "-get-workshop-mock-location-id"
    public static String getWorkshopOrganizerId = "-get-workshop-mock-organizer-id"
    public static LocalDate getWorkshopWorkshopDate = LocalDate.of(2023, 9, 18)
    public static short getWorkshopDuration = (short) 60
    public static short getWorkshopCost = (short) 5500
    public static LocalTime getWorkshopStartTime = LocalTime.of(8, 30, 0)

    // Get All Workshops
    public static String getAllWorkshopClientId = "get-all-workshop-mock-client-id"
    // Get All Workshops - Workshop One
    public static String getAllWorkshopIdOne = "-get-all-workshop-mock-workshop-1-id"
    public static String getAllWorkshopLocationIdOne = "-get-all-workshop-mock-location-1-id"
    public static String getAllWorkshopOrganizerIdOne = "-get-all-workshop-mock-organizer-1-id"
    public static LocalDate getAllWorkshopWorkshopDateOne = LocalDate.of(2023, 9, 23)
    public static short getAllWorkshopDurationOne = (short) 60
    public static short getAllWorkshopCostOne = (short) 1500
    public static LocalTime getAllWorkshopStartTimeOne = LocalTime.of(8, 30, 0)
    // Get All Workshops - Workshop Two
    public static String getAllWorkshopIdTwo = "-get-all-workshop-mock-workshop-2-id"
    public static String getAllWorkshopLocationIdTwo = "-get-all-workshop-mock-location-2-id"
    public static String getAllWorkshopOrganizerIdTwo = "-get-all-workshop-mock-organizer-2-id"
    public static LocalDate getAllWorkshopWorkshopDateTwo = LocalDate.of(2023, 9, 28)
    public static short getAllWorkshopDurationTwo = (short) 75
    public static short getAllWorkshopCostTwo = (short) 3500
    public static LocalTime getAllWorkshopStartTimeTwo = LocalTime.of(9, 30, 0)

    // Delete Workshop
    public static String deleteWorkshopClientId = "delete-workshop-mock-client-id"
    public static String deleteWorkshopId = "-delete-workshop-mock-workshop-id"
    public static String deleteWorkshopLocationId = "-delete-workshop-mock-location-id"
    public static String deleteWorkshopOrganizerId = "-delete-workshop-mock-organizer-id"
    public static LocalDate deleteWorkshopWorkshopDate = LocalDate.of(2023, 9, 28)
    public static short deleteWorkshopDuration = (short) 45
    public static short deleteWorkshopCost = (short) 2500
    public static LocalTime deleteWorkshopStartTime = LocalTime.of(10, 30, 0)

    // Create Workshop
    public static String createWorkshopClientId = "create-workshop-mock-client-id"
    public static String createWorkshopLocationId = "-create-workshop-mock-location-id"
    public static String createWorkshopOrganizerId = "-create-workshop-mock-organizer-id"
    public static LocalDate createWorkshopWorkshopDate = LocalDate.of(2023, 9, 28)
    public static short createWorkshopDuration = (short) 15
    public static short createWorkshopCost = (short) 2500
    public static LocalTime createWorkshopStartTime = LocalTime.of(11, 30, 0)

    // Update Workshop
    public static String updateWorkshopClientId = "update-workshop-mock-client-id"
    public static String updateWorkshopId = "-update-workshop-mock-workshop-id"
    public static String updateWorkshopLocationId = "-update-workshop-mock-location-id"
    public static String updateWorkshopOrganizerId = "-update-workshop-mock-organizer-id"
    public static LocalDate updateWorkshopWorkshopDate = LocalDate.of(2023, 9, 28)
    public static short updateWorkshopDuration = (short) 15
    public static short updateWorkshopCost = (short) 2500
    public static LocalTime updateWorkshopStartTime = LocalTime.of(11, 30, 0)
}
