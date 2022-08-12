package com.he1extg.pdfreader.controller.rest

import com.he1extg.pdfreader.storage.FileInfo
import com.he1extg.pdfreader.storage.FileInfoList
import com.he1extg.pdfreader.storage.StorageHandler
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

import org.mockito.BDDMockito.given
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.HttpStatus
import java.nio.file.Path
import java.nio.file.Paths

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
internal class FileOperationsTest(
    @Autowired val testRestTemplate: TestRestTemplate
) {

    @MockBean
    lateinit var storageHandler: StorageHandler

    @Test
    fun serveFile() {
    }

    @Test
    fun getFilesList_noFiles_emptyList() {
        given(storageHandler.loadAllAsFileInfoStream()).willReturn(FileInfoList(listOf()))

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
        given(storageHandler.loadAllAsFileInfoStream()).willReturn(fileInfoList)

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
    fun uploadPDFandConvertToMP3() {
    }

    @Test
    fun convertPDFtoMP3() {
    }
}