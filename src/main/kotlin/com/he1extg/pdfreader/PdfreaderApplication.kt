package com.he1extg.pdfreader

import com.he1extg.pdfreader.storage.StorageProperties
import com.he1extg.pdfreader.storage.StorageHandler
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean

@SpringBootApplication
@EnableConfigurationProperties(StorageProperties::class)
class PdfreaderApplication {
	@Bean
	fun init(storageHandler: StorageHandler): CommandLineRunner = CommandLineRunner {
		storageHandler.deleteAll()
		storageHandler.init()
	}
}

fun main(args: Array<String>) {
	runApplication<PdfreaderApplication>(*args)
}
