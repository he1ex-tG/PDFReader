package com.he1extg.pdfreader.ttsprocessing

import org.springframework.web.multipart.MultipartFile
import java.io.InputStream

interface Converter {
    fun convert(inputStream: InputStream): InputStream
}