package com.krillinator.lektion_9_userdetails_service_password_encoders.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/user")
class UserController {

    @GetMapping
    fun nonUserTest(): String {

        return "Did it work? Can non-users access this endpoint?"
    }

    @GetMapping("/authenticated")
    fun userTest(): String {

        return "Did it work? Can USERS access this endpoint?"
    }

    @GetMapping("/authenticated/manager")
    fun userTestManager(): String {

        return "Did it work? Can MANAGER access this endpoint?"
    }

    @GetMapping("/authenticated/admin")
    fun userTestAdmin(): String {

        return "Did it work? Can ADMIN access this endpoint?"
    }

}