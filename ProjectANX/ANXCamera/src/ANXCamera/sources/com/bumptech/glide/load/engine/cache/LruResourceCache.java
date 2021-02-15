package com.bumptech.glide.load.engine.cache;

import android.annotation.SuppressLint;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.bumptech.glide.load.Key;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.load.engine.cache.MemoryCache.ResourceRemovedListener;
import com.bumptech.glide.util.LruCache;

public class LruResourceCache extends LruCache implements MemoryCache {
    private ResourceRemovedListener listener;

    public LruResourceCache(long j) {
        super(j);
    }

    /* access modifiers changed from: protected */
    public int getSize(@Nullable Resource resource) {
        return resource == null ? super.getSize(null) : resource.getSize();
    }

    /* access modifiers changed from: protected */
    public void onItemEvicted(@NonNull Key key, @Nullable Resource resource) {
        ResourceRemovedListener resourceRemovedListener = this.listener;
        if (resourceRemovedListener != null && resource != null) {
            resourceRemovedListener.onResourceRemoved(resource);
        }
    }

    @Nullable
    public /* bridge */ /* synthetic */ Resource put(@NonNull Key key, @Nullable Resource resource) {
        return (Resource) super.put(key, resource);
    }

    @Nullable
    public /* bridge */ /* synthetic */ Resource remove(@NonNull Key key) {
        return (Resource) super.remove(key);
    }

    public void setResourceRemovedListener(@NonNull ResourceRemovedListener resourceRemovedListener) {
        this.listener = resourceRemovedListener;
    }

    @SuppressLint({"InlinedApi"})
    public void trimMemory(int i) {
        if (i >= 40) {
            clearMemory();
        } else if (i >= 20 || i == 15) {
            trimToSize(getMaxSize() / 2);
        }
    }
}
