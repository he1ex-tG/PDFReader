package com.he1extg.pdfreader.entity

import com.he1extg.pdfreader.security.UserRole
import com.he1extg.pdfreader.security.UserStatus
import javax.persistence.*

@Entity
@Table(name = "Users")
class User(
    var login: String,
    var password: String,
    @Enumerated(value = EnumType.STRING) var role: UserRole? = null,
    @Enumerated(value = EnumType.STRING) var status: UserStatus? = null,
    @Id @GeneratedValue var ID: Long? = null,
)