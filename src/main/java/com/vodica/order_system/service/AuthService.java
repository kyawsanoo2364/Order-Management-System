package com.vodica.order_system.service;

import com.vodica.order_system.dto.*;
import com.vodica.order_system.entity.User;
import com.vodica.order_system.enums.UserRoleEnum;
import com.vodica.order_system.exceptions.ResourceAlreadyExistsException;
import com.vodica.order_system.exceptions.ResourceNotFoundException;
import com.vodica.order_system.reponse.AuthApiResponse;
import com.vodica.order_system.repository.UserRepository;
import com.vodica.order_system.security.JwtUtils;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final JwtUtils jwtUtils;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;

    public AuthApiResponse login(LoginRequestDTO requestDTO){
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(requestDTO.getEmail(),requestDTO.getPassword())
        );
        User user = userRepository.findByEmail(requestDTO.getEmail()).orElseThrow(()->
                new ResourceNotFoundException("User not found with this email")
                );
        String refreshToken = jwtUtils.generateRefreshToken(user);
        String accessToken = jwtUtils.generateToken(user.getEmail());
        TokenDTO tokens = new TokenDTO(accessToken,refreshToken);
        AuthDataDTO data = new AuthDataDTO(new UserDTO(user),tokens);
        return new AuthApiResponse("success","Login Successfully",data);
    }

    public AuthApiResponse register(RegisterRequestDTO requestDTO){
        Optional<User> existingUser = userRepository.findByEmail(requestDTO.getEmail());
        if(existingUser.isPresent()){
            throw new ResourceAlreadyExistsException("User is already exists with this email");
        }
        String hashedPassword = passwordEncoder.encode(requestDTO.getPassword());
        User newUser = User.builder()
                .name(requestDTO.getName())
                .email(requestDTO.getEmail())
                .password(hashedPassword)
                .role(UserRoleEnum.USER)
                .address(requestDTO.getAddress())
                .build();
        userRepository.save(newUser);

        return new AuthApiResponse("success","Sign up successfully",null);
    }

    public AuthApiResponse refreshToken(String refreshToken){
        if(!jwtUtils.verifyRefreshToken(refreshToken)){
            throw new JwtException("Invalid refresh token or expired");
        }
        String username = jwtUtils.extractUsernameFromRefreshToken(refreshToken);
        User existingUser = userRepository.findByEmail(username).orElseThrow(()->
                new ResourceNotFoundException("User not found with this email "+username)
                );
        String accessToken = jwtUtils.generateToken(username);
        TokenDTO tokens = new TokenDTO(accessToken,null);
        AuthDataDTO authData = new AuthDataDTO(new UserDTO(existingUser),tokens);
        return new AuthApiResponse("success","Refresh Token successfully",authData);
    }

}
