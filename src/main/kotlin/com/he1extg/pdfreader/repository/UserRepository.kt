package com.he1extg.pdfreader.repository

import com.he1extg.pdfreader.entity.User
import org.springframework.data.repository.CrudRepository

interface UserRepository : CrudRepository<User, Long> {

    override fun findAll(): List<User>
    fun findByID(id: Long): User?
    fun findByLogin(login: String): User?
}