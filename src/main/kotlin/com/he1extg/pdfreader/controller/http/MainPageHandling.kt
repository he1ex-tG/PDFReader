package com.he1extg.pdfreader.controller.http

import com.he1extg.pdfreader.controller.rest.FileOperations
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.servlet.mvc.support.RedirectAttributes

@Controller
class MainPageHandling(
    val fileOperations: FileOperations
) {
    @GetMapping("/")
    fun listUploadedFiles(model: Model): String {
        model.addAttribute(
            "files",
            fileOperations.getFileList().filesInfo
        )
        return "index"
    }

    @PostMapping("/")
    fun handleFileUpload(@RequestParam("file") file: MultipartFile, redirectAttributes: RedirectAttributes): String {
        if (!file.isEmpty) {
            fileOperations.saveFile(file)
        }
        return "redirect:/"
    }

    @GetMapping("/users/login")
    fun signin(): String {
        return "login"
    }
}