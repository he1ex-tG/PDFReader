package com.he1extg.pdfreader.storage

import com.he1extg.pdfreader.controller.rest.FileOperations
import com.he1extg.pdfreader.entity.StoredFile
import com.he1extg.pdfreader.entity.User
import com.he1extg.pdfreader.exception.StorageException
import com.he1extg.pdfreader.exception.StorageFileNotFoundException
import com.he1extg.pdfreader.repository.StoredFileRepository
import com.he1extg.pdfreader.repository.UserRepository
import com.he1extg.pdfreader.ttsprocessing.PDFReader
import com.he1extg.pdfreader.ttsprocessing.TTS
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Profile
import org.springframework.core.io.ByteArrayResource
import org.springframework.core.io.Resource
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder
import java.io.IOException
import java.io.InputStream
import java.net.MalformedURLException
import java.nio.file.Path

@Service
@Profile("h2database")
class StorageHandlerServiceH2(
    val userRepository: UserRepository,
    val storedFileRepository: StoredFileRepository,
) : StorageHandler {

    @Autowired
    private lateinit var pdfReader: PDFReader
    @Autowired
    private lateinit var tts: TTS

    // !!! Temporary solution
    private val userAdmin = User("admin", "admin")

    override fun init() {
        userRepository.save(userAdmin)
    }

    override fun convertPdfToMP3(filePDF: MultipartFile): InputStream {
        if (filePDF.isEmpty) {
            throw StorageException("Failed to store empty file " + filePDF.originalFilename)
        }
        val pdfText = pdfReader.extractText(filePDF.inputStream)
        return tts.stream(pdfText)
    }

    private fun Path.maxFilesControl(amount: Int) {
        TODO("Not yet implemented")
    }

    override fun storePdfAsMP3(filePDF: MultipartFile) {
        try {
            if (filePDF.isEmpty) {
                throw StorageException("Failed to store empty file " + filePDF.originalFilename)
            }
            val fileNameToStore = filePDF.originalFilename!!.split(".").first() + ".mp3"
            val fileToStore = convertPdfToMP3(filePDF)

            val newFileToStore = StoredFile(fileNameToStore, fileToStore.readBytes(), owner = userAdmin)
            storedFileRepository.save(newFileToStore)

            //rootLocation.maxFilesControl(maxFilesToStore)
        } catch (e: IOException) {
            throw StorageException("Failed to store file " + filePDF.originalFilename, e)
        }
    }

    override fun loadAllAsFileInfoStream() = FileInfoList(
        storedFileRepository.findAll().map {
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