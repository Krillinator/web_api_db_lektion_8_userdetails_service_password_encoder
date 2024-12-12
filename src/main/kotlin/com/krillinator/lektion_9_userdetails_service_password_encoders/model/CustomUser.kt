package com.krillinator.lektion_9_userdetails_service_password_encoders.model

import com.krillinator.lektion_9_userdetails_service_password_encoders.model.authority.UserRole
import jakarta.persistence.*

@Entity
class CustomUser(
    var username: String,
    var password: String,

    @Enumerated(value = EnumType.STRING)
    var role: UserRole,

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id: Long? = null
) {
}

