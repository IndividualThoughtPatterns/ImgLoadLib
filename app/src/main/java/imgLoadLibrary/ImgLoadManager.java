package imgLoadLibrary;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.util.LruCache;
import android.widget.ImageView;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.net.URL;
import java.util.concurrent.ExecutorService;
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

    public void getCacheSize() {
        Log.d("mydebug", "cache size: " + this.urlToImgLoaderMapCache.size());
    }

    ExecutorService threadPoolExecutor = new ThreadPoolExecutor(
            10,
            20,
            3000,
            TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<>()
    );

    private ImgLoadManager() {
    }

    // можно пока переименовать в "get", т.к. нет параметров, которые сюда передаются
    public static ImgLoadManager with() {
        if (singleton == null) {
            // здесь будет билдер и его метод создания синглтона, если надо будет.
            singleton = new ImgLoadManager();
        }
        return singleton;
    }

    public void load(String url, ImageView imageView) {
        Bitmap cached = urlToImgLoaderMapCache.get(url);
        WeakReference<ImageView> imageViewRef = new WeakReference<>(imageView);
        if (cached == null) {
            Log.d("mydebug", "работает загрузка по сети " + url);

            threadPoolExecutor.execute(() -> {
                try (BufferedInputStream bufferedInputStream = new BufferedInputStream(new URL(url).openStream())) {
                    Bitmap bitmap = BitmapFactory.decodeStream(bufferedInputStream);

                    imageViewRef.get().post(() -> {
                        urlToImgLoaderMapCache.put(url, bitmap.copy(bitmap.getConfig(), true));
                        imageViewRef.get().setImageBitmap(bitmap);
                    });
                } catch (IOException e) {
                    throw new RuntimeException(e);
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
