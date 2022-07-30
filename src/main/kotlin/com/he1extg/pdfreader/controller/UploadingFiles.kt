package com.he1extg.pdfreader.controller

import com.he1extg.pdfreader.exception.StorageFileNotFoundException
import com.he1extg.pdfreader.storage.StorageService
import com.he1extg.pdfreader.ttsprocessing.MP3StreamAudioPlayer
import com.sun.speech.freetts.VoiceManager
import com.sun.speech.freetts.audio.SingleFileAudioPlayer
import org.springframework.core.io.Resource
import org.springframework.http.HttpHeaders
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder
import org.springframework.web.servlet.mvc.support.RedirectAttributes
import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.io.OutputStream
import java.util.stream.Collectors
import javax.sound.sampled.AudioFileFormat

@Controller
class UploadingFiles(
    val storageService: StorageService
) {
    @GetMapping("/")
    fun listUploadedFiles(model: Model): String {
        model.addAttribute(
            "files",
            storageService.loadAll().map { it ->
                MvcUriComponentsBuilder.fromMethodName(
                    UploadingFiles::class.java,
                    "serveFile",
                    it.fileName.toString()
                ).build().toUri().toString()
            }.collect(Collectors.toList())
        )


        System.setProperty("freetts.voices", "com.sun.speech.freetts.en.us.cmu_us_kal.KevinVoiceDirectory")
        val voiceManager = VoiceManager.getInstance()
        val voice = voiceManager.getVoice("kevin16")

        //voice.speak("Hello world!")
        val aaaa = MP3StreamAudioPlayer()
        voice.allocate()
        val def = voice.audioPlayer
        voice.audioPlayer = aaaa
        voice.speak("aaaaaaaaaaaaaaaa!")
        val sss = aaaa.outputStream
        voice.audioPlayer = def
        voice.speak()
        //aaaa.close()

        return "uploadForm"
    }

    @GetMapping("/files/{fileName:.+}")
    @ResponseBody
    fun serveFile(@PathVariable fileName: String): ResponseEntity<Resource> {
        val file: Resource = storageService.loadAsResource(fileName)
        return ResponseEntity.ok().header(
            HttpHeaders.CONTENT_DISPOSITION,
            "attachment; filename=\"${file.filename}\""
        ).body(file)
    }

    @PostMapping("/")
    fun handleFileUpload(@RequestParam("file") file: MultipartFile, redirectAttributes: RedirectAttributes): String {
        storageService.store(file)
        redirectAttributes.addFlashAttribute(
            "message",
            "You successfully uploaded ${file.originalFilename}!"
        )
        return "redirect:/"
    }

    @ExceptionHandler(StorageFileNotFoundException::class)
    fun handleStorageFileNotFound(exc: StorageFileNotFoundException?): ResponseEntity<*>? {
        return ResponseEntity.notFound().build<Any>()
    }
}