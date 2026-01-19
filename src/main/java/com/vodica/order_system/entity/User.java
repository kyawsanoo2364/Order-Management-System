package com.vodica.order_system.entity;

import com.vodica.order_system.enums.UserRoleEnum;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class User extends BaseEntity {
    @Column(name = "name",nullable = false)
    private String name;

    @Column(name = "email",nullable = false,unique = true)
    private String email;

    @Column(name = "password",nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(name = "role",nullable = false)
    private UserRoleEnum role = UserRoleEnum.USER;

    @Column(name = "address")
    private String address;

    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL, orphanRemoval = true,fetch = FetchType.LAZY)
    private List<Order> orders;

}
