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

    @Test
    fun findByID_return_UserOrNull() {
        val newUser = User("he1ex", "111")
        entityManager.persist(newUser)
        entityManager.flush()

        val dataAnswerUser = userRepository.findByID(1L)
        val dataAnswerNull = userRepository.findByID(10L)

        assertThat(dataAnswerUser).isNotNull
        assertThat(dataAnswerUser?.Login).isEqualTo(newUser.Login)
        assertThat(dataAnswerNull).isNull()
    }
}