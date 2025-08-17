package com.example.WorkforceManagement.dto.workorder;

import lombok.Data;
import java.time.LocalDate;
import java.time.OffsetDateTime;

@Data
public class WorkOrderDTO {
    private Long workOrderId;
    private String title;
    private String description;
    private String customerName;
    private String customerMobile;
    private String customerEmail;
    private String customerAddress;
    private LocalDate proposedSchedulingDate;
    private OffsetDateTime createdAt;
    private Long createdById;
    private String createdByName;
    private String status;
}