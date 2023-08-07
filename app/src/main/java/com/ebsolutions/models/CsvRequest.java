package com.ebsolutions.models;


import io.micronaut.serde.annotation.Serdeable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@Serdeable
@AllArgsConstructor
@NoArgsConstructor
public class CsvRequest {
    private String trackingId;
    private short year;
    private byte month;
}
