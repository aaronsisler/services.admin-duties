package com.ebsolutions.models;


import io.micronaut.serde.annotation.Serdeable;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
@Serdeable
public class CsvResponse {
    private String trackingId;
}
