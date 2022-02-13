package com.tenniscourts.guests;

import com.tenniscourts.schedules.Schedule;
import com.tenniscourts.schedules.ScheduleDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface GuestMapper {

    GuestDTO map(Guest source);
    Guest map(GuestDTO source);
}
