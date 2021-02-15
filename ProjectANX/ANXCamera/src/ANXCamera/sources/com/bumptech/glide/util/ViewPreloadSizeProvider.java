package com.bumptech.glide.util;

import android.graphics.drawable.Drawable;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.bumptech.glide.ListPreloader.PreloadSizeProvider;
import com.bumptech.glide.request.target.CustomViewTarget;
import com.bumptech.glide.request.target.SizeReadyCallback;
import com.bumptech.glide.request.transition.Transition;
import java.util.Arrays;

public class ViewPreloadSizeProvider implements PreloadSizeProvider, SizeReadyCallback {
    private int[] size;
    private SizeViewTarget viewTarget;

    final class SizeViewTarget extends CustomViewTarget {
        SizeViewTarget(@NonNull View view) {
            super(view);
        }

        public void onLoadFailed(@Nullable Drawable drawable) {
        }

        /* access modifiers changed from: protected */
        public void onResourceCleared(@Nullable Drawable drawable) {
        }

        public void onResourceReady(@NonNull Object obj, @Nullable Transition transition) {
        }
    }

    public ViewPreloadSizeProvider() {
    }

    public ViewPreloadSizeProvider(@NonNull View view) {
        this.viewTarget = new SizeViewTarget(view);
        this.viewTarget.getSize(this);
    }

    @Nullable
    public int[] getPreloadSize(@NonNull Object obj, int i, int i2) {
        int[] iArr = this.size;
        if (iArr == null) {
            return null;
        }
        return Arrays.copyOf(iArr, iArr.length);
    }

    public void onSizeReady(int i, int i2) {
        this.size = new int[]{i, i2};
        this.viewTarget = null;
    }

    public void setView(@NonNull View view) {
        if (this.size == null && this.viewTarget == null) {
            this.viewTarget = new SizeViewTarget(view);
            this.viewTarget.getSize(this);
        }
    }
}
