package com.he1extg.pdfreader.ttsprocessing

import com.sun.speech.freetts.audio.AudioPlayer
import com.sun.speech.freetts.util.Utilities
import java.io.*
import java.util.*
import javax.sound.sampled.AudioFileFormat
import javax.sound.sampled.AudioFormat
import javax.sound.sampled.AudioInputStream
import javax.sound.sampled.AudioSystem

class MP3StreamAudioPlayer : AudioPlayer {
    private var currentFormat: AudioFormat? = null
    private val baseName: String = ""
    private var outputData: ByteArray = byteArrayOf()
    private var curIndex = 0
    private var totBytes = 0
    private val outputType: AudioFileFormat.Type = AudioFileFormat.Type.WAVE
    private val outputList: Vector<InputStream> = Vector<InputStream>()

    var outputStream: ByteArrayOutputStream = ByteArrayOutputStream()
        private set

    @Synchronized
    override fun setAudioFormat(format: AudioFormat) {
        currentFormat = format
    }

    override fun getAudioFormat(): AudioFormat {
        return currentFormat!!
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
    override fun close() {
        try {
            val file = File(baseName)
            val iS: InputStream = SequenceInputStream(outputList.elements())
            val ais = AudioInputStream(iS, currentFormat, (totBytes / currentFormat!!.frameSize).toLong())
            println("Wrote synthesized speech to " + baseName)
            //AudioSystem.write(ais, outputType, file)
            AudioSystem.write(ais, outputType, outputStream)
        } catch (var4: IOException) {
            System.err.println("Can't write audio to " + baseName)
        } catch (var5: IllegalArgumentException) {
            System.err.println("Can't write audio type " + outputType)
        }
    }

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

    override fun toString(): String = outputStream.toString()

    override fun showMetrics() {}
}