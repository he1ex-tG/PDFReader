package com.he1extg.pdfreader.ttsprocessing

import com.sun.speech.freetts.Voice
import com.sun.speech.freetts.VoiceManager
import com.sun.speech.freetts.audio.JavaStreamingAudioPlayer
import org.springframework.stereotype.Service
import java.io.InputStream

@Service
class TTSService : TTS {
    private lateinit var voice: Voice

    private val mp3StreamWrapper = MP3StreamWrapper()
    private val inputStream: InputStream
        get() = mp3StreamWrapper.inputStream

    private val javaStreamAudioPlayer = JavaStreamingAudioPlayer()
    private val mp3StreamAudioPlayer = MP3StreamAudioPlayer(mp3StreamWrapper)

    init {
        System.setProperty("freetts.voices", "com.sun.speech.freetts.en.us.cmu_us_kal.KevinVoiceDirectory")
        val voiceManager = VoiceManager.getInstance()
        voice = voiceManager.getVoice("kevin16")
        voice.allocate()
    }

    override fun speak(text: String) {
        voice.audioPlayer = javaStreamAudioPlayer
        voice.speak(text)
    }

    override fun speak(inputStream: InputStream) {
        voice.audioPlayer = javaStreamAudioPlayer
        voice.speak(inputStream)
    }

    override fun stream(text: String): InputStream {
        voice.audioPlayer = mp3StreamAudioPlayer
        voice.speak(text)
        return inputStream
    }
}