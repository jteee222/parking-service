package com.assessment.parkingservice.dto;

import lombok.Data;

@Data
public class CheckoutResponseDTO {
    private String licensePlate;

    private String lotId;

    private long minutesParked;

    private double totalCost;
}
