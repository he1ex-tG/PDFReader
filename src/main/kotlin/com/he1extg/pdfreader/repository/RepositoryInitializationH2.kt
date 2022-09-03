package com.he1extg.pdfreader.repository

import com.he1extg.pdfreader.entity.User
import com.he1extg.pdfreader.repositoryhandler.RepositoryHandlerBase
import com.he1extg.pdfreader.security.UserRole
import com.he1extg.pdfreader.security.UserStatus
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile

@Configuration
@Profile("h2database")
class RepositoryInitializationH2() {

    @Autowired
    lateinit var handlerBase: RepositoryHandlerBase

    init {
        userRepositoryInit()
    }

    private fun userRepositoryInit() {
        handlerBase.userRepository.save(
            User(
                "admin",
                /*admin*/"{bcrypt}\$2a\$12\$L9iYxslNXJ7/PsVUX3QJ/.BXd8k6FroGd38A4dBY2Oe/bSjvVbF2a",
                UserRole.ADMIN,
                UserStatus.ACTIVE
            )
        )
        handlerBase.userRepository.save(
            User(
                "guest",
                "",
                UserRole.USER,
                UserStatus.ACTIVE
            )
        )
    }
}