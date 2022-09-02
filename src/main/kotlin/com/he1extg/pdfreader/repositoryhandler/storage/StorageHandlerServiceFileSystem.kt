package com.he1extg.pdfreader.repositoryhandler.storage

import com.he1extg.pdfreader.exception.StorageException
import com.he1extg.pdfreader.exception.StorageFileNotFoundException
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Profile
import org.springframework.core.io.Resource
import org.springframework.core.io.UrlResource
import org.springframework.stereotype.Service
import org.springframework.util.FileSystemUtils
import java.io.ByteArrayInputStream
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
@EnableConfigurationProperties(StoragePropertiesFileStorage::class)
@Profile("filestorage")
class StorageHandlerServiceFileSystem(properties: StoragePropertiesFileStorage) : StorageHandler {

    private val rootLocation: Path = Paths.get(properties.uploadDir)
    private val maxFilesToStore = properties.maxFilesToStore.toInt()

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

    override fun save(fileName: String, inputStream: InputStream) {
        try {
            /*if (inputStream.isEmpty) {
                throw StorageException("Failed to store empty file " + inputStream.originalFilename)
            }*/
            val filePath = rootLocation.resolve(fileName)
            Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING)
            rootLocation.maxFilesControl(maxFilesToStore)
        } catch (e: IOException) {
            throw StorageException("Failed to store file $fileName", e)
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

    override fun list(): List<String> {
        return loadAll().map {
            it.fileName.toString()
        }
    }

    override fun load(fileName: String): InputStream =
        try {
            val file: Path = rootLocation.resolve(fileName)
            val resource: Resource = UrlResource(file.toUri())
            if (resource.exists() || resource.isReadable) {
                ByteArrayInputStream(resource.file.readBytes())
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