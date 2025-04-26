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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ImgLoadManager {
    private static ImgLoadManager singleton = null;
    private int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
    private int cacheSize = maxMemory / 8;
    private ConcurrentHashMap<String, Boolean> urlSettedMap = new ConcurrentHashMap<>();
    private final LruCache<String, Bitmap> urlToBitmapMapCache = new LruCache<String, Bitmap>(cacheSize) {
        @Override
        protected int sizeOf(String key, Bitmap bitmap) {
            return bitmap.getByteCount() / 1024;
        }
    };
    private HashMap<String, List<ImageSetter>> urlToImageSetterMap = new HashMap<>();

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
        Log.d("mydebug", "cache size: " + this.urlToBitmapMapCache.size());
    }

    public static synchronized ImgLoadManager get() {
        if (singleton == null) {
            singleton = new ImgLoadManager();
        }
        return singleton;
    }

    public void load(String url, ImageView imageView) {
        WeakReference<ImageView> imageViewRef = new WeakReference<>(imageView);
        Future future = threadPoolExecutor.submit(() -> {
            // Log.d("mydebug", Thread.currentThread().getName() + " load " );
            getBitmapFromCacheOrDownload(url, imageViewRef);
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
//                urlToImageSetterMap.get(url).clear();
//                urlToImageSetterMap.remove(url);
            }
        });
    }

    private void getBitmapFromCacheOrDownload(String url, WeakReference<ImageView> imageViewRef) {
        Bitmap cached = urlToBitmapMapCache.get(url);

        if (urlToImageSetterMap.get(url) == null) {
            urlToImageSetterMap.put(url, new ArrayList<>());
        }
        ImageSetter imageSetter = new ImageSetter(url, imageViewRef);
        urlToImageSetterMap.get(url).add(imageSetter);

        if (cached != null) {
            Log.d("mydebug", "cache hit");
            imageSetter.setImage(cached);
        } else if (urlSettedMap.get(url) == null) {
            urlSettedMap.put(url, true);
            Log.d("mydebug", "UrlSetted");

            try (BufferedInputStream bufferedInputStream = new BufferedInputStream(new URL(url).openStream())) {
                Bitmap bitmap = BitmapFactory.decodeStream(bufferedInputStream);
                Log.d("mydebug", Thread.currentThread().getName() + " network download: " + url);

                if (bitmap != null) {
                    urlToBitmapMapCache.put(url, bitmap.copy(bitmap.getConfig(), true));
                    Log.d("mydebug", "putted");
                    urlToImageSetterMap.get(url).forEach(IS -> IS.setImage(bitmap));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
