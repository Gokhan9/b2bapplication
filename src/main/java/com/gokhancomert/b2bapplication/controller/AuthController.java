package com.gokhancomert.b2bapplication.controller;

import com.gokhancomert.b2bapplication.mapper.UserMapper;
import com.gokhancomert.b2bapplication.request.UserLoginRequest;
import com.gokhancomert.b2bapplication.request.UserRegisterRequest;
import com.gokhancomert.b2bapplication.response.UserResponse;
import com.gokhancomert.b2bapplication.model.User;
import com.gokhancomert.b2bapplication.repository.UserRepository;
import com.gokhancomert.b2bapplication.security.JwtUtil;
import com.gokhancomert.b2bapplication.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;
    private final UserService userService;


    public AuthController(UserRepository userRepository, JwtUtil jwtUtil, PasswordEncoder passwordEncoder, UserService userService) {
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
        this.userService = userService;
    }

    @PostMapping("/register")
    public UserResponse register(@RequestBody UserRegisterRequest userRegisterRequest) {
        User user = UserMapper.toEntity(userRegisterRequest);
        User savedUser = userService.registerUser(user);
        return UserMapper.toResponse(savedUser);
    }

    @PostMapping("/login") //Bu metod, HTTP POST isteği ile /login endpoint’ine gelen talepleri karşılar.
    public Map<String, String> login(@RequestBody UserLoginRequest userLoginRequest) {
        User user1 = userService.loginUser(userLoginRequest.getUsername(), userLoginRequest.getPassword());
        String token = jwtUtil.generateToken(user1.getUsername(), user1.getRoles());
        return Collections.singletonMap("token", token);
    }
}
