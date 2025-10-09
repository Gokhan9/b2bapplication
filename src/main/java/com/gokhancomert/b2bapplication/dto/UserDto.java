package com.gokhancomert.b2bapplication.dto;

import lombok.Data;

import java.util.Set;

@Data
public class UserDto {
    private Long id;
    private String username;
    private String password;
    private String email;
    private Set<String> roles;
}
