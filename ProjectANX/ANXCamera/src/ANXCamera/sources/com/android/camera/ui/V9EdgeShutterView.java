package com.android.camera.ui;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Message;
import android.os.SystemProperties;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import com.android.camera.Camera;
import com.android.camera.CameraAppImpl;
import com.android.camera.Display;
import com.android.camera.R;
import com.android.camera.Util;
import com.android.camera.log.Log;
import com.android.camera.module.ModuleManager;
import java.util.Locale;
import miui.view.animation.CubicEaseOutInterpolator;

public class V9EdgeShutterView extends View implements V6FunctionUI {
    private static final int CENTER_RADIUS = CameraAppImpl.getAndroidContext().getResources().getDimensionPixelSize(R.dimen.v6_edge_shutter_center_radius);
    private static final int FLYINOUT_DURATION = 250;
    private static final int MOVE_DURATION = 300;
    private static final int MSG_CAPTURE_MOVING = 2;
    private static final int MSG_CAPTURE_POSTURE_CHANGED = 1;
    private static final int MSG_LONG_TIME_UNUSED = 0;
    private static final int NEAR_THRESHOLD = Util.dpToPixel(66.67f);
    private static int NORMAL_TAP_MAXY = ((Display.getWindowHeight() * SystemProperties.getInt("camera_edge_max", 75)) / 100);
    private static int NORMAL_TOUCH_MAXY = (NORMAL_TAP_MAXY + (NEAR_THRESHOLD / 2));
    private static final int OUTER_CIRCLE_WIDTH = Util.dpToPixel(1.0f);
    private static final int OUT_RADIUS = CameraAppImpl.getAndroidContext().getResources().getDimensionPixelSize(R.dimen.v6_edge_shutter_out_radius);
    private static final int PRESS_DURATION = 250;
    private static final String TAG = "CameraEdgeShutterView";
    private static final int UNUSED_TRIGGER_TIME = 5000;
    private static final int VIDEO_CENTER_COLOR = -1032447;
    private static final int VIDEO_OUTER_COLOR = -1862270977;
    private static final int VIEW_FLYOUT = 2;
    private static final int VIEW_INVISIBLE = 4;
    private static final int VIEW_MOVE = 3;
    private static final int VIEW_VISIBLE = 1;
    private static final int VIEW_WIDTH = CameraAppImpl.getAndroidContext().getResources().getDimensionPixelSize(R.dimen.v6_edge_shutter_width);
    private AnimatorListener mAnimatorListener;
    private Paint mCenterPaint;
    /* access modifiers changed from: private */
    public ValueAnimator mClickAnim;
    /* access modifiers changed from: private */
    public ValueAnimator mFlyOutAnim;
    private ValueAnimator mFlyinAnim;
    private Interpolator mFlyinInterpolator = new OvershootInterpolator();
    private Handler mHandler = new Handler() {
        public void dispatchMessage(Message message) {
            int i = message.what;
            if (i != 0) {
                if (i == 1) {
                    V9EdgeShutterView.this.checkPosture();
                    return;
                } else if (i != 2) {
                    return;
                }
            }
            V9EdgeShutterView.this.hideShutterView();
        }
    };
    private MessageDispatcher mMessageDispatcher;
    /* access modifiers changed from: private */
    public ValueAnimator mMoveAnim;
    private Interpolator mMoveInterpolator = new CubicEaseOutInterpolator();
    private Paint mOuterPaint;
    private Interpolator mPressInterpolator = new ReverseInterpolator();
    private Rect mVisibleBounds = new Rect(0, 0, Display.getWindowWidth(), Display.getWindowHeight());
    /* access modifiers changed from: private */
    public int mVisibleState;

    class CustomAnimatorListener extends AnimatorListenerAdapter {
        private CustomAnimatorListener() {
        }

        public void onAnimationCancel(Animator animator) {
            StringBuilder sb = new StringBuilder();
            sb.append("onAnimationCancel animation=");
            sb.append(animator);
            Log.v(V9EdgeShutterView.TAG, sb.toString());
            V9EdgeShutterView.this.mVisibleState = 1;
        }

        public void onAnimationEnd(Animator animator) {
            V9EdgeShutterView v9EdgeShutterView;
            int i;
            StringBuilder sb = new StringBuilder();
            sb.append("onAnimationEnd animation=");
            sb.append(animator);
            Log.v(V9EdgeShutterView.TAG, sb.toString());
            if (animator == V9EdgeShutterView.this.mFlyOutAnim && V9EdgeShutterView.this.mVisibleState == 2) {
                i = 4;
                V9EdgeShutterView.this.setRelateVisible(4);
                v9EdgeShutterView = V9EdgeShutterView.this;
            } else {
                if (animator == V9EdgeShutterView.this.mMoveAnim && V9EdgeShutterView.this.mVisibleState == 3) {
                    v9EdgeShutterView = V9EdgeShutterView.this;
                    i = 1;
                }
                V9EdgeShutterView v9EdgeShutterView2 = V9EdgeShutterView.this;
                v9EdgeShutterView2.setX((float) v9EdgeShutterView2.getLeft());
            }
            v9EdgeShutterView.mVisibleState = i;
            V9EdgeShutterView v9EdgeShutterView22 = V9EdgeShutterView.this;
            v9EdgeShutterView22.setX((float) v9EdgeShutterView22.getLeft());
        }

