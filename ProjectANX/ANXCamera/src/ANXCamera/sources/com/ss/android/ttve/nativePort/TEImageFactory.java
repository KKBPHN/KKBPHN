package com.ss.android.ttve.nativePort;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.media.ExifInterface;
import androidx.annotation.Keep;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import java.io.IOException;

@Keep
public class TEImageFactory {
    private static final String BITMAP = "bitmap";

    @Keep
    public class ImageInfo {
        Bitmap bitmap;
        int height;
        String mimeType;
        int rotation = 0;
        int width;

        public Bitmap getBitmap() {
            return this.bitmap;
        }

        public int getHeight() {
            return this.height;
        }

        public String getMimeType() {
            return this.mimeType;
        }

        public int getRotation() {
            return this.rotation;
        }

        public int getWidth() {
            return this.width;
        }
    }

    public static ImageInfo decodeFile(@NonNull String str) {
        return decodeFile(str, null);
    }

    public static ImageInfo decodeFile(@NonNull String str, @Nullable Options options) {
        if (options == null) {
            options = new Options();
        }
        options.inPreferredConfig = Config.ARGB_8888;
        Bitmap decodeFile = BitmapFactory.decodeFile(str, options);
        if (decodeFile == null) {
            return null;
        }
        Config config = decodeFile.getConfig();
        Config config2 = Config.ARGB_8888;
        if (config != config2) {
            Bitmap copy = decodeFile.copy(config2, false);
            recycleBitmap(decodeFile);
            decodeFile = copy;
        }
        ImageInfo imageInfo = new ImageInfo();
        imageInfo.width = decodeFile.getWidth();
        imageInfo.height = decodeFile.getHeight();
        imageInfo.bitmap = decodeFile;
        imageInfo.mimeType = BITMAP;
        imageInfo.rotation = getRotation(str);
        return imageInfo;
    }

    public static ImageInfo getImageInfo(@NonNull String str) {
        Options options = new Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(str, options);
        ImageInfo imageInfo = new ImageInfo();
        imageInfo.width = options.outWidth;
        imageInfo.height = options.outHeight;
        imageInfo.mimeType = options.outMimeType;
        return imageInfo;
    }

    public static int getRotation(String str) {
        ExifInterface exifInterface;
        try {
            exifInterface = new ExifInterface(str);
        } catch (IOException e) {
            e.printStackTrace();
            exifInterface = null;
        }
        int i = 0;
        if (exifInterface != null) {
            int attributeInt = exifInterface.getAttributeInt(androidx.exifinterface.media.ExifInterface.TAG_ORIENTATION, 0);
            if (attributeInt == 3) {
                i = 180;
            } else if (attributeInt != 6) {
                return attributeInt != 8 ? 0 : 270;
            } else {
                return 90;
            }
        }
        return i;
    }

    public static void recycleBitmap(Bitmap bitmap) {
        if (bitmap != null && !bitmap.isRecycled()) {
            bitmap.recycle();
        }
    }
}
