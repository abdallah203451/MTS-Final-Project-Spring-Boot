package com.example.WorkforceManagement.dto.workorder;

import com.example.WorkforceManagement.validation.Phone;
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
    @Phone
    private String customerMobile;

    @Email(message = "must be a valid email")
    private String customerEmail;

    private String customerAddress;

    @NotNull
    private LocalDate proposedSchedulingDate;
}
