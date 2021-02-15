package com.bumptech.glide.load.model;

import android.net.Uri;
import android.text.TextUtils;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.bumptech.glide.load.Key;
import com.bumptech.glide.util.Preconditions;
import java.net.URL;
import java.security.MessageDigest;
import java.util.Map;

public class GlideUrl implements Key {
    private static final String ALLOWED_URI_CHARS = "@#&=*+-_.,:!?()/~'%;$";
    @Nullable
    private volatile byte[] cacheKeyBytes;
    private int hashCode;
    private final Headers headers;
    @Nullable
    private String safeStringUrl;
    @Nullable
    private URL safeUrl;
    @Nullable
    private final String stringUrl;
    @Nullable
    private final URL url;

    public GlideUrl(String str) {
        this(str, Headers.DEFAULT);
    }

    public GlideUrl(String str, Headers headers2) {
        this.url = null;
        Preconditions.checkNotEmpty(str);
        this.stringUrl = str;
        Preconditions.checkNotNull(headers2);
        this.headers = headers2;
    }

    public GlideUrl(URL url2) {
        this(url2, Headers.DEFAULT);
    }

    public GlideUrl(URL url2, Headers headers2) {
        Preconditions.checkNotNull(url2);
        this.url = url2;
        this.stringUrl = null;
        Preconditions.checkNotNull(headers2);
        this.headers = headers2;
    }

    private byte[] getCacheKeyBytes() {
        if (this.cacheKeyBytes == null) {
            this.cacheKeyBytes = getCacheKey().getBytes(Key.CHARSET);
        }
        return this.cacheKeyBytes;
    }

    private String getSafeStringUrl() {
        if (TextUtils.isEmpty(this.safeStringUrl)) {
            String str = this.stringUrl;
            if (TextUtils.isEmpty(str)) {
                URL url2 = this.url;
                Preconditions.checkNotNull(url2);
                str = url2.toString();
            }
            this.safeStringUrl = Uri.encode(str, ALLOWED_URI_CHARS);
        }
        return this.safeStringUrl;
    }

    private URL getSafeUrl() {
        if (this.safeUrl == null) {
            this.safeUrl = new URL(getSafeStringUrl());
        }
        return this.safeUrl;
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof GlideUrl)) {
            return false;
        }
        GlideUrl glideUrl = (GlideUrl) obj;
        return getCacheKey().equals(glideUrl.getCacheKey()) && this.headers.equals(glideUrl.headers);
    }

    public String getCacheKey() {
        String str = this.stringUrl;
        if (str != null) {
            return str;
        }
        URL url2 = this.url;
        Preconditions.checkNotNull(url2);
        return url2.toString();
    }

    public Map getHeaders() {
        return this.headers.getHeaders();
    }

    public int hashCode() {
        if (this.hashCode == 0) {
            this.hashCode = getCacheKey().hashCode();
            this.hashCode = (this.hashCode * 31) + this.headers.hashCode();
        }
        return this.hashCode;
    }

    public String toString() {
        return getCacheKey();
    }

    public String toStringUrl() {
        return getSafeStringUrl();
    }

    public URL toURL() {
        return getSafeUrl();
    }

    public void updateDiskCacheKey(@NonNull MessageDigest messageDigest) {
        messageDigest.update(getCacheKeyBytes());
    }
}
