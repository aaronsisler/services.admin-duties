package com.ebsolutions.constants

import java.time.DayOfWeek
import java.time.LocalTime

class EventTestConstants {
    // Leading Dashes on event ids are to help with the DB Setup
    // Get Event
    public static String getEventClientId = "get-event-mock-client-id"
    public static String getEventId = "-get-event-mock-event-id"
    public static String getEventLocationId = "-get-event-mock-location-id"
    public static String getEventOrganizerId = "-get-event-mock-organizer-id"
    public static DayOfWeek getEventDayOfWeek = DayOfWeek.MONDAY
    public static short getEventDuration = (short) 60
    public static LocalTime getEventStartTime = LocalTime.of(8, 30, 0)

    // Get All Events
    public static String getAllEventClientId = "get-all-event-mock-client-id"
    // Get All Events - Event One
    public static String getAllEventIdOne = "-get-all-event-mock-event-1-id"
    public static String getAllEventLocationIdOne = "-get-all-event-mock-location-1-id"
    public static String getAllEventOrganizerIdOne = "-get-all-event-mock-organizer-1-id"
    public static DayOfWeek getAllEventDayOfWeekOne = DayOfWeek.MONDAY
    public static short getAllEventDurationOne = (short) 60
    public static LocalTime getAllEventStartTimeOne = LocalTime.of(8, 30, 0)
    // Get All Events - Event Two
    public static String getAllEventIdTwo = "-get-all-event-mock-event-2-id"
    public static String getAllEventLocationIdTwo = "-get-all-event-mock-location-2-id"
    public static String getAllEventOrganizerIdTwo = "-get-all-event-mock-organizer-2-id"
    public static DayOfWeek getAllEventDayOfWeekTwo = DayOfWeek.TUESDAY
    public static short getAllEventDurationTwo = (short) 75
    public static LocalTime getAllEventStartTimeTwo = LocalTime.of(9, 30, 0)

    // Delete Event
    public static String deleteEventClientId = "delete-event-mock-client-id"
    public static String deleteEventId = "-delete-event-mock-event-id"
    public static String deleteEventLocationId = "-delete-event-mock-location-id"
    public static String deleteEventOrganizerId = "-delete-event-mock-organizer-id"
    public static LocalTime deleteEventStartTime = LocalTime.of(10, 30, 0)
    public static short deleteEventDuration = (short) 45
    public static DayOfWeek deleteEventDayOfWeek = DayOfWeek.WEDNESDAY

    // Create Event
    public static String createEventClientId = "create-event-mock-client-id"
    public static String createEventLocationId = "-create-event-mock-location-id"
    public static String createEventOrganizerId = "-create-event-mock-organizer-id"
    public static LocalTime createEventStartTime = LocalTime.of(11, 30, 0)
    public static short createEventDuration = (short) 15
    public static DayOfWeek createEventDayOfWeek = DayOfWeek.THURSDAY

    // Update Event
    public static String updateEventClientId = "update-event-mock-client-id"
    public static String updateEventId = "-update-event-mock-event-id"
    public static String updateEventLocationId = "-update-event-mock-location-id"
    public static String updateEventOrganizerId = "-update-event-mock-organizer-id"
    public static DayOfWeek updateEventDayOfWeek = DayOfWeek.THURSDAY
    public static short updateEventDuration = (short) 15
    public static LocalTime updateEventStartTime = LocalTime.of(11, 30, 0)

}
