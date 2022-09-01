package com.he1extg.pdfreader.ttsprocessing

import com.he1extg.pdfreader.exception.StorageException
import org.springframework.stereotype.Service
import java.io.InputStream

@Service
class ConverterService(
    val pdfReader: PDFReader,
    val tts: TTS,
) : Converter {

    override fun convert(inputStream: InputStream): InputStream {
        /*if (inputStream.isEmpty) {
            throw StorageException("Failed to store empty file " + inputStream.originalFilename)
        }*/
        val pdfText = pdfReader.extractText(inputStream)
        return tts.stream(pdfText)
    }
}