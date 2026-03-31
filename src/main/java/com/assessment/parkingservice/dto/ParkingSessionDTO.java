package com.assessment.parkingservice.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ParkingSessionDTO {
    private Long sessionId;

    private String lotId;

    private String licensePlate;

    private LocalDateTime entryTime;

    private LocalDateTime exitTime;

    private Double totalCost;
}