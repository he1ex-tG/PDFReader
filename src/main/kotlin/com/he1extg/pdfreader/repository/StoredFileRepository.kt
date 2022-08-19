package com.he1extg.pdfreader.repository

import com.he1extg.pdfreader.entity.StoredFile
import org.springframework.data.repository.CrudRepository

interface StoredFileRepository : CrudRepository<StoredFile, Long> {
    override fun findAll(): List<StoredFile>
    fun getStoredFileByOwnerID(id: Long): List<StoredFile>
    fun getStoredFileByID(id: Long): StoredFile?
    fun getStoredFileByFileName(fileName: String): StoredFile?
}