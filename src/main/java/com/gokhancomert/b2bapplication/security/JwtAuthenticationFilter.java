package com.gokhancomert.b2bapplication.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;

@Component //Spring bunu otomatik olarak bean yapar, böylece security zincirine ekleyebilirsin.
public class JwtAuthenticationFilter extends OncePerRequestFilter { //OncePerRequestFilter: Spring Security’nin her request için sadece bir kere çalışan özel bir filtresidir.

    private final JwtUtil jwtUtil; //Token çözmek ve doğrulamak için.
    private final CustomUserDetailsService userDetailsService;  //Username’den kullanıcı bilgilerini (roller, şifre hash’i, yetkiler) yüklemek için.

    public JwtAuthenticationFilter(JwtUtil jwtUtil, CustomUserDetailsService userDetailsService) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

    // doFilterInternal: Spring Security zincirinde asıl iş burada yapılır:
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
        throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization"); //Gelen request’in Authorization header’ına bakar.
        String token = null;
        String username = null;

        //Eğer username bulunduysa ve Spring Security context’inde authentication daha önce set edilmemişse: Veritabanından (ya da memory’den) kullanıcı bilgilerini (UserDetails) yükler.
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7);    // "Bearer " kısmını at
            username = jwtUtil.getUsername(token);  // token içinden username çıkar
        }

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            //Token geçerliyse (jwtUtil.validateToken) → yeni bir Authentication nesnesi oluşturulur.
            //Bu nesne içine: Kullanıcı detayları (userDetails), Yetkiler/roller (userDetails.getAuthorities()) konur.
            //Sonra SecurityContextHolder içine set edilir. Böylece Spring Security artık “bu kullanıcı login olmuş” der.
            if (jwtUtil.validateToken(token)) {
                UsernamePasswordAuthenticationToken authenticationToken =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        }
        filterChain.doFilter(request, response); //Filtrenin işini bitirir ve request’i zincirdeki diğer filtrelere gönderir. Eğer burayı yazmazsan, request aşağıya gitmez → uygulama tıkanır.
    }
}