        public void onAnimationStart(Animator animator) {
            if (animator == V9EdgeShutterView.this.mClickAnim) {
                V9EdgeShutterView v9EdgeShutterView = V9EdgeShutterView.this;
                v9EdgeShutterView.setX((float) v9EdgeShutterView.getLeft());
            }
        }
    }

    class ReverseInterpolator implements Interpolator {
        private final Interpolator mInterpolator;

        private ReverseInterpolator(V9EdgeShutterView v9EdgeShutterView) {
            this((Interpolator) new AccelerateDecelerateInterpolator());
        }

        private ReverseInterpolator(Interpolator interpolator) {
            if (interpolator == null) {
                interpolator = new AccelerateDecelerateInterpolator();
            }
            this.mInterpolator = interpolator;
        }

        public float getInterpolation(float f) {
            int i = (((double) f) > 0.5d ? 1 : (((double) f) == 0.5d ? 0 : -1));
            Interpolator interpolator = this.mInterpolator;
            if (i > 0) {
                f = Math.abs(f - 1.0f);
            }
            return interpolator.getInterpolation(f * 2.0f);
        }
    }

    public V9EdgeShutterView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    /* access modifiers changed from: private */
    public void checkPosture() {
        int capturePosture = ((Camera) getContext()).getCapturePosture();
        if ((capturePosture == 1 && getLeft() != 0) || ((capturePosture == 2 && getLeft() == 0) || (capturePosture == 0 && getTop() > NORMAL_TAP_MAXY))) {
            hideShutterView();
        }
    }

    private boolean couldTouch(int i, int i2) {
        return this.mVisibleState == 1 && Math.abs(i - getLeft()) <= NEAR_THRESHOLD && Math.abs(i2 - ((getTop() + getBottom()) / 2)) <= NEAR_THRESHOLD;
    }

    private ValueAnimator createClickAnimation() {
        float[] fArr = new float[1];
        fArr[0] = (float) (getLeft() < VIEW_WIDTH ? getLeft() + (VIEW_WIDTH / 5) : getLeft() - (VIEW_WIDTH / 5));
        ObjectAnimator ofFloat = ObjectAnimator.ofFloat(this, "x", fArr);
        ofFloat.setInterpolator(this.mPressInterpolator);
        ofFloat.setDuration(250);
        ofFloat.addListener(this.mAnimatorListener);
        return ofFloat;
    }

    private ValueAnimator createFlyInAnimation() {
        int left = getLeft();
        int i = VIEW_WIDTH;
        if (left < i) {
            i = -i;
        }
        ObjectAnimator ofFloat = ObjectAnimator.ofFloat(this, "translationX", new float[]{(float) i, (float) 0});
        ofFloat.setInterpolator(this.mFlyinInterpolator);
        ofFloat.setDuration(250);
        ofFloat.addListener(this.mAnimatorListener);
        return ofFloat;
    }

    private ValueAnimator createFlyOutAnimation() {
        int left = getLeft();
        int i = VIEW_WIDTH;
        if (left < i) {
            i = -i;
        }
        ObjectAnimator ofFloat = ObjectAnimator.ofFloat(this, "translationX", new float[]{(float) 0, (float) i});
        ofFloat.setInterpolator(this.mMoveInterpolator);
        ofFloat.setDuration(250);
        ofFloat.addListener(this.mAnimatorListener);
        return ofFloat;
    }

    private ValueAnimator createMoveAnimation(Rect rect) {
        ObjectAnimator ofPropertyValuesHolder = ObjectAnimator.ofPropertyValuesHolder(this, new PropertyValuesHolder[]{PropertyValuesHolder.ofFloat("x", new float[]{(float) rect.left}), PropertyValuesHolder.ofFloat("y", new float[]{(float) rect.top})});
        ofPropertyValuesHolder.setInterpolator(this.mMoveInterpolator);
        ofPropertyValuesHolder.setDuration(300);
        ofPropertyValuesHolder.addListener(this.mAnimatorListener);
        return ofPropertyValuesHolder;
    }

