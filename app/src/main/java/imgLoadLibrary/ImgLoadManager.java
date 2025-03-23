package imgLoadLibrary;

import android.util.Log;
import android.util.LruCache;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ImgLoadManager {
    static ImgLoadManager singleton = null;
    int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
    int cacheSize = maxMemory / 8;
    private final LruCache<String, ImgLoader> urlToImgLoaderMapCache = new LruCache<>(cacheSize);

    public int getEvictionCount() {
        return this.urlToImgLoaderMapCache.evictionCount();
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

    public ImgLoader load(String url) {
        ImgLoader imgLoader = urlToImgLoaderMapCache.get(url);
        if (imgLoader == null) {
            imgLoader = new ImgLoader(url, threadPoolExecutor);
            urlToImgLoaderMapCache.put(url, imgLoader);
            Log.d("mydebug", "loaded from network " + url);
        } else {
            Log.d("mydebug", "loaded from cache " + url);
        }
        Log.d("mydebug", "lru size: " + urlToImgLoaderMapCache.size() + "");
        urlToImgLoaderMapCache.trimToSize(cacheSize);
        Log.d("mydebug", "eviction count: " + urlToImgLoaderMapCache.evictionCount());
        return imgLoader;
    }
}
