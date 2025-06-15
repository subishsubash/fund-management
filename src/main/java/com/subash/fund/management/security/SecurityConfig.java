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
     * Defines the password encoder bean using BCrypt.
     *
     * @return the password encoder instance
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Configures HTTP security for the application.
     * <ul>
     *     <li>Allows user registration without authentication</li>
     *     <li>Restricts DELETE and GET (all users) access to ADMIN role only</li>
     *     <li>Requires authentication for all other endpoints</li>
     * </ul>
     * Uses HTTP Basic for authentication and disables CSRF for simplicity (typically used for stateless APIs).
     *
     * @param http the HTTP security configuration
     * @return configured security filter chain
     * @throws Exception if the configuration fails
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
                        .requestMatchers(HttpMethod.DELETE, "/v1/api/funds").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/v1/api/funds").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/v1/api/funds").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/v1/api/funds/order/buy").hasRole("USER")
                        .requestMatchers(HttpMethod.POST, "/v1/api/funds/order/sell").hasRole("USER")
                        .anyRequest().authenticated()
                )
                .httpBasic(Customizer.withDefaults());

        return http.build();
    }

    /**
     * Provides the {@link AuthenticationManager} bean from the Spring Security context.
     * Used to process authentication requests.
     *
     * @param authConfig the authentication configuration
     * @return the authentication manager
     * @throws Exception if retrieval fails
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }
}
