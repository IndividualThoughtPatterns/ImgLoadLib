package imgLoadLibrary;

import android.graphics.Bitmap;
import android.widget.ImageView;

import java.lang.ref.WeakReference;

public class ImageSetter {
    public WeakReference<ImageView> imageViewRef;
    public String url;

    ImageSetter(String url, WeakReference<ImageView> imageViewRef) {
        this.imageViewRef = imageViewRef;
        this.url = url;
    }

    public void setImage(Bitmap bitmap) {
        imageViewRef.get().post(() -> {
            imageViewRef.get().setImageBitmap(bitmap);
        });
    }
}
