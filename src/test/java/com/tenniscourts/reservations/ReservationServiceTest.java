package com.tenniscourts.reservations;

import com.tenniscourts.schedules.Schedule;
import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
@ContextConfiguration(classes = ReservationService.class)
public class ReservationServiceTest {

    @InjectMocks
    ReservationService reservationService;

    @Test
    public void getRefundValueFullRefund() {
        testRefund(BigDecimal.valueOf(10), LocalDateTime.now().plusDays(2));
    }

    @Test
    public void getRefundValue75Percent() {
        testRefund(BigDecimal.valueOf(7.5), LocalDateTime.now().plusHours(14));
    }

    @Test
    public void getRefundValue50Percent() {
        testRefund(BigDecimal.valueOf(5.0), LocalDateTime.now().plusHours(6));
    }

    @Test
    public void getRefundValue25Percent() {
        testRefund(BigDecimal.valueOf(2.5), LocalDateTime.now().plusHours(1));
    }

    private void testRefund(BigDecimal refundExpected, LocalDateTime startDateTime) {
        Schedule schedule = new Schedule();
        schedule.setStartDateTime(startDateTime);

        Reservation reservation = Reservation.builder().schedule(schedule).value(new BigDecimal(10L)).build();
        Assert.assertEquals(0, refundExpected.compareTo(reservationService.getRefundValue(reservation)));
    }
}