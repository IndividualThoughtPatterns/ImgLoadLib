package imgLoadLibrary;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class ImgLoader {
    private final ImageView imageView;
    private URL _url = null;

    public ImgLoader(ImageView imageView, String url) {
        this.imageView = imageView;
        try {
            this._url = new URL(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    public void load() {
        new Thread(() -> {
            Bitmap bitmap;
            try (BufferedInputStream bufferedInputStream = new BufferedInputStream(_url.openStream())) {
                bitmap = BitmapFactory.decodeStream(bufferedInputStream);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            imageView.post(() -> imageView.setImageBitmap(bitmap));
        }).start();
    }
}
