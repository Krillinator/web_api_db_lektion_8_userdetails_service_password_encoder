package com.krillinator.lektion_9_userdetails_service_password_encoders.model

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

// Exists only for: Separation of Concerns (Closely Tied to Entity + CustomUserDetailsService)
class CustomUserDetails(
    private var username: String = "",
    private var password: String = "",
    private var authorities: MutableCollection<GrantedAuthority> = mutableListOf()
): UserDetails{

    override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
        return authorities
    }

    override fun getPassword(): String {
        return password
    }

    override fun getUsername(): String {
        return username
    }

}
