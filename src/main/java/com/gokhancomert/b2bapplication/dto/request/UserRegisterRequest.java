package com.gokhancomert.b2bapplication.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRegisterRequest {

    @NotBlank(message = "Kullanıcı Adı Boş Olamaz.")
    private String username;

    @NotBlank(message = "Şifre Boş Olamaz.")
    @Size(min = 8, message = "Şifre En Az 8 Karakter Uzunluğunda Olmalıdır.")
    private String password;

    @NotBlank(message = "E-Posta Boş Olamaz")
    @Email(message = "Geçerli Bir E-Posta Formatı Girilmelidir.")
    private String email;
}
