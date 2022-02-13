package com.tenniscourts.guests;

import com.fasterxml.jackson.core.type.TypeReference;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@SpringBootTest
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
public class GuestControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private GuestRepository guestRepository;

    @Test
    @Sql("/sql/guests.sql")
    @Transactional
    public void testFindGuests() throws Exception {
        MvcResult mvcResult = this.mvc.perform(MockMvcRequestBuilders.get("/guests?page=0&size=2")).andReturn();

        List<GuestDTO> guestDTOS = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), new TypeReference<List<GuestDTO>>() {
        });

        Set<Long> ids = new HashSet<>(Arrays.asList(1L, 2L));
        Set<Long> actualIds = guestDTOS.stream().map(GuestDTO::getId).collect(Collectors.toSet());

        Assertions.assertIterableEquals(ids, actualIds);
    }

    @Test
    @Sql("/sql/guests.sql")
    @Transactional
    public void testFindGuestById() throws Exception {
        MvcResult mvcResult = this.mvc.perform(MockMvcRequestBuilders.get("/guests/1")).andReturn();

        GuestDTO guestDTO = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), GuestDTO.class);

        Assertions.assertEquals(1L, guestDTO.getId());
        Assertions.assertEquals("Tom Hanks", guestDTO.getName());
    }

    @Test
    @Transactional
    public void testAddGuest() throws Exception {
        GuestDTO guestDTOForTest = new GuestDTO();
        String playerName = "New Player";
        guestDTOForTest.setName(playerName);

        MvcResult mvcResult = this.mvc.perform(MockMvcRequestBuilders.post("/guests")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(guestDTOForTest))).andReturn();

        Assertions.assertEquals(HttpStatus.CREATED.value(), mvcResult.getResponse().getStatus());

        String resourceLocation = mvcResult.getResponse().getHeader(HttpHeaders.LOCATION);
        Assertions.assertNotNull(resourceLocation);

        MvcResult mvcResultGet = this.mvc.perform(MockMvcRequestBuilders.get(resourceLocation)).andReturn();

        GuestDTO guestDTO = objectMapper.readValue(mvcResultGet.getResponse().getContentAsString(), GuestDTO.class);

        Assertions.assertEquals(playerName, guestDTO.getName());
    }

    @Test
    @Transactional
    @Sql("/sql/guests.sql")
    public void testUpdateGuest() throws Exception {
        GuestDTO guestDTOForTest = new GuestDTO();
        String playerName = "New Player";
        guestDTOForTest.setName(playerName);
        guestDTOForTest.setId(1L);

        this.mvc.perform(MockMvcRequestBuilders.put("/guests/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(guestDTOForTest))).andReturn();

        MvcResult mvcResultGet = this.mvc.perform(MockMvcRequestBuilders.get("/guests/1")).andReturn();

        GuestDTO guestDTO = objectMapper.readValue(mvcResultGet.getResponse().getContentAsString(), GuestDTO.class);

        Assertions.assertEquals(playerName, guestDTO.getName());
    }

    @Test
    @Sql("/sql/guests.sql")
    @Transactional
    public void testDeleteGuestById() throws Exception {
        this.mvc.perform(MockMvcRequestBuilders.delete("/guests/1"));

        MvcResult mvcResult = this.mvc.perform(MockMvcRequestBuilders.get("/guests/1")).andReturn();

        Assertions.assertEquals(HttpStatus.NOT_FOUND.value(), mvcResult.getResponse().getStatus());
    }
}
