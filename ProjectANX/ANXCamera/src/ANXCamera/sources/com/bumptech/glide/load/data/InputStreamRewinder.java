package com.bumptech.glide.load.data;

import androidx.annotation.NonNull;
import com.bumptech.glide.load.engine.bitmap_recycle.ArrayPool;
import com.bumptech.glide.load.resource.bitmap.RecyclableBufferedInputStream;
import java.io.InputStream;

public final class InputStreamRewinder implements DataRewinder {
    private static final int MARK_READ_LIMIT = 5242880;
    private final RecyclableBufferedInputStream bufferedStream;

    public final class Factory implements com.bumptech.glide.load.data.DataRewinder.Factory {
        private final ArrayPool byteArrayPool;

        public Factory(ArrayPool arrayPool) {
            this.byteArrayPool = arrayPool;
        }

        @NonNull
        public DataRewinder build(InputStream inputStream) {
            return new InputStreamRewinder(inputStream, this.byteArrayPool);
        }

        @NonNull
        public Class getDataClass() {
            return InputStream.class;
        }
    }

    public InputStreamRewinder(InputStream inputStream, ArrayPool arrayPool) {
        this.bufferedStream = new RecyclableBufferedInputStream(inputStream, arrayPool);
        this.bufferedStream.mark(MARK_READ_LIMIT);
    }

    public void cleanup() {
        this.bufferedStream.release();
    }

    public void fixMarkLimits() {
        this.bufferedStream.fixMarkLimit();
    }

    @NonNull
    public InputStream rewindAndGet() {
        this.bufferedStream.reset();
        return this.bufferedStream;
    }
}
