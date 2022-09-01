package com.he1extg.pdfreader.storage

import java.io.InputStream

interface StorageHandler {
    fun init()
    fun save(fileName: String, inputStream: InputStream)
    fun list(): List<String>
    fun load(fileName: String): InputStream
    fun deleteAll(): Boolean
}