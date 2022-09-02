package com.he1extg.pdfreader.repositoryhandler.user

import com.he1extg.pdfreader.entity.User
import com.he1extg.pdfreader.repository.UserRepository
import com.he1extg.pdfreader.security.UserRole
import com.he1extg.pdfreader.security.UserStatus
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class UserHandlerService : UserHandler {

    @Autowired
    lateinit var userRepository: UserRepository
    @Autowired
    lateinit var passwordEncoder: PasswordEncoder

    private val userLogin: String
        get() = SecurityContextHolder.getContext().authentication.name ?:
            throw UsernameNotFoundException("User not found in Security context!")
    override val currentUser: User
        get() = userRepository.findByLogin(userLogin) ?:
            throw UsernameNotFoundException("User not found in database!")

    override fun save(username: String, password: String) {
        val newUser = User(
            username,
            passwordEncoder.encode(password),
            UserRole.USER,
            UserStatus.ACTIVE
        )
        userRepository.save(newUser)
    }
}