package com.gokhancomert.b2bapplication.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRegisterRequest {
    private String username;
    private String password;
}
