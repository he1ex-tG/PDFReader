package com.he1extg.pdfreader.repository

import com.he1extg.pdfreader.entity.User
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager

@DataJpaTest
class UserRepositoryTest @Autowired constructor(
    val entityManager: TestEntityManager,
    val userRepository: UserRepository,
) {

    private val newUser = User("he1ex", "111")

    @Test
    fun findByID_return_UserOrNull() {
        entityManager.persist(newUser)

        val dataAnswerUser = userRepository.findByID(1L)
        assertThat(dataAnswerUser).isNotNull
        assertThat(dataAnswerUser?.login).isEqualTo(newUser.login)

        val dataAnswerNull = userRepository.findByID(10L)
        assertThat(dataAnswerNull).isNull()
    }
}