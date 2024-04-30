
package com.grupor.spoto5.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    @Autowired
    public RepositoryUserDetailsService userDetailsService;

    @Bean
    public PasswordEncoder passwordEncoder() { return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {

        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());

        return authProvider;
    }


    @Bean
    public SecurityFilterChain filterChainWeb(HttpSecurity http) throws Exception {
        http.authenticationProvider(authenticationProvider());

        http
                .authorizeHttpRequests(authorize -> authorize

                    // PUBLIC PAGES
                    .requestMatchers("/", "/album/{id}", "/album/*", "/album/{id}/*", "/css/**", "/login", "/logout", "/loginerror", "/register").permitAll()


                    // PRIVATE PAGES
                    .requestMatchers("/addcomment/*", "/deleteComment/*", "/user").hasAnyRole("USER")
                    .requestMatchers("/newalbum", "/deletealbum/*", "/editalbum/*", "/adminpage").hasAnyRole("ADMIN")
                )

                .formLogin(formLogin -> formLogin
                    .loginPage("/login")
                    .failureUrl("/loginerror")
                    .defaultSuccessUrl("/")
                    .permitAll()
                )

                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/")
                        .permitAll()
                );

// Disable CSRF at the moment
        return http.build();
    }
}
