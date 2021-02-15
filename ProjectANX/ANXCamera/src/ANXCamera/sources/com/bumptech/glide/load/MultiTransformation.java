package com.bumptech.glide.load;

import android.content.Context;
import androidx.annotation.NonNull;
import com.bumptech.glide.load.engine.Resource;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.Collection;

public class MultiTransformation implements Transformation {
    private final Collection transformations;

    public MultiTransformation(@NonNull Collection collection) {
        if (!collection.isEmpty()) {
            this.transformations = collection;
            return;
        }
        throw new IllegalArgumentException("MultiTransformation must contain at least one Transformation");
    }

    @SafeVarargs
    public MultiTransformation(@NonNull Transformation... transformationArr) {
        if (transformationArr.length != 0) {
            this.transformations = Arrays.asList(transformationArr);
            return;
        }
        throw new IllegalArgumentException("MultiTransformation must contain at least one Transformation");
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof MultiTransformation)) {
            return false;
        }
        return this.transformations.equals(((MultiTransformation) obj).transformations);
    }

    public int hashCode() {
        return this.transformations.hashCode();
    }

    @NonNull
    public Resource transform(@NonNull Context context, @NonNull Resource resource, int i, int i2) {
        Resource resource2 = resource;
        for (Transformation transform : this.transformations) {
            Resource transform2 = transform.transform(context, resource2, i, i2);
            if (resource2 != null && !resource2.equals(resource) && !resource2.equals(transform2)) {
                resource2.recycle();
            }
            resource2 = transform2;
        }
        return resource2;
    }

    public void updateDiskCacheKey(@NonNull MessageDigest messageDigest) {
        for (Transformation updateDiskCacheKey : this.transformations) {
            updateDiskCacheKey.updateDiskCacheKey(messageDigest);
        }
    }
}
