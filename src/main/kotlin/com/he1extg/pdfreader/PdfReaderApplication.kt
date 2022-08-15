package com.he1extg.pdfreader

import com.he1extg.pdfreader.storage.StorageProperties
import com.he1extg.pdfreader.storage.StorageHandler
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean

@SpringBootApplication
@EnableConfigurationProperties(StorageProperties::class)
class PdfReaderApplication

fun main(args: Array<String>) {
	runApplication<PdfReaderApplication>(*args)
}
