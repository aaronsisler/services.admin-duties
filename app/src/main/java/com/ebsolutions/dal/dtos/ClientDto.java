package com.ebsolutions.dal.dtos;

import lombok.*;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
@Builder
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ClientDto extends DatabaseDto {
    @NonNull
    private String name;
}
