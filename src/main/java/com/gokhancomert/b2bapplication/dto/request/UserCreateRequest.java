package com.gokhancomert.b2bapplication.dto.request;

import lombok.Data;

import java.util.Set;

@Data
public class UserCreateRequest {
    private String username;
    private String email;
    private String password;
    private Set<String> roles;
}
