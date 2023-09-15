package com.ebsolutions.models;

import io.micronaut.serde.annotation.Serdeable;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalTime;

@Data
@Serdeable
@Slf4j
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class BaseEvent extends Base {
    @NotBlank
    private String locationId;
    @NotBlank
    private String organizerId;
    @NotBlank
    private String name;
    private String category;
    @NotBlank
    private String description;
    @NotBlank
    private LocalTime startTime;
    @NotBlank
    private short duration;
}
