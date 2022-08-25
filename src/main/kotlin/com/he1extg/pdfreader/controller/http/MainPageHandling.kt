package com.he1extg.pdfreader.controller.http

import com.he1extg.pdfreader.controller.rest.FileOperations
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.servlet.mvc.support.RedirectAttributes
import javax.servlet.http.HttpServletRequest

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

    @GetMapping("/auth/logout")
    fun logout(request: HttpServletRequest): String {
        val authentication: Authentication? = SecurityContextHolder.getContext().authentication
        if (authentication != null) {
            request.session.invalidate()
        }
        // ?logout where? ))
        // return "redirect:/auth/login?logout"
        return "redirect:/"
    }
}