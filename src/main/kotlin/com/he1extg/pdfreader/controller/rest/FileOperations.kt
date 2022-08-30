package com.he1extg.pdfreader.controller.rest

import com.he1extg.pdfreader.exception.StorageFileNotFoundException
import com.he1extg.pdfreader.storage.StorageHandler
import com.he1extg.pdfreader.storage.FileInfoList
import org.springframework.core.io.InputStreamResource
import org.springframework.core.io.Resource
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping("/files")
class FileOperations(
    val storageHandler: StorageHandler
) {
    @GetMapping("/{fileName:.+}")
    fun serveFile(@PathVariable fileName: String): ResponseEntity<Resource> {
        val file: Resource = storageHandler.loadAsResource(fileName)
        return ResponseEntity.ok().header(
            HttpHeaders.CONTENT_DISPOSITION,
            "attachment; filename=\"${file.filename}\""
        ).body(file)
    }

    @GetMapping("")
    fun getFilesList(): FileInfoList {
        return storageHandler.loadAllAsFileInfoStream()
    }

    @PostMapping("")
    fun uploadPdfAndConvertToMP3(@RequestParam("file") file: MultipartFile): HttpStatus {
        if (!file.isEmpty) {
            storageHandler.storePdfAsMP3(file)
        }
        return HttpStatus.OK
    }

    @PostMapping("/file")
    fun convertPdfToMP3(@RequestParam("file") file: MultipartFile): ResponseEntity<Resource> =
        if (!file.isEmpty) {
            val mp3InputStream = storageHandler.convertPdfToMP3(file)
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