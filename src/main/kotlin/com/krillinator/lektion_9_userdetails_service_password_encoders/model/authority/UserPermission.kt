package com.krillinator.lektion_9_userdetails_service_password_encoders.model.authority

enum class UserPermission(private val content: String) {
    READ("read"),
    WRITE("write"),
    UPDATE("update"),
    DELETE("delete");

    fun getContent(): String {
        return content
    }

}
