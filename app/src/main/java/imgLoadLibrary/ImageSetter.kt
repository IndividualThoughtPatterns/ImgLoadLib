package imgLoadLibrary

import android.graphics.Bitmap
import android.widget.ImageView
import java.lang.ref.WeakReference

class ImageSetter internal constructor(
    private var imageViewRef: WeakReference<ImageView>
) {
    fun setImage(bitmap: Bitmap?) {
        imageViewRef.get()!!.post {
            imageViewRef.get()!!.setImageBitmap(bitmap)
        }
    }
}
