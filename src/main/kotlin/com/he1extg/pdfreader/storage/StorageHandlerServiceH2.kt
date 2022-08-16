package com.he1extg.pdfreader.storage

import org.springframework.core.io.Resource
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.io.InputStream
import java.nio.file.Path

//@Service
class StorageHandlerServiceH2 : StorageHandler {

    override fun init() {
        TODO("Not yet implemented")
    }

    override fun convertPDFtoMP3(filePDF: MultipartFile): InputStream {
        TODO("Not yet implemented")
    }

    override fun storePDFAsMP3(filePDF: MultipartFile) {
        TODO("Not yet implemented")
    }

    override fun loadAll(): List<Path> {
        TODO("Not yet implemented")
    }

    override fun loadAllAsFileInfoStream(): FileInfoList {
        TODO("Not yet implemented")
    }

    override fun load(fileName: String): Path {
        TODO("Not yet implemented")
    }

    override fun loadAsResource(fileName: String): Resource {
        TODO("Not yet implemented")
    }

    override fun deleteAll(): Boolean {
        TODO("Not yet implemented")
    }
}