package com.assessment.parkingservice.dto;

import lombok.Data;

@Data
public class LotStatusDTO {
    private String lotId;

    private int capacity;

    private long occupied;

    private long available;
}
