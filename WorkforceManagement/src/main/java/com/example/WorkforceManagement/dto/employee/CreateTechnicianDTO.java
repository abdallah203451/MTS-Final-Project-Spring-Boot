package com.example.WorkforceManagement.dto.employee;

import com.example.WorkforceManagement.validation.Password;
import com.example.WorkforceManagement.validation.Phone;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateTechnicianDTO {
    @NotBlank
    private String name;
    @Email(message = "must be a valid email")
    @NotBlank
    private String email;
    @NotBlank
    @Password
    private String password;
    @Phone
    private String phoneNumber;
}