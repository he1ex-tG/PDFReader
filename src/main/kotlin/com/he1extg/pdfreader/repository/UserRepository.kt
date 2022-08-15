package com.he1extg.pdfreader.repository

import com.he1extg.pdfreader.entity.User
import org.springframework.data.repository.CrudRepository

interface UserRepository : CrudRepository<User, Long> {
    fun findByID(id: Long): User?
}