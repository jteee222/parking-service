package com.assessment.parkingservice.dto;

import com.assessment.parkingservice.enums.VehicleType;
import lombok.Data;

@Data
public class VehicleDTO {
    private String licensePlate;

    private String type;

    private String ownerName;
}