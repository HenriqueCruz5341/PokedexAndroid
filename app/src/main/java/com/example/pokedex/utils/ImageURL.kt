package com.example.pokedex.utils

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Handler
import android.os.Looper
import java.net.URL
import java.util.concurrent.Executors

/**
 * A ImageURL class to load an image from a URL.
 *
 * This class is used to load an image from a URL with no need to create a new instance of the class.
 * This class is used to avoid the repetition of code.
 * This class allows the user to pass a callback to run when the image is loaded.
 */
class ImageURL {
    companion object {
        /**
         * This function is used to load an image from a URL
         * @param url The url of the image
         * @param callback The callback to run when the image is loaded
         */
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

    /**
     * This interface is used to run a function when the image is loaded
     */
    interface OnImageLoaded {
        fun run(value: Bitmap)
    }

}