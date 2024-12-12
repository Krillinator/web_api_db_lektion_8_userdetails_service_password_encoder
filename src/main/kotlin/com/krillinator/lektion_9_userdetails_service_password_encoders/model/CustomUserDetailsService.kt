package com.krillinator.lektion_9_userdetails_service_password_encoders.model

import com.krillinator.lektion_9_userdetails_service_password_encoders.repository.CustomUserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import java.lang.NullPointerException
import java.util.Optional

// Override - Spring's way of authenticating user
@Service
class CustomUserDetailsService(
    @Autowired val customUserRepository: CustomUserRepository
): UserDetailsService{

    // Spring's way of finding user (authentication)
    override fun loadUserByUsername(username: String?): UserDetails {
        if (username == null) { throw NullPointerException("Username is null") }

        // OUR DB
        val optionalUser: Optional<CustomUser> = customUserRepository.findByUsername(username) // Optional<CustomUser> == Entity

        if (optionalUser.isPresent) {
            println("username was found!")
            val user = optionalUser.get() // CustomUser ENTITY (NOT OPTIONAL ANYMORE)

            return CustomUserDetails(
                user.username,
                user.password,
                user.role.getAuthorities() // [ROLE + PERMISSIONS]
            )
        } else {
            throw UsernameNotFoundException("Username wasn't found")
        }

    }

}

