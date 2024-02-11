package com.lora.skylink.common

import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.DiffUtil


inline fun <T : Any> basicDiffUtil(
    crossinline areItemsTheSame: (T, T) -> Boolean = { old, new -> old == new },
    crossinline areContentsTheSame: (T, T) -> Boolean = { old, new -> old == new }
) = object : DiffUtil.ItemCallback<T>() {
    override fun areItemsTheSame(oldItem: T, newItem: T): Boolean =
        areItemsTheSame(oldItem, newItem)

    override fun areContentsTheSame(oldItem: T, newItem: T): Boolean =
        areContentsTheSame(oldItem, newItem)
}
fun loge(text: String){
    Log.e("joshtag", "FLUGEL - " + text)
}
fun logd(text: String){
    Log.d("joshtag", "FLUGEL - " + text)
}
fun logi(text: String){
    Log.i("joshtag", "FLUGEL - " + text)
}
fun logw(text: String){
    Log.w("joshtag", "FLUGEL - " + text)
}
fun logv(text: String){
    Log.v("joshtag", "FLUGEL - " + text)
}