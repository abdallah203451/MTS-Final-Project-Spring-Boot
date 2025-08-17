package com.example.WorkforceManagement.dto.workorder;

import lombok.Data;
import jakarta.validation.constraints.*;
import java.time.LocalDate;

@Data
public class WorkOrderUpdateDTO {
    @NotBlank
    @Size(max = 400)
    private String title;

    private String description;

    @NotBlank
    @Size(max = 200)
    private String customerName;

    @NotBlank
    @Size(max = 30)
    private String customerMobile;

    @Email
    @Size(max = 320)
    private String customerEmail;

    private String customerAddress;

    @NotNull
    private LocalDate proposedSchedulingDate;
}
