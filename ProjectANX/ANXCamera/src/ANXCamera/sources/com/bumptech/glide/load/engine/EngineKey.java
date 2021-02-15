package com.bumptech.glide.load.engine;

import androidx.annotation.NonNull;
import com.bumptech.glide.load.Key;
import com.bumptech.glide.load.Options;
import com.bumptech.glide.util.Preconditions;
import java.security.MessageDigest;
import java.util.Map;

class EngineKey implements Key {
    private int hashCode;
    private final int height;
    private final Object model;
    private final Options options;
    private final Class resourceClass;
    private final Key signature;
    private final Class transcodeClass;
    private final Map transformations;
    private final int width;

    EngineKey(Object obj, Key key, int i, int i2, Map map, Class cls, Class cls2, Options options2) {
        Preconditions.checkNotNull(obj);
        this.model = obj;
        Preconditions.checkNotNull(key, "Signature must not be null");
        this.signature = key;
        this.width = i;
        this.height = i2;
        Preconditions.checkNotNull(map);
        this.transformations = map;
        Preconditions.checkNotNull(cls, "Resource class must not be null");
        this.resourceClass = cls;
        Preconditions.checkNotNull(cls2, "Transcode class must not be null");
        this.transcodeClass = cls2;
        Preconditions.checkNotNull(options2);
        this.options = options2;
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof EngineKey)) {
            return false;
        }
        EngineKey engineKey = (EngineKey) obj;
        return this.model.equals(engineKey.model) && this.signature.equals(engineKey.signature) && this.height == engineKey.height && this.width == engineKey.width && this.transformations.equals(engineKey.transformations) && this.resourceClass.equals(engineKey.resourceClass) && this.transcodeClass.equals(engineKey.transcodeClass) && this.options.equals(engineKey.options);
    }

    public int hashCode() {
        if (this.hashCode == 0) {
            this.hashCode = this.model.hashCode();
            this.hashCode = (this.hashCode * 31) + this.signature.hashCode();
            this.hashCode = (this.hashCode * 31) + this.width;
            this.hashCode = (this.hashCode * 31) + this.height;
            this.hashCode = (this.hashCode * 31) + this.transformations.hashCode();
            this.hashCode = (this.hashCode * 31) + this.resourceClass.hashCode();
            this.hashCode = (this.hashCode * 31) + this.transcodeClass.hashCode();
            this.hashCode = (this.hashCode * 31) + this.options.hashCode();
        }
        return this.hashCode;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("EngineKey{model=");
        sb.append(this.model);
        sb.append(", width=");
        sb.append(this.width);
        sb.append(", height=");
        sb.append(this.height);
        sb.append(", resourceClass=");
        sb.append(this.resourceClass);
        sb.append(", transcodeClass=");
        sb.append(this.transcodeClass);
        sb.append(", signature=");
        sb.append(this.signature);
        sb.append(", hashCode=");
        sb.append(this.hashCode);
        sb.append(", transformations=");
        sb.append(this.transformations);
        sb.append(", options=");
        sb.append(this.options);
        sb.append('}');
        return sb.toString();
    }

    public void updateDiskCacheKey(@NonNull MessageDigest messageDigest) {
        throw new UnsupportedOperationException();
    }
}
