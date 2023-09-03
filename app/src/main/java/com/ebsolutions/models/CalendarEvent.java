package com.ebsolutions.models;

import io.micronaut.serde.annotation.Serdeable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;

@Builder
@Data
@Serdeable
@Slf4j
@AllArgsConstructor
@NoArgsConstructor
public class CalendarEvent {
    private LocalDate eventDate;
    private Location location;
    private Organizer organizer;
    private Event event;
}
