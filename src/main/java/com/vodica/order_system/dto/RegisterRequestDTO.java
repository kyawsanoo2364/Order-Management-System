package com.vodica.order_system.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RegisterRequestDTO {
    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "Email is required")
    @Email
    private String email;

    @NotBlank(message = "Address is required")
    private String address;

    @NotBlank(message = "Password is required")
    @Min(value = 6,message = "Password must be at least 6 characters")
    private String password;
}
