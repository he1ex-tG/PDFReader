package com.he1extg.pdfreader.ttsprocessing

import java.io.InputStream

interface TTS {
    fun speak(text: String)
    fun stream(text: String): InputStream
}