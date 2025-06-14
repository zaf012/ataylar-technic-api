package com.ay_za.ataylar_technic.config;


import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Ataylar Technic API")
                        .description("Ataylar Technic uygulaması için REST API dokümantasyonu")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Ataylar Technic")
                                .email("akglzfr@gmail.com")
                                .url("https://www.ataylar-technic.com"))
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT")))
                .servers(List.of(
                        new Server()
                                .url("http://localhost:8080")
                                .description("Development Server"),
                        new Server()
                                .url("https://api.ataylar-technic.com")
                                .description("Production Server")
                ));
    }
}
