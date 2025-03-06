package imgLoadLibrary;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.net.MalformedURLException;
import java.net.URL;

public class ImgLoader {
    Thread thread = null;

    public void load(WeakReference<ImageView> imageViewRef, String url) throws MalformedURLException, InterruptedException {
        URL _url = new URL(url);
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

            thread = new Thread(() -> {
                Bitmap bitmap;
                try (BufferedInputStream bufferedInputStream = new BufferedInputStream(_url.openStream())) {
                    bitmap = BitmapFactory.decodeStream(bufferedInputStream);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

                imageViewRef.get().post(() -> imageViewRef.get().setImageBitmap(bitmap));
            });
            thread.start();
            imageViewRef.get().removeOnAttachStateChangeListener(onAttachStateChangeListener);
        }
    }
}
