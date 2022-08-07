package com.he1extg.pdfreader.storage

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "storage")
class StorageProperties {
    /**
     * Folder location for storing files
     */
    lateinit var uploadDir: String
    lateinit var maxFilesToStore: String
}