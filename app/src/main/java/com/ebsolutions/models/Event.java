package com.ebsolutions.models;

import io.micronaut.serde.annotation.Serdeable;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.sql.Time;
import java.time.LocalDateTime;

@Builder
@Data
@Serdeable
@Slf4j
@AllArgsConstructor
@NoArgsConstructor
public class Event {
    @NotBlank
    private String clientId;
    private String eventId;
    @NotBlank
    private String name;
    private String category;
    @NotBlank
    private String description;
    @NotBlank
    private byte dayOfWeek;
    @NotBlank
    private Time startTime;
    @NotBlank
    private short duration;
    private LocalDateTime createdOn;
    private LocalDateTime lastUpdatedOn;
}
