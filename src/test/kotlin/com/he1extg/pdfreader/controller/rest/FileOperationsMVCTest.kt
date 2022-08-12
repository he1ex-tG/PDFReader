package com.he1extg.pdfreader.controller.rest

import com.he1extg.pdfreader.storage.StorageHandler
import org.hamcrest.Matchers.containsString
import org.junit.jupiter.api.Test
import org.mockito.BDDMockito.given
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.core.io.Resource
import org.springframework.http.MediaType
import org.springframework.mock.web.MockMultipartFile
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.multipart

@WebMvcTest(FileOperations::class)
internal class FileOperationsMVCTest(
    @Autowired val mvc: MockMvc,
) {
    @MockBean
    lateinit var storageHandler: StorageHandler

    @Value("classpath:test/test.mp3")
    lateinit var resourceMP3: Resource
    @Value("classpath:test/test.pdf")
    lateinit var resourcePDF: Resource

    @Test
    fun uploadPDFandConvertToMP3() {
        val mockFile = MockMultipartFile("file", resourcePDF.file.readBytes())
        mvc.multipart ("/files") {
            file(mockFile)
            contentType = MediaType.MULTIPART_FORM_DATA
        }.andExpect {
            status {
                isOk()
            }
        }
    }

    @Test
    fun convertPDFtoMP3_normalPDF() {
        val mockFile = MockMultipartFile("file", resourcePDF.file.readBytes())
        given(storageHandler.convertPDFtoMP3(mockFile)).willReturn(resourceMP3.inputStream)

        mvc.multipart ("/file") {
            file(mockFile)
            contentType = MediaType.MULTIPART_FORM_DATA
        }.andExpect {
            status {
                isOk()
            }
            content {
                contentType(MediaType.APPLICATION_JSON)
                string(containsString("LAME"))
            }
        }
    }
}