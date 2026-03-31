package com.assessment.parkingservice.repository;

import com.assessment.parkingservice.entity.ParkingLot;
import com.assessment.parkingservice.entity.ParkingSession;
import com.assessment.parkingservice.entity.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ParkingSessionRepository extends JpaRepository<ParkingSession, Long> {

    Optional<ParkingSession> findByVehicleAndExitTimeIsNull(Vehicle vehicle);

    List<ParkingSession> findByParkingLotAndExitTimeIsNull(ParkingLot lot);

    long countByParkingLotAndExitTimeIsNull(ParkingLot lot);
}