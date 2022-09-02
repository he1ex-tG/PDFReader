package com.he1extg.pdfreader.controller.rest

import com.he1extg.pdfreader.exception.StorageFileNotFoundException
import com.he1extg.pdfreader.repositoryhandler.storage.FileInfo
import com.he1extg.pdfreader.repositoryhandler.storage.FileInfoList
import org.springframework.core.io.ByteArrayResource
import org.springframework.core.io.Resource
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder
import java.io.InputStream

@RestController
@RequestMapping("/files")
class FileOperations : FileOperationsBase() {

    @GetMapping("/{fileName:.+}")
    fun getFile(@PathVariable fileName: String): ResponseEntity<Resource> {
        val inputStream: InputStream = load(fileName)
        val resource: Resource = object : ByteArrayResource(inputStream.readBytes()) {
            override fun getFilename(): String = fileName
        }
        return ResponseEntity.ok().header(
            HttpHeaders.CONTENT_DISPOSITION,
            "attachment; filename=\"${resource.filename}\""
        ).body(resource)
    }

    @GetMapping("")
    fun getFileList(): FileInfoList {
        val fileList = list()
        val fileInfoList = FileInfoList(
            fileList.map {
                FileInfo(
                    it,
                    MvcUriComponentsBuilder.fromMethodName(
                        FileOperations::class.java,
                        FileOperations::getFile.name,
                        it
                    ).build().toUri().toString(),
                )
            }
        )
        return fileInfoList
    }

    @PostMapping("")
    fun saveFile(@RequestParam("file") file: MultipartFile): HttpStatus {
        if (!file.isEmpty) {
            save(file.originalFilename!!, file.inputStream)
        }
        return HttpStatus.OK
    }

    @PostMapping("/file")
    fun convert(@RequestParam("file") file: MultipartFile): ResponseEntity<Resource> =
        if (!file.isEmpty) {
            val newFileName =  file.originalFilename!!.split(".").first() + ".mp3"
            val converted = convert(file.inputStream)
            val resource: Resource = object : ByteArrayResource(converted.readBytes()) {
                override fun getFilename(): String = newFileName
            }
            ResponseEntity.ok().header(
                HttpHeaders.CONTENT_DISPOSITION,
                ""
            ).body(resource)
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