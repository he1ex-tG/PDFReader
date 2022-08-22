package com.he1extg.pdfreader.entity

import com.he1extg.pdfreader.security.UserRole
import com.he1extg.pdfreader.security.UserStatus
import javax.persistence.*

@Entity
class User(
    var login: String,
    var password: String,
    @Enumerated(value = EnumType.STRING) var role: UserRole,
    @Enumerated(value = EnumType.STRING) var status: UserStatus,
    @Id @GeneratedValue var ID: Long? = null,
)