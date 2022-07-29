package com.he1extg.pdfreader.ttsprocessing

import java.io.OutputStream

interface TTS {
    fun speak(text: String)
    fun stream(text: String): OutputStream
}