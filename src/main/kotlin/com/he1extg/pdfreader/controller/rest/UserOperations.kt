package com.he1extg.pdfreader.controller.rest

import com.he1extg.pdfreader.repositoryhandler.user.UserHandler
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/users")
class UserOperations {

    @Autowired
    lateinit var userHandler: UserHandler

    /*@GetMapping("")
    fun getUsers(): List<String> {}

    @GetMapping("/ban/{username}")
    fun banUser(@PathVariable username: String) {}

    @DeleteMapping("")
    fun deleteUser(@RequestParam("username") username: String) {}*/

    @PostMapping("")
    fun addUser(@RequestParam username: String, @RequestParam password: String): HttpStatus {
        userHandler.save(username, password)
        return HttpStatus.OK
    }
}