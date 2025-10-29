package com.ay_za.ataylar_technic.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.IpAddressMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/**").access((authentication, context) -> {
                    IpAddressMatcher ipMatcher = new IpAddressMatcher("127.0.0.1");
                    boolean granted = ipMatcher.matches(context.getRequest());
                    return new org.springframework.security.authorization.AuthorizationDecision(granted);
                })
                .anyRequest().permitAll()
            );
        return http.build();
    }
}

