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

    init {
        System.setProperty("freetts.voices", "com.sun.speech.freetts.en.us.cmu_us_kal.KevinVoiceDirectory")
        val voiceManager = VoiceManager.getInstance()
        val voice = voiceManager.getVoice("kevin16")
        voice.speak("Hello world!")
        voice.audioPlayer = NullAudioPlayer()
        voice.speak("aaaaaaaaaaaaaaaa!")
        voice.allocate()
    }

    override fun speak(text: String) {
        TODO("Searching a good tts")
    }

    override fun stream(text: String): OutputStream {
        TODO("Searching a good tts")
    }
}