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

    override fun pdfToVoice(filePathString: String) =
        tts.speakIt(extractText(filePathString))

    override fun pdfToVoice(inputFileStream: InputStream) =
        tts.speakIt(inputFileStream.toString())

    override fun pdfToByteStream(filePathString: String): OutputStream =
        tts.toOutputStream(extractText(filePathString))

    override fun pdfToByteStream(inputFileStream: InputStream): OutputStream =
        tts.toOutputStream(inputFileStream.toString())
}