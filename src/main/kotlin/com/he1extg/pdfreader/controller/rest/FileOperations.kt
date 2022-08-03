package com.he1extg.pdfreader.controller.rest

import com.he1extg.pdfreader.exception.StorageFileNotFoundException
import com.he1extg.pdfreader.storage.FileHandler
import org.springframework.core.io.Resource
import org.springframework.http.HttpHeaders
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
class FileOperations(
    val fileHandler: FileHandler
) {
    @GetMapping("/files/{fileName:.+}")
    @ResponseBody
    fun serveFile(@PathVariable fileName: String): ResponseEntity<Resource> {
        val file: Resource = fileHandler.loadAsResource(fileName)
        return ResponseEntity.ok().header(
            HttpHeaders.CONTENT_DISPOSITION,
            "attachment; filename=\"${file.filename}\""
        ).body(file)
    }

    @ExceptionHandler(StorageFileNotFoundException::class)
    fun handleStorageFileNotFound(exc: StorageFileNotFoundException?): ResponseEntity<*>? {
        return ResponseEntity.notFound().build<Any>()
    }
}