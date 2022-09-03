package com.he1extg.pdfreader.repositoryhandler.user

import com.he1extg.pdfreader.entity.User

interface UserHandler {

    fun save(username: String, password: String)
}