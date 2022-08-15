package com.he1extg.pdfreader.storage

import org.springframework.core.io.Resource
import org.springframework.web.multipart.MultipartFile
import java.io.InputStream
import java.nio.file.Path

interface StorageHandler {
    fun init()
    fun convertPDFtoMP3(filePDF: MultipartFile): InputStream
    fun storePDFAsMP3(filePDF: MultipartFile)
    fun loadAll(): List<Path>
    fun loadAllAsFileInfoStream(): FileInfoList
    fun load(fileName: String): Path
    fun loadAsResource(fileName: String): Resource
    fun deleteAll(): Boolean
}