    private void flyto(int i, int i2) {
        Rect reviseLocation = reviseLocation(i, i2, this.mVisibleBounds);
        StringBuilder sb = new StringBuilder();
        sb.append("flyto ");
        sb.append(getVisibility());
        sb.append(" rec=");
        sb.append(reviseLocation);
        sb.append(" viewstate:");
        sb.append(getViewState());
        Log.v(TAG, sb.toString());
        if (getVisibility() != 0) {
            if (getVisibility() == 8) {
                setInitLayoutParameters(reviseLocation);
            }
            layout(reviseLocation.left, reviseLocation.top, reviseLocation.right, reviseLocation.bottom);
            this.mFlyinAnim = createFlyInAnimation();
            this.mFlyinAnim.start();
            this.mVisibleState = 1;
            setRelateVisible(0);
            return;
        }
        ValueAnimator valueAnimator = this.mFlyOutAnim;
        if (valueAnimator != null && valueAnimator.isStarted()) {
            this.mFlyOutAnim.cancel();
        }
        ValueAnimator valueAnimator2 = this.mFlyinAnim;
        if (valueAnimator2 != null && valueAnimator2.isStarted()) {
            this.mFlyinAnim.cancel();
        }
        this.mMoveAnim = createMoveAnimation(reviseLocation);
        this.mMoveAnim.start();
        this.mVisibleState = 3;
        layout(reviseLocation.left, reviseLocation.top, reviseLocation.right, reviseLocation.bottom);
        setX((float) reviseLocation.left);
    }

    private String getViewState() {
        return String.format(Locale.ENGLISH, "View state mleft=%d mtop=%d width=%d height=%d mVisibleState=%d getVisibility()=%d", new Object[]{Integer.valueOf(getLeft()), Integer.valueOf(getTop()), Integer.valueOf(getWidth()), Integer.valueOf(getHeight()), Integer.valueOf(this.mVisibleState), Integer.valueOf(getVisibility())});
    }

    /* access modifiers changed from: private */
    public void hideShutterView() {
        if (this.mVisibleState == 1) {
            this.mFlyOutAnim = createFlyOutAnimation();
            this.mFlyOutAnim.start();
            this.mVisibleState = 2;
        }
    }

    private boolean isDeviceStateReady(int i, int i2, int i3) {
        String sb;
        Camera camera = (Camera) getContext();
        boolean isStable = camera.isStable();
        String str = TAG;
        if (!isStable) {
            sb = "Device is not stable, ignore edgetouch";
        } else {
            int capturePosture = camera.getCapturePosture();
            String str2 = " y=";
            String str3 = " x=";
            if (capturePosture == 0 && i2 > i3) {
                StringBuilder sb2 = new StringBuilder();
                sb2.append("Device post wrong, y is too big, capturePosture=");
                sb2.append(capturePosture);
                sb2.append(str3);
                sb2.append(i);
                sb2.append(str2);
                sb2.append(i2);
                sb2.append(" maxY=");
                sb2.append(i3);
                sb = sb2.toString();
            } else if ((capturePosture != 1 || i == 0) && (capturePosture != 2 || i != 0)) {
                return true;
            } else {
                StringBuilder sb3 = new StringBuilder();
                sb3.append("Device post wrong, touching bottom edge, capturePosture=");
                sb3.append(capturePosture);
                sb3.append(str3);
                sb3.append(i);
                sb3.append(str2);
                sb3.append(i2);
                sb = sb3.toString();
            }
        }
        Log.v(str, sb);
        return false;
    }

    private void resendUnusedMessage() {
        this.mHandler.removeMessages(0);
        this.mHandler.sendEmptyMessageDelayed(0, 5000);
    }

    private Rect reviseLocation(int i, int i2, Rect rect) {
        StringBuilder sb = new StringBuilder();
        sb.append("flyto reviseLocation x");
        sb.append(i);
        sb.append(" y=");
        sb.append(i2);
        sb.append(" bounds=");
        sb.append(rect);
        sb.append(" viewstate:");
        sb.append(getViewState());
        Log.v(TAG, sb.toString());
        int windowHeight = i2 - ((VIEW_WIDTH * i2) / Display.getWindowHeight());
        int i3 = VIEW_WIDTH;
        Rect rect2 = new Rect(i, windowHeight, i + i3, i3 + windowHeight);
        if (rect.contains(rect2)) {
            return rect2;
        }
        int i4 = rect2.left;
        int i5 = rect.left;
        if (i4 < i5) {
            rect2.right = i5 + rect2.width();
            rect2.left = rect.left;
        } else {
            int i6 = rect2.right;
            int i7 = rect.right;
            if (i6 > i7) {
                rect2.left = i7 - rect2.width();
                rect2.right = rect.right;
            }
        }
        int i8 = rect2.top;
        int i9 = rect.top;
        if (i8 < i9) {
            rect2.bottom = i9 + rect2.height();
            rect2.top = rect.top;
        } else {
            int i10 = rect2.bottom;
            int i11 = rect.bottom;
            if (i10 > i11) {
                rect2.top = i11 - rect2.height();
                rect2.bottom = rect.bottom;
            }
        }
        return rect2;
    }

