package com.assessment.parkingservice.service;

import com.assessment.parkingservice.dto.CheckoutResponseDTO;
import com.assessment.parkingservice.dto.ParkingSessionDTO;
import com.assessment.parkingservice.dto.VehicleDTO;
import com.assessment.parkingservice.entity.ParkingLot;
import com.assessment.parkingservice.entity.ParkingSession;
import com.assessment.parkingservice.entity.Vehicle;
import com.assessment.parkingservice.mapper.ParkingMapper;
import com.assessment.parkingservice.repository.ParkingLotRepository;
import com.assessment.parkingservice.repository.ParkingSessionRepository;
import com.assessment.parkingservice.repository.VehicleRepository;
import com.assessment.parkingservice.service.impl.ParkingServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyDouble;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ParkingServiceTest {

    private ParkingLotRepository lotRepo;
    private VehicleRepository vehicleRepo;
    private ParkingSessionRepository sessionRepo;
    private ParkingMapper mapper;

    private ParkingServiceImpl service;

    @BeforeEach
    void setUp() {
        lotRepo = mock(ParkingLotRepository.class);
        vehicleRepo = mock(VehicleRepository.class);
        sessionRepo = mock(ParkingSessionRepository.class);
        mapper = mock(ParkingMapper.class);

        service = new ParkingServiceImpl(lotRepo, vehicleRepo, sessionRepo, mapper);
    }

    @Test
    void checkIn_successful() {
        String plate = "ABC123";
        String lotId = "LOT1";

        Vehicle vehicle = new Vehicle();
        vehicle.setLicensePlate(plate);

        ParkingLot lot = new ParkingLot();
        lot.setLotId(lotId);
        lot.setCapacity(2);
        lot.setOccupiedSpaces(0);

        ParkingSession session = new ParkingSession();
        session.setVehicle(vehicle);
        session.setParkingLot(lot);
        session.setEntryTime(LocalDateTime.now());

        ParkingSessionDTO sessionDTO = new ParkingSessionDTO();

        when(vehicleRepo.findById(plate)).thenReturn(Optional.of(vehicle));
        when(lotRepo.findById(lotId)).thenReturn(Optional.of(lot));
        when(sessionRepo.findByVehicleAndExitTimeIsNull(vehicle)).thenReturn(Optional.empty());
        when(sessionRepo.countByParkingLotAndExitTimeIsNull(lot)).thenReturn(0L);
        when(sessionRepo.save(any())).thenReturn(session);
        when(lotRepo.save(lot)).thenReturn(lot);
        when(mapper.toDTO(session)).thenReturn(sessionDTO);

        ParkingSessionDTO result = service.checkIn(plate, lotId);

        assertThat(result).isEqualTo(sessionDTO);
        assertThat(lot.getOccupiedSpaces()).isEqualTo(1);

        verify(sessionRepo).save(any(ParkingSession.class));
        verify(lotRepo).save(lot);
    }

    @Test
    void checkIn_alreadyParked_throws() {
        Vehicle vehicle = new Vehicle();
        ParkingLot lot = new ParkingLot();

        when(vehicleRepo.findById("ABC123")).thenReturn(Optional.of(vehicle));
        when(lotRepo.findById("LOT1")).thenReturn(Optional.of(lot));
        when(sessionRepo.findByVehicleAndExitTimeIsNull(vehicle))
                .thenReturn(Optional.of(new ParkingSession()));

        assertThrows(RuntimeException.class, () -> service.checkIn("ABC123", "LOT1"));
    }

    @Test
    void checkOut_successful() {
        Vehicle vehicle = new Vehicle();
        vehicle.setLicensePlate("ABC123");

        ParkingLot lot = new ParkingLot();
        lot.setOccupiedSpaces(1);
        lot.setCostPerMinute(2.0);

        ParkingSession session = new ParkingSession();
        session.setVehicle(vehicle);
        session.setParkingLot(lot);
        session.setEntryTime(LocalDateTime.now().minusMinutes(10));

        CheckoutResponseDTO checkoutDTO = new CheckoutResponseDTO();

        when(vehicleRepo.findById("ABC123")).thenReturn(Optional.of(vehicle));
        when(sessionRepo.findByVehicleAndExitTimeIsNull(vehicle)).thenReturn(Optional.of(session));
        when(sessionRepo.save(session)).thenReturn(session);
        when(lotRepo.save(lot)).thenReturn(lot);
        when(mapper.toCheckoutDTO(any(), anyLong(), anyDouble())).thenReturn(checkoutDTO);

        CheckoutResponseDTO result = service.checkOut("ABC123");

        assertThat(result).isEqualTo(checkoutDTO);
        assertThat(lot.getOccupiedSpaces()).isEqualTo(0);
        assertThat(session.getExitTime()).isNotNull();
        assertThat(session.getTotalCost()).isGreaterThan(0);
    }

    @Test
    void getLotStatus_returnsCorrectValues() {
        ParkingLot lot = new ParkingLot();
        lot.setLotId("LOT1");
        lot.setCapacity(5);

        when(lotRepo.findById("LOT1")).thenReturn(Optional.of(lot));
        when(sessionRepo.countByParkingLotAndExitTimeIsNull(lot)).thenReturn(2L);

        service.getLotStatus("LOT1");

        verify(mapper).toLotStatusDTO(lot, 2L, 3L);
    }

    @Test
    void getVehiclesInLot_returnsVehicles() {
        ParkingLot lot = new ParkingLot();
        lot.setLotId("LOT1");

        Vehicle v1 = new Vehicle();
        Vehicle v2 = new Vehicle();

        ParkingSession s1 = new ParkingSession();
        s1.setVehicle(v1);

        ParkingSession s2 = new ParkingSession();
        s2.setVehicle(v2);

        when(lotRepo.findById("LOT1")).thenReturn(Optional.of(lot));
        when(sessionRepo.findByParkingLotAndExitTimeIsNull(lot)).thenReturn(List.of(s1, s2));
        when(mapper.toDTO(v1)).thenReturn(new VehicleDTO());
        when(mapper.toDTO(v2)).thenReturn(new VehicleDTO());

        List<VehicleDTO> result = service.getVehiclesInLot("LOT1");

        assertThat(result).hasSize(2);
    }
}