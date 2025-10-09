package com.gokhancomert.b2bapplication.mapper;

import com.gokhancomert.b2bapplication.dto.UserDto;
import com.gokhancomert.b2bapplication.request.UserRegisterRequest;
import com.gokhancomert.b2bapplication.response.UserResponse;
import com.gokhancomert.b2bapplication.model.User;

import java.util.stream.Collectors;

public class UserMapper {

    public UserDto toDto(User user) {
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setUsername(user.getUsername());
        userDto.setEmail(user.getEmail());
        userDto.setRoles(user.getRoles()
                .stream()
                .map(role -> role.getName())
                .collect(Collectors.toSet()));
        return userDto;
    }

    public User toEntity(UserDto userDto) {
        User user = new User();
        user.setUsername(userDto.getUsername());
        user.setPassword(userDto.getPassword());
        user.setEmail(userDto.getEmail());
    }
}
