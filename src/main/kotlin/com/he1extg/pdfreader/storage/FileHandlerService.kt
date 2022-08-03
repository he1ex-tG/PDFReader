package com.he1extg.pdfreader.storage

import com.he1extg.pdfreader.controller.http.MainPageHandling
import com.he1extg.pdfreader.controller.rest.FileOperations
import com.he1extg.pdfreader.exception.StorageException
import com.he1extg.pdfreader.exception.StorageFileNotFoundException
import com.he1extg.pdfreader.ttsprocessing.PDFReader
import com.he1extg.pdfreader.ttsprocessing.TTS
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.io.Resource
import org.springframework.core.io.UrlResource
import org.springframework.stereotype.Service
import org.springframework.util.FileSystemUtils
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder
import java.io.IOException
import java.io.InputStream
import java.net.MalformedURLException
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.util.stream.Stream
import javax.sound.sampled.AudioSystem


@Service
class FileHandlerService(properties: StorageProperties) : FileHandler {
    private val rootLocation: Path = Paths.get(properties.uploadDir)

    @Autowired
    private lateinit var pdfReader: PDFReader
    @Autowired
    private lateinit var tts: TTS

    override fun storeAsMP3(filePDF: MultipartFile) {
        try {
            if (filePDF.isEmpty) {
                throw StorageException("Failed to store empty file " + filePDF.originalFilename)
            }
            val filePath = rootLocation.resolve(filePDF.originalFilename!!.split(".").first() + ".mp3")
            val pdfText = pdfReader.extractText(filePDF.inputStream)
            val mp3InputStream = tts.stream(pdfText)
            Files.copy(mp3InputStream, filePath)
        } catch (e: IOException) {
            throw StorageException("Failed to store file " + filePDF.originalFilename, e)
        }
    }

    override fun loadAll(): Stream<Path> =
        try {
            Files.walk(rootLocation, 1)
                .filter { path -> !path.equals(rootLocation) }
                .map { path -> rootLocation.relativize(path) }
        } catch (e: IOException) {
            throw StorageException("Failed to read stored files", e)
        }

    override fun loadAllAsModelInfo(): Stream<HTTPModelFileInfo> {
        val storedFiles = loadAll()
        val modelFileInfo = storedFiles.map {
            HTTPModelFileInfo(
                it.fileName.toString(),
                MvcUriComponentsBuilder.fromMethodName(
                    FileOperations::class.java,
                    "serveFile",
                    it.fileName.toString()
                ).build().toUri().toString(),
                MvcUriComponentsBuilder.fromMethodName(
                    MainPageHandling::class.java,
                    "playFile",
                    it.fileName.toString()
                ).build().toUri().toString()
            )
        }
        return modelFileInfo
    }

    override fun load(fileName: String): Path =
        rootLocation.resolve(fileName)

    override fun loadAsResource(fileName: String): Resource =
        try {
            val file: Path = load(fileName)
            val resource: Resource = UrlResource(file.toUri())
            if (resource.exists() || resource.isReadable) {
                resource
            } else {
                throw StorageFileNotFoundException("Could not read file: $fileName")
            }
        } catch (e: MalformedURLException) {
            throw StorageFileNotFoundException("Could not read file: $fileName", e)
        }

    /*override fun loadAsAudioFile(fileName: String): InputStream {
        return try {
            val file: Path = load(fileName)
            val resource: Resource = UrlResource(file.toUri())
            if (resource.exists() || resource.isReadable) {
                resource
            } else {
                throw StorageFileNotFoundException("Could not read file: $fileName")
            }
        } catch (e: MalformedURLException) {
            throw StorageFileNotFoundException("Could not read file: $fileName", e)
        }
    }*/

    override fun deleteAll(): Boolean =
        FileSystemUtils.deleteRecursively(rootLocation.toFile())

    override fun init() {
        try {
            Files.createDirectory(rootLocation)
        } catch (e: IOException) {
            throw StorageException("Could not initialize storage", e)
        }
    }
}

class HTTPModelFileInfo(
    val name: String,
    val dlURIString: String,
    val plURIString: String,
)