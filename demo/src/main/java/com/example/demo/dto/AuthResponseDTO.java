package com.example.demo.dto;

public class AuthResponseDTO {

    private boolean success;
    private String message;
    private String email;

    // Constructors
    public AuthResponseDTO() {}

    public AuthResponseDTO(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public AuthResponseDTO(boolean success, String message, String email) {
        this.success = success;
        this.message = message;
        this.email = email;
    }

    // Getters and Setters
    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}