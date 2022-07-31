package com.he1extg.pdfreader.ttsprocessing

import com.itextpdf.text.pdf.PdfReader
import com.itextpdf.text.pdf.parser.PdfTextExtractor
import com.itextpdf.text.pdf.parser.SimpleTextExtractionStrategy
import org.springframework.stereotype.Service
import java.io.InputStream
import java.io.OutputStream

@Service
class PDFReaderService(val tts: TTS) : PDFReader {

    private fun extractText(filePathString: String): String {
        val pr = PdfReader(filePathString)
        val sb = StringBuilder()
        for (page in (1..pr.numberOfPages)) {
            sb.append(PdfTextExtractor.getTextFromPage(pr, page, SimpleTextExtractionStrategy()))
        }
        return sb.toString()
    }

    private fun extractText(inputFileStream: InputStream): String {
        val pr = PdfReader(inputFileStream)
        val sb = StringBuilder()
        for (page in (1..pr.numberOfPages)) {
            sb.append(PdfTextExtractor.getTextFromPage(pr, page, SimpleTextExtractionStrategy()))
        }
        return sb.toString()
    }

    override fun pdfToVoice(filePathString: String) =
        tts.speak(extractText(filePathString))

    override fun pdfToVoice(inputFileStream: InputStream) =
        tts.speak(extractText(inputFileStream))

    override fun pdfToByteStream(filePathString: String): InputStream =
        tts.stream(extractText(filePathString))

    override fun pdfToByteStream(inputFileStream: InputStream): InputStream =
        tts.stream(extractText(inputFileStream))
}