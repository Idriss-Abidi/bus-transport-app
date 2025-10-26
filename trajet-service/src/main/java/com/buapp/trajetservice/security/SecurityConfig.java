//package com.buapp.trajetservice.security;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.web.SecurityFilterChain;
//
//@Configuration
//@EnableWebSecurity
//public class SecurityConfig {
//
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//
//        http
//                // 🔓 Allow access to H2 console
//                .authorizeHttpRequests(auth -> auth
//                        .requestMatchers("/h2-console/**").permitAll()
//                        .anyRequest().authenticated()
//                )
//
//                // 🪄 Disable CSRF for H2 console (needed to make POST requests work)
//                .csrf(csrf -> csrf.disable())
//
//                // 🧱 Allow frames (H2 uses iframes)
//                .headers(headers -> headers.frameOptions(frame -> frame.disable()))
//
//                // 💬 Default form login
//                .formLogin(form -> form.defaultSuccessUrl("/", true));
//
//        return http.build();
//    }
//}
