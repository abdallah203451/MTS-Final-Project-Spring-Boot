package com.example.WorkforceManagement.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = PhoneValidator.class)
@Target({ ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface Phone {
    String message() default "Invalid phone number. Must start with 010,011,012 or 015 and be 11 digits.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}