package com.example.pokedex.utils

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Handler
import android.os.Looper
import java.net.URL
import java.util.concurrent.Executors

class ImageURL {
    companion object {
        fun loadImageBitmap(url: String, callback: OnImageLoaded) {
            val executor = Executors.newSingleThreadExecutor()
            val handler = Handler(Looper.getMainLooper())
            executor.execute {
                try {
                    val stream = URL(url).openStream()
                    val image = BitmapFactory.decodeStream(stream)

                    handler.post {
                        callback.run(image)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    interface OnImageLoaded {
        fun run(value: Bitmap)
    }

}