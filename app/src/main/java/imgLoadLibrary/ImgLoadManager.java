package imgLoadLibrary;

import android.util.Log;

import java.util.HashMap;

public class ImgLoadManager {
    static ImgLoadManager singleton = null;
    private final HashMap<String, ImgLoader> urlToImgLoaderMap = new HashMap<>();

    // можно пока переименовать в "get", т.к. нет параметров, которые сюда передаются
    public static ImgLoadManager with() {
        if (singleton == null) {
            // здесь будет билдер и его метод создания синглтона, если надо будет.
            singleton = new ImgLoadManager();
        }
        return singleton;
    }

    public ImgLoader load(String url) {
        ImgLoader imgLoader;
        if (urlToImgLoaderMap.containsKey(url)) {
            imgLoader = urlToImgLoaderMap.get(url);
        } else {
            imgLoader = new ImgLoader(url);
            urlToImgLoaderMap.put(url, imgLoader);
        }
        return imgLoader;
    }

    // вспомогательная функция для тестирования
    public void logHashMapSize() {
        Log.d("mydebug", urlToImgLoaderMap.size() + " " + urlToImgLoaderMap);
        for (ImgLoader imgLoader : urlToImgLoaderMap.values()) {
            Log.d("mydebug", "thread is alive: " + imgLoader.thread.isAlive());
        }
    }
}
