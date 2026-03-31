package com.assessment.parkingservice.scheduler;

import com.assessment.parkingservice.entity.ParkingLot;
import com.assessment.parkingservice.entity.ParkingSession;
import com.assessment.parkingservice.repository.ParkingLotRepository;
import com.assessment.parkingservice.repository.ParkingSessionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
@Slf4j
public class ParkingScheduler {

    private final ParkingSessionRepository repo;
    private final ParkingLotRepository lotRepo;

    @Scheduled(fixedRateString = "${parking.scheduler.fixedRate}")
    public void autoCheckout() {
        log.info("Auto Check Out Service running at {}", LocalDateTime.now());

        for (ParkingSession s : repo.findAll()) {

            if (s.getExitTime() == null) {

                long minutes = Duration.between(s.getEntryTime(), LocalDateTime.now()).toMinutes();

                if (minutes > 15) {
                    log.info("Checking out session: {}", s.getSessionId());
                    s.setExitTime(LocalDateTime.now());

                    double cost = minutes * s.getParkingLot().getCostPerMinute();
                    s.setTotalCost(cost);

                    ParkingLot lot = s.getParkingLot();
                    lot.setOccupiedSpaces(lot.getOccupiedSpaces() - 1);
                    lotRepo.save(lot);

                    repo.save(s);
                    log.info("Successfully checked out");
                }
            }
        }
    }
}