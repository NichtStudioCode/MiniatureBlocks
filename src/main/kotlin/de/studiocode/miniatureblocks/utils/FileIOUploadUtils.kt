package de.studiocode.miniatureblocks.utils

import com.google.gson.JsonParser
import java.io.File

object FileIOUploadUtils {
    
    private const val REQUEST_URL = "https://file.io"
    
    fun uploadToFileIO(file: File): String? {
        try {
            val request = HttpMultipartRequest(REQUEST_URL)
            request.addFormFile("file", file)
            val response = request.complete()
            if (response.isNotEmpty()) {
                val jsonObject = JsonParser().parse(response[0]).asJsonObject
                if (jsonObject.has("link")) return jsonObject.get("link").asString
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }
    
}