package com.gokhancomert.b2bapplication.mapper;

import com.gokhancomert.b2bapplication.dto.UserDto;
import com.gokhancomert.b2bapplication.model.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class UserMapper {

    private final PasswordEncoder passwordEncoder;

    public UserMapper(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    public UserDto toDto(User user) {
        UserDto userDto = new UserDto();

        userDto.setId(user.getId());
        userDto.setUsername(user.getUsername());
        userDto.setEmail(user.getEmail());
        userDto.setRoles(user.getRoles());
        return userDto;
    }

    public User toUser(UserDto userDto) {
        User user = new User();
        user.setUsername(userDto.getUsername());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        user.setEmail(userDto.getEmail());
        user.setRoles(userDto.getRoles());
        return user;

    }
}
