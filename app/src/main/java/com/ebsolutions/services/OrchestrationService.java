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
    private final Map<DayOfWeek, ArrayList<Event>> dayOfWeekEventListMap = new HashMap<>();
    private List<Location> locations;
    private List<Organizer> organizers;
    private List<Event> events;
    private final MetricsStopWatch metricsStopWatch = new MetricsStopWatch(false);

    public OrchestrationService(ClientDao clientDao, OrganizerDao organizerDao, LocationDao locationDao, EventDao eventDao, CsvDao csvDao) {
        this.clientDao = clientDao;
        this.organizerDao = organizerDao;
        this.locationDao = locationDao;
        this.eventDao = eventDao;
        this.csvDao = csvDao;
    }


    public void createCsv(CsvRequest csvRequest) throws IOException {

        this.client = this.clientDao.read(csvRequest.getClientId());

        if (this.client == null) {
            log.error("ERROR::{}", this.getClass().getName());
            throw new CsvGenerationException(MessageFormat.format("Error in {0}", this.getClass().getName()));
        }

        this.locations = this.locationDao.readAll(csvRequest.getClientId());
        this.organizers = this.organizerDao.readAll(csvRequest.getClientId());
        this.events = this.eventDao.readAll(csvRequest.getClientId());

        this.processEvents();
        this.processDates(csvRequest);
        this.processLocations();
        this.processOrganizers();

        String filepath = MessageFormat.format("{0}/{1}.csv", this.getFilepath(), LocalDateTime.now().toString());
        this.csvDao.create(this.calendarEvents, filepath);
        // TODO this.fileStorageDao.create();
    }

    private void processEvents() {
        metricsStopWatch.resetStopWatchClock();
        Set<DayOfWeek> dayOfWeekSet = new HashSet<>(this.events.stream().map(event -> event.getDayOfWeek()).toList());
        dayOfWeekSet.stream().forEach(dayOfWeek -> this.dayOfWeekEventListMap.put(dayOfWeek, new ArrayList<>()));

        this.events.stream().forEach(event -> this.dayOfWeekEventListMap.get(event.getDayOfWeek()).add(event));
        metricsStopWatch.logElapsedTime(MessageFormat.format("{0}::{1}", this.getClass().getName(), "processEvents"));
    }

    private void processDates(CsvRequest csvRequest) {
        metricsStopWatch.resetStopWatchClock();
        LocalDate startOfMonth = LocalDate.of(csvRequest.getYear(), csvRequest.getMonth(), 1);
        LocalDate startOfNextMonth = startOfMonth.plusMonths(1);
        try {
            startOfMonth.datesUntil(startOfNextMonth).forEach(this::processDate);
        } catch (Exception e) {
            log.error("ERROR::{}", this.getClass().getName(), e);
            throw new CsvGenerationException(MessageFormat.format("Error in {0}", this.getClass().getName()), e);
        } finally {
            metricsStopWatch.logElapsedTime(MessageFormat.format("{0}::{1}", this.getClass().getName(), "processDates"));
        }
    }

    private void processDate(LocalDate date) {
        if (!this.dayOfWeekEventListMap.containsKey(date.getDayOfWeek())) {
            return;
        }

        this.dayOfWeekEventListMap.get(date.getDayOfWeek())
                .forEach(event -> this.calendarEvents.add(CalendarEvent.builder().event(event).eventDate(date).build()));
    }

    private void processLocations() {
        metricsStopWatch.resetStopWatchClock();
        Map<String, Location> locationMap = this.locations.stream()
                .collect(Collectors.toMap(Location::getLocationId, Function.identity()));

        this.calendarEvents.forEach(calendarEvent -> calendarEvent.setLocation(locationMap.get(calendarEvent.getEvent().getLocationId())));
        metricsStopWatch.logElapsedTime(MessageFormat.format("{0}::{1}", this.getClass().getName(), "processLocations"));
    }

    private void processOrganizers() {
        metricsStopWatch.resetStopWatchClock();
        Map<String, Organizer> organizerMap = this.organizers.stream()
                .collect(Collectors.toMap(Organizer::getOrganizerId, Function.identity()));

        this.calendarEvents.forEach(calendarEvent -> calendarEvent.setOrganizer(organizerMap.get(calendarEvent.getEvent().getOrganizerId())));
        metricsStopWatch.logElapsedTime(MessageFormat.format("{0}::{1}", this.getClass().getName(), "processOrganizers"));
    }

    private String getFilepath() throws IOException {
        return Paths.get(this.getClass().getResource("/output").getPath()).toString();
    }
}
