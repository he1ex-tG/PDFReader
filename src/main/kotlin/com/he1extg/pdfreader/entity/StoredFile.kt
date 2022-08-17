package com.he1extg.pdfreader.entity

import java.sql.Timestamp
import java.time.LocalDateTime
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.Lob
import javax.persistence.ManyToOne

@Entity
class StoredFile(
    var fileName: String,
    @Lob var file: ByteArray,
    var timestamp: Timestamp = Timestamp.valueOf(LocalDateTime.now()),
    @ManyToOne var owner: User,
    @Id @GeneratedValue var ID: Long? = null,
)