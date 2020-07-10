package de.studiocode.miniatureblocks.utils

import java.io.*
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLConnection
import java.nio.charset.Charset

class HttpMultipartRequest(requestUrl: String, private val charset: Charset = Charsets.UTF_8) {

    private val connection = URL(requestUrl).openConnection() as HttpURLConnection
    private val boundary = StringUtils.random(25)
    private val outputStream: OutputStream
    private val writer: PrintWriter

    init {
        connection.useCaches = true
        connection.doOutput = true
        connection.doInput = true
        connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=$boundary")

        outputStream = connection.outputStream
        writer = PrintWriter(OutputStreamWriter(outputStream, charset))
    }

    fun addHeaderField(name: String, value: String) {
        writer.printlnAndFlush("$name: $value")
    }

    fun addFormField(name: String, value: String) {
        writer.println("--$boundary")
        writer.println("Content-Disposition: form-data; name=$name")
        writer.println("Content-Type: text/plain; charset=$charset")
        writer.println()
        writer.printlnAndFlush(value)
    }

    fun addFormFile(fieldName: String, file: File) {
        val fileName = file.name
        writer.println("--$boundary")
        writer.println("Content-Disposition: form-data; name=$fieldName; filename=$fileName")
        writer.println("Content-Type: " + URLConnection.guessContentTypeFromName(fileName))
        writer.println("Content-Transfer-Encoding: binary")
        writer.printlnAndFlush()

        val inputStream = file.inputStream()
        inputStream.copyTo(outputStream)
        outputStream.flush()
        inputStream.close()

        writer.printlnAndFlush()
    }

    fun complete(): List<String> {
        writer.printlnAndFlush()
        writer.println("--$boundary--")
        writer.close()

        val responseCode = connection.responseCode
        if (responseCode == HttpURLConnection.HTTP_OK) {
            val reader = connection.inputStream.bufferedReader(charset)
            return reader.readLines()
        } else throw IOException("Server responded with non-OK status: $responseCode")
    }

    private fun PrintWriter.printlnAndFlush() {
        println()
        flush()
    }

    private fun PrintWriter.printlnAndFlush(string: String) {
        println(string)
        flush()
    }

}