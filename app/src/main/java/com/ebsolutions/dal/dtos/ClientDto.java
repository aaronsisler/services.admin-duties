package com.ebsolutions.dal.dtos;

import io.micronaut.serde.annotation.Serdeable;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;

@Slf4j
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@DynamoDbBean
@Serdeable
public class ClientDto extends DatabaseDto {
    @NonNull
    private String name;
}
