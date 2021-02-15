package com.android.camera.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.android.camera.ui.DragLayout.OnDragListener;

public class PopupMenuLayout extends FrameLayout implements OnDragListener {
    public PopupMenuLayout(@NonNull Context context) {
        super(context);
    }

    public PopupMenuLayout(@NonNull Context context, @Nullable AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public PopupMenuLayout(@NonNull Context context, @Nullable AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }

    public void onDragDone(boolean z) {
        if (z) {
            setAlpha(0.0f);
            setVisibility(8);
            return;
        }
        setAlpha(1.0f);
        setVisibility(0);
        setTranslationY(0.0f);
    }

    public void onDragProgress(int i, boolean z) {
        setTranslationY((float) i);
        setAlpha(1.0f - Math.min(1.0f, ((float) Math.abs(i)) / DragLayout.getAnimationConfig().getDisappearDistance()));
    }

    public void onDragStart(boolean z) {
        float f;
        if (z) {
            f = 1.0f;
        } else {
            setVisibility(0);
            f = 0.0f;
        }
        setAlpha(f);
    }
}
