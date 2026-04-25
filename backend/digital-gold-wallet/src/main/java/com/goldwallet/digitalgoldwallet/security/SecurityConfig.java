package com.goldwallet.digitalgoldwallet.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    // 🔐 PASSWORD ENCODER
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // 🌐 CORS — allow Angular dev server
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of("http://localhost:4200"));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));
        config.setAllowedHeaders(List.of("*"));
        config.setExposedHeaders(List.of("Authorization"));
        config.setAllowCredentials(true);
        config.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    // 👤 USERS (TEAM MEMBERS) — aligned with frontend credentials
    @Bean
    public UserDetailsService userDetailsService() {

        PasswordEncoder encoder = passwordEncoder();

        return new InMemoryUserDetailsManager(

                // 🔑 Admin — access to all modules
                User.builder()
                        .username("admin")
                        .password(encoder.encode("admin123"))
                        .roles("USER", "VENDOR", "GOLD", "PAYMENT", "TRANSACTION")
                        .build(),

                // 👤 User module — Pavithra
                User.builder()
                        .username("pavithra")
                        .password(encoder.encode("pavi123"))
                        .roles("USER")
                        .build(),

                // 🏪 Vendor module — Caitlyn
                User.builder()
                        .username("caitlyn")
                        .password(encoder.encode("cait123"))
                        .roles("VENDOR")
                        .build(),

                // 💰 Payment + Wallet module — Bavanagaran
                User.builder()
                        .username("bavanagaran")
                        .password(encoder.encode("bavan123"))
                        .roles("PAYMENT")
                        .build(),

                // 🪙 Gold module — Suba
                User.builder()
                        .username("suba")
                        .password(encoder.encode("suba123"))
                        .roles("GOLD")
                        .build(),

                // 📜 Transaction module — Mirudhula
                User.builder()
                        .username("mirudhula")
                        .password(encoder.encode("mirudhula123"))
                        .roles("TRANSACTION")
                        .build()
        );
    }

    // 🚀 SECURITY RULES
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))

                .authorizeHttpRequests(auth -> auth

                        // 🔐 Swagger (secured)
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").authenticated()

                        // ==================================================
                        // 🔥 SPECIFIC RULES FIRST (VERY IMPORTANT)
                        // ==================================================

                        // 💰 PAYMENT MODULE
                        .requestMatchers("/api/v1/users/*/payments").hasAnyRole("PAYMENT", "USER", "VENDOR", "GOLD", "TRANSACTION")
                        .requestMatchers("/api/v1/payments/**").hasRole("PAYMENT")
                        .requestMatchers("/api/v1/wallets/**").hasRole("PAYMENT")

                        // 🪙 GOLD MODULE
                        .requestMatchers("/api/v1/users/*/gold/**").hasRole("GOLD")
                        .requestMatchers("/api/v1/branches/*/gold/**").hasRole("GOLD")
                        .requestMatchers("/api/v1/gold/**").hasRole("GOLD")

                        // 📜 TRANSACTION MODULE
                        .requestMatchers("/api/v1/users/*/transactions").hasRole("TRANSACTION")
                        .requestMatchers("/api/v1/branches/*/transactions").hasRole("TRANSACTION")
                        .requestMatchers("/api/v1/transactions/**").hasRole("TRANSACTION")

                        // ==================================================
                        // 🧩 GENERAL RULES (AFTER SPECIFIC)
                        // ==================================================

                        // 👤 USER MODULE
                        .requestMatchers("/api/v1/users/**").hasAnyRole("USER", "PAYMENT", "GOLD", "TRANSACTION")
                        .requestMatchers("/api/v1/addresses/**").hasRole("USER")

                        // 🏪 VENDOR MODULE
                        .requestMatchers("/api/v1/vendors/*/branches/**").hasRole("VENDOR")
                        .requestMatchers("/api/v1/vendors/**").hasRole("VENDOR")
                        .requestMatchers("/api/v1/branches/**").hasRole("VENDOR")

                        // 🔒 AUTH PING
                        .requestMatchers("/api/v1/auth/ping").authenticated()

                        // 🔒 ANY OTHER REQUEST
                        .anyRequest().authenticated()
                )

                // ✅ HTTP Basic (no deprecated warning)
                .httpBasic(httpBasic -> {});

        return http.build();
    }
}