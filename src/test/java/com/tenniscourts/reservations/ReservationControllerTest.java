package com.tenniscourts.reservations;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@SpringBootTest
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
public class ReservationControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ReservationRepository reservationRepository;

    @Transactional
    @Test
    @Sql("/sql/reservations.sql")
    public void testAddReservation() throws Exception {
        CreateReservationRequestDTO createReservationRequestDTO = new CreateReservationRequestDTO();
        createReservationRequestDTO.setGuestId(-1L);
        createReservationRequestDTO.setScheduleId(-1L);

         this.mvc.perform(post("/reservations")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createReservationRequestDTO)));

        List<Reservation> reservations = reservationRepository.findBySchedule_Id(-1L);

        Assertions.assertEquals(1L, reservations.size());

        Reservation reservation = reservations.get(0);

        Assertions.assertEquals(-1L, reservation.getSchedule().getId());
        Assertions.assertEquals(-1L, reservation.getGuest().getId());
        Assertions.assertEquals(BigDecimal.valueOf(10), reservation.getValue());
    }
}
