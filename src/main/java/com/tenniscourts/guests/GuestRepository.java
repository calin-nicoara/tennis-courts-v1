package com.tenniscourts.guests;

import com.tenniscourts.schedules.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface GuestRepository extends JpaRepository<Guest, Long>,
        JpaSpecificationExecutor<Guest> {

}
