package com.bumptech.glide.load.resource.bytes;

import androidx.annotation.NonNull;
import com.bumptech.glide.load.data.DataRewinder;
import java.nio.ByteBuffer;

public class ByteBufferRewinder implements DataRewinder {
    private final ByteBuffer buffer;

    public class Factory implements com.bumptech.glide.load.data.DataRewinder.Factory {
        @NonNull
        public DataRewinder build(ByteBuffer byteBuffer) {
            return new ByteBufferRewinder(byteBuffer);
        }

        @NonNull
        public Class getDataClass() {
            return ByteBuffer.class;
        }
    }

    public ByteBufferRewinder(ByteBuffer byteBuffer) {
        this.buffer = byteBuffer;
    }

    public void cleanup() {
    }

    @NonNull
    public ByteBuffer rewindAndGet() {
        this.buffer.position(0);
        return this.buffer;
    }
}
