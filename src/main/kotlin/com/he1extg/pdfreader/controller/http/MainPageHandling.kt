package com.he1extg.pdfreader.controller.http

import com.he1extg.pdfreader.controller.rest.FileOperations
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContext
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.context.HttpSessionSecurityContextRepository
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.servlet.mvc.support.RedirectAttributes
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpSession
import javax.servlet.http.HttpSessionEvent

@Controller
class MainPageHandling(
    val fileOperations: FileOperations
) {
    @GetMapping("/")
    fun listUploadedFiles(model: Model): String {
        model.addAttribute(
            "files",
            fileOperations.getFilesList().filesInfo
        )
        return "index"
    }

    @PostMapping("/")
    fun handleFileUpload(@RequestParam("file") file: MultipartFile, redirectAttributes: RedirectAttributes): String {
        if (!file.isEmpty) {
            fileOperations.uploadPdfAndConvertToMP3(file)
        }
        return "redirect:/"
    }

    @GetMapping("/auth/login")
    fun signin(): String {
        return "login"
    }

    @GetMapping("/test")
    fun logout(request: HttpServletRequest): String {
        /*val authentication: Authentication? = SecurityContextHolder.getContext().authentication
        if (authentication != null) {
            request.session.invalidate()
        }*/
        // ?logout where? ))
        // return "redirect:/auth/login?logout"
        println("Session ID in request: ${request.session.id}")
        val mContext = SecurityContextHolder.getContext()
        val mHttpSess = request.session
        println("Session ID in security: ${SecurityContextHolder.getContext().authentication}")
        return "redirect:/"
    }
}