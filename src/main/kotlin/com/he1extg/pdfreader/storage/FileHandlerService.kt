package com.he1extg.pdfreader.storage

import com.he1extg.pdfreader.controller.rest.FileOperations
import com.he1extg.pdfreader.exception.StorageException
import com.he1extg.pdfreader.exception.StorageFileNotFoundException
import com.he1extg.pdfreader.ttsprocessing.PDFReader
import com.he1extg.pdfreader.ttsprocessing.TTS
import javazoom.jl.player.Player
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


@Service
class FileHandlerService(properties: StorageProperties) : FileHandler {
    private val rootLocation: Path = Paths.get(properties.uploadDir)

    @Autowired
    private lateinit var pdfReader: PDFReader
    @Autowired
    private lateinit var tts: TTS

    override fun convertPDFtoMP3(filePDF: MultipartFile): InputStream {
        if (filePDF.isEmpty) {
            throw StorageException("Failed to store empty file " + filePDF.originalFilename)
        }
        val pdfText = pdfReader.extractText(filePDF.inputStream)
        return tts.stream(pdfText)
    }

    override fun storeAsMP3(filePDF: MultipartFile) {
        try {
            if (filePDF.isEmpty) {
                throw StorageException("Failed to store empty file " + filePDF.originalFilename)
            }
            val fileNameToStore = filePDF.originalFilename!!.split(".").first() + ".mp3"
            val filePath = rootLocation.resolve(fileNameToStore)
            Files.copy(convertPDFtoMP3(filePDF), filePath)
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

    override fun playAudioFile(fileName: String) {
        val inputStream = loadAsResource(fileName).file.inputStream()
        val mediaPlayer = Player(inputStream)
        mediaPlayer.play()
    }

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
)