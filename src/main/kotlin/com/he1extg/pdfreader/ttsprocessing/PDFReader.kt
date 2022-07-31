package com.he1extg.pdfreader.ttsprocessing

import java.io.InputStream

interface PDFReader {
    fun pdfToVoice(filePathString: String)
    fun pdfToVoice(inputFileStream: InputStream)
    fun pdfToByteStream(filePathString: String): InputStream
    fun pdfToByteStream(inputFileStream: InputStream): InputStream
}