package com.airbnb.lottie;

import android.graphics.Bitmap;
import androidx.annotation.Nullable;
import androidx.annotation.RestrictTo;
import androidx.annotation.RestrictTo.Scope;

public class O00O0Oo {
    private final String O00O00o0;
    @Nullable
    private Bitmap bitmap;
    private final String fileName;
    private final int height;
    private final String id;
    private final int width;

    @RestrictTo({Scope.LIBRARY})
    public O00O0Oo(int i, int i2, String str, String str2, String str3) {
        this.width = i;
        this.height = i2;
        this.id = str;
        this.fileName = str2;
        this.O00O00o0 = str3;
    }

    public String O00O0oo() {
        return this.O00O00o0;
    }

    @Nullable
    public Bitmap getBitmap() {
        return this.bitmap;
    }

    public String getFileName() {
        return this.fileName;
    }

    public int getHeight() {
        return this.height;
    }

    public String getId() {
        return this.id;
    }

    public int getWidth() {
        return this.width;
    }

    public void setBitmap(@Nullable Bitmap bitmap2) {
        this.bitmap = bitmap2;
    }
}
