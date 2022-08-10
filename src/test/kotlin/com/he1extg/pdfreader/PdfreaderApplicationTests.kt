package com.he1extg.pdfreader

import com.he1extg.pdfreader.controller.rest.FileOperations
import com.he1extg.pdfreader.storage.HTTPModelFileInfo
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import java.util.stream.Collectors
import java.util.stream.Stream

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class PdfreaderApplicationTests(
	@Autowired val restTemplate: TestRestTemplate
) {

	@Test
	fun contextLoads() {
	}

	@Test
	fun `Get files list`() {
		//val answer: ResponseEntity<Stream<HTTPModelFileInfo>> = restTemplate.getForEntity("/files", Stream::class.java)
		//assertThat(answer.statusCode).isEqualTo(HttpStatus.OK)
		//assertThat(answer.count()).isEqualTo(0)
	}
}
