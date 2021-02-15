package com.bumptech.glide.request.target;

import android.graphics.drawable.Drawable;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import androidx.annotation.Nullable;

public abstract class ThumbnailImageViewTarget extends ImageViewTarget {
    public ThumbnailImageViewTarget(ImageView imageView) {
        super(imageView);
    }

    @Deprecated
    public ThumbnailImageViewTarget(ImageView imageView, boolean z) {
        super(imageView, z);
    }

    public abstract Drawable getDrawable(Object obj);

    /* access modifiers changed from: protected */
    public void setResource(@Nullable Object obj) {
        LayoutParams layoutParams = ((ImageView) this.view).getLayoutParams();
        Drawable drawable = getDrawable(obj);
        if (layoutParams != null) {
            int i = layoutParams.width;
            if (i > 0) {
                int i2 = layoutParams.height;
                if (i2 > 0) {
                    drawable = new FixedSizeDrawable(drawable, i, i2);
                }
            }
        }
        ((ImageView) this.view).setImageDrawable(drawable);
    }
}
