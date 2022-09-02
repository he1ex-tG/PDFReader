package com.he1extg.pdfreader.repositoryhandler.storage

import com.he1extg.pdfreader.entity.StoredFile
import com.he1extg.pdfreader.entity.User
import com.he1extg.pdfreader.exception.StorageException
import com.he1extg.pdfreader.exception.StorageFileNotFoundException
import com.he1extg.pdfreader.repository.StoredFileRepository
import com.he1extg.pdfreader.repository.UserRepository
import com.he1extg.pdfreader.repositoryhandler.user.UserHandler
import com.he1extg.pdfreader.security.UserRole
import com.he1extg.pdfreader.security.UserStatus
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Service
import java.io.ByteArrayInputStream
import java.io.IOException
import java.io.InputStream
import java.net.MalformedURLException

@Service
@EnableConfigurationProperties(StoragePropertiesH2Database::class)
@Profile("h2database")
class StorageHandlerServiceH2(
    properties: StoragePropertiesH2Database,
) : StorageHandler {

    @Autowired
    lateinit var userHandler: UserHandler
    @Autowired
    lateinit var storedFileRepository: StoredFileRepository

    private val maxFilesToStore = properties.maxFilesToStore.toInt()

    override fun init() {
    }

    private fun StoredFileRepository.maxFilesControl(amount: Int) {
        val storedFiles = this.getStoredFileByOwner(userHandler.currentUser)
        if (storedFiles.size > amount) {
            val myTimestampComparator = Comparator<StoredFile> { a, b -> a.timestamp.compareTo(b.timestamp) }
            val id = storedFiles.minOfWith(myTimestampComparator) { it }.ID
            id?.let {
                this.deleteById(id)
            } ?: throw Exception("Internal H2 database error! File ID is null.")
        }
    }

    override fun save(fileName: String, inputStream: InputStream) {
        try {
            val newFileToStore = StoredFile(fileName, inputStream.readBytes(), owner = userHandler.currentUser)
            storedFileRepository.save(newFileToStore)

            storedFileRepository.maxFilesControl(maxFilesToStore)
        } catch (e: IOException) {
            throw StorageException("Failed to store file $fileName", e)
        }
    }

    override fun list(): List<String> {
        return storedFileRepository.getStoredFileByOwner(userHandler.currentUser)
            .map { it.fileName }
    }

    override fun load(fileName: String): InputStream =
        try {
            val storedFile = storedFileRepository.getStoredFileByFileNameAndOwner(fileName, userHandler.currentUser)
            if (storedFile != null) {
                ByteArrayInputStream(storedFile.file)
            } else {
                throw StorageFileNotFoundException("Could not find file: $fileName")
            }
        } catch (e: MalformedURLException) {
            throw StorageFileNotFoundException("Could not find file: $fileName", e)
        }

    override fun deleteAll(): Boolean {
        return true
    }
}
