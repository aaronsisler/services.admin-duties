package com.ebsolutions.models;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.serde.annotation.Serdeable;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Builder
@Data
@Serdeable
@Slf4j
@AllArgsConstructor
@NoArgsConstructor
public class Event extends Object {
    @NotBlank
    private String clientId;
    private String eventId;
    @NotBlank
    private String locationId;
    @NotBlank
    private String organizerId;
    @NotBlank
    private String name;
    private String category;
    @NotBlank
    private String description;
    @NonNull
    private DayOfWeek dayOfWeek;
    private LocalTime startTime;
    private short duration;
    private LocalDateTime createdOn;
    private LocalDateTime lastUpdatedOn;
}
