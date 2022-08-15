package com.he1extg.pdfreader.storage

import org.springframework.boot.ApplicationRunner
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class StorageHandlerServiceFileSystemConfig {

    @Bean
    fun init(storageHandler: StorageHandler) = ApplicationRunner {
        storageHandler.deleteAll()
        storageHandler.init()
    }
}