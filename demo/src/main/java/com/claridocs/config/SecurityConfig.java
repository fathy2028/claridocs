package com.claridocs.config;

import com.claridocs.security.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    private final CustomUserDetailsService userDetailsService;

    public SecurityConfig(CustomUserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    // @Bean
    // public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    //     http
    //             .csrf(csrf -> csrf.disable())
    //             .authorizeHttpRequests(auth -> auth
    //                     .anyRequest().permitAll() 
    //             )
    //             .formLogin(form -> form.disable())
    //             .httpBasic(basic -> basic.disable());

    //     return http.build();
    // }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http
    .csrf(csrf -> csrf.disable()) // disable CSRF for REST
    .authorizeHttpRequests(auth -> auth
    .requestMatchers("/login", "/logout", "/error").permitAll()
    .requestMatchers("/departments/**", "/employees/**",
    "/admin/**").hasRole("ADMIN")
    .requestMatchers("/documents/**").hasAnyRole("ADMIN", "EMPLOYEE")
    .anyRequest().authenticated()
    )
    .formLogin(form -> form.disable()) // disable default login form
    .httpBasic(basic -> basic.disable()); // disable HTTP Basic auth

    return http.build();
    }
}
