package com.krillinator.lektion_9_userdetails_service_password_encoders.controller

import com.krillinator.lektion_9_userdetails_service_password_encoders.config.jwt.JwtUtil
import com.krillinator.lektion_9_userdetails_service_password_encoders.model.CustomUserDetails
import jakarta.servlet.http.Cookie
import jakarta.servlet.http.HttpServletResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.util.concurrent.TimeUnit

@RestController
class AuthenticationController @Autowired constructor(
    val authenticationManager: AuthenticationManager,
    val jwtUtil: JwtUtil
) {

    @PostMapping("/login")
    fun authenticateUser(
        @RequestParam username: String?,
        @RequestParam password: String?,
        response: HttpServletResponse
    ): ResponseEntity<String> {
        try {
            // Authenticate user using AuthenticationManager
            val authenticationToken = UsernamePasswordAuthenticationToken(username, password)
            val authentication: Authentication = authenticationManager.authenticate(authenticationToken) // This triggers the CustomUserDetailsService

            // IMPORTANT - MUST BE THE SAME TYPE AS RETURNED WITHIN - CUSTOM_USER_DETAILS_SERVICE CLASS
            val customUser = authentication.principal

            // Type-check
            if (customUser is CustomUserDetails) {
                println("-------------------")
                println(customUser.authorities.toString())
                println("-------------------")
                val token: String = jwtUtil.generateJwtToken(
                    customUser.username,
                    customUser.authorities.toString()
                )

                // Prepare Cookie
                val cookie: Cookie = Cookie("authToken", token)
                cookie.isHttpOnly = true // No JS (prevent XSS)
                cookie.secure = false // HTTPS
                cookie.path = "/" // Available to whole App
                cookie.maxAge = TimeUnit.HOURS.toSeconds(1).toInt()
                response.addCookie(cookie)

                println(cookie)

                return ResponseEntity.ok(token)
            } else {
                return ResponseEntity.internalServerError()
                    .body("Authenticated principal is not of type CustomUserDetails")
            }
        } catch (e: BadCredentialsException) {
            // Important - This can't be caught by global exception handler - handle manually

            return ResponseEntity.status(401).body("Bad Credentials..")
        }
    }

}