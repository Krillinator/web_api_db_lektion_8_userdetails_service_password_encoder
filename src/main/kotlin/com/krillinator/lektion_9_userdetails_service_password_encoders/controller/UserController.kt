package com.krillinator.lektion_9_userdetails_service_password_encoders.controller

import com.krillinator.lektion_9_userdetails_service_password_encoders.model.CustomUser
import com.krillinator.lektion_9_userdetails_service_password_encoders.repository.CustomUserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/user")
class UserController(
    @Autowired val customUserRepository: CustomUserRepository,
    @Autowired val passwordEncoder: PasswordEncoder
) {

    // TODO - User Validation
    @PostMapping
    fun saveUserTest(@RequestBody customUser: CustomUser): String {

        customUser.password = passwordEncoder.encode(customUser.password) // Convert to Bcrypt
        customUserRepository.save(customUser) // !bcrypt

        return "SAVING USER"
    }

}