package com.ss.android.vesdk;

import androidx.annotation.Keep;

@Keep
public interface VEStickerAnimator {
    float getDegree(int i);

    float getScaleX(int i);

    float getScaleY(int i);

    float getTransX(int i);

    float getTransY(int i);
}
