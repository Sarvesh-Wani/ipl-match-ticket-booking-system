package com.coditas.iplmatchticketbookingsystem.config;

import com.coditas.iplmatchticketbookingsystem.OAuth2SuccessHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    @Autowired
    private JWTFilter jwtFilter;

    @Autowired
    private OAuth2SuccessHandler oAuth2SuccessHandler;

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http){

        http.authorizeHttpRequests(
                auth ->
                auth
                        .requestMatchers("/api/v1/users/register","/api/v1/login").permitAll()
                        .requestMatchers("/api/v1/tickets/**").hasRole("USER")
                        .requestMatchers(HttpMethod.GET,"/api/v1/matches/**").hasAnyRole("USER","ADMIN")
                        .requestMatchers("/api/v1/admin/register",
                                "/api/v1/standiums",
                                "/api/v1/matches/**").hasRole("ADMIN")
                        .anyRequest().authenticated()
        );

        http.csrf(csrf -> csrf.disable());
        http.httpBasic(Customizer.withDefaults());

        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        http.oauth2Login(oauth -> oauth.successHandler(oAuth2SuccessHandler));
        return http.build();
    }

    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration authconfig){
        return authconfig.getAuthenticationManager();
    }
}
