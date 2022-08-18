package com.he1extg.pdfreader

import com.he1extg.pdfreader.storage.StorageProperties
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication

@SpringBootApplication
@EnableConfigurationProperties(StorageProperties::class)
class PdfReaderApplication

fun main(args: Array<String>) {
	runApplication<PdfReaderApplication>(*args) {
		setAdditionalProfiles("h2database")
	}
}
