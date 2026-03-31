package com.assessment.parkingservice.service.impl;

import com.assessment.parkingservice.dto.CheckoutResponseDTO;
import com.assessment.parkingservice.dto.ParkingLotDTO;
import com.assessment.parkingservice.dto.LotStatusDTO;
import com.assessment.parkingservice.dto.ParkingSessionDTO;
import com.assessment.parkingservice.dto.VehicleDTO;
import com.assessment.parkingservice.entity.ParkingLot;
import com.assessment.parkingservice.entity.ParkingSession;
import com.assessment.parkingservice.entity.Vehicle;
import com.assessment.parkingservice.exception.ResourceNotFoundException;
import com.assessment.parkingservice.mapper.ParkingMapper;
import com.assessment.parkingservice.repository.ParkingLotRepository;
import com.assessment.parkingservice.repository.ParkingSessionRepository;
import com.assessment.parkingservice.repository.VehicleRepository;
import com.assessment.parkingservice.service.ParkingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ParkingServiceImpl implements ParkingService {

    private final ParkingLotRepository lotRepo;
    private final VehicleRepository vehicleRepo;
    private final ParkingSessionRepository sessionRepo;
    private final ParkingMapper mapper;

    @Override
    public VehicleDTO registerVehicle(VehicleDTO dto) {
        return mapper.toDTO(vehicleRepo.save(mapper.toEntity(dto)));
    }

    @Override
    public ParkingLotDTO registerLot(ParkingLotDTO dto) {
        return mapper.toDTO(lotRepo.save(mapper.toEntity(dto)));
    }

    @Override
    public ParkingSessionDTO checkIn(String plate, String lotId) {

        Vehicle vehicle = vehicleRepo.findById(plate)
                .orElseThrow(() -> new ResourceNotFoundException("Vehicle not found"));

        ParkingLot lot = lotRepo.findById(lotId)
                .orElseThrow(() -> new ResourceNotFoundException("Lot not found"));

        sessionRepo.findByVehicleAndExitTimeIsNull(vehicle)
                .ifPresent(s -> { throw new RuntimeException("Already parked"); });

        long occupied = sessionRepo.countByParkingLotAndExitTimeIsNull(lot);
        if (occupied >= lot.getCapacity()) {
            throw new RuntimeException("Lot full");
        }

        ParkingSession s = new ParkingSession();
        s.setVehicle(vehicle);
        s.setParkingLot(lot);
        s.setEntryTime(LocalDateTime.now());

        lot.setOccupiedSpaces((int) occupied + 1);
        lotRepo.save(lot);

        return mapper.toDTO(sessionRepo.save(s));
    }

    @Override
    public CheckoutResponseDTO checkOut(String plate) {

        Vehicle vehicle = vehicleRepo.findById(plate)
                .orElseThrow(() -> new ResourceNotFoundException("Vehicle not found"));

        ParkingSession s = sessionRepo.findByVehicleAndExitTimeIsNull(vehicle)
                .orElseThrow(() -> new RuntimeException("Not parked"));

        s.setExitTime(LocalDateTime.now());

        long minutes = Duration.between(s.getEntryTime(), s.getExitTime()).toMinutes();
        double cost = minutes * s.getParkingLot().getCostPerMinute();

        s.setTotalCost(cost);

        ParkingLot lot = s.getParkingLot();
        lot.setOccupiedSpaces(lot.getOccupiedSpaces() - 1);
        lotRepo.save(lot);

        sessionRepo.save(s);

        return mapper.toCheckoutDTO(s, minutes, cost);
    }

    @Override
    public LotStatusDTO getLotStatus(String lotId) {

        ParkingLot lot = lotRepo.findById(lotId)
                .orElseThrow(() -> new ResourceNotFoundException("Lot not found"));

        long occupied = sessionRepo.countByParkingLotAndExitTimeIsNull(lot);
        long available = lot.getCapacity() - occupied;

        return mapper.toLotStatusDTO(lot, occupied, available);
    }

    @Override
    public List<VehicleDTO> getVehiclesInLot(String lotId) {

        ParkingLot lot = lotRepo.findById(lotId)
                .orElseThrow(() -> new ResourceNotFoundException("Lot not found"));

        return sessionRepo.findByParkingLotAndExitTimeIsNull(lot)
                .stream()
                .map(ParkingSession::getVehicle)
                .map(mapper::toDTO)
                .toList();
    }
}