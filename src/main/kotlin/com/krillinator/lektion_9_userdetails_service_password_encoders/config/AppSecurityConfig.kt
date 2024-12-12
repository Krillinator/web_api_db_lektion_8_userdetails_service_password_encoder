package com.krillinator.lektion_9_userdetails_service_password_encoders.config

import com.krillinator.lektion_9_userdetails_service_password_encoders.model.CustomUserDetailsService
import com.krillinator.lektion_9_userdetails_service_password_encoders.model.authority.UserPermission
import com.krillinator.lektion_9_userdetails_service_password_encoders.model.authority.UserRole.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.dao.DaoAuthenticationProvider
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain

@Configuration
@EnableWebSecurity // Enable Security Override
class AppSecurityConfig(
    @Autowired val passwordEncoder: PasswordEncoder,
    @Autowired val customUserDetailsService: CustomUserDetailsService
) {

    // Spring -> find components & objects (component scanning)
    // What would happen if we didn't have component scanning?
    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {

        http
            .csrf { it.disable() } // Disable CSRF protection (During Debugging Only)
            .authorizeHttpRequests { it
                .requestMatchers("/", "/login", "/logout", "/user", "/user/password").permitAll()
                .requestMatchers("/user/admin").hasRole(ADMIN.name) // UserRole.ADMIN.name
                .requestMatchers("/user/user").hasRole(USER.name)
                .requestMatchers("/user/read").hasAnyAuthority(UserPermission.READ.getContent())
                .anyRequest().authenticated() // Must Log In
            }
            .formLogin {}
            // .authenticationProvider(customDaoAuthenticationProvider())

        return http.build()
    }

    // Spring, use our custom implementation (Explicit)
    @Bean
    fun customDaoAuthenticationProvider(): DaoAuthenticationProvider {
        val dao = DaoAuthenticationProvider()
        dao.setPasswordEncoder(passwordEncoder)
        dao.setUserDetailsService(customUserDetailsService)

        return dao
    }

}