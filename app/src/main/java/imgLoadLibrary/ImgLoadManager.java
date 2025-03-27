package imgLoadLibrary;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.util.LruCache;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ImgLoadManager {
    static ImgLoadManager singleton = null;
    int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
    int cacheSize = maxMemory / 8;
    private final LruCache<String, Bitmap> urlToImgLoaderMapCache = new LruCache<String, Bitmap>(cacheSize) {
        @Override
        protected int sizeOf(String key, Bitmap bitmap) {
            return bitmap.getByteCount() / 1024;
        }
    };

    ExecutorService threadPoolExecutor = new ThreadPoolExecutor(
            10,
            20,
            3000,
            TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<>()
    );

    private ImgLoadManager() {
    }

    public void logCacheSize() {
        Log.d("mydebug", "cache size: " + this.urlToImgLoaderMapCache.size());
    }

    public static ImgLoadManager get() {
        if (singleton == null) {
            singleton = new ImgLoadManager();
        }
        return singleton;
    }

    public void load(String url, ImageView imageView) {
        Bitmap cached = urlToImgLoaderMapCache.get(url);
        WeakReference<ImageView> imageViewRef = new WeakReference<>(imageView);
        if (cached == null) {
            Log.d("mydebug", "network download: " + url);

            Future future = threadPoolExecutor.submit(() -> {
                try (BufferedInputStream bufferedInputStream = new BufferedInputStream(new URL(url).openStream())) {
                    Bitmap bitmap = BitmapFactory.decodeStream(bufferedInputStream);

                    imageViewRef.get().post(() -> {
                        if (bitmap != null) {
                            urlToImgLoaderMapCache.put(url, bitmap.copy(bitmap.getConfig(), true));
                            imageViewRef.get().setImageBitmap(bitmap);
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });

            imageViewRef.get().addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() {
                @Override
                public void onViewAttachedToWindow(@NonNull View v) {

                }

                @Override
                public void onViewDetachedFromWindow(@NonNull View v) {
                    if (future != null) {
                        future.cancel(true);
                    }
                }
            });
        } else {
            Log.d("mydebug", "byte count: " + cached.getByteCount());
            imageViewRef.get().setImageBitmap(cached);
            Log.d("mydebug", "loaded from cache " + url);
        }
        Log.d("mydebug", "lru size: " + urlToImgLoaderMapCache.size() + "");
        Log.d("mydebug", "eviction count: " + urlToImgLoaderMapCache.evictionCount());
    }
}
