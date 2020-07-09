package de.studiocode.miniatureblocks.utils

import com.google.gson.JsonParser
import org.apache.http.client.methods.HttpPost
import org.apache.http.entity.mime.MultipartEntityBuilder
import org.apache.http.impl.client.HttpClients
import java.io.File
import java.io.InputStreamReader

object FileIOUploadUtils {
    private val postRequest = HttpPost("https://file.io")

    fun uploadFile(file: File?): String? {
        try {
            val builder = MultipartEntityBuilder.create()
            builder.addBinaryBody("file", file)
            val multipart = builder.build()
            postRequest.entity = multipart
            val stream = HttpClients.createDefault().execute(postRequest).entity.content
            val jsonObject = JsonParser().parse(InputStreamReader(stream)).asJsonObject
            if (jsonObject.has("link")) return jsonObject.get("link").asString
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }
}