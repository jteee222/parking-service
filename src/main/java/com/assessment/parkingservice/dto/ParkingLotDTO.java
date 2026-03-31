package com.assessment.parkingservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ParkingLotDTO {
    private String lotId;

    private String location;

    private int capacity;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private int occupiedSpaces;

    private double costPerMinute;
}