package com.krillinator.lektion_9_userdetails_service_password_encoders.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder

@Configuration
class BCryptPasswordEncoderConfig {

    @Bean
    fun newPasswordEncoder(): BCryptPasswordEncoder {
        return BCryptPasswordEncoder(15) // 4-31 (weaker = faster)
    }

}