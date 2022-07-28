package com.he1extg.pdfreader.storage

import com.he1extg.pdfreader.exception.StorageException
import com.he1extg.pdfreader.exception.StorageFileNotFoundException
import org.springframework.core.io.Resource
import org.springframework.core.io.UrlResource
import org.springframework.stereotype.Service
import org.springframework.util.FileSystemUtils
import org.springframework.web.multipart.MultipartFile
import java.io.IOException
import java.net.MalformedURLException
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.util.stream.Stream


@Service
class FileSystemStorageService(properties: StorageProperties) : StorageService {
    private val rootLocation: Path = Paths.get(properties.location)

    override fun store(file: MultipartFile) {
        try {
            if (file.isEmpty) {
                throw StorageException("Failed to store empty file " + file.originalFilename)
            }
            Files.copy(file.inputStream, rootLocation.resolve(file.originalFilename))
        } catch (e: IOException) {
            throw StorageException("Failed to store file " + file.originalFilename, e)
        }
    }

    override fun loadAll(): Stream<Path> {
        return try {
            Files.walk(rootLocation, 1)
                .filter { path -> !path.equals(rootLocation) }
                .map { path -> rootLocation.relativize(path) }
        } catch (e: IOException) {
            throw StorageException("Failed to read stored files", e)
        }
    }

    override fun load(fileName: String): Path {
        return rootLocation.resolve(fileName)
    }

    override fun loadAsResource(fileName: String): Resource {
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
    }

    override fun deleteAll() {
        FileSystemUtils.deleteRecursively(rootLocation.toFile())
    }

    override fun init() {
        try {
            Files.createDirectory(rootLocation)
        } catch (e: IOException) {
            throw StorageException("Could not initialize storage", e)
        }
    }
}