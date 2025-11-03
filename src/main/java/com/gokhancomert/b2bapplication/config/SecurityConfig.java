package com.gokhancomert.b2bapplication.config;

import com.gokhancomert.b2bapplication.security.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    //SecurityFilterChain: Spring Security’nin en temel yapı taşıdır. Her gelen HTTP isteği bu filtre zincirinden geçer.
    //HttpSecurity: Bu obje sayesinde hangi endpoint’lere kimlerin erişebileceğini, session yönetimini, csrf ayarlarını vs. yapılandırırız.
    //@Bean: Bu metodu Spring’e bir güvenlik yapılandırma bean’i olarak kaydeder. Yani Spring uygulama ayağa kalkarken bu ayarları kullanır..
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) //CSRF (Cross-Site Request Forgery) saldırılarına karşı korumadır.
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers("/api/admin/**").hasRole("ADMIN") //yetki verilen admin erişim sağlar.
                        .requestMatchers("/api/products/**", "/api/categories/**").permitAll() //api'lere herkes erişebilir.
                        .anyRequest().authenticated() //geri kalan tüm endpoint’ler giriş yapmış (token’ı olan) kullanıcı ister.
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)); //STATELESS ile Spring’e session oluşturma demiş oluyoruz.

        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build(); //Yapılandırmayı tamamlar ve filtre zincirini Spring’e verir. Her request bu kurallara göre değerlendirilir.
    }


    //AuthenticationManager, Spring Security’nin kimlik doğrulama (authentication) sürecini yöneten ana bileşenidir.
    //Kullanıcının girdiği username ve password değerlerini alır, bunları UserDetailsService ve PasswordEncoder ile karşılaştırır.
    //Eğer bilgiler doğruysa → Authentication nesnesi döner (kullanıcı doğrulanmış olur)
    //AuthenticationConfiguration, Spring Security’nin otomatik yapılandırma sınıfıdır.
    //İçinde UserDetailsService, PasswordEncoder, AuthenticationProvider gibi bizim tanımladığımız bean’leri kullanarak bir AuthenticationManager üretir.
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }
}
