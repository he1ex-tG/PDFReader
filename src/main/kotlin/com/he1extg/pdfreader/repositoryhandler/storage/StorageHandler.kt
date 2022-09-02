package com.he1extg.pdfreader.repositoryhandler.storage

import java.io.InputStream

interface StorageHandler {
    fun init()
    fun save(fileName: String, inputStream: InputStream)
    fun list(): List<String>
    fun load(fileName: String): InputStream
    fun deleteAll(): Boolean
}