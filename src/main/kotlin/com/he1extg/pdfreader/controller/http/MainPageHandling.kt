package com.he1extg.pdfreader.controller.http

import com.he1extg.pdfreader.storage.FileHandler
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.servlet.mvc.support.RedirectAttributes
import java.util.stream.Collectors

@Controller
class MainPageHandling(
    val fileHandler: FileHandler
) {
    @GetMapping("/")
    fun listUploadedFiles(model: Model): String {
        model.addAttribute(
            "files",
            fileHandler.loadAllAsModelInfo().collect(Collectors.toList())
        )
        return "index"
    }

    @GetMapping("/play/{fileName:.+}")
    fun playFile(@PathVariable fileName: String): String {
        fileHandler.playAudioFile(fileName)
        return "redirect:/"
    }

    @PostMapping("/")
    fun handleFileUpload(@RequestParam("file") file: MultipartFile, redirectAttributes: RedirectAttributes): String {
        if (!file.isEmpty) {
            fileHandler.storeAsMP3(file)
        }
        return "redirect:/"
    }
}