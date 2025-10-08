package com.gokhancomert.b2bapplication.dto.mapper;

import com.gokhancomert.b2bapplication.dto.request.UserRegisterRequest;
import com.gokhancomert.b2bapplication.dto.response.UserResponse;
import com.gokhancomert.b2bapplication.model.User;

public class UserMapper {

    public static User toEntity(UserRegisterRequest request) {
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(request.getPassword());
        return user;
    }

    public static UserResponse toResponse(User user) {
        UserResponse response = new UserResponse();
        response.setId(user.getId());
        response.setUsername(user.getUsername());
        response.setRole(user.getRoles().toString()); //
        return response;
    }
}
