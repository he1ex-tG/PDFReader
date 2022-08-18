package com.he1extg.pdfreader.storage

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Profile


@ConfigurationProperties(prefix = "storage")
@Profile("h2database")
class StoragePropertiesH2Database {
    /**
     * Maximum files to store
     */
    lateinit var maxFilesToStore: String
}

@ConfigurationProperties(prefix = "storage")
@Profile("filestorage")
class StoragePropertiesFileStorage {
    /**
     * Folder location for storing files
     */
    lateinit var uploadDir: String

    /**
     * Maximum files to store
     */
    lateinit var maxFilesToStore: String
}