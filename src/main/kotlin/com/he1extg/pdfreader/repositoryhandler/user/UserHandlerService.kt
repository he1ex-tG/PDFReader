package com.he1extg.pdfreader.repositoryhandler.user

import com.he1extg.pdfreader.entity.User
import com.he1extg.pdfreader.repositoryhandler.RepositoryHandlerBase
import com.he1extg.pdfreader.security.UserRole
import com.he1extg.pdfreader.security.UserStatus
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class UserHandlerService : RepositoryHandlerBase(), UserHandler {

    @Autowired
    lateinit var passwordEncoder: PasswordEncoder

    override fun save(username: String, password: String) {
        println(currentUser)
        val newUser = User(
            username,
            passwordEncoder.encode(password),
            UserRole.USER,
            UserStatus.ACTIVE
        )
        userRepository.save(newUser)
    }
}