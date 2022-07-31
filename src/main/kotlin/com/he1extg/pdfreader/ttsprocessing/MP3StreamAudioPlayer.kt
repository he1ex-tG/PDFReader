package com.he1extg.pdfreader.ttsprocessing

import com.sun.speech.freetts.audio.AudioPlayer
import java.io.*
import java.util.*
import javax.sound.sampled.AudioFileFormat
import javax.sound.sampled.AudioFormat
import javax.sound.sampled.AudioInputStream
import javax.sound.sampled.AudioSystem

class MP3StreamAudioPlayer(
    private val mp3StreamWrapper: MP3StreamWrapper
) : AudioPlayer {
    private var currentFormat: AudioFormat = AudioFormat(8000.0F, 16, 1, true, true)
    private var outputData: ByteArray = byteArrayOf()
    private var curIndex = 0
    private var totBytes = 0
    private val outputType: AudioFileFormat.Type = AudioFileFormat.Type.WAVE
    private val outputList: Vector<InputStream> = Vector<InputStream>()

    @Synchronized
    override fun setAudioFormat(format: AudioFormat) {
        currentFormat = format
    }

    override fun getAudioFormat(): AudioFormat {
        return currentFormat
    }

    override fun pause() {}

    @Synchronized
    override fun resume() {
    }

    @Synchronized
    override fun cancel() {
    }

    @Synchronized
    override fun reset() {
    }

    override fun startFirstSampleTimer() {}

    @Synchronized
    override fun close() {}

    override fun getVolume(): Float {
        return 1.0f
    }

    override fun setVolume(volume: Float) {}

    override fun begin(size: Int) {
        outputData = ByteArray(size)
        curIndex = 0
    }

    override fun end(): Boolean {
        outputList.add(ByteArrayInputStream(outputData))
        totBytes += outputData.size
        return true
    }

    override fun drain(): Boolean {
        val iS: InputStream = SequenceInputStream(outputList.elements())
        val inputAStream = AudioInputStream(iS, currentFormat, (totBytes / currentFormat.frameSize).toLong())
        val bos = ByteArrayOutputStream()
        AudioSystem.write(inputAStream, outputType, bos)
        mp3StreamWrapper.inputStream = ByteArrayInputStream(bos.toByteArray())
        return true
    }

    @Synchronized
    override fun getTime(): Long {
        return -1L
    }

    @Synchronized
    override fun resetTime() {
    }

    override fun write(audioData: ByteArray): Boolean {
        return this.write(audioData, 0, audioData.size)
    }

    override fun write(bytes: ByteArray, offset: Int, size: Int): Boolean {
        System.arraycopy(bytes, offset, outputData, curIndex, size)
        curIndex += size
        return true
    }

    override fun toString(): String = ""

    override fun showMetrics() {}
}

class MP3StreamWrapper {
    var inputStream: ByteArrayInputStream = ByteArrayInputStream(byteArrayOf())
}