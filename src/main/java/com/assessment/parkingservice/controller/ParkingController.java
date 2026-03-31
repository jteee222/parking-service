package com.assessment.parkingservice.controller;

import com.assessment.parkingservice.dto.CheckoutResponseDTO;
import com.assessment.parkingservice.dto.ParkingLotDTO;
import com.assessment.parkingservice.dto.LotStatusDTO;
import com.assessment.parkingservice.dto.ParkingSessionDTO;
import com.assessment.parkingservice.dto.VehicleDTO;
import com.assessment.parkingservice.service.ParkingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/parking")
@RequiredArgsConstructor
public class ParkingController {

    private final ParkingService service;

    @PostMapping("/vehicles")
    public ResponseEntity<VehicleDTO> registerVehicle(@RequestBody VehicleDTO dto) {
        return ResponseEntity.ok(service.registerVehicle(dto));
    }

    @PostMapping("/lots")
    public ResponseEntity<ParkingLotDTO> registerLot(@RequestBody ParkingLotDTO dto) {
        return ResponseEntity.ok(service.registerLot(dto));
    }

    @PostMapping("/checkin")
    public ResponseEntity<ParkingSessionDTO> checkIn(String plate, String lotId) {
        return ResponseEntity.ok(service.checkIn(plate, lotId));
    }

    @PostMapping("/checkout")
    public ResponseEntity<CheckoutResponseDTO> checkOut(String plate) {
        return ResponseEntity.ok(service.checkOut(plate));
    }

    @GetMapping("/lots/{id}/status")
    public ResponseEntity<LotStatusDTO> status(@PathVariable String id) {
        return ResponseEntity.ok(service.getLotStatus(id));
    }

    @GetMapping("/lots/{id}/vehicles")
    public ResponseEntity<List<VehicleDTO>> vehicles(@PathVariable String id) {
        return ResponseEntity.ok(service.getVehiclesInLot(id));
    }
}