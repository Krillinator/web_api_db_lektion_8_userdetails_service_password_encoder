package com.krillinator.lektion_9_userdetails_service_password_encoders.controller

import com.krillinator.lektion_9_userdetails_service_password_encoders.config.jwt.JwtUtil
import com.krillinator.lektion_9_userdetails_service_password_encoders.model.CustomUserDetails
import jakarta.servlet.http.Cookie
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpHeaders
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.util.concurrent.TimeUnit


@RestController
class AuthenticationController (
    val authenticationManager: AuthenticationManager,
    val jwtUtil: JwtUtil
) {

    @GetMapping("/header")
    fun testHeader(
        request: HttpServletRequest
    ): ResponseEntity<String> {

        println("---DEBUGGING---")
        // LOCAL
        println("---LOCAL---")
        println(request.localAddr)
        println(request.localName)
        println(request.localPort)
        // REMOTE
        println("---REMOTE---")
        println(request.remoteAddr)
        println(request.remoteHost)
        println(request.remotePort)
        println("---OTHER---")
        println(request.cookies.map { it.name })
        println(request.isRequestedSessionIdValid)
        println(request.isRequestedSessionIdFromURL)
        println(request.isRequestedSessionIdFromCookie)


        val headers = HttpHeaders()
        headers.set("myTest", "JWT")

        return ResponseEntity.ok()
            .headers(headers)
            .body("Testing ResponseEntity myTest")
    }

    // Authentication
    @PostMapping("/login")
    fun authenticateUser(
        @RequestParam username: String?,
        @RequestParam password: String?,
        response: HttpServletResponse
    ): ResponseEntity<String> {
        try {
            // Authenticate user using AuthenticationManager
            val authenticationToken = UsernamePasswordAuthenticationToken(username, password) // UserDetailsService
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
                val cookie = Cookie("authToken", token)
                cookie.isHttpOnly = true // No JS (prevent XSS)
                cookie.secure = false // HTTPS
                cookie.path = "/" // Available to whole App
                cookie.maxAge = TimeUnit.HOURS.toSeconds(1).toInt()
                response.addCookie(cookie)

                println(cookie)

                return ResponseEntity.ok("Login Succesful")
            } else {
                return ResponseEntity.internalServerError()
                    .body("Authenticated principal is not of type CustomUserDetails")
            }
        } catch (e: BadCredentialsException) {
            // Important - This can't be caught by global exception handler - handle manually

            return ResponseEntity.status(401).body("Bad Credentials..")
        }
    }

    @GetMapping("/who-am-i")
    fun checkedLoggedInUser(request: HttpServletRequest): ResponseEntity<String> {
        println("Headers received:")
        request.headerNames.asIterator().forEachRemaining { headerName: String ->
            println(
                "$headerName: " + request.getHeader(
                    headerName
                )
            )
        }

        val authentication = SecurityContextHolder.getContext().authentication
        println("---who-am-i---")
        println(authentication)

        // Check if the user is authenticated (it won't be null if the filter worked correctly)
        if (authentication != null && authentication.isAuthenticated) {
            // Extract the username (or any other user details from the authentication object)
            println(authentication.authorities)
            val username = authentication.name // The username is typically stored as the principal
            println(username)
            return ResponseEntity.ok("Logged in user: " + username + authentication.authorities)
        } else {
            return ResponseEntity.status(401).body("User is not authenticated")
        }
    }


}