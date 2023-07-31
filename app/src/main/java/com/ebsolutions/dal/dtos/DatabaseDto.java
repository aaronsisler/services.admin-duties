package com.ebsolutions.dal.dtos;

import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;

@Slf4j
@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class DatabaseDto {
    @NonNull
    private String clientId;
    @NonNull
    private LocalDate createdOn;
    @NonNull
    private LocalDate lastUpdatedOn;
}
