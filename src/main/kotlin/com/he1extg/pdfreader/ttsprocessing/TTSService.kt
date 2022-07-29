package com.he1extg.pdfreader.ttsprocessing

import com.sun.speech.freetts.Voice
import com.sun.speech.freetts.VoiceManager
import org.springframework.stereotype.Service
import java.io.OutputStream

@Service
class TTSService : TTS {
    private lateinit var voice: Voice

    init {
        System.setProperty("freetts.voices", "com.sun.speech.freetts.en.us.cmu_us_kal.KevinVoiceDirectory")
        val voiceManager = VoiceManager.getInstance()
        voice = voiceManager.getVoice("kevin16")
        voice.allocate()
    }

    override fun speak(text: String) {
        TODO("Searching a good tts")
    }

    override fun stream(text: String): OutputStream {
        TODO("Searching a good tts")
    }
}