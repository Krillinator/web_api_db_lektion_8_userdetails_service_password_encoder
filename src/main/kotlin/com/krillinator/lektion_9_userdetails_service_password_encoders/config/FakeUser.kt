package com.krillinator.lektion_9_userdetails_service_password_encoders.config

import org.springframework.context.annotation.Bean
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.provisioning.InMemoryUserDetailsManager
import org.springframework.stereotype.Component

@Component
class FakeUser {

    @Bean
    fun createFakeUser(): InMemoryUserDetailsManager {

        val benny: UserDetails = User.withDefaultPasswordEncoder()
            .username("Benny")
            .password("123")
            .roles("ADMIN") // Automatic prefix: ROLE_ADMIN
            .build()

        val frida: UserDetails = User.withDefaultPasswordEncoder()
            .username("Frida")
            .password("321")
            .roles("MANAGER")
            .build()

        return InMemoryUserDetailsManager(benny, frida)

    }

}