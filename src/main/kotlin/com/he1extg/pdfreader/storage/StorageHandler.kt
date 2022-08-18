package com.he1extg.pdfreader.storage

import org.springframework.core.io.Resource
import org.springframework.web.multipart.MultipartFile
import java.io.InputStream


interface StorageHandler {
    fun init()
    fun convertPdfToMP3(filePDF: MultipartFile): InputStream
    fun storePdfAsMP3(filePDF: MultipartFile)
    fun loadAllAsFileInfoStream(): FileInfoList
    fun loadAsResource(fileName: String): Resource
    fun deleteAll(): Boolean
}