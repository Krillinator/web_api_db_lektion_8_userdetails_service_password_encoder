package com.krillinator.lektion_9_userdetails_service_password_encoders

import com.krillinator.lektion_9_userdetails_service_password_encoders.model.authority.UserRole
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class Lektion9UserdetailsServicePasswordEncodersApplication

fun main(args: Array<String>) {
	runApplication<Lektion9UserdetailsServicePasswordEncodersApplication>(*args)

	println(UserRole.USER.getPermissions().map { index -> index.getContent() })
	println(UserRole.ADMIN.getPermissions().map { index -> index.getContent() })

	println(UserRole.ADMIN.getAuthorities())

}
