package com.he1extg.pdfreader.storage

import com.he1extg.pdfreader.controller.rest.FileOperations
import com.he1extg.pdfreader.entity.StoredFile
import com.he1extg.pdfreader.entity.User
import com.he1extg.pdfreader.exception.StorageException
import com.he1extg.pdfreader.exception.StorageFileNotFoundException
import com.he1extg.pdfreader.repository.StoredFileRepository
import com.he1extg.pdfreader.repository.UserRepository
import com.he1extg.pdfreader.security.UserRole
import com.he1extg.pdfreader.security.UserStatus
import com.he1extg.pdfreader.ttsprocessing.PDFReader
import com.he1extg.pdfreader.ttsprocessing.TTS
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Profile
import org.springframework.core.io.ByteArrayResource
import org.springframework.core.io.Resource
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder
import java.io.IOException
import java.io.InputStream
import java.net.Authenticator
import java.net.MalformedURLException

@Service
@EnableConfigurationProperties(StoragePropertiesH2Database::class)
@Profile("h2database")
class StorageHandlerServiceH2(
    properties: StoragePropertiesH2Database,
    val userRepository: UserRepository,
    val storedFileRepository: StoredFileRepository,
) : StorageHandler {
    private val maxFilesToStore = properties.maxFilesToStore.toInt()

    @Autowired
    private lateinit var pdfReader: PDFReader
    @Autowired
    private lateinit var tts: TTS

    override fun init() {
        userRepository.save(
            User(
                "admin",
                /*admin*/"\$2a\$12\$L9iYxslNXJ7/PsVUX3QJ/.BXd8k6FroGd38A4dBY2Oe/bSjvVbF2a",
                UserRole.ADMIN,
                UserStatus.ACTIVE
            )
        )
        userRepository.save(
            User(
                "anonymousUser",
                "",
                UserRole.USER,
                UserStatus.ACTIVE
            )
        )
    }

    override fun convertPdfToMP3(filePDF: MultipartFile): InputStream {
        if (filePDF.isEmpty) {
            throw StorageException("Failed to store empty file " + filePDF.originalFilename)
        }
        val pdfText = pdfReader.extractText(filePDF.inputStream)
        return tts.stream(pdfText)
    }

    private fun StoredFileRepository.maxFilesControl(amount: Int) {
        val storedFiles = this.getStoredFileByOwnerLogin(SecurityContextHolder.getContext().authentication.name)
        if (storedFiles.size > amount) {
            val myTimestampComparator = Comparator<StoredFile> { a, b -> a.timestamp.compareTo(b.timestamp) }
            val id = storedFiles.minOfWith(myTimestampComparator) { it }.ID
            id?.let {
                this.deleteById(id)
            } ?: throw Exception("Internal H2 database error! File ID is null.")
        }
    }

    override fun storePdfAsMP3(filePDF: MultipartFile) {
        try {
            if (filePDF.isEmpty) {
                throw StorageException("Failed to store empty file " + filePDF.originalFilename)
            }
            val fileNameToStore = filePDF.originalFilename!!.split(".").first() + ".mp3"
            val fileToStore = convertPdfToMP3(filePDF)

            val fileOwner = userRepository.findByLogin(SecurityContextHolder.getContext().authentication.name)

            val newFileToStore = StoredFile(fileNameToStore, fileToStore.readBytes(), owner = fileOwner!!)
            storedFileRepository.save(newFileToStore)

            storedFileRepository.maxFilesControl(maxFilesToStore)
        } catch (e: IOException) {
            throw StorageException("Failed to store file " + filePDF.originalFilename, e)
        }
    }

    override fun loadAllAsFileInfoStream() = FileInfoList(
        storedFileRepository.getStoredFileByOwnerLogin(SecurityContextHolder.getContext().authentication.name).map {
            FileInfo(
                it.fileName,
                MvcUriComponentsBuilder.fromMethodName(
                    FileOperations::class.java,
                    "serveFile",
                    it.fileName
                ).build().toUri().toString(),
            )
        }
    )

    override fun loadAsResource(fileName: String): Resource =
        try {
            val storedFile = storedFileRepository.getStoredFileByFileName(fileName)
            if (storedFile != null) {
                val resource: Resource = object : ByteArrayResource(storedFile.file, storedFile.fileName) {
                    override fun getFilename(): String = storedFile.fileName
                }
                resource
            } else {
                throw StorageFileNotFoundException("Could not find file: $fileName")
            }
        } catch (e: MalformedURLException) {
            throw StorageFileNotFoundException("Could not find file: $fileName", e)
        }

    override fun deleteAll(): Boolean {
        userRepository.deleteAll()
        storedFileRepository.deleteAll()
        return true
    }
}