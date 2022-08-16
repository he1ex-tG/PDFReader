package com.he1extg.pdfreader.repository

import com.he1extg.pdfreader.entity.StoredFile
import com.he1extg.pdfreader.entity.User
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager
import org.springframework.core.io.ClassPathResource

@DataJpaTest
class StoredStoredFileRepositoryTest @Autowired constructor(
    val entityManager: TestEntityManager,
    val storedFileRepository: StoredFileRepository,
    val userRepository: UserRepository,
) {

    @Test
    fun findByID_return_FileOrNull() {
        val newUser = User("he1ex", "123")
        entityManager.persist(newUser)

        val file = ClassPathResource("test/test.mp3")
        val newStoredFile = StoredFile(file.file.name, file.file, newUser)
        entityManager.persist(newStoredFile)

        entityManager.flush()

        val fileList = storedFileRepository.findAll()
        assertThat(fileList.size).isEqualTo(1)

        val answer1 = storedFileRepository.getStoredFileByID(10L)
        val answer2 = storedFileRepository.getStoredFileByID(2L)

        assertThat(answer1).isNull()
        assertThat(answer2).isNotNull
        assertThat(answer2?.FileName).isEqualTo(newStoredFile.FileName)
    }
}