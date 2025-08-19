package com.example.WorkforceManagement.exception;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public class ValidationErrorResponse {
    private LocalDateTime timestamp;
    private int status;
    private String message;
    private Map<String, List<String>> errors;

    public ValidationErrorResponse(LocalDateTime timestamp, int status, String message, Map<String, List<String>> errors) {
        this.timestamp = timestamp;
        this.status = status;
        this.message = message;
        this.errors = errors;
    }

    // getters and setters (or use Lombok @Data)
    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
    public int getStatus() { return status; }
    public void setStatus(int status) { this.status = status; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    public Map<String, List<String>> getErrors() { return errors; }
    public void setErrors(Map<String, List<String>> errors) { this.errors = errors; }
}
