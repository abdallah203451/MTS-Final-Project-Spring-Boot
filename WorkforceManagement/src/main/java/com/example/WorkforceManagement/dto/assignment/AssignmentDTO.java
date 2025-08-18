package com.example.WorkforceManagement.dto.assignment;

import lombok.Data;
import java.time.LocalDate;
import java.time.OffsetDateTime;

@Data
public class AssignmentDTO {
    private Long assignmentId;
    private Long workOrderId;
    private Long employeeId;
    private LocalDate workDate;
    private Integer intervalId;
    private String intervalLabel;
    private OffsetDateTime assignedAt;
    private Long assignedById;
}