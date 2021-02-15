package com.bumptech.glide.load.resource;

import androidx.annotation.NonNull;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.util.Preconditions;

public class SimpleResource implements Resource {
    protected final Object data;

    public SimpleResource(@NonNull Object obj) {
        Preconditions.checkNotNull(obj);
        this.data = obj;
    }

    @NonNull
    public final Object get() {
        return this.data;
    }

    @NonNull
    public Class getResourceClass() {
        return this.data.getClass();
    }

    public final int getSize() {
        return 1;
    }

    public void recycle() {
    }
}
