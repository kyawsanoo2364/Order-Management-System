package com.vodica.order_system.controller;

import com.vodica.order_system.dto.LoginRequestDTO;
import com.vodica.order_system.dto.RefreshTokenRequestDTO;
import com.vodica.order_system.dto.RegisterRequestDTO;
import com.vodica.order_system.reponse.AuthApiResponse;
import com.vodica.order_system.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/auth")
public class AuthController {
    private final AuthService authService;

   @PostMapping("/sign-in")
    public ResponseEntity<AuthApiResponse> signIn(@RequestBody @Valid LoginRequestDTO requestDTO){
       AuthApiResponse res = authService.login(requestDTO);
       return new ResponseEntity<>(res, HttpStatus.OK);
   }

   @PostMapping("/sign-up")
    public ResponseEntity<AuthApiResponse> signUp(@RequestBody @Valid RegisterRequestDTO requestDTO){
       AuthApiResponse res = authService.register(requestDTO);
       return new ResponseEntity<>(res,HttpStatus.CREATED);
   }

   @PostMapping("/refreshToken")
    public ResponseEntity<AuthApiResponse> refreshToken(@RequestBody @Valid RefreshTokenRequestDTO requestDTO){
        AuthApiResponse res = authService.refreshToken(requestDTO.getRefreshToken());
        return new ResponseEntity<>(res,HttpStatus.OK);
   }

}
