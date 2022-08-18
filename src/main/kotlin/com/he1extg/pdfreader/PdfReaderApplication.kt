package com.he1extg.pdfreader

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication


@SpringBootApplication
class PdfReaderApplication

fun main(args: Array<String>) {
	runApplication<PdfReaderApplication>(*args) {
		//setAdditionalProfiles("h2database")
		setAdditionalProfiles("filestorage")
	}
}
