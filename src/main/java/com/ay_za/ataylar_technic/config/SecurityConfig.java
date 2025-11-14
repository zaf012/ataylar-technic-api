package com.ay_za.ataylar_technic.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.IpAddressMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource(corsConfigurationSource())) // CORS eklendi
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

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        // React uygulamanızın çalıştığı origin
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:3000"));

        // İzin verilen HTTP metodları
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));

        // İzin verilen header'lar
        configuration.setAllowedHeaders(Arrays.asList("*"));

        // Credentials (çerezler) izni - eğer gerekirse true yapın
        configuration.setAllowCredentials(false);

        // Preflight request cache süresi (saniye)
        configuration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/api/**", configuration);

        return source;
    }
}

