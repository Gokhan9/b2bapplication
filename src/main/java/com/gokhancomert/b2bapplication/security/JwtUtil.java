package com.gokhancomert.b2bapplication.security;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.Set;

@Component /// Bu sınıfı Spring'in yönetmesi için bean olarak işaretler
public class JwtUtil {

    /// JWT imzalamak için kullanılacak gizli anahtar (en az 32 karakter uzunluğunda olmalı)
    private final Key SECRET_KEY = Keys.hmacShaKeyFor("supersecretkeysupersecretkeysupersecretkey123".getBytes());

    /// Token geçerlilik süresi (1 saat)
    private final long EXPIRATION_TIME = 1000 * 60 * 60; // 1 saat


    /// Kullanıcı adı ve roller bilgisine göre JWT token oluşturur.
    public String generateToken(String username, Set<String> roles) {
        return Jwts.builder()
                .setSubject(username) /// token konusu = kullanıcı adı
                .claim("roles", roles) /// ek bilgi eklendi = roller
                .setIssuedAt(new Date()) /// token oluşturma zamanı
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME)) // token geçerlilik süresi
                .signWith(SECRET_KEY) /// gizi anahtar ile imza
                .compact(); /// token'ı string dön.
    }

    /// Token içinden kullanıcı adını çözümler.
    public String getUsername(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY) /// Doğrulama için aynı gizli anahtar kullanılır
                .build()
                .parseClaimsJws(token) /// Tokeni çözümle (parsing)
                .getBody()
                .getSubject(); /// Subject alanı (kullanıcı adı) alınır
    }


    /// Tokenin geçerliliğini kontrol eder (süre dolmuş mu, imza geçerli mi).
    public boolean validateToken(String token) {
        try {
            /// token doğrulamaya çalışır
            Jwts.parserBuilder().setSigningKey(SECRET_KEY).build().parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            /// token geçersiz ve hatalıysa false döner
            return false;
        }
    }
}
