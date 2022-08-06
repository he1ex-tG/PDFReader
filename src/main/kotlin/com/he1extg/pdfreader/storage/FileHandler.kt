package com.he1extg.pdfreader.storage

import org.springframework.core.io.Resource
import org.springframework.web.multipart.MultipartFile
import java.io.InputStream
import java.nio.file.Path
import java.util.stream.Stream

interface FileHandler {
    fun init()
    fun convertPDFtoMP3(filePDF: MultipartFile): InputStream
    fun storeAsMP3(filePDF: MultipartFile)
    fun loadAll(): Stream<Path>
    fun loadAllAsModelInfo(): Stream<HTTPModelFileInfo>
    fun load(fileName: String): Path
    fun loadAsResource(fileName: String): Resource
    fun playAudioFile(fileName: String)
    fun deleteAll(): Boolean
}