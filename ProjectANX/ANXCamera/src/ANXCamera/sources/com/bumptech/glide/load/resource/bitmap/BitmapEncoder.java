package com.bumptech.glide.load.resource.bitmap;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.bumptech.glide.load.EncodeStrategy;
import com.bumptech.glide.load.Option;
import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.ResourceEncoder;
import com.bumptech.glide.load.data.BufferedOutputStream;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.load.engine.bitmap_recycle.ArrayPool;
import com.bumptech.glide.util.LogTime;
import com.bumptech.glide.util.Util;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class BitmapEncoder implements ResourceEncoder {
    public static final Option COMPRESSION_FORMAT = Option.memory("com.bumptech.glide.load.resource.bitmap.BitmapEncoder.CompressionFormat");
    public static final Option COMPRESSION_QUALITY = Option.memory("com.bumptech.glide.load.resource.bitmap.BitmapEncoder.CompressionQuality", Integer.valueOf(90));
    private static final String TAG = "BitmapEncoder";
    @Nullable
    private final ArrayPool arrayPool;

    @Deprecated
    public BitmapEncoder() {
        this.arrayPool = null;
    }

    public BitmapEncoder(@NonNull ArrayPool arrayPool2) {
        this.arrayPool = arrayPool2;
    }

    private CompressFormat getFormat(Bitmap bitmap, Options options) {
        CompressFormat compressFormat = (CompressFormat) options.get(COMPRESSION_FORMAT);
        return compressFormat != null ? compressFormat : bitmap.hasAlpha() ? CompressFormat.PNG : CompressFormat.JPEG;
    }

    /* JADX WARNING: Can't wrap try/catch for region: R(4:21|(2:37|38)|39|40) */
    /* JADX WARNING: Code restructure failed: missing block: B:28:0x005f, code lost:
        if (r6 == null) goto L_0x0062;
     */
    /* JADX WARNING: Missing exception handler attribute for start block: B:39:0x00b5 */
    /* JADX WARNING: Removed duplicated region for block: B:27:0x005a A[Catch:{ all -> 0x0050 }] */
    /* JADX WARNING: Removed duplicated region for block: B:37:0x00b2 A[SYNTHETIC, Splitter:B:37:0x00b2] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean encode(@NonNull Resource resource, @NonNull File file, @NonNull Options options) {
        OutputStream outputStream;
        String str = TAG;
        Bitmap bitmap = (Bitmap) resource.get();
        CompressFormat format = getFormat(bitmap, options);
        Integer.valueOf(bitmap.getWidth());
        Integer.valueOf(bitmap.getHeight());
        long logTime = LogTime.getLogTime();
        int intValue = ((Integer) options.get(COMPRESSION_QUALITY)).intValue();
        boolean z = false;
        OutputStream outputStream2 = null;
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            try {
                outputStream = this.arrayPool != null ? new BufferedOutputStream(fileOutputStream, this.arrayPool) : fileOutputStream;
                bitmap.compress(format, intValue, outputStream);
                outputStream.close();
                z = true;
            } catch (IOException e) {
                e = e;
                outputStream2 = fileOutputStream;
                try {
                    if (Log.isLoggable(str, 3)) {
                    }
                } catch (Throwable th) {
                    th = th;
                    if (outputStream != null) {
                        outputStream.close();
                    }
                    throw th;
                }
            } catch (Throwable th2) {
                th = th2;
                outputStream = fileOutputStream;
                if (outputStream != null) {
                }
                throw th;
            }
        } catch (IOException e2) {
            e = e2;
            if (Log.isLoggable(str, 3)) {
                Log.d(str, "Failed to encode Bitmap", e);
            }
        }
        try {
            outputStream.close();
        } catch (IOException unused) {
        }
        if (Log.isLoggable(str, 2)) {
            StringBuilder sb = new StringBuilder();
            sb.append("Compressed with type: ");
            sb.append(format);
            sb.append(" of size ");
            sb.append(Util.getBitmapByteSize(bitmap));
            sb.append(" in ");
            sb.append(LogTime.getElapsedMillis(logTime));
            sb.append(", options format: ");
            sb.append(options.get(COMPRESSION_FORMAT));
            sb.append(", hasAlpha: ");
            sb.append(bitmap.hasAlpha());
            Log.v(str, sb.toString());
        }
        return z;
    }

    @NonNull
    public EncodeStrategy getEncodeStrategy(@NonNull Options options) {
        return EncodeStrategy.TRANSFORMED;
    }
}
