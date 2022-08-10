package com.he1extg.pdfreader

import com.he1extg.pdfreader.storage.FileInfoList
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.HttpStatus

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class PdfreaderApplicationTests(
	@Autowired val restTemplate: TestRestTemplate
) {

	@Test
	fun contextLoads() {
	}

	@Test
	fun `Get files list`() {
		//val answer: ResponseEntity<Stream<HTTPModelFileInfo>> = restTemplate.getForEntity("/files", Stream<HTTPModelFileInfo>::class.java)
		val answer = restTemplate.getForEntity("/files", FileInfoList::class.java)
		assertThat(answer.statusCode).isEqualTo(HttpStatus.OK)
		val answerBody = answer.body!!.filesInfo
		assertThat(answerBody.count()).isEqualTo(0)
	}
}
