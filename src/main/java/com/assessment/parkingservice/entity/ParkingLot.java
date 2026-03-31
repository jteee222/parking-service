package com.assessment.parkingservice.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Size;
import lombok.Data;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "parking_lots")
@Data
public class ParkingLot {

    @Id
    private String lotId;

    private String location;
    private int capacity;
    private int occupiedSpaces;
    private double costPerMinute;
}