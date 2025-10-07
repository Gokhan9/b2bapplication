package com.gokhancomert.b2bapplication.controller;

import com.gokhancomert.b2bapplication.model.User;
import com.gokhancomert.b2bapplication.repository.UserRepository;
import com.gokhancomert.b2bapplication.security.JwtUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    public AuthController(UserRepository userRepository, JwtUtil jwtUtil, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/register")
    public User register(@RequestBody User user) {
        //Kullanıcının gönderdiği şifreyi hash’ler. passwordEncoder.encode(...) ile şifre güvenli hale getirilir (veritabanında düz metin olarak saklanmaz).Bu adım güvenlik açısından zorunludur.
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        //Yeni kullanıcıya otomatik olarak "USER" rolü veriliyor. Admin veya başka roller daha sonra atanabilir.
        user.setRoles(Collections.singleton("USER"));
        return userRepository.save(user);
    }

    @PostMapping("/login") //Bu metod, HTTP POST isteği ile /login endpoint’ine gelen talepleri karşılar.
    public Map<String, String> login(@RequestBody User user) {
        //Kullanıcının gönderdiği username ile veritabanında kayıtlı kullanıcı aranır, Eğer bulunmazsa "User not found" hatası fırlatılır.
        User userFound = userRepository.findByUsername(user.getUsername())
                .orElseThrow(() -> new RuntimeException("User Not Found."));

        // passwordEncoder.matches(...) kullanıcının girdiği şifreyi veritabanındaki hash ile karşılaştırır, Eğer şifre doğruysa giriş başarılıdır.
        if(passwordEncoder.matches(user.getPassword(), userFound.getPassword())) {
            //Kullanıcı adı ve roller ile bir JWT token üretilir, Bu token daha sonra API çağrılarında kimlik doğrulama için kullanılır.
            String token = jwtUtil.generateToken(userFound.getUsername(), userFound.getRoles());
            //Token JSON formatında client’a gönderilir:
            return Collections.singletonMap("token", token);
        } else {
            throw new RuntimeException("Invalid Credentials."); //Eğer şifre yanlışsa veya kullanıcı bulunamazsa, hata fırlatılır.
        }
    }
}
