package com.gokhancomert.b2bapplication.service;

import com.gokhancomert.b2bapplication.model.User;
import com.gokhancomert.b2bapplication.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
//UserDetailsService: Spring Security’nin authentication (kimlik doğrulama) sırasında kullanıcı bilgilerini yüklemek için kullandığı standart interface. CustomUserDetailsService, bizim UserRepository tabanlı kullanıcı kaynağımızı Spring Security’ye bağlar.
@Service
public class CustomUserDetailsService implements UserDetailsService {

    //Kullanıcıyı veritabanından çekmek için UserRepository dependency injection ile alınmış.
    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    //Spring Security giriş yapan kullanıcı adını buraya gönderir.
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //Burada UserRepository kullanılarak veritabanında kullanıcı aranır.
        User user = userRepository.findByUsername(username)
                //Bulunmazsa UsernameNotFoundException fırlatılır (Spring Security bunu handle eder → login başarısız olur).
                .orElseThrow(() -> new UsernameNotFoundException("Username not found" +  username));

        //Spring Security’nin kendi User sınıfından bir nesne oluşturuluyor.
        return new org.springframework.security.core.userdetails.User(
                user.getUsername(), //kullanıcı adı
                user.getPassword(), //şifre hash’i (BCrypt vs.)
                Collections.singleton(() -> "ROLE_" + user.getRoles()) //kullanıcının rol(ler)i, Spring Security rollerin "ROLE_" prefix’i ile başlamasını ister. USER → ROLE_USER, ADMIN → ROLE_ADMIN
        );
    }
}
