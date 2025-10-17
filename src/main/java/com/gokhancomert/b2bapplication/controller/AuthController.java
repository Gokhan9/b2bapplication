package com.gokhancomert.b2bapplication.controller;

import com.gokhancomert.b2bapplication.dto.UserDto;
import com.gokhancomert.b2bapplication.dto.request.UserLoginRequest;
import com.gokhancomert.b2bapplication.dto.request.UserRegisterRequest;
import com.gokhancomert.b2bapplication.model.User;
import com.gokhancomert.b2bapplication.security.JwtUtil;
import com.gokhancomert.b2bapplication.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final JwtUtil jwtUtil;
    private final UserService userService;


    public AuthController(JwtUtil jwtUtil,
                          UserService userService) {
        this.jwtUtil = jwtUtil;
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<UserDto> register(@RequestBody UserRegisterRequest registerRequest) {
        UserDto savedUser = userService.registerUser(registerRequest);
        return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
    }

    @PostMapping("/login") //Bu metod, HTTP POST isteği ile /login endpoint’ine gelen talepleri karşılar.
    public Map<String, String> login(@RequestBody UserLoginRequest userLoginRequest) {
        User user1 = userService.loginUser(userLoginRequest.getUsername(), userLoginRequest.getPassword());
        String token = jwtUtil.generateToken(user1.getUsername(), user1.getRoles());
        return Collections.singletonMap("token", token);
    }
}
