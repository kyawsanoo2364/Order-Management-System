package com.vodica.order_system.dto;

import com.vodica.order_system.entity.User;
import com.vodica.order_system.enums.UserRoleEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private Long id;
    private String name;
    private String email;
    private UserRoleEnum role;

    public UserDTO(User entity){
        this.id = entity.getId();
        this.name = entity.getName();
        this.email = entity.getEmail();
        this.role = entity.getRole();
    }
}
