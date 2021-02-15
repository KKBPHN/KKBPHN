package com.bumptech.glide.load.resource;

import android.content.Context;
import androidx.annotation.NonNull;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.engine.Resource;
import java.security.MessageDigest;

public final class UnitTransformation implements Transformation {
    private static final Transformation TRANSFORMATION = new UnitTransformation();

    private UnitTransformation() {
    }

    @NonNull
    public static UnitTransformation get() {
        return (UnitTransformation) TRANSFORMATION;
    }

    @NonNull
    public Resource transform(@NonNull Context context, @NonNull Resource resource, int i, int i2) {
        return resource;
    }

    public void updateDiskCacheKey(@NonNull MessageDigest messageDigest) {
    }
}
