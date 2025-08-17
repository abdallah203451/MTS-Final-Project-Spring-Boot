package com.example.WorkforceManagement.dto.assignment;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public class AssignmentReassignDTO {
    @NotNull
    private Long newEmployeeId;

    @NotNull
    private LocalDate workDate;

    @NotNull
    private Long intervalId;

    // getters and setters
}
