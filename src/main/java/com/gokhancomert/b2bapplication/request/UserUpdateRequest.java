package com.gokhancomert.b2bapplication.request;

import lombok.Data;

import java.util.Set;

@Data
public class UserUpdateRequest {
    private String email;
    private String password;
    private Set<String> roles;
}
