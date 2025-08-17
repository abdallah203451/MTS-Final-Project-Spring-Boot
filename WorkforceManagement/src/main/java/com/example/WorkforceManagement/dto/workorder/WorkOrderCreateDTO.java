package com.example.WorkforceManagement.dto.workorder;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.time.LocalDate;

@Data
public class WorkOrderCreateDTO {
    @NotBlank
    private String title;
    private String description;
    @NotBlank
    private String customerName;
    @NotBlank
    private String customerMobile;
    private String customerEmail;
    private String customerAddress;
    @NotNull
    private LocalDate proposedSchedulingDate;
}