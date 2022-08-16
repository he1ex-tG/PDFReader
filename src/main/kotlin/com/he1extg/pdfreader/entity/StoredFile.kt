package com.he1extg.pdfreader.entity

import java.io.File
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.ManyToOne

@Entity
class StoredFile(
    var fileName: String,
    var file: File,
    @ManyToOne var owner: User,
    @Id @GeneratedValue var ID: Long? = null,
)