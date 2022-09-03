package com.he1extg.pdfreader.repositoryhandler

import com.he1extg.pdfreader.entity.User
import com.he1extg.pdfreader.repository.StoredFileRepository
import com.he1extg.pdfreader.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UsernameNotFoundException

open class RepositoryHandlerBase {

    @Autowired
    protected lateinit var userRepository: UserRepository
    @Autowired
    protected lateinit var storedFileRepository: StoredFileRepository

    private val userLogin: String
        get() = SecurityContextHolder.getContext().authentication.name ?:
        throw UsernameNotFoundException("User not found in Security context!")
    protected val currentUser: User
        get() = userRepository.findByLogin(userLogin) ?:
        throw UsernameNotFoundException("User not found in database!")
}