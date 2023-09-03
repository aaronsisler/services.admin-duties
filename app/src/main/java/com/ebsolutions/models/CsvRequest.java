package com.ebsolutions.models;


import io.micronaut.serde.annotation.Serdeable;
import jakarta.validation.constraints.NotBlank;
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
    private short year;
    private byte month;
    @NotBlank
    private String clientId;
    private String trackingId;
}
