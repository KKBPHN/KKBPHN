package com.bumptech.glide.load;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.collection.ArrayMap;
import com.bumptech.glide.util.CachedHashCodeArrayMap;
import java.security.MessageDigest;

public final class Options implements Key {
    private final ArrayMap values = new CachedHashCodeArrayMap();

    private static void updateDiskCacheKey(@NonNull Option option, @NonNull Object obj, @NonNull MessageDigest messageDigest) {
        option.update(obj, messageDigest);
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof Options)) {
            return false;
        }
        return this.values.equals(((Options) obj).values);
    }

    @Nullable
    public Object get(@NonNull Option option) {
        return this.values.containsKey(option) ? this.values.get(option) : option.getDefaultValue();
    }

    public int hashCode() {
        return this.values.hashCode();
    }

    public void putAll(@NonNull Options options) {
        this.values.putAll(options.values);
    }

    @NonNull
    public Options set(@NonNull Option option, @NonNull Object obj) {
        this.values.put(option, obj);
        return this;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Options{values=");
        sb.append(this.values);
        sb.append('}');
        return sb.toString();
    }

    public void updateDiskCacheKey(@NonNull MessageDigest messageDigest) {
        for (int i = 0; i < this.values.size(); i++) {
            updateDiskCacheKey((Option) this.values.keyAt(i), this.values.valueAt(i), messageDigest);
        }
    }
}
