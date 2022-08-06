package com.he1extg.pdfreader.controller.rest

import com.he1extg.pdfreader.exception.StorageFileNotFoundException
import com.he1extg.pdfreader.storage.FileHandler
import org.springframework.core.io.InputStreamResource
import org.springframework.core.io.Resource
import org.springframework.http.HttpHeaders
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import java.io.InputStream

@RestController
class FileOperations(
    val fileHandler: FileHandler
) {
    @GetMapping("/files/{fileName:.+}")
    fun serveFile(@PathVariable fileName: String): ResponseEntity<Resource> {
        val file: Resource = fileHandler.loadAsResource(fileName)
        return ResponseEntity.ok().header(
            HttpHeaders.CONTENT_DISPOSITION,
            "attachment; filename=\"${file.filename}\""
        ).body(file)
    }

    @GetMapping("/files")
    fun getFilesList() {

    }

    @PostMapping("/files")
    fun convertPDFtoMP3(@RequestParam file: MultipartFile): ResponseEntity<Resource> =
        if (!file.isEmpty) {
            val mp3InputStream = fileHandler.convertPDFtoMP3(file)
            ResponseEntity.ok().header(
                HttpHeaders.CONTENT_DISPOSITION,
                ""
            ).body(InputStreamResource(mp3InputStream))
        } else {
            ResponseEntity.noContent().header(
                HttpHeaders.CONTENT_DISPOSITION,
                ""
            ).build()
        }

    @ExceptionHandler(StorageFileNotFoundException::class)
    fun handleStorageFileNotFound(exc: StorageFileNotFoundException?): ResponseEntity<*>? {
        return ResponseEntity.notFound().build<Any>()
    }
}