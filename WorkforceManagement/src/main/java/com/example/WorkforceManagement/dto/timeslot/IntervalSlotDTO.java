package com.example.WorkforceManagement.dto.timeslot;

import lombok.Data;

@Data
public class IntervalSlotDTO {
    private Integer intervalId;
    private String label;
    private boolean available;
}