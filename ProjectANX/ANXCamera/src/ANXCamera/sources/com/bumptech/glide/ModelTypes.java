package com.bumptech.glide;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import androidx.annotation.CheckResult;
import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RawRes;
import java.io.File;
import java.net.URL;

interface ModelTypes {
    @CheckResult
    @NonNull
    Object load(@Nullable Bitmap bitmap);

    @CheckResult
    @NonNull
    Object load(@Nullable Drawable drawable);

    @CheckResult
    @NonNull
    Object load(@Nullable Uri uri);

    @CheckResult
    @NonNull
    Object load(@Nullable File file);

    @CheckResult
    @NonNull
    Object load(@RawRes @DrawableRes @Nullable Integer num);

    @CheckResult
    @NonNull
    Object load(@Nullable Object obj);

    @CheckResult
    @NonNull
    Object load(@Nullable String str);

    @CheckResult
    @Deprecated
    Object load(@Nullable URL url);

    @CheckResult
    @NonNull
    Object load(@Nullable byte[] bArr);
}
