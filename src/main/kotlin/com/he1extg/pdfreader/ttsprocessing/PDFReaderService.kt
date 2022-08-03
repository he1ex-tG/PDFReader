package com.he1extg.pdfreader.ttsprocessing

import com.itextpdf.text.pdf.PdfReader
import com.itextpdf.text.pdf.parser.PdfTextExtractor
import com.itextpdf.text.pdf.parser.SimpleTextExtractionStrategy
import org.springframework.stereotype.Service
import java.io.InputStream

@Service
class PDFReaderService : PDFReader {

    override fun extractText(filePathString: String): String {
        val pr = PdfReader(filePathString)
        val sb = StringBuilder()
        for (page in (1..pr.numberOfPages)) {
            sb.append(PdfTextExtractor.getTextFromPage(pr, page, SimpleTextExtractionStrategy()))
        }
        return sb.toString()
    }

    override fun extractText(file: InputStream): String {
        val pr = PdfReader(file)
        val sb = StringBuilder()
        for (page in (1..pr.numberOfPages)) {
            sb.append(PdfTextExtractor.getTextFromPage(pr, page, SimpleTextExtractionStrategy()))
        }
        return sb.toString()
    }
}