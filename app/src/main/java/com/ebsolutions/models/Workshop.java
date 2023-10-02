package com.ebsolutions.models;

import io.micronaut.serde.annotation.Serdeable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;

@Data
@Serdeable
@Slf4j
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class Workshop extends BaseEvent {
    private String workshopId;
    private LocalDate workshopDate;
    /**
     * Value in pennies
     */
    private short cost;
}
