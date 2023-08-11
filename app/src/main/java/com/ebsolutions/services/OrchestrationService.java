package com.ebsolutions.services;

import com.ebsolutions.dal.daos.*;
import com.ebsolutions.exceptions.CsvGenerationException;
import com.ebsolutions.models.*;
import io.micronaut.context.annotation.Prototype;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.file.Paths;
import java.text.MessageFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Prototype
public class OrchestrationService {
    private final ClientDao clientDao;
    private final OrganizerDao organizerDao;
    private final LocationDao locationDao;
    private final EventDao eventDao;
    private final CsvDao csvDao;
    private final List<CalendarEvent> calendarEvents = new ArrayList<>();
    private Client client;
    private Map<DayOfWeek, ArrayList<Event>> dayOfWeekEventListMap = new HashMap<>();
    private List<Location> locations;
    private List<Organizer> organizers;
    private List<Event> events;

    public OrchestrationService(ClientDao clientDao, OrganizerDao organizerDao, LocationDao locationDao, EventDao eventDao, CsvDao csvDao) {
        this.clientDao = clientDao;
        this.organizerDao = organizerDao;
        this.locationDao = locationDao;
        this.eventDao = eventDao;
        this.csvDao = csvDao;
    }

    public void createCsv(CsvRequest csvRequest) throws IOException {
        LocalDate startOfMonth = LocalDate.of(csvRequest.getYear(), csvRequest.getMonth(), 1);
        LocalDate startOfNextMonth = startOfMonth.plusMonths(1);

        this.client = this.clientDao.read(csvRequest.getClientId());

        if (this.client == null) {
            log.error("ERROR::{}", this.getClass().getName());
            throw new CsvGenerationException(MessageFormat.format("Error in {0}", this.getClass().getName()));
        }

        this.locations = this.locationDao.readAll(csvRequest.getClientId());
        this.organizers = this.organizerDao.readAll(csvRequest.getClientId());
        this.events = this.eventDao.readAll(csvRequest.getClientId());
        this.processEvents();

        try {
            startOfMonth.datesUntil(startOfNextMonth).forEach(this::processDate);
        } catch (Exception e) {
            log.error("ERROR::{}", this.getClass().getName(), e);
            throw new CsvGenerationException(MessageFormat.format("Error in {0}", this.getClass().getName()), e);
        }

        this.processLocations();
        this.processOrganizers();
        String filepath = MessageFormat.format("{0}/{1}.csv", this.getFilepath(), LocalDateTime.now().toString());
        this.csvDao.create(this.calendarEvents, filepath);
        // TODO this.fileStorageDao.create();
    }

    private void processEvents() {
        Set<DayOfWeek> dayOfWeekSet = new HashSet<>(this.events.stream().map(event -> event.getDayOfWeek()).toList());
        dayOfWeekSet.stream().forEach(dayOfWeek -> this.dayOfWeekEventListMap.put(dayOfWeek, new ArrayList<>()));

        this.events.stream().forEach(event -> this.dayOfWeekEventListMap.get(event.getDayOfWeek()).add(event));
    }

    private void processDate(LocalDate date) {
        if (!this.dayOfWeekEventListMap.containsKey(date.getDayOfWeek())) {
            return;
        }

        this.dayOfWeekEventListMap.get(date.getDayOfWeek())
                .forEach(event -> this.calendarEvents.add(CalendarEvent.builder().event(event).eventDate(date).build()));
    }

    private void processLocations() {
        Map<String, Location> locationMap = this.locations.stream()
                .collect(Collectors.toMap(Location::getLocationId, Function.identity()));

        this.calendarEvents.forEach(calendarEvent -> calendarEvent.setLocation(locationMap.get(calendarEvent.getEvent().getLocationId())));
    }

    private void processOrganizers() {
        Map<String, Organizer> organizerMap = this.organizers.stream()
                .collect(Collectors.toMap(Organizer::getOrganizerId, Function.identity()));

        this.calendarEvents.forEach(calendarEvent -> calendarEvent.setOrganizer(organizerMap.get(calendarEvent.getEvent().getOrganizerId())));
    }

    private String getFilepath() throws IOException {
        return Paths.get(this.getClass().getResource("/output").getPath()).toString();
    }


//Workflow
// DONE: Validate the inputs
// DONE Year
// DONE Month
// DONE: ClientId exists
// DONE: Fetch all active events
// TBD: Fetch valid workshops for given year and month
// DONE: Find the beginning and end dates of the year and month combo
// Flush out the dates of the month with their day i.e. Monday, Tuesday
// Remove blackout dates for a location
// Start Function
// Loop across the dates of the month
// check what day of week date is
// for event that matches that day of week, add it to the Calendar Events list
// End Function
// Remove blackout dates for an event
// Add workshop(s) to the Calendar Events list
// START Table data for CSV
// Add header row to the table
// Go through the calendar events and find the columns needed per row. Make sure this works for Workshops
// END Table data for CSV
// Generate CSV

}
