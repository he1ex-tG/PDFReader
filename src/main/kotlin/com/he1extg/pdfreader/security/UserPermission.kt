package com.he1extg.pdfreader.security

enum class UserPermission(
    val permission: String,
) {
    ROLE_ADMIN("role_admin"),
    ROLE_DEFAULT("role_default");
}