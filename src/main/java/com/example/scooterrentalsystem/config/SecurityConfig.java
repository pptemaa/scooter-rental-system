package com.example.scooterrentalsystem.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final JwtFilter jwtFilter;

    public SecurityConfig(JwtFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/scooters/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/rental-points/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/tariffs/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/scooter-models/**").permitAll()
                        .requestMatchers("/api/users/**").hasAuthority("MANAGER")
                        .requestMatchers("/api/roles/**").hasAuthority("MANAGER")
                        .requestMatchers(HttpMethod.POST, "/api/scooters/**", "/api/rental-points/**", "/api/tariffs/**", "/api/scooter-models/**").hasAuthority("MANAGER")
                        .requestMatchers(HttpMethod.PUT, "/api/scooters/**", "/api/rental-points/**", "/api/tariffs/**", "/api/scooter-models/**").hasAuthority("MANAGER")
                        .requestMatchers(HttpMethod.PATCH, "/api/scooters/**", "/api/rental-points/**", "/api/tariffs/**", "/api/scooter-models/**").hasAuthority("MANAGER")
                        .requestMatchers(HttpMethod.DELETE, "/api/scooters/**", "/api/rental-points/**", "/api/tariffs/**", "/api/scooter-models/**").hasAuthority("MANAGER")
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
