package org.itone.trello.taskservice.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
@Configuration
public class SecurityConfiguration {
    //It will disable auth to any incoming request, that comes with spring security dependency by default
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return web -> web.ignoring()
                .anyRequest();
    }
}
