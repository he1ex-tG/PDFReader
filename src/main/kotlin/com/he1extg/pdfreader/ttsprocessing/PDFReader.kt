package com.he1extg.pdfreader.ttsprocessing

import com.itextpdf.text.pdf.PdfReader
import com.itextpdf.text.pdf.parser.PdfTextExtractor
import com.itextpdf.text.pdf.parser.SimpleTextExtractionStrategy
import org.springframework.stereotype.Service

@Service
class PDFReader {
    fun sss(fileName: String) {
        val ss: PdfReader = PdfReader(fileName)
        for (page in (1..ss.numberOfPages)) {
            val strategy = SimpleTextExtractionStrategy()
            val rrr = PdfTextExtractor.getTextFromPage(ss, page, strategy)
            TTS().speak(rrr)
        }
    }
}