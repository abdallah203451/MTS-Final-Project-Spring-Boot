package com.example.WorkforceManagement.dto.timeslot;

import lombok.Data;
import java.time.LocalDate;
import java.util.List;

@Data
public class DaySlotsDTO {
    private LocalDate date;
    private List<IntervalSlotDTO> intervals;
}