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
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

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

    @Transactional
    @Test
    @Sql("/sql/reservations.sql")
    public void testAddReservation() throws Exception {
        Long guestId = -1L;
        Long scheduleId = -1L;

        CreateReservationRequestDTO createReservationRequestDTO = new CreateReservationRequestDTO();
        createReservationRequestDTO.setGuestId(guestId);
        createReservationRequestDTO.setScheduleId(scheduleId);

        MvcResult mvcResult = this.mvc.perform(post("/reservations")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createReservationRequestDTO))).andReturn();

        String resourceLocation = mvcResult.getResponse().getHeader(HttpHeaders.LOCATION);
        Assertions.assertNotNull(resourceLocation);

        MvcResult mvcResultGet = this.mvc.perform(MockMvcRequestBuilders.get(resourceLocation)).andReturn();

        ReservationDTO reservationDTO = objectMapper.readValue(mvcResultGet.getResponse().getContentAsString(), ReservationDTO.class);

        Assertions.assertEquals(scheduleId, reservationDTO.getScheduleId());
        Assertions.assertEquals(guestId, reservationDTO.getGuestId());
        Assertions.assertEquals(BigDecimal.valueOf(10), reservationDTO.getValue());
    }


}
