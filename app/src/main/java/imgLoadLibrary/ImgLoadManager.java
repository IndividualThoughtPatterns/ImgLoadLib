package imgLoadLibrary;

import android.util.Log;
import android.widget.ImageView;

import java.lang.ref.WeakReference;
import java.net.MalformedURLException;
import java.util.HashMap;

public class ImgLoadManager {
    static ImgLoadManager singleton = null;
    private final HashMap<String, ImgLoader> urlToImgLoaderMap = new HashMap<>();

    public static ImgLoadManager with() {
        if (singleton == null) {
            // здесь будет билдер и его метод создания синглтона, если надо будет.
            singleton = new ImgLoadManager();
        }
        return singleton;
    }

    public void load(ImageView imageView, String url) throws MalformedURLException, InterruptedException {
        ImgLoader imgLoader = new ImgLoader();
        urlToImgLoaderMap.put(url, imgLoader);
        imgLoader.load(new WeakReference<>(imageView), url);
    }

    public void logHashMapSize() {
        Log.d("mydebug", urlToImgLoaderMap.size() + " " + urlToImgLoaderMap);
        for (ImgLoader imgLoader : urlToImgLoaderMap.values()) {
            Log.d("mydebug", "thread is alive: " + imgLoader.thread.isAlive());
        }
    }
}
