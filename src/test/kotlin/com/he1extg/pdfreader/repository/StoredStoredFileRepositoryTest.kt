package com.he1extg.pdfreader.repository

import com.he1extg.pdfreader.entity.StoredFile
import com.he1extg.pdfreader.entity.User
import com.he1extg.pdfreader.security.UserRole
import com.he1extg.pdfreader.security.UserStatus
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
    private val newUser = User("he1ex", "123", UserRole.USER, UserStatus.ACTIVE)
    private val newStoredFile = StoredFile(
        ClassPathResource("test/test.mp3").file.name,
        ClassPathResource("test/test.mp3").file.readBytes(),
        owner = newUser
    )

    @Test
    fun findByID_return_FileOrNull() {
        entityManager.apply {
            persist(newUser)
            persist(newStoredFile)
        }

        val fileList = storedFileRepository.findAll()
        assertThat(fileList.size).isEqualTo(1)

        val answerNull = storedFileRepository.getStoredFileByID(10L)
        assertThat(answerNull).isNull()

        val answerNotNull = storedFileRepository.getStoredFileByID(fileList[0].ID!!)
        assertThat(answerNotNull).isNotNull
        assertThat(answerNotNull?.fileName).isEqualTo(newStoredFile.fileName)
    }

    @Test
    fun findAll_return_FileListOrEmptyList() {
        val fileListEmpty = storedFileRepository.findAll()
        assertThat(fileListEmpty.size).isEqualTo(0)

        entityManager.apply {
            persist(newUser)
            persist(newStoredFile)
        }

        val fileList = storedFileRepository.findAll()
        assertThat(fileList.size).isEqualTo(1)
    }

    @Test
    fun getStoredFilesByOwnerID_return_FileListOrEmptyList() {
        entityManager.apply {
            persist(newUser)
            persist(newStoredFile)
        }

        val userList = userRepository.findAll()
        assertThat(userList.size).isEqualTo(1)

        val fileListEmpty = storedFileRepository.getStoredFileByOwnerID(10L)
        assertThat(fileListEmpty.size).isEqualTo(0)

        val fileList = storedFileRepository.getStoredFileByOwnerID(userList[0].ID!!)
        assertThat(fileList.size).isEqualTo(1)
    }

    @Test
    fun getStoredFileByFileName_return_FileOrNull() {
        entityManager.apply {
            persist(newUser)
            persist(newStoredFile)
        }

        val fileEmpty = storedFileRepository.getStoredFileByFileName("asd")
        assertThat(fileEmpty).isNull()

        val file = storedFileRepository.getStoredFileByFileName("test.mp3")
        assertThat(file!!.owner).isEqualTo(newUser)
        //Player(ByteArrayInputStream(file.file)).play()
    }
}