package com.he1extg.pdfreader.entity

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id

@Entity
class User(
    var login: String,
    var password: String,
    @Id @GeneratedValue var ID: Long? = null,
)