package com.grupor.spoto5.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
@EnableWebSecurity
public class RestSecurityConfig {

    @Autowired
    public RepositoryUserDetailsService userDetailsService;

    @Autowired
    public DaoAuthenticationProvider authenticationProvider;



    @Bean
    public SecurityFilterChain filterChainRest(HttpSecurity http) throws Exception {

        http.authenticationProvider(authenticationProvider);

        http.authorizeHttpRequests(authorize -> authorize
            // PRIVATE ENDPOINTS
                .requestMatchers(HttpMethod.GET,"/api/users").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PUT,"/api/books/**").hasRole("USER")
                .requestMatchers(HttpMethod.DELETE,"/api/books/**").hasRole("ADMIN")
            // PUBLIC ENDPOINTS
            .anyRequest().permitAll()
        );


        // Disable Form login Authentication
        http.formLogin(formLogin -> formLogin.disable());

        // Disable CSRF protection (it is difficult to implement in REST APIs)
        http.csrf(csrf -> csrf.disable());
        // Enable Basic Authentication
        http.httpBasic(Customizer.withDefaults());

        http.sessionManagement(management -> management.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        return http.build();
    }
}