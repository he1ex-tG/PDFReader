package com.he1extg.pdfreader.ttsprocessing

import java.io.InputStream
import java.io.OutputStream

interface PDFReader {
    fun pdfToVoice(filePathString: String)
    fun pdfToVoice(inputFileStream: InputStream)
    fun pdfToByteStream(filePathString: String): OutputStream
    fun pdfToByteStream(inputFileStream: InputStream): OutputStream
}