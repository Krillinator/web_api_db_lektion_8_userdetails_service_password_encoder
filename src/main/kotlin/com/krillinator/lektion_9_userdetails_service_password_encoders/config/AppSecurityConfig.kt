package com.krillinator.lektion_9_userdetails_service_password_encoders.config

import com.krillinator.lektion_9_userdetails_service_password_encoders.config.jwt.JwtAuthenticationFilter
import com.krillinator.lektion_9_userdetails_service_password_encoders.model.CustomUserDetailsService
import com.krillinator.lektion_9_userdetails_service_password_encoders.model.authority.UserPermission
import com.krillinator.lektion_9_userdetails_service_password_encoders.model.authority.UserRole.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.dao.DaoAuthenticationProvider
import org.springframework.security.config.Customizer
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configurers.CorsConfigurer
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter


@Configuration
@EnableWebSecurity // Enable Security Override
class AppSecurityConfig @Autowired constructor(
     val passwordEncoder: PasswordEncoder,
     val customUserDetailsService: CustomUserDetailsService,
     val jwtAuthenticationFilter: JwtAuthenticationFilter
) {

    // Spring -> find components & objects (component scanning)
    // What would happen if we didn't have component scanning?
    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {

        http
            .csrf { it.disable() } // Disable CSRF protection (During Debugging Only)
            .cors { Customizer.withDefaults<CorsConfigurer<HttpSecurity>>() } // TODO - Not enabled correctly
            .authorizeHttpRequests { it
                .requestMatchers("/", "/login", "/logout", "/user", "/user/password", "/who-am-i").permitAll()
                .requestMatchers("/user/admin").hasRole(ADMIN.name) // UserRole.ADMIN.name
                .requestMatchers("/user/user").hasRole(USER.name)
                .requestMatchers("/user/read").hasAnyAuthority(UserPermission.READ.getContent())
                .anyRequest().authenticated() // Must Log In
            }

            // .authenticationProvider(customDaoAuthenticationProvider())
            .sessionManagement { it.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter::class.java) // on each request

        return http.build()
    }

    // Spring, use our custom implementation (Explicit)

    @Bean
    @Throws(Exception::class)
    fun authenticationManager(http: HttpSecurity): AuthenticationManager {
        val daoAuthenticationProvider = DaoAuthenticationProvider()
        daoAuthenticationProvider.setUserDetailsService(customUserDetailsService)
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder)

        val authenticationManagerBuilder = http.getSharedObject(
            AuthenticationManagerBuilder::class.java
        )
        authenticationManagerBuilder.authenticationProvider(daoAuthenticationProvider)

        return authenticationManagerBuilder.build()
    }

}