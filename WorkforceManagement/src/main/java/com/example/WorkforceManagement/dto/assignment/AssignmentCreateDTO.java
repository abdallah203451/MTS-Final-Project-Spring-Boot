package com.example.WorkforceManagement.dto.assignment;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.time.LocalDate;

@Data
public class AssignmentCreateDTO {
    @NotNull
    private Long workOrderId;
    @NotNull
    private Long employeeId;
    @NotNull
    private LocalDate workDate;
    @NotNull
    private Integer intervalId;
}