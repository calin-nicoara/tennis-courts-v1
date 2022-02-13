package com.tenniscourts.schedules;

import com.tenniscourts.exceptions.EntityNotFoundException;
import com.tenniscourts.tenniscourts.TennisCourt;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
@Transactional(readOnly = true)
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;
    private final ScheduleMapper scheduleMapper;

    @Transactional
    public ScheduleDTO addSchedule(Long tennisCourtId, CreateScheduleRequestDTO createScheduleRequestDTO) {
        TennisCourt tennisCourt = new TennisCourt();
        tennisCourt.setId(tennisCourtId);

        Schedule newScheduleForTennisCourt = Schedule.builder()
                .startDateTime(createScheduleRequestDTO.getStartDateTime())
                .endDateTime(createScheduleRequestDTO.getStartDateTime().plusHours(1))
                .reservations(new ArrayList<>())
                .tennisCourt(tennisCourt)
                .build();

        return scheduleMapper.map(scheduleRepository.saveAndFlush(newScheduleForTennisCourt));
    }

    public List<ScheduleDTO> findSchedulesByDates(LocalDateTime startDate, LocalDateTime endDate) {
        return scheduleMapper.map(scheduleRepository.findByStartDateTimeBetween(startDate, endDate));
    }

    public ScheduleDTO findSchedule(Long scheduleId) {
        return scheduleRepository.findById(scheduleId).map(scheduleMapper::map).orElseThrow(() -> {
            throw new EntityNotFoundException("Schedule not found.");
        });
    }

    public List<ScheduleDTO> findSchedulesByTennisCourtId(Long tennisCourtId) {
        return scheduleMapper.map(scheduleRepository.findByTennisCourt_IdOrderByStartDateTime(tennisCourtId));
    }
}
