package com.bumptech.glide.load;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.bumptech.glide.util.Preconditions;
import java.security.MessageDigest;

public final class Option {
    private static final CacheKeyUpdater EMPTY_UPDATER = new CacheKeyUpdater() {
        public void update(@NonNull byte[] bArr, @NonNull Object obj, @NonNull MessageDigest messageDigest) {
        }
    };
    private final CacheKeyUpdater cacheKeyUpdater;
    private final Object defaultValue;
    private final String key;
    private volatile byte[] keyBytes;

    public interface CacheKeyUpdater {
        void update(@NonNull byte[] bArr, @NonNull Object obj, @NonNull MessageDigest messageDigest);
    }

    private Option(@NonNull String str, @Nullable Object obj, @NonNull CacheKeyUpdater cacheKeyUpdater2) {
        Preconditions.checkNotEmpty(str);
        this.key = str;
        this.defaultValue = obj;
        Preconditions.checkNotNull(cacheKeyUpdater2);
        this.cacheKeyUpdater = cacheKeyUpdater2;
    }

    @NonNull
    public static Option disk(@NonNull String str, @NonNull CacheKeyUpdater cacheKeyUpdater2) {
        return new Option(str, null, cacheKeyUpdater2);
    }

    @NonNull
    public static Option disk(@NonNull String str, @Nullable Object obj, @NonNull CacheKeyUpdater cacheKeyUpdater2) {
        return new Option(str, obj, cacheKeyUpdater2);
    }

    @NonNull
    private static CacheKeyUpdater emptyUpdater() {
        return EMPTY_UPDATER;
    }

    @NonNull
    private byte[] getKeyBytes() {
        if (this.keyBytes == null) {
            this.keyBytes = this.key.getBytes(Key.CHARSET);
        }
        return this.keyBytes;
    }

    @NonNull
    public static Option memory(@NonNull String str) {
        return new Option(str, null, emptyUpdater());
    }

    @NonNull
    public static Option memory(@NonNull String str, @NonNull Object obj) {
        return new Option(str, obj, emptyUpdater());
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof Option)) {
            return false;
        }
        return this.key.equals(((Option) obj).key);
    }

    @Nullable
    public Object getDefaultValue() {
        return this.defaultValue;
    }

    public int hashCode() {
        return this.key.hashCode();
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Option{key='");
        sb.append(this.key);
        sb.append('\'');
        sb.append('}');
        return sb.toString();
    }

    public void update(@NonNull Object obj, @NonNull MessageDigest messageDigest) {
        this.cacheKeyUpdater.update(getKeyBytes(), obj, messageDigest);
    }
}
