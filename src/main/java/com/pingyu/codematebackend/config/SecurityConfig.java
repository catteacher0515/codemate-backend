package com.pingyu.codematebackend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity; // 确保这行在
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity // 确保这个注解在
public class SecurityConfig {

    @Bean // 确保这个注解在
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // 禁用 CSRF
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().permitAll() // 【核心】允许所有请求
                );
        return http.build();
    }
}