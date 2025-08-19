package com.example.WorkforceManagement.dto.workorder;

import com.example.WorkforceManagement.validation.Phone;
import jakarta.validation.constraints.Email;
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
    @Phone
    private String customerMobile;
    @Email(message = "must be a valid email")
    private String customerEmail;
    private String customerAddress;
    @NotNull
    private LocalDate proposedSchedulingDate;
}