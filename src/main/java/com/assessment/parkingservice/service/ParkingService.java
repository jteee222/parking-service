package com.assessment.parkingservice.service;

import com.assessment.parkingservice.dto.CheckoutResponseDTO;
import com.assessment.parkingservice.dto.ParkingLotDTO;
import com.assessment.parkingservice.dto.LotStatusDTO;
import com.assessment.parkingservice.dto.ParkingSessionDTO;
import com.assessment.parkingservice.dto.VehicleDTO;

import java.util.List;

public interface ParkingService {

    VehicleDTO registerVehicle(VehicleDTO dto);

    ParkingLotDTO registerLot(ParkingLotDTO dto);

    ParkingSessionDTO checkIn(String plate, String lotId);

    CheckoutResponseDTO checkOut(String plate);

    LotStatusDTO getLotStatus(String lotId);

    List<VehicleDTO> getVehiclesInLot(String lotId);
}