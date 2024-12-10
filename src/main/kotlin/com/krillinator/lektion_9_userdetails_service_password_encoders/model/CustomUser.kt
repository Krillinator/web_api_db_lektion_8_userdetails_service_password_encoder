package com.krillinator.lektion_9_userdetails_service_password_encoders.model

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id

@Entity
class CustomUser(
    var username: String,
    var password: String,

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id: Long? = null
) {
}

