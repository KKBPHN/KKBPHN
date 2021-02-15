package com.android.camera.aiwatermark;

import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.FrameLayout.LayoutParams;
import com.android.camera.data.DataRepository;
import com.android.camera.data.data.runing.ComponentRunningAIWatermark;

public class DragListener implements OnTouchListener {
    private ComponentRunningAIWatermark mAIWatermark = null;
    private boolean mIsDragging = false;
    private OnClickListener mListener = null;
    private int[] mLocation = new int[4];
    private float mLocationLeft;
    private float mLocationTop;
    private float mOriginalX;
    private float mOriginalY;
    private View mParent = null;
    private Rect mRect;

    public DragListener(Rect rect, OnClickListener onClickListener) {
        this.mRect = rect;
        this.mAIWatermark = DataRepository.dataItemRunning().getComponentRunningAIWatermark();
        this.mListener = onClickListener;
    }

    private void fixLocation(View view) {
        LayoutParams layoutParams = new LayoutParams(-2, -2);
        layoutParams.leftMargin = view.getLeft();
        layoutParams.topMargin = view.getTop();
        layoutParams.width = view.getWidth();
        layoutParams.height = view.getHeight();
        layoutParams.setMargins(view.getLeft(), view.getTop(), 0, 0);
        view.setLayoutParams(layoutParams);
    }

    private int[] getNewLocation(View view, float f, float f2, Rect rect) {
        int i = (int) (this.mLocationLeft + f);
        int i2 = (int) (this.mLocationTop + f2);
        int width = view.getWidth() + i;
        int height = view.getHeight() + i2;
        int i3 = rect.left;
        if (i <= i3) {
            width = i3 + view.getWidth();
            i = i3;
        }
        int i4 = rect.top;
        if (i2 <= i4) {
            height = i4 + view.getHeight();
            i2 = i4;
        }
        int i5 = rect.right;
        if (width >= i5) {
            i = i5 - view.getWidth();
            width = i5;
        }
        int i6 = rect.bottom;
        if (height >= i6) {
            i2 = i6 - view.getHeight();
        } else {
            i6 = height;
        }
        return new int[]{i, i2, width, i6};
    }

    private void updateLocation(MotionEvent motionEvent, View view) {
        float rawX = motionEvent.getRawX() - this.mOriginalX;
        float rawY = motionEvent.getRawY() - this.mOriginalY;
        Rect rect = this.mRect;
        if (rect == null) {
            if (this.mParent == null) {
                this.mParent = (View) view.getParent();
            }
            this.mRect = new Rect(this.mParent.getLeft(), this.mParent.getTop(), this.mParent.getRight(), this.mParent.getBottom());
            rect = this.mRect;
        }
        this.mLocation = getNewLocation(view, rawX, rawY, rect);
        int[] iArr = this.mLocation;
        view.layout(iArr[0], iArr[1], iArr[2], iArr[3]);
        if (motionEvent.getAction() == 1) {
            fixLocation(view);
        }
        this.mAIWatermark.updateLocation(this.mLocation, this.mRect, -1);
        this.mAIWatermark.setHasMove(true);
    }

    public boolean onTouch(View view, MotionEvent motionEvent) {
        int action = motionEvent.getAction();
        if (action != 0) {
            if (action != 1) {
                if (action == 2 || action == 7) {
                    this.mIsDragging = true;
                    updateLocation(motionEvent, view);
                }
            } else if (this.mIsDragging) {
                updateLocation(motionEvent, view);
            } else {
                this.mListener.onClick(view);
            }
            return true;
        }
        this.mOriginalX = motionEvent.getRawX();
        this.mOriginalY = motionEvent.getRawY();
        this.mLocationLeft = (float) view.getLeft();
        this.mLocationTop = (float) view.getTop();
        this.mIsDragging = false;
        return true;
    }

    public void reInit(Rect rect, int[] iArr) {
        this.mRect = rect;
        this.mIsDragging = false;
        this.mLocation = iArr;
        this.mAIWatermark.updateLocation(this.mLocation, this.mRect, -1);
    }
}
