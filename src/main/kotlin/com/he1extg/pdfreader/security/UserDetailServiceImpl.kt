package com.he1extg.pdfreader.security

import com.he1extg.pdfreader.repositoryhandler.RepositoryHandlerBase
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class UserDetailServiceImpl : UserDetailsService {

    @Autowired
    lateinit var handlerBase: RepositoryHandlerBase

    override fun loadUserByUsername(username: String): UserDetails {
        val user = handlerBase.userRepository.findByLogin(username) ?: throw UsernameNotFoundException("User doesn't exists")
        return UserDetailsImpl(user)
    }
}
