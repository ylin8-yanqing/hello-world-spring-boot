package com.example.helloworld.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.header.writers.ReferrerPolicyHeaderWriter;

/**
 * Baseline security configuration (YSJP-214 / HLSA §6).
 *
 * <p>Controls:
 * <ul>
 *   <li>Security response headers (X-Content-Type-Options, X-Frame-Options,
 *       Strict-Transport-Security, Content-Security-Policy, Referrer-Policy)</li>
 *   <li>Permit GET /hello without authentication (demo-grade, no auth required)</li>
 *   <li>Disable CSRF (stateless REST API)</li>
 *   <li>Deny all other requests by default (least-privilege default)</li>
 * </ul>
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            // YSJP-214: Disable CSRF – REST API is stateless
            .csrf(AbstractHttpConfigurer::disable)

            // YSJP-214: Security headers
            .headers(headers -> headers
                // Prevent MIME-type sniffing
                .contentTypeOptions(ct -> {})
                // Prevent clickjacking
                .frameOptions(fo -> fo.deny())
                // HSTS – enforce HTTPS in deployed environments
                .httpStrictTransportSecurity(hsts -> hsts
                    .includeSubDomains(true)
                    .maxAgeInSeconds(31536000))
                // Referrer policy
                .referrerPolicy(rp -> rp
                    .policy(ReferrerPolicyHeaderWriter.ReferrerPolicy.NO_REFERRER))
                // Content Security Policy – restrict sources (demo-grade)
                .contentSecurityPolicy(csp -> csp
                    .policyDirectives("default-src 'none'; frame-ancestors 'none'"))
            )

            // YSJP-214: Permit /hello; deny everything else by default
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/hello", "/health").permitAll()
                .anyRequest().denyAll()
            );

        return http.build();
    }
}
