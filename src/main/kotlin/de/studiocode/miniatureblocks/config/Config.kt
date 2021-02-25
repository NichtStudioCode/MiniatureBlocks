package de.studiocode.miniatureblocks.config

import org.bukkit.plugin.java.JavaPlugin

class Config(private val plugin: JavaPlugin) {
    
    companion object {
        
        const val CUSTOM_UPLOADER_REQUEST_URL = "custom-uploader-request"
        const val CUSTOM_UPLOADER_HOST_URL = "custom-uploader-host"
        const val CUSTOM_UPLOADER_KEY = "custom-uploader-key"
        
    }
    
    private val config = plugin.config
    
    init {
        config.options().copyDefaults(true)
        plugin.saveDefaultConfig()
        
        save()
    }
    
    fun hasCustomUploader(): Boolean = hasCustomUploaderRequest() && hasCustomUploaderHost() && hasCustomUploaderKey()
    
    private fun hasCustomUploaderRequest(): Boolean =
        config.isSet(CUSTOM_UPLOADER_REQUEST_URL) && config.getString(CUSTOM_UPLOADER_REQUEST_URL)
            ?.isNotEmpty() ?: false
    
    fun getCustomUploaderRequest(): String = config.getString(CUSTOM_UPLOADER_REQUEST_URL) ?: ""
    
    private fun hasCustomUploaderHost(): Boolean =
        config.isSet(CUSTOM_UPLOADER_HOST_URL) && config.getString(CUSTOM_UPLOADER_HOST_URL)?.isNotEmpty() ?: false
    
    fun getCustomUploaderHost(): String = config.getString(CUSTOM_UPLOADER_HOST_URL) ?: ""
    
    private fun hasCustomUploaderKey(): Boolean =
        config.isSet(CUSTOM_UPLOADER_KEY) && config.getString(CUSTOM_UPLOADER_KEY)?.isNotEmpty() ?: false
    
    fun getCustomUploaderKey(): String = config.getString(CUSTOM_UPLOADER_KEY) ?: ""
    
    private fun save() = plugin.saveConfig()
    
}