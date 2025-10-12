package com.gokhancomert.b2bapplication.controller;

import com.gokhancomert.b2bapplication.dto.UserDto;
import com.gokhancomert.b2bapplication.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<UserDto> getAllUsers() {
        return userService.findAll();
    }

    @GetMapping("/{id}")
    public UserDto getUserById(@PathVariable Long id) {
        return userService.getById(id);
    }

    @PostMapping
    public UserDto createUser(@RequestBody UserDto userDto) {
        return userService.createUser(userDto);
    }

    @PostMapping("/register")
    public ResponseEntity<UserDto> registerUser(@RequestBody UserDto userDto) {
        UserDto savedUser = userService.createUser(userDto);
        return ResponseEntity.ok(savedUser);
    }

    @PutMapping("/{id}")
    public UserDto updateUser(@PathVariable Long id,
                              @RequestBody UserDto userDto) {
        return userService.updateUserById(id, userDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUserById(id);
        return ResponseEntity.ok().build();
    }
}
