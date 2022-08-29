package com.he1extg.pdfreader.repository

import com.he1extg.pdfreader.entity.User
import com.he1extg.pdfreader.security.UserRole
import com.he1extg.pdfreader.security.UserStatus
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.factory.PasswordEncoderFactories
import org.springframework.security.crypto.password.DelegatingPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder

@DataJpaTest
class UserRepositoryTest @Autowired constructor(
    val entityManager: TestEntityManager,
    val userRepository: UserRepository,
) {
    private val newUser = User(
        "he1ex",
        DelegatingPasswordEncoder("bc", mapOf("bc" to BCryptPasswordEncoder())).encode("111"),
        UserRole.USER,
        UserStatus.ACTIVE
    )

    @Test
    fun findByID_return_UserOrNull() {
        entityManager.persist(newUser)

        val dataAnswerUser = userRepository.findByID(1L)
        assertThat(dataAnswerUser).isNotNull
        assertThat(dataAnswerUser?.login).isEqualTo(newUser.login)
        assertThat(dataAnswerUser?.password).contains("bc")

        val dataAnswerNull = userRepository.findByID(10L)
        assertThat(dataAnswerNull).isNull()
    }
}