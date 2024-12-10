package com.krillinator.lektion_9_userdetails_service_password_encoders.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.web.SecurityFilterChain

@Configuration
@EnableWebSecurity // Enable Security Override
class AppSecurityConfig {

    // Spring -> find components & objects (component scanning)
    // What would happen if we didn't have component scanning?
    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {

        http
            .authorizeHttpRequests { it
                .requestMatchers("/", "/login", "/logout", "/user").permitAll()
                .requestMatchers("/user/authenticated/admin").hasRole("ADMIN")
                .requestMatchers("/user/authenticated/manager").hasRole("MANAGER")
                .anyRequest().authenticated() // Must Log In
            }
            .formLogin {}

        return http.build()
    }

}