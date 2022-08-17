package com.he1extg.pdfreader.storage

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
import java.nio.file.StandardCopyOption
import java.util.stream.Collectors
import kotlin.io.path.getLastModifiedTime


@Service
class StorageHandlerServiceFileSystem(properties: StorageProperties) : StorageHandler {
    private val rootLocation: Path = Paths.get(properties.uploadDir)
    private val maxFilesToStore = properties.maxFilesToStore.toInt()

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

    private fun Path.maxFilesControl(amount: Int) {
        val filesInDir = Files.list(this).collect(Collectors.toList())
        if (filesInDir.count() > amount) {
            val myPathComparator = Comparator<Path> { a, b ->
                val f1 = a.getLastModifiedTime().toMillis()
                val f2 = b.getLastModifiedTime().toMillis()
                if (f1 - f2 >= 0)
                    1
                else
                    -1
            }
            Files.delete(filesInDir.minOfWith(myPathComparator) { it })
        }
    }

    override fun storePDFAsMP3(filePDF: MultipartFile) {
        try {
            if (filePDF.isEmpty) {
                throw StorageException("Failed to store empty file " + filePDF.originalFilename)
            }
            val fileNameToStore = filePDF.originalFilename!!.split(".").first() + ".mp3"
            val filePath = rootLocation.resolve(fileNameToStore)
            Files.copy(convertPDFtoMP3(filePDF), filePath, StandardCopyOption.REPLACE_EXISTING)
            rootLocation.maxFilesControl(maxFilesToStore)
        } catch (e: IOException) {
            throw StorageException("Failed to store file " + filePDF.originalFilename, e)
        }
    }

    private fun loadAll(): List<Path> =
        try {
            Files.walk(rootLocation, 1)
                .filter { path -> !path.equals(rootLocation) }
                .map { path -> rootLocation.relativize(path) }.collect(Collectors.toList())
        } catch (e: IOException) {
            throw StorageException("Failed to read stored files", e)
        }

    override fun loadAllAsFileInfoStream() = FileInfoList(
        loadAll().map {
            FileInfo(
                it.fileName.toString(),
                MvcUriComponentsBuilder.fromMethodName(
                    FileOperations::class.java,
                    "serveFile",
                    it.fileName.toString()
                ).build().toUri().toString(),
            )
        }
    )

    override fun loadAsResource(fileName: String): Resource =
        try {
            val file: Path = rootLocation.resolve(fileName)
            val resource: Resource = UrlResource(file.toUri())
            if (resource.exists() || resource.isReadable) {
                resource
            } else {
                throw StorageFileNotFoundException("Could not read file: $fileName")
            }
        } catch (e: MalformedURLException) {
            throw StorageFileNotFoundException("Could not read file: $fileName", e)
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