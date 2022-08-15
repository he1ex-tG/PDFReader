package com.he1extg.pdfreader.storage

import com.he1extg.pdfreader.entity.User
import com.he1extg.pdfreader.repository.UserRepository
import org.springframework.boot.ApplicationRunner
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class StorageHandlerServiceH2Config {

    @Bean
    fun databaseInit(userRepository: UserRepository) = ApplicationRunner {
        val userAdmin = User("admin", "admin")
        userRepository.save(userAdmin)
    }
}