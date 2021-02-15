package com.android.camera.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import com.android.camera.CameraSettings;
import com.android.camera.animation.FolmeUtils;
import com.android.camera.data.DataRepository;
import com.android.camera.ui.drawable.zoom.CameraZoomAnimateDrawable;

public class ZoomView extends View {
    private static final String TAG = "ZoomView";
    private CameraZoomAnimateDrawable mCameraZoomAnimateDrawable;
    private boolean mIsHorizontal;
    private boolean mIsInited;
    private boolean mIsVisible;
    private boolean mIsZoomMoving;
    private float mMaxZoomRatio;
    private float mMinZoomRatio;
    private Point mPointView;
    private boolean mSupportVideoSat;
    public zoomValueChangeListener mZoomValueChangeListener;

    public interface zoomValueChangeListener {
        void onZoomTouchDown();

        void onZoomTouchUp();

        void onZoomValueChanged(float f);

        void setGestureDetectorEnable(boolean z);
    }

    public ZoomView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public float getCurrentZoomRatio() {
        CameraZoomAnimateDrawable cameraZoomAnimateDrawable = this.mCameraZoomAnimateDrawable;
        if (cameraZoomAnimateDrawable != null) {
            return cameraZoomAnimateDrawable.getCurrentZoomRatio();
        }
        return 1.0f;
    }

    public void hide() {
        this.mIsVisible = false;
        this.mIsZoomMoving = false;
        CameraZoomAnimateDrawable cameraZoomAnimateDrawable = this.mCameraZoomAnimateDrawable;
        if (cameraZoomAnimateDrawable != null) {
            cameraZoomAnimateDrawable.reset();
        }
        FolmeUtils.animateHide(this);
    }

    public void init() {
        if (!this.mIsInited) {
            this.mIsInited = true;
            this.mCameraZoomAnimateDrawable = new CameraZoomAnimateDrawable(getContext(), this.mIsHorizontal);
            this.mCameraZoomAnimateDrawable.setCallback(this);
            this.mCameraZoomAnimateDrawable.updateZoomRatio(this.mMinZoomRatio, this.mMaxZoomRatio);
            this.mPointView = new Point();
            invalidate();
        }
    }

    public void invalidateDrawable(Drawable drawable) {
        invalidate();
    }

    public boolean isVisible() {
        return this.mIsVisible;
    }

    public boolean isZoomMoving() {
        return this.mIsZoomMoving;
    }

    /* access modifiers changed from: protected */
    public void onDraw(Canvas canvas) {
        if (this.mIsVisible && this.mIsInited) {
            CameraZoomAnimateDrawable cameraZoomAnimateDrawable = this.mCameraZoomAnimateDrawable;
            if (cameraZoomAnimateDrawable != null) {
                cameraZoomAnimateDrawable.draw(canvas);
            }
        }
    }

    public boolean onKeyEventSmoothZoom(int i, KeyEvent keyEvent) {
        if (this.mIsVisible && this.mIsInited) {
            CameraZoomAnimateDrawable cameraZoomAnimateDrawable = this.mCameraZoomAnimateDrawable;
            if (cameraZoomAnimateDrawable != null) {
                if (i == 168) {
                    cameraZoomAnimateDrawable.setSmoothSpeed(0.01357699f);
                } else if (i == 169) {
                    cameraZoomAnimateDrawable.setSmoothSpeed(-0.01357699f);
                }
                if (keyEvent.getAction() == 0 && this.mCameraZoomAnimateDrawable.startTouchDownAnimation(this.mZoomValueChangeListener, false)) {
                    this.mIsZoomMoving = true;
                }
                if (1 == keyEvent.getAction()) {
                    this.mIsZoomMoving = false;
                    this.mCameraZoomAnimateDrawable.stopTouchUpAnimation(false);
                }
                return true;
            }
        }
        return false;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:22:0x004e, code lost:
        if (r5 != 3) goto L_0x0075;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean onTouchEvent(MotionEvent motionEvent) {
        if (!this.mIsVisible || !this.mIsInited || this.mCameraZoomAnimateDrawable == null) {
            return false;
        }
        this.mPointView.set((int) motionEvent.getX(), (int) motionEvent.getY());
        if (motionEvent.getActionMasked() == 0) {
            if (!this.mCameraZoomAnimateDrawable.containsSliderTouchRect(this.mPointView)) {
                return false;
            }
            if (this.mCameraZoomAnimateDrawable.startTouchDownAnimation(this.mZoomValueChangeListener, true)) {
                this.mIsZoomMoving = true;
                zoomValueChangeListener zoomvaluechangelistener = this.mZoomValueChangeListener;
                if (zoomvaluechangelistener != null) {
                    zoomvaluechangelistener.setGestureDetectorEnable(false);
                }
            }
        }
        int action = motionEvent.getAction();
        if (action != 0) {
            if (action != 1) {
                if (action == 2) {
                    if (!this.mIsZoomMoving) {
                        return false;
                    }
                    this.mCameraZoomAnimateDrawable.move(this.mPointView);
                }
            }
            this.mZoomValueChangeListener.onZoomTouchUp();
            if (!this.mIsZoomMoving) {
                return false;
            }
            this.mIsZoomMoving = false;
            this.mCameraZoomAnimateDrawable.stopTouchUpAnimation(true);
        } else {
            this.mZoomValueChangeListener.onZoomTouchDown();
        }
        return true;
    }

    public void reInit() {
        if (CameraSettings.getRetainZoom(DataRepository.dataItemGlobal().getCurrentMode()) <= this.mMinZoomRatio) {
            CameraZoomAnimateDrawable cameraZoomAnimateDrawable = this.mCameraZoomAnimateDrawable;
            if (cameraZoomAnimateDrawable != null) {
                cameraZoomAnimateDrawable.reset();
                invalidate();
            }
        }
    }

    public void setCurrentZoomRatio(float f) {
        CameraZoomAnimateDrawable cameraZoomAnimateDrawable = this.mCameraZoomAnimateDrawable;
        if (cameraZoomAnimateDrawable != null) {
            cameraZoomAnimateDrawable.setCurrentZoomRatio(f);
            invalidate();
        }
    }

    public void setIsHorizonal(boolean z) {
        this.mIsHorizontal = z;
    }

    public void setOrientation(int i) {
        CameraZoomAnimateDrawable cameraZoomAnimateDrawable = this.mCameraZoomAnimateDrawable;
        if (cameraZoomAnimateDrawable != null) {
            cameraZoomAnimateDrawable.setOrientation(i);
        }
    }

    public void setSupportVideoSat(boolean z) {
        this.mSupportVideoSat = z;
        CameraZoomAnimateDrawable cameraZoomAnimateDrawable = this.mCameraZoomAnimateDrawable;
        if (cameraZoomAnimateDrawable != null) {
            cameraZoomAnimateDrawable.setSupportVideoSat(z);
        }
    }

    public void setZoomValueChangeListener(zoomValueChangeListener zoomvaluechangelistener) {
        this.mZoomValueChangeListener = zoomvaluechangelistener;
    }

    public void show(int i) {
        this.mIsVisible = true;
        CameraZoomAnimateDrawable cameraZoomAnimateDrawable = this.mCameraZoomAnimateDrawable;
        if (cameraZoomAnimateDrawable != null) {
            cameraZoomAnimateDrawable.setOrientation(i);
        }
        FolmeUtils.animateShow(this);
    }

    public void updateZoomRatio(float f, float f2) {
        this.mMinZoomRatio = f;
        this.mMaxZoomRatio = f2;
        CameraZoomAnimateDrawable cameraZoomAnimateDrawable = this.mCameraZoomAnimateDrawable;
        if (cameraZoomAnimateDrawable != null) {
            cameraZoomAnimateDrawable.updateZoomRatio(this.mMinZoomRatio, this.mMaxZoomRatio);
        }
    }
}
