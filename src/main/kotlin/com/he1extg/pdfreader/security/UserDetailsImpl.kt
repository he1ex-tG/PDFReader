package com.he1extg.pdfreader.security

import com.he1extg.pdfreader.entity.User
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

class UserDetailsImpl(
    user: User,
) : UserDetails {

    private val username = user.login
    private val password = user.password
    private val authorities = user.role?.authorities ?: UserRole.USER.authorities
    private val isActive = user.status == UserStatus.ACTIVE

    override fun getAuthorities(): MutableCollection<out GrantedAuthority> = authorities.toMutableList()
    override fun getPassword(): String = password
    override fun getUsername(): String = username
    override fun isAccountNonExpired(): Boolean = isActive
    override fun isAccountNonLocked(): Boolean = isActive
    override fun isCredentialsNonExpired(): Boolean = isActive
    override fun isEnabled(): Boolean = isActive
}