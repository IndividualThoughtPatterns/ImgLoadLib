package imgLoadLibrary;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.net.MalformedURLException;
import java.net.URL;

public class ImgLoader {
    Thread thread = null; // без private на время тестирования
    private final URL _url;
    boolean loadTried = false;
    private Bitmap bitmap = null;

    ImgLoader(String url) {
        try {
            this._url = new URL(url);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    public void into(ImageView imageView) {
        WeakReference<ImageView> imageViewRef = new WeakReference<>(imageView);
        if (imageViewRef.get() != null) {
            View.OnAttachStateChangeListener onAttachStateChangeListener = new View.OnAttachStateChangeListener() {
                @Override
                public void onViewAttachedToWindow(@NonNull View v) {

                }

                @Override
                public void onViewDetachedFromWindow(@NonNull View v) {
                    if (thread != null && thread.isAlive()) {
                        thread.interrupt();
                    }
                }
            };

            imageViewRef.get().addOnAttachStateChangeListener(onAttachStateChangeListener);

            if (loadTried == false) {
                Log.d("mydebug", "работает загрузка по сети");
                loadTried = true; // если картинка еще не была загружена, то сделать запрос
                thread = new Thread(() -> {
                    try (BufferedInputStream bufferedInputStream = new BufferedInputStream(_url.openStream())) {
                        bitmap = BitmapFactory.decodeStream(bufferedInputStream);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }

                    imageViewRef.get().post(() -> imageViewRef.get().setImageBitmap(bitmap));
                });
                thread.start();
            } else { // если запрос картинки уже был сделан, то заполнить ImageView тем, что есть
                imageViewRef.get().setImageBitmap(bitmap);
            }

            imageViewRef.get().removeOnAttachStateChangeListener(onAttachStateChangeListener);
        }
    }
}
