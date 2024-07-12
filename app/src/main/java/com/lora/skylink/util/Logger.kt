package com.lora.skylink.util

interface Logger {
    fun loge(text: String)
    fun logd(text: String)
    fun logi(text: String)
    fun logw(text: String)
}

object AppLogger : Logger {

    const val TAG = "joshtag"
    val red = "\u001b[31m"

    override fun logd(text: String) {
        println("$TAG DEBUG: $text")
    }
    override fun loge(text: String) {
        println("$red$TAG ERROR: $text")
    }
    override fun logi(text: String) {
        println("$red$TAG INFO: $text")
    }

    override fun logw(text: String) {
        println("$TAG WARN: $text")
    }
}