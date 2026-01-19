package com.vodica.order_system.service;

import com.vodica.order_system.entity.User;
import com.vodica.order_system.repository.UserRepository;
import com.vodica.order_system.security.MyUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MyUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(username).orElseThrow(()->
                new UsernameNotFoundException("User not found"));
        return new MyUserDetails(user);
    }
}