    private void setInitLayoutParameters(Rect rect) {
        LayoutParams layoutParams = (LayoutParams) getLayoutParams();
        layoutParams.leftMargin = rect.left;
        layoutParams.topMargin = rect.top;
        setLayoutParams(layoutParams);
    }

    /* access modifiers changed from: private */
    public void setRelateVisible(int i) {
        RelativeLayout relativeLayout = (RelativeLayout) getParent();
        if (relativeLayout != null) {
            int i2 = 8;
            if (8 != i) {
                i2 = 0;
            }
            relativeLayout.setVisibility(i2);
        }
        setVisibility(i);
    }

    public void cancelAnimation() {
        animate().cancel();
        setX((float) getLeft());
        setY((float) getTop());
    }

    public void enableControls(boolean z) {
    }

    public void onCameraOpen() {
    }

    public void onCreate() {
        if (ModuleManager.isVideoModule()) {
            this.mCenterPaint.setColor(VIDEO_CENTER_COLOR);
            this.mOuterPaint.setColor(VIDEO_OUTER_COLOR);
            return;
        }
        this.mCenterPaint.setColor(-1);
        this.mOuterPaint.setColor(-1);
    }

    public void onDeviceMoving() {
        this.mHandler.sendEmptyMessage(2);
    }

    public void onDevicePostureChanged() {
        this.mHandler.sendEmptyMessage(1);
    }

    /* access modifiers changed from: protected */
    public void onDraw(Canvas canvas) {
        int i = VIEW_WIDTH;
        canvas.drawCircle((float) (i / 2), (float) (i / 2), (float) (OUT_RADIUS - 2), this.mOuterPaint);
        int i2 = VIEW_WIDTH;
        canvas.drawCircle((float) (i2 / 2), (float) (i2 / 2), (float) CENTER_RADIUS, this.mCenterPaint);
    }

    public boolean onEdgeTap(int i, int i2) {
        StringBuilder sb = new StringBuilder();
        sb.append("onEdgeTouch x=");
        sb.append(i);
        sb.append(" y=");
        sb.append(i2);
        sb.append(" viewstate:");
        sb.append(getViewState());
        Log.v(TAG, sb.toString());
        if (couldTouch(i, i2) || !isDeviceStateReady(i, i2, NORMAL_TAP_MAXY)) {
            return false;
        }
        flyto(i, i2);
        resendUnusedMessage();
        return true;
    }

    public boolean onEdgeTouch(int i, int i2) {
        StringBuilder sb = new StringBuilder();
        sb.append("onEdgeTouch x=");
        sb.append(i);
        sb.append(" y=");
        sb.append(i2);
        sb.append(" viewstate:");
        sb.append(getViewState());
        Log.v(TAG, sb.toString());
        if (!couldTouch(i, i2) || !isDeviceStateReady(i, i2, NORMAL_TOUCH_MAXY)) {
            return false;
        }
        ValueAnimator valueAnimator = this.mFlyinAnim;
        if (valueAnimator != null && valueAnimator.isStarted()) {
            this.mFlyinAnim.cancel();
        }
        MessageDispatcher messageDispatcher = this.mMessageDispatcher;
        if (messageDispatcher != null) {
            messageDispatcher.dispatchMessage(0, R.id.v9_edge_shutter_view, 2, null, null);
        }
        this.mClickAnim = createClickAnimation();
        this.mClickAnim.start();
        resendUnusedMessage();
        return true;
    }

    /* access modifiers changed from: protected */
    public void onFinishInflate() {
        this.mCenterPaint = new Paint();
        this.mCenterPaint.setAntiAlias(true);
        this.mCenterPaint.setColor(-1);
        this.mCenterPaint.setStyle(Style.FILL);
        this.mOuterPaint = new Paint();
        this.mOuterPaint.setAntiAlias(true);
        this.mOuterPaint.setColor(-1);
        this.mOuterPaint.setStyle(Style.STROKE);
        this.mOuterPaint.setStrokeWidth((float) OUTER_CIRCLE_WIDTH);
        this.mAnimatorListener = new CustomAnimatorListener();
    }

    /* access modifiers changed from: protected */
    public void onMeasure(int i, int i2) {
        int i3 = VIEW_WIDTH;
        setMeasuredDimension(i3, i3);
    }

    public void onPause() {
        this.mHandler.removeMessages(0);
        this.mHandler.removeMessages(1);
        this.mHandler.removeMessages(2);
        setRelateVisible(8);
    }

    public void onResume() {
    }

    public void setMessageDispatcher(MessageDispatcher messageDispatcher) {
        this.mMessageDispatcher = messageDispatcher;
    }
}
