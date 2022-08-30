package com.he1extg.pdfreader.controller.rest

import com.he1extg.pdfreader.entity.User
import com.he1extg.pdfreader.repository.UserRepository
import com.he1extg.pdfreader.security.UserRole
import com.he1extg.pdfreader.security.UserStatus
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/users")
class UserOperations(
    val userRepository: UserRepository
) {

    @GetMapping("")
    fun getUsers(): List<String> {
        return userRepository.findAll().map { it.login }
    }

    @GetMapping("/ban/{username}")
    fun banUser(@PathVariable username: String) {

    }

    @DeleteMapping("/{username}")
    fun deleteUser(@PathVariable username: String) {

    }

    @PostMapping("")
    fun addUser(@RequestParam username: String, @RequestParam password: String) {
        val newUser = User(
            username,
            password,
            UserRole.USER,
            UserStatus.ACTIVE
        )
        userRepository.save(newUser)
    }
}