package com.krillinator.lektion_9_userdetails_service_password_encoders.model.authority

import com.krillinator.lektion_9_userdetails_service_password_encoders.model.authority.UserPermission.*
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority

enum class UserRole(private val permissions: List<UserPermission>) {
    USER(listOf(READ)),
    ADMIN(listOf(
        READ,
        WRITE,
        UPDATE,
        DELETE
    ));

    // TODO - Does this return strings OR address
    fun getPermissions(): List<UserPermission> {
        return permissions
    }

    fun getAuthorities(): MutableCollection<GrantedAuthority> {

        // TODO:
        //  ROLE_ + this.name (ADMIN) = ROLE_ADMIN
        //  List<Permissions> = "", "", "" , "" (instead of list of strings)
        //  ONE array with both: ROLES + PERMISSIONS

        val permissions: List<GrantedAuthority> = this.getPermissions().map { SimpleGrantedAuthority(it.getContent()) }
        val authorities: MutableCollection<GrantedAuthority> = mutableListOf(SimpleGrantedAuthority("ROLE_" + this.name))

        // [READ, WRITE, UPDATE, DELETE] - convert to strings
        permissions.map { element -> authorities.add(element) }

        return authorities
    }

}