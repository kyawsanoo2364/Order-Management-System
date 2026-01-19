package com.vodica.order_system.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RefreshTokenRequestDTO {
    @NotBlank(message = "Refresh Token is required")
    private String refreshToken;
}
