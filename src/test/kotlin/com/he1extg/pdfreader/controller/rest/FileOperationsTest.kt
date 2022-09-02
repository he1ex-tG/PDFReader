package com.he1extg.pdfreader.controller.rest

import com.he1extg.pdfreader.repositoryhandler.storage.FileInfo
import com.he1extg.pdfreader.repositoryhandler.storage.FileInfoList
import com.he1extg.pdfreader.repositoryhandler.storage.StorageHandler
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.mockito.BDDMockito.*
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.core.io.ClassPathResource
import org.springframework.core.io.Resource
import org.springframework.http.HttpHeaders.CONTENT_DISPOSITION
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.RequestEntity
import org.springframework.util.LinkedMultiValueMap
import java.nio.file.Path
import java.nio.file.Paths

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
internal class FileOperationsTest(
    @Autowired val testRestTemplate: TestRestTemplate
) {

    @MockBean
    lateinit var storageHandler: StorageHandler

    val testFileName = "test"
    @Value("classpath:test/test.mp3")
    lateinit var resourceMP3: Resource
    @Value("classpath:test/test.pdf")
    lateinit var resourcePDF: Resource

    @Test
    fun serveFile_existFile() {
        given(storageHandler.load(MockitoHelper.anyObject())).willReturn(resourceMP3)

        val answer = testRestTemplate.getForEntity("/files/$testFileName.mp3", Resource::class.java)

        assertThat(answer.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(answer.headers[CONTENT_DISPOSITION]?.get(0)).contains(testFileName)
        assertThat(answer.body).isNotNull
    }

    @Test
    fun getFilesList_noFiles_emptyList() {
        given(storageHandler.list()).willReturn(FileInfoList(listOf()))

        val answer = testRestTemplate.getForEntity("/files", FileInfoList::class.java)

        assertThat(answer.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(answer.body).isNotNull
        assertThat(answer.body?.filesInfo).isEmpty()
    }

    @Test
    fun getFilesList_someFilesExists_listOfFiles() {
        val listOfPath = listOf<Path>(Paths.get("C:/1.mp3"), Paths.get("E:/test/2.mp3"))
        val fileInfoList = FileInfoList(
            listOfPath.map {
                FileInfo(
                    it.fileName.toString(),
                    it.toFile().absolutePath,
                )
            }
        )
        given(storageHandler.list()).willReturn(fileInfoList)

        val answer = testRestTemplate.getForEntity("/files", FileInfoList::class.java)

        assertThat(answer.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(answer.body).isNotNull
        answer.body?.let {
            assertThat(it.filesInfo).isNotEmpty
            assertThat(it.filesInfo.size).isEqualTo(2)
            assertThat(it.filesInfo[0].name).isEqualTo("1.mp3")
            assertThat(it.filesInfo[1].dlURIString).contains("2.mp3")
        }
    }

    @Test
    fun uploadPDFandConvertToMP3_normalPDF_requestStatusOK() {
        val file = ClassPathResource("/test/$testFileName.pdf")
        val requestEntity = RequestEntity.post("/files")
            .contentType(MediaType.MULTIPART_FORM_DATA)
            .body(LinkedMultiValueMap<String, Any>().apply { add("file", file) })

        val answer = testRestTemplate.exchange(requestEntity, Nothing::class.java)

        assertThat(answer.statusCode).isEqualTo(HttpStatus.OK)
    }

    @Test
    fun convertPDFtoMP3_normalPDF_fileMP3() {
        val file = ClassPathResource("/test/$testFileName.pdf")
        val requestEntity = RequestEntity.post("/file")
            .contentType(MediaType.MULTIPART_FORM_DATA)
            .body(LinkedMultiValueMap<String, Any>().apply { add("file", file) })
        given(storageHandler.convertPdfToMP3(MockitoHelper.anyObject())).willReturn(resourceMP3.file.inputStream())

        val answer = testRestTemplate.exchange(requestEntity, Resource::class.java)

        assertThat(answer.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(answer.body).isNotNull
        answer.body?.let {
            assertThat(it.contentLength()).isEqualTo(resourceMP3.contentLength())
            //Player(it.inputStream).play()
        }
    }
}

object MockitoHelper {
    fun <T> anyObject(): T {
        Mockito.any<T>()
        return uninitialized()
    }
    @Suppress("UNCHECKED_CAST")
    fun <T> uninitialized(): T = null as T
}