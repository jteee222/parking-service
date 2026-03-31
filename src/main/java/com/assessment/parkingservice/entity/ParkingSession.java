package com.assessment.parkingservice.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "parking_sessions")
@Data
public class ParkingSession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long sessionId;

    @ManyToOne
    @JoinColumn(name = "lot_id")
    private ParkingLot parkingLot;

    @ManyToOne
    @JoinColumn(name = "license_plate")
    private Vehicle vehicle;

    private LocalDateTime entryTime;
    private LocalDateTime exitTime;
    private Double totalCost;
}