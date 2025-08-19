package com.example.WorkforceManagement.dto.auth;

import com.example.WorkforceManagement.validation.Password;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest {
    @Email(message = "must be a valid email")
    @NotBlank(message = "email is required")
    private String email;

    @NotBlank(message = "password is required")
    @Password
    private String password;
}