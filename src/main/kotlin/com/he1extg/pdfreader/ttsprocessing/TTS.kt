package com.he1extg.pdfreader.ttsprocessing

import com.sun.speech.freetts.Voice
import com.sun.speech.freetts.VoiceManager

class TTS {
    private lateinit var voice: Voice

    init {
        System.setProperty("freetts.voices", "com.sun.speech.freetts.en.us.cmu_us_kal.KevinVoiceDirectory")
        val voiceManager = VoiceManager.getInstance()
        voice = voiceManager.getVoice("kevin16")
        voice.allocate()
    }

    fun speak(text: String) {
        voice.speak(text)
    }
}