package com.he1extg.pdfreader.ttsprocessing

import com.sun.speech.freetts.Voice
import com.sun.speech.freetts.VoiceManager
import com.sun.speech.freetts.audio.AudioPlayer
import com.sun.speech.freetts.audio.JavaClipAudioPlayer
import com.sun.speech.freetts.audio.JavaStreamingAudioPlayer
import com.sun.speech.freetts.audio.MultiFileAudioPlayer
import com.sun.speech.freetts.audio.NullAudioPlayer
import com.sun.speech.freetts.audio.RawFileAudioPlayer
import com.sun.speech.freetts.audio.SingleFileAudioPlayer
import org.springframework.stereotype.Service
import java.io.ByteArrayInputStream
import java.io.File
import java.io.InputStream
import java.io.OutputStream
import java.util.*
import javax.sound.sampled.AudioFormat
import javax.speech.Central
import javax.speech.synthesis.SpeakableEvent
import javax.speech.synthesis.SpeakableListener
import javax.speech.synthesis.Synthesizer
import javax.speech.synthesis.SynthesizerModeDesc

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

    override fun stream(text: String): InputStream {
        voice.audioPlayer = mp3StreamAudioPlayer
        voice.speak(text)
        return inputStream
    }
}