package com.gokhancomert.b2bapplication.mapper;

import com.gokhancomert.b2bapplication.dto.UserDto;
import com.gokhancomert.b2bapplication.dto.request.UserCreateRequest;
import com.gokhancomert.b2bapplication.dto.request.UserRegisterRequest;
import com.gokhancomert.b2bapplication.dto.request.UserUpdateRequest;
import com.gokhancomert.b2bapplication.model.User;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

@Mapper(componentModel = "spring")
public abstract class UserMapper {

    @Autowired
    protected PasswordEncoder passwordEncoder;

    public abstract UserDto toDto(User user);

    @Mapping(target = "password", qualifiedByName = "encodePassword")
    public abstract User toUser(UserRegisterRequest request);

    @Mapping(target = "password", qualifiedByName = "encodePassword")
    public abstract User toUser(UserCreateRequest request);

    @Mapping(target = "id", ignore = true)
    public abstract void updateUserFromDto(UserUpdateRequest dto, @MappingTarget User entity);

    @Named("encodePassword")
    public String encodePassword(String password) {
        return passwordEncoder.encode(password);
    }
}
