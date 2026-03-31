package com.assessment.parkingservice.scheduler;

import com.assessment.parkingservice.entity.ParkingLot;
import com.assessment.parkingservice.entity.ParkingSession;
import com.assessment.parkingservice.repository.ParkingLotRepository;
import com.assessment.parkingservice.repository.ParkingSessionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class ParkingSchedulerTest {

    private ParkingSessionRepository repo;
    private ParkingLotRepository lotRepo;
    private ParkingScheduler scheduler;

    @BeforeEach
    void setUp() {
        repo = mock(ParkingSessionRepository.class);
        lotRepo = mock(ParkingLotRepository.class);
        scheduler = new ParkingScheduler(repo, lotRepo);
    }

    @Test
    void autoCheckout_shouldCheckoutSessionsOver15Minutes() {
        ParkingLot lot = new ParkingLot();
        lot.setLotId("LOT1");
        lot.setOccupiedSpaces(2);
        lot.setCostPerMinute(2.0);

        ParkingSession session1 = new ParkingSession();
        session1.setEntryTime(LocalDateTime.now().minusMinutes(20));
        session1.setExitTime(null);
        session1.setParkingLot(lot);

        ParkingSession session2 = new ParkingSession();
        session2.setEntryTime(LocalDateTime.now().minusMinutes(10));
        session2.setExitTime(null);
        session2.setParkingLot(lot);

        when(repo.findAll()).thenReturn(List.of(session1, session2));
        when(lotRepo.save(any())).thenReturn(lot);
        when(repo.save(any())).thenReturn(session1);

        scheduler.autoCheckout();

        assertThat(session1.getExitTime()).isNotNull();
        assertThat(session1.getTotalCost()).isEqualTo(20 * lot.getCostPerMinute());

        assertThat(session2.getExitTime()).isNull();
        assertThat(session2.getTotalCost()).isNull();

        assertThat(lot.getOccupiedSpaces()).isEqualTo(1);

        verify(repo).save(session1);
        verify(lotRepo).save(lot);

        verify(repo, never()).save(session2);
    }

    @Test
    void autoCheckout_shouldDoNothingIfNoSessions() {
        when(repo.findAll()).thenReturn(List.of());

        scheduler.autoCheckout();

        verify(repo, never()).save(any());
        verify(lotRepo, never()).save(any());
    }
}