package com.tenniscourts.schedules;

import com.tenniscourts.exceptions.EntityNotFoundException;
import com.tenniscourts.tenniscourts.TennisCourtDTO;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.List;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@SpringBootTest
@RunWith(SpringRunner.class)
public class ScheduleServiceTest {

    @Autowired
    private ScheduleService scheduleService;

    @Test
    public void testAddSchedule() {
        long tennisCourtId = 1L;
        LocalDateTime startTime = LocalDateTime.now();

        CreateScheduleRequestDTO createScheduleRequestDTO = new CreateScheduleRequestDTO();
        createScheduleRequestDTO.setStartDateTime(startTime);
        createScheduleRequestDTO.setTennisCourtId(tennisCourtId);

        ScheduleDTO scheduleDTO = scheduleService.addSchedule(tennisCourtId, createScheduleRequestDTO);

        Assertions.assertEquals(2L, scheduleDTO.getId());
        Assertions.assertEquals(startTime, scheduleDTO.getStartDateTime());
        Assertions.assertEquals(startTime.plusHours(1), scheduleDTO.getEndDateTime());
        Assertions.assertEquals(tennisCourtId, scheduleDTO.getTennisCourtId());
    }

    @Test
    public void testFindSchedulesByDates() {
        List<ScheduleDTO> schedulesByDates = scheduleService.findSchedulesByDates(LocalDateTime.parse("2020-12-20T20:00:00.0"),
                LocalDateTime.parse("2020-12-20T21:00:00.0"));

        Assertions.assertEquals(1, schedulesByDates.size());
        ScheduleDTO scheduleDTO = schedulesByDates.get(0);

        Assertions.assertEquals(1L, scheduleDTO.getId());
        Assertions.assertEquals(LocalDateTime.parse("2020-12-20T20:00:00.0"), scheduleDTO.getStartDateTime());
        Assertions.assertEquals(LocalDateTime.parse("2020-12-20T21:00:00.0"), scheduleDTO.getEndDateTime());
        Assertions.assertEquals(1L, scheduleDTO.getTennisCourtId());
        Assertions.assertNotNull(scheduleDTO.getTennisCourt());
    }

    @Test
    public void testFindScheduleWhenIdExists() {
        ScheduleDTO scheduleDTO = scheduleService.findSchedule(1L);

        Assertions.assertEquals(1L, scheduleDTO.getId());
        Assertions.assertEquals(LocalDateTime.parse("2020-12-20T20:00:00.0"), scheduleDTO.getStartDateTime());
        Assertions.assertEquals(LocalDateTime.parse("2020-12-20T21:00:00.0"), scheduleDTO.getEndDateTime());
        Assertions.assertEquals(1L, scheduleDTO.getTennisCourtId());
        Assertions.assertNotNull(scheduleDTO.getTennisCourt());
    }

    @Test
    public void testFindScheduleWhenIdDoesNotExist() {
        EntityNotFoundException entityNotFoundException = Assertions.assertThrows(EntityNotFoundException.class, () -> scheduleService.findSchedule(-1L));

        Assertions.assertEquals("Schedule not found.", entityNotFoundException.getMessage());
    }

    @Test
    public void testFindSchedulesByTennisCourtId() {
        long tennisCourtId = 1L;

        List<ScheduleDTO> schedulesByTennisCourtId = scheduleService.findSchedulesByTennisCourtId(tennisCourtId);

        Assertions.assertEquals(1, schedulesByTennisCourtId.size());
        ScheduleDTO scheduleDTO = schedulesByTennisCourtId.get(0);

        Assertions.assertEquals(1L, scheduleDTO.getId());
        Assertions.assertEquals(LocalDateTime.parse("2020-12-20T20:00:00.0"), scheduleDTO.getStartDateTime());
        Assertions.assertEquals(LocalDateTime.parse("2020-12-20T21:00:00.0"), scheduleDTO.getEndDateTime());
        Assertions.assertEquals(tennisCourtId, scheduleDTO.getTennisCourtId());
        Assertions.assertNotNull(scheduleDTO.getTennisCourt());
    }
}
