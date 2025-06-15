package com.subash.fund.management.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Security configuration class for the application.
 * <p>
 * Sets up HTTP security rules, authentication manager, and password encoder.
 * Uses HTTP Basic authentication and role-based access control.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    /**
     * Defines the password encoder bean used for encoding and validating passwords.
     * BCrypt is used for secure one-way password hashing.
     *
     * @return {@link PasswordEncoder} instance using BCrypt
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Configures the security filter chain to enforce endpoint access rules and authentication.
     *
     * <p>Details of configuration:
     * <ul>
     *   <li>Disables CSRF protection (not needed for REST APIs)</li>
     *   <li>Allows unrestricted access to Swagger/OpenAPI documentation</li>
     *   <li>Restricts fund management endpoints to ADMIN users</li>
     *   <li>Restricts fund order placement to USER role</li>
     *   <li>Requires authentication for all other requests</li>
     * </ul>
     *
     * @param http the {@link HttpSecurity} object provided by Spring Security
     * @return configured {@link SecurityFilterChain}
     * @throws Exception if there is any error during configuration
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/v3/api-docs/**",
                                "/swagger-ui/**",
                                "/swagger-ui.html").permitAll()
                        .requestMatchers(HttpMethod.POST, "/v1/api/funds").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/v1/api/funds").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/v1/api/funds/order").hasRole("USER")
                        .anyRequest().authenticated()
                )
                .httpBasic(Customizer.withDefaults());

        return http.build();
    }


    /**
     * Provides the {@link AuthenticationManager} bean used for authenticating credentials.
     * Delegates to Spring Security’s {@link AuthenticationConfiguration}.
     *
     * @param authConfig the {@link AuthenticationConfiguration} instance
     * @return the configured {@link AuthenticationManager}
     * @throws Exception if an error occurs while retrieving the authentication manager
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }
}
