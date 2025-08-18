package com.example.WorkforceManagement.dto.assignment;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ReassignmentDTO {
    @NotNull
    private Long workOrderId;
    @NotNull
    private Long newEmployeeId;
}
