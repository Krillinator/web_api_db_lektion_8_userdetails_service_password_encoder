package com.krillinator.lektion_9_userdetails_service_password_encoders.repository

import com.krillinator.lektion_9_userdetails_service_password_encoders.model.CustomUser
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.util.Optional

@Repository
interface CustomUserRepository: JpaRepository<CustomUser, Long> {

    // Custom Query - Automatically convert to a QUERY (find by username in user table)
    fun findByUsername(username: String): Optional<CustomUser> // Abstract Method

}