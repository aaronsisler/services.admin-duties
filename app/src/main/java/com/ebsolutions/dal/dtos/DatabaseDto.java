package com.ebsolutions.dal.dtos;

import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;

@Slf4j
@Data
@Builder
public abstract class DatabaseDto {
    private String clientId;
    private LocalDate createdOn;
    private LocalDate lastUpdatedOn;
}
