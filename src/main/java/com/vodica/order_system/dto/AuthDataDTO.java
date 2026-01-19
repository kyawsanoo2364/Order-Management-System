package com.vodica.order_system.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthDataDTO {
    private UserDTO user;
    private TokenDTO tokens;
}
