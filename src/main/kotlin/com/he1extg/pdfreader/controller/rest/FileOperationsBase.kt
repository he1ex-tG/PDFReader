package com.he1extg.pdfreader.controller.rest

import com.he1extg.pdfreader.repositoryhandler.storage.StorageHandler
import com.he1extg.pdfreader.ttsprocessing.Converter
import org.springframework.beans.factory.annotation.Autowired
import java.io.InputStream

open class FileOperationsBase {

    @Autowired
    private lateinit var storageHandler: StorageHandler
    @Autowired
    private lateinit var converter: Converter

    fun convert(inputStream: InputStream): InputStream {
        return converter.convert(inputStream)
    }

    fun save(fileName: String, inputStream: InputStream) {
        val converted = convert(inputStream)
        val fileNameToStore =  fileName!!.split(".").first() + ".mp3"
        storageHandler.save(fileNameToStore, converted)
    }

    fun load(fileName: String): InputStream {
        return storageHandler.load(fileName)
    }

    fun list(): List<String> {
        return storageHandler.list()
    }
}