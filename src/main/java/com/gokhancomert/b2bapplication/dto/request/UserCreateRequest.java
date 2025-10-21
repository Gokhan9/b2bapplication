package com.gokhancomert.b2bapplication.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserCreateRequest {
    private String username;
    private String email;
    private String password;
    private Set<String> roles;
}
