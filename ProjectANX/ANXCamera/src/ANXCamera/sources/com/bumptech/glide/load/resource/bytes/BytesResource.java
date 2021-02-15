package com.bumptech.glide.load.resource.bytes;

import androidx.annotation.NonNull;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.util.Preconditions;

public class BytesResource implements Resource {
    private final byte[] bytes;

    public BytesResource(byte[] bArr) {
        Preconditions.checkNotNull(bArr);
        this.bytes = bArr;
    }

    @NonNull
    public byte[] get() {
        return this.bytes;
    }

    @NonNull
    public Class getResourceClass() {
        return byte[].class;
    }

    public int getSize() {
        return this.bytes.length;
    }

    public void recycle() {
    }
}
