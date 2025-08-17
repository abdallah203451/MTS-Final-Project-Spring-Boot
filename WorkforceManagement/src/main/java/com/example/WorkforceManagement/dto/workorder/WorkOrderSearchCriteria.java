package com.example.WorkforceManagement.dto.workorder;

import lombok.Data;
import java.time.LocalDate;

@Data
public class WorkOrderSearchCriteria {
    private String q;
    private String status;
    private LocalDate fromDate;
    private LocalDate toDate;
}