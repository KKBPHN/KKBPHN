package com.android.camera.fragment.manually.adapter;

import com.android.camera.fragment.manually.ZoomValueListener;
import com.android.camera.ui.BaseHorizontalZoomView.HorizontalDrawAdapter;
import com.android.camera.ui.BaseHorizontalZoomView.OnPositionSelectListener;

public abstract class AbstractZoomSliderAdapter extends HorizontalDrawAdapter implements OnPositionSelectListener {
    protected ZoomValueListener mZoomValueListener;

    public abstract boolean isEnable();

    public abstract Object mapPositionToValue(float f);

    public abstract float mapValueToPosition(Object obj);

    public abstract void setEnable(boolean z);
}
