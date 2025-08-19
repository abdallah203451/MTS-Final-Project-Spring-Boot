package com.example.WorkforceManagement.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PhoneValidator implements ConstraintValidator<Phone, String> {
    private static final String EGY_PHONE_REGEX = "^(?:010|011|012|015)\\d{8}$";

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            // if you prefer null to be valid, return true here and annotate with @NotNull/@NotBlank on DTOs
            return false;
        }
        String trimmed = value.trim();
        return trimmed.matches(EGY_PHONE_REGEX);
    }
}
