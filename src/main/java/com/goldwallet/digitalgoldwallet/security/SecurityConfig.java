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

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    // 🔐 PASSWORD ENCODER
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // 👤 USERS (TEAM MEMBERS)
    @Bean
    public UserDetailsService userDetailsService() {

        PasswordEncoder encoder = passwordEncoder();

        return new InMemoryUserDetailsManager(

                User.builder()
                        .username("pavithra")
                        .password(encoder.encode("pavi123"))
                        .roles("USER")
                        .build(),

                User.builder()
                        .username("caitlyn")
                        .password(encoder.encode("cait123"))
                        .roles("VENDOR")
                        .build(),

                User.builder()
                        .username("bavan")
                        .password(encoder.encode("bavan123"))
                        .roles("PAYMENT")
                        .build(),

                User.builder()
                        .username("suba")
                        .password(encoder.encode("suba123"))
                        .roles("GOLD")
                        .build(),

                User.builder()
                        .username("transaction")
                        .password(encoder.encode("trans123"))
                        .roles("TRANSACTION")
                        .build()
        );
    }

    // 🚀 SECURITY RULES
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .csrf(AbstractHttpConfigurer::disable)

                .authorizeHttpRequests(auth -> auth

                        // 🔐 Swagger (secured)
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").authenticated()

                        // ==================================================
                        // 🔥 SPECIFIC RULES FIRST (VERY IMPORTANT)
                        // ==================================================

                        // 💰 PAYMENT MODULE
                        .requestMatchers("/api/v1/users/*/payments").hasRole("PAYMENT")
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
                        .requestMatchers("/api/v1/users/**").hasRole("USER")
                        .requestMatchers("/api/v1/addresses/**").hasRole("USER")

                        // 🏪 VENDOR MODULE
                        .requestMatchers("/api/v1/vendors/*/branches/**").hasRole("VENDOR")
                        .requestMatchers("/api/v1/vendors/**").hasRole("VENDOR")
                        .requestMatchers("/api/v1/branches/**").hasRole("VENDOR")

                        // 🔒 ANY OTHER REQUEST
                        .anyRequest().authenticated()
                )

                // ✅ FIXED (no deprecated warning)
                .httpBasic(httpBasic -> {});

        return http.build();
    }
}