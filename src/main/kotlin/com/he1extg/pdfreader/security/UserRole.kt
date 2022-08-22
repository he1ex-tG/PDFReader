package com.he1extg.pdfreader.security

import org.springframework.security.core.authority.SimpleGrantedAuthority
import java.util.stream.Collectors

enum class UserRole(
    private val permissions: Set<UserPermission>
) {
    USER(setOf(UserPermission.DEFAULT));

    val authorities: Set<SimpleGrantedAuthority>
        get() = permissions.stream()
            .map { SimpleGrantedAuthority(it.permission) }
            .collect(Collectors.toSet())
}