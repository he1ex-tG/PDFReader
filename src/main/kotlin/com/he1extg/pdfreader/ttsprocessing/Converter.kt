package com.he1extg.pdfreader.ttsprocessing

import java.io.InputStream

interface Converter {
    fun convert(inputStream: InputStream): InputStream
}