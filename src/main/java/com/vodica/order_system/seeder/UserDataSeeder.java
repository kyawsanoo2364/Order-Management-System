package com.vodica.order_system.seeder;

import com.vodica.order_system.entity.User;
import com.vodica.order_system.enums.UserRoleEnum;
import com.vodica.order_system.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserDataSeeder implements CommandLineRunner {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        //init admin user
        if(!userRepository.existsByEmail("admin@gmail.com")){
            User user = User.builder()
                    .name("admin")
                    .email("admin@gmail.com")
                    .password(passwordEncoder.encode("123456"))
                    .address("United States")
                    .role(UserRoleEnum.ADMIN)
                    .build();
            userRepository.save(user);
        }
    }
}
