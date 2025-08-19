package com.example.WorkforceManagement.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordConstraintValidator implements ConstraintValidator<Password, String> {

    @Override
    public void initialize(Password constraintAnnotation) {
        // no-op
    }

    @Override
    public boolean isValid(String password, ConstraintValidatorContext context) {
        if (password == null) {
            buildViolation(context, "Password must not be null.");
            return false;
        }

        String trimmed = password.trim();

        if (trimmed.length() < 8) {
            buildViolation(context, "Password must be at least 8 characters long.");
            return false;
        }

        if (!trimmed.chars().anyMatch(Character::isLowerCase)) {
            buildViolation(context, "Password must contain at least one lowercase letter.");
            return false;
        }

        if (!trimmed.chars().anyMatch(Character::isUpperCase)) {
            buildViolation(context, "Password must contain at least one uppercase letter.");
            return false;
        }

        if (!trimmed.chars().anyMatch(Character::isDigit)) {
            buildViolation(context, "Password must contain at least one digit.");
            return false;
        }

        // at least one special character (non-alphanumeric). You can refine the set if you like.
        if (trimmed.matches("[A-Za-z0-9]*")) {
            buildViolation(context, "Password must contain at least one special character (e.g. @,#,$,%).");
            return false;
        }

        // no whitespace allowed (optional). If you want to allow spaces, remove this check.
        if (trimmed.matches(".*\\s+.*")) {
            buildViolation(context, "Password must not contain whitespace characters.");
            return false;
        }

        return true;
    }

    private void buildViolation(ConstraintValidatorContext context, String message) {
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(message)
                .addConstraintViolation();
    }
}
