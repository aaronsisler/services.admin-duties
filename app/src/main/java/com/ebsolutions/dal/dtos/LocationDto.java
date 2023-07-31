package com.ebsolutions.dal.dtos;

import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
@Builder
public class LocationDto extends DatabaseDto {
    private String locationId;
    private String name;
}
