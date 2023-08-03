package com.ebsolutions.models;

import io.micronaut.serde.annotation.Serdeable;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;

@Builder
@Data
@Serdeable
@Slf4j
@AllArgsConstructor
@NoArgsConstructor
public class Organizer {
    @NotBlank
    private String clientId;
    private String organizerId;
    @NotBlank
    private String name;
    private LocalDateTime createdOn;
    private LocalDateTime lastUpdatedOn;
}
