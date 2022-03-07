package com.programmers.kmooc.network

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import kotlinx.coroutines.*
import java.lang.Exception
import java.net.URL

object ImageLoader {
    private val imageCache = mutableMapOf<String, Bitmap>()

    fun loadImage(url: String, completed: (Bitmap?) -> Unit) {
        if (url.isEmpty()) {
            completed(null)
            return
        }

        // 이미지가 이미 존재
        if (imageCache.containsKey(url)) {
            completed(imageCache[url])
            return
        }

        // 이미지 다운
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val bitmap = BitmapFactory.decodeStream(URL(url).openStream())
                imageCache[url] = bitmap

                withContext(Dispatchers.Main) {
                    completed(bitmap)
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    completed(null)
                }
            }
        }
    }
}