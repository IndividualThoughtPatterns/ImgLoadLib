package imgLoadLibrary

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import android.util.LruCache
import android.view.View
import android.view.View.OnAttachStateChangeListener
import android.widget.ImageView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.BufferedInputStream
import java.io.IOException
import java.lang.ref.WeakReference
import java.net.URL
import java.util.concurrent.ConcurrentHashMap
import java.util.function.Consumer
import kotlin.coroutines.ContinuationInterceptor
import kotlin.coroutines.EmptyCoroutineContext

class ImgLoadManager private constructor() {
    private val maxMemory = (Runtime.getRuntime().maxMemory() / 1024).toInt()
    private val cacheSize = maxMemory / 8
    private val urlSettedMap = ConcurrentHashMap<String, Boolean?>()
    private val urlToBitmapMapCache: LruCache<String, Bitmap> =
        object : LruCache<String, Bitmap>(cacheSize) {
            override fun sizeOf(key: String, bitmap: Bitmap): Int {
                return bitmap.byteCount / 1024
            }
        }
    private val urlToImageSetterMap = HashMap<String, MutableList<ImageSetter>?>()

    private val scope = CoroutineScope(EmptyCoroutineContext) // Dispatchers.IO

    fun logCacheSize() {
        Log.d("mydebug", "cache size: " + urlToBitmapMapCache.size())
    }

    fun load(url: String, imageView: ImageView, i: Int) {
        val imageViewRef = WeakReference(imageView)
        val job: Job = scope.launch {
            if (isActive) {

                getBitmapFromCacheOrDownload(url, imageViewRef)

            }
        }

        imageViewRef.get()!!.addOnAttachStateChangeListener(object : OnAttachStateChangeListener {
            override fun onViewAttachedToWindow(v: View) {
            }

            override fun onViewDetachedFromWindow(v: View) {
                job.cancel()
                //                urlToImageSetterMap.get(url).clear();
//                urlToImageSetterMap.remove(url);
            }
        })
    }

    private suspend fun getBitmapFromCacheOrDownload(
        url: String,
        imageViewRef: WeakReference<ImageView>
    ) {
        withContext(Dispatchers.IO) {
            println("loading: " + " on thread " + Thread.currentThread().name + " dispatcher: " + coroutineContext[ContinuationInterceptor])
            val cached = urlToBitmapMapCache[url]

            if (urlToImageSetterMap[url] == null) {
                urlToImageSetterMap[url] = ArrayList()
            }
            val imageSetter = ImageSetter(imageViewRef)
            urlToImageSetterMap[url]!!.add(imageSetter)

            if (cached != null) {
                Log.d("mydebug", "cache hit")
                imageSetter.setImage(cached)
            } else if (urlSettedMap[url] == null) {
                urlSettedMap[url] = true
                Log.d("mydebug", "UrlSetted")

                try {
                    BufferedInputStream(URL(url).openStream()).use { bufferedInputStream ->
                        val bitmap = BitmapFactory.decodeStream(bufferedInputStream)
                        Log.d("mydebug", Thread.currentThread().name + " network download: " + url)
                        if (bitmap != null) {
                            urlToBitmapMapCache.put(url, bitmap.copy(bitmap.config, true))
                            Log.d("mydebug", "putted")
                            urlToImageSetterMap[url]!!
                                .forEach(Consumer { IS: ImageSetter -> IS.setImage(bitmap) })
                        }
                    }
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
            println("loading of " + " on thread " + Thread.currentThread().name + " dispatcher: " + coroutineContext[ContinuationInterceptor] + " completed!")
        }
    }

    companion object {
        private var singleton: ImgLoadManager? = null

        @Synchronized
        fun get(): ImgLoadManager? {
            if (singleton == null) {
                singleton = ImgLoadManager()
            }
            return singleton
        }
    }
}
