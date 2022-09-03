package com.he1extg.pdfreader.security

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.annotation.Order
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.web.servlet.invoke
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.DelegatingPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.util.matcher.AntPathRequestMatcher

@Configuration
@EnableWebSecurity
class SecurityConfig {

    @Order(1)
    @Bean
    fun adminApiFilterChain(http: HttpSecurity): SecurityFilterChain {
        http.invoke {
            securityMatcher("/users")
            csrf {
                disable()
            }
            /* Anonymous users can add new users */
            authorizeRequests {
                authorize(HttpMethod.POST, "/users", permitAll)
            }
            /* Any another operation e.g. list or delete - only for admins */
            authorizeRequests {
                authorize(anyRequest, hasAuthority(UserPermission.ROLE_ADMIN.permission))
            }
        }
        return http.build()
    }

    @Bean
    fun httpFilterChain(http: HttpSecurity): SecurityFilterChain {
        http.invoke {
            csrf {
                disable()
            }
            anonymous {
                principal = "guest"
            }
            /* Default politic is permitAll */
            authorizeRequests {
                authorize("/**", permitAll)
            }
            formLogin {
                permitAll()
                loginPage = "/users/login"
                defaultSuccessUrl("/", true)
            }
            logout {
                logoutRequestMatcher = AntPathRequestMatcher("/users/logout")
                logoutSuccessUrl = "/users/login?logout"
                invalidateHttpSession = true
                clearAuthentication = true
                deleteCookies("JSESSIONID")
            }
        }
        return http.build()
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        val idForEncode = "bcrypt"
        val encoders: Map<String, PasswordEncoder> = mapOf(
            idForEncode to BCryptPasswordEncoder(12)
        )
        return DelegatingPasswordEncoder(idForEncode, encoders)
    }

}