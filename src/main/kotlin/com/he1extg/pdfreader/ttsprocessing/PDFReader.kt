package com.he1extg.pdfreader.ttsprocessing

import java.io.InputStream

interface PDFReader {
    fun extractText(filePathString: String): String
    fun extractText(file: InputStream): String
}