package com.he1extg.pdfreader.ttsprocessing

import java.io.OutputStream

interface TTS {
    fun speakIt(text: String)
    fun toOutputStream(text: String): OutputStream
}