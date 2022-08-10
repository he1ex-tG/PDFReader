package com.he1extg.pdfreader

import com.he1extg.pdfreader.controller.rest.FileOperations
import com.he1extg.pdfreader.storage.FileInfo
import com.he1extg.pdfreader.storage.FileInfoList
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.mockito.BDDMockito.given
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.HttpStatus

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class PdfreaderApplicationTests(
	@Autowired val restTemplate: TestRestTemplate
) {
	@MockBean
	lateinit var fileOperations: FileOperations

	@Test
	fun contextLoads() {
	}

	@Test
	fun `Get empty files list`() {
		val testFileInfoList = FileInfoList(
			listOf()
		)
		given(fileOperations.getFilesList()).willReturn(testFileInfoList)
		val answer = restTemplate.getForEntity("/files", FileInfoList::class.java)
		assertThat(answer.statusCode).isEqualTo(HttpStatus.OK)
		val answerBody = answer.body!!.filesInfo
		assertThat(answerBody.count()).isEqualTo(0)
	}

	@Test
	fun `Get files list with mock`() {
		val testFileInfoList = FileInfoList(
			listOf(
				FileInfo("file1", "file1_dl_string"),
				FileInfo("file2", "file2_dl_string")
			)
		)
		given(fileOperations.getFilesList()).willReturn(testFileInfoList)
		val answer = restTemplate.getForEntity("/files", FileInfoList::class.java)
		assertThat(answer.statusCode).isEqualTo(HttpStatus.OK)
		val answerBody = answer.body!!.filesInfo
		assertThat(answerBody.count()).isEqualTo(2)
		assertThat(answerBody[0].name).isEqualTo("file1")
		assertThat(answerBody[1].dlURIString).contains("file2_")
	}
}
