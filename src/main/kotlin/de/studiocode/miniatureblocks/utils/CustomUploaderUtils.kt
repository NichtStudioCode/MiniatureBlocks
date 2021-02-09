package de.studiocode.miniatureblocks.utils

import com.google.gson.JsonParser
import java.io.File
import java.io.IOException

object CustomUploaderUtils {
    
    fun uploadFile(reqUrl: String, hostUrl: String, key: String, file: File, fileName: String = file.name): String? {
        try {
            val request = HttpMultipartRequest(reqUrl)
            request.addFormField("key", key)
            request.addFormFile("file", file, fileName)
            val response = request.complete()
            if (response.isNotEmpty()) {
                val jsonObject = JsonParser().parse(response[0]).asJsonObject
                val status = jsonObject.get("status").asString
                if (status == "ok") {
                    return "$hostUrl/${jsonObject.get("file").asString}"
                } else throw IOException("Upload failed with non-ok status: $status")
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }
    
    fun deleteFile(reqUrl: String, key: String, fileName: String): Boolean {
        try {
            val request = HttpMultipartRequest(reqUrl)
            request.addFormField("key", key)
            request.addFormField("rmFile", fileName)
            val response = request.complete()
            if (response.isNotEmpty()) {
                val jsonObject = JsonParser().parse(response[0]).asJsonObject
                val status = jsonObject.get("status").asString
                if (status == "ok") return true
                else throw IOException("Deletion failed with non-ok status: $status")
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return false
    }
    
}