package com.bumptech.glide.load.model;

import android.util.Log;
import androidx.annotation.NonNull;
import com.bumptech.glide.load.Encoder;
import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.engine.bitmap_recycle.ArrayPool;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class StreamEncoder implements Encoder {
    private static final String TAG = "StreamEncoder";
    private final ArrayPool byteArrayPool;

    public StreamEncoder(ArrayPool arrayPool) {
        this.byteArrayPool = arrayPool;
    }

    /* JADX WARNING: Removed duplicated region for block: B:21:0x0038 A[Catch:{ all -> 0x002e }] */
    /* JADX WARNING: Removed duplicated region for block: B:23:0x003f A[SYNTHETIC, Splitter:B:23:0x003f] */
    /* JADX WARNING: Removed duplicated region for block: B:29:0x004a A[SYNTHETIC, Splitter:B:29:0x004a] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean encode(@NonNull InputStream inputStream, @NonNull File file, @NonNull Options options) {
        String str = TAG;
        byte[] bArr = (byte[]) this.byteArrayPool.get(65536, byte[].class);
        boolean z = false;
        FileOutputStream fileOutputStream = null;
        try {
            FileOutputStream fileOutputStream2 = new FileOutputStream(file);
            while (true) {
                try {
                    int read = inputStream.read(bArr);
                    if (read == -1) {
                        break;
                    }
                    fileOutputStream2.write(bArr, 0, read);
                } catch (IOException e) {
                    e = e;
                    fileOutputStream = fileOutputStream2;
                    try {
                        if (Log.isLoggable(str, 3)) {
                        }
                        if (fileOutputStream != null) {
                        }
                        this.byteArrayPool.put(bArr);
                        return z;
                    } catch (Throwable th) {
                        th = th;
                        if (fileOutputStream != null) {
                            try {
                                fileOutputStream.close();
                            } catch (IOException unused) {
                            }
                        }
                        this.byteArrayPool.put(bArr);
                        throw th;
                    }
                } catch (Throwable th2) {
                    th = th2;
                    fileOutputStream = fileOutputStream2;
                    if (fileOutputStream != null) {
                    }
                    this.byteArrayPool.put(bArr);
                    throw th;
                }
            }
            fileOutputStream2.close();
            z = true;
            try {
                fileOutputStream2.close();
            } catch (IOException unused2) {
            }
        } catch (IOException e2) {
            e = e2;
            if (Log.isLoggable(str, 3)) {
                Log.d(str, "Failed to encode data onto the OutputStream", e);
            }
            if (fileOutputStream != null) {
                fileOutputStream.close();
            }
            this.byteArrayPool.put(bArr);
            return z;
        }
        this.byteArrayPool.put(bArr);
        return z;
    }
}
