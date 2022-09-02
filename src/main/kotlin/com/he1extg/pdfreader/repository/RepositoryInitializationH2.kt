package com.he1extg.pdfreader.repository

import com.he1extg.pdfreader.entity.User
import com.he1extg.pdfreader.security.UserRole
import com.he1extg.pdfreader.security.UserStatus
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile

@Configuration
@Profile("h2database")
class RepositoryInitializationH2(
    val userRepository: UserRepository
) {

    init {
        userRepositoryInit()
    }

    private fun userRepositoryInit() {
        userRepository.save(
            User(
                "admin",
                /*admin*/"{bcrypt}\$2a\$12\$L9iYxslNXJ7/PsVUX3QJ/.BXd8k6FroGd38A4dBY2Oe/bSjvVbF2a",
                UserRole.ADMIN,
                UserStatus.ACTIVE
            )
        )
        userRepository.save(
            User(
                "anonymousUser",
                "",
                UserRole.USER,
                UserStatus.ACTIVE
            )
        )
    }
}