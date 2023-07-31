package com.ebsolutions.dal.dtos;

import lombok.*;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Builder
@Data
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class WorkshopDto extends DatabaseDto {
    private String workshopId;
}
