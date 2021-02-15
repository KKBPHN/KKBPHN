package com.android.camera.ui.zoom;

import O00000Oo.O00000oO.O000000o.C0124O00000oO;
import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.AttributeSet;
import android.util.Log;
import android.util.Property;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import androidx.annotation.DimenRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.core.view.ViewCompat;
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat;
import androidx.customview.widget.ExploreByTouchHelper;
import com.android.camera.HybridZoomingSystem;
import com.android.camera.R;
import com.android.camera.Util;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import miui.view.animation.CubicEaseOutInterpolator;
import miui.view.animation.QuadraticEaseOutInterpolator;

public class ZoomRatioToggleView extends ViewGroup implements OnClickListener, OnLongClickListener {
    private static final int ANIMATOR_TYPE_IMAGE_IMAGE = 6;
    private static final int ANIMATOR_TYPE_IMAGE_NONE = 3;
    private static final int ANIMATOR_TYPE_IMAGE_TEXT = 4;
    private static final int ANIMATOR_TYPE_NONE_IMAGE = 1;
    private static final int ANIMATOR_TYPE_NONE_TEXT = 0;
    private static final int ANIMATOR_TYPE_TEXT_IMAGE = 5;
    private static final int ANIMATOR_TYPE_TEXT_NONE = 2;
    private static final int ANIMATOR_TYPE_TEXT_TEXT = 7;
    private static final int ANIMATOR_TYPE_TEXT_TEXT_IMMERSIVE_OUT = 8;
    private static final int INVALID_INDEX = -1;
    private static final float INVALID_ZOOM_RATIO = -1.0f;
    private static final int PIXELS_PER_SECOND = 1000;
    private static final String TAG = "ZoomRatioToggleView";
    private static final int TOUCH_SCROLL_THRESHOLD = 10;
    private static final int TOUCH_STATE_CLICK = 1;
    private static final int TOUCH_STATE_IDLE = 0;
    private static final int TOUCH_STATE_LONG_CLICK = 3;
    private static final int TOUCH_STATE_SCROLL = 2;
    private static final int TOUCH_STATE_SHOWVIEW = 4;
    private static final boolean UI_DEBUG_ENABLED = Log.isLoggable(TAG, 3);
    private static final int VELOCITY_THRESHOLD = 100;
    private ToggleStateListener mActionListener;
    private int mCurrentModule;
    private int mCurrentSelectedChildIndex;
    private final Handler mHandler;
    private final Runnable mIndexUpdater;
    private boolean mIsImmersive;
    private boolean mIsSuppressed;
    private int mItemHeight;
    private int mItemWidth;
    private Runnable mLongPressRunnable;
    private Paint mPaint;
    private Rect mRect;
    private AnimatorSet mShowZoomChildAnimatorSet;
    /* access modifiers changed from: private */
    public int mTouchStartX;
    /* access modifiers changed from: private */
    public int mTouchStartY;
    /* access modifiers changed from: private */
    public int mTouchState;
    private int mUseSliderType;
    private VelocityTracker mVelocityTracker;
    private float[] mZoomArray;
    private AnimatorSet mZoomInOutAnimatorSet;
    /* access modifiers changed from: private */
    public float mZoomRatio;
    private AnimatorSet mZoomShowAnimatorSet;
    public final Runnable mZoomTipAnnounceRunnable;
    private MyAccessHelper myAccessHelper;

    class MyAccessHelper extends ExploreByTouchHelper {
        private Rect mAccessRect;
        private int mSelectIndex;

        private MyAccessHelper(@NonNull View view) {
            super(view);
            this.mAccessRect = new Rect();
        }

        /* access modifiers changed from: protected */
        public int getVirtualViewAt(float f, float f2) {
            this.mSelectIndex = ZoomRatioToggleView.this.getContainingChildIndex((int) f, (int) f2);
            return this.mSelectIndex;
        }

        /* access modifiers changed from: protected */
        public void getVisibleVirtualViews(List list) {
            if (ZoomRatioToggleView.this.getChildCount() > 0) {
                for (int i = 0; i < ZoomRatioToggleView.this.getChildCount(); i++) {
                    list.add(Integer.valueOf(i));
                }
            }
        }

        /* access modifiers changed from: protected */
        public boolean onPerformActionForVirtualView(int i, int i2, @Nullable Bundle bundle) {
            if (i2 == 16) {
                ZoomRatioToggleView.this.clickChildAtIndex(i);
            }
            return true;
        }

        /* access modifiers changed from: protected */
        public void onPopulateNodeForVirtualView(int i, @NonNull AccessibilityNodeInfoCompat accessibilityNodeInfoCompat) {
            CharSequence charSequence;
            if (ZoomRatioToggleView.this.getChildCount() <= 0 || this.mSelectIndex < 0) {
                this.mAccessRect.setEmpty();
                accessibilityNodeInfoCompat.setBoundsInParent(this.mAccessRect);
                accessibilityNodeInfoCompat.setEnabled(false);
                charSequence = "";
            } else {
                ZoomRatioView zoomRatioView = (ZoomRatioView) Objects.requireNonNull((ZoomRatioView) ZoomRatioToggleView.this.getChildAt(i));
                zoomRatioView.getHitRect(this.mAccessRect);
                accessibilityNodeInfoCompat.setBoundsInParent(this.mAccessRect);
                accessibilityNodeInfoCompat.setClickable(true);
                accessibilityNodeInfoCompat.addAction(16);
                charSequence = zoomRatioView.getContentDescriptionString();
            }
            accessibilityNodeInfoCompat.setContentDescription(charSequence);
        }
    }

    public interface ToggleStateListener {
        boolean isInteractive();

        void onClick(ZoomRatioView zoomRatioView);

        boolean toShowSlideView(ZoomRatioView zoomRatioView);
    }

    public class ViewSpec {
        public static final int SLIDER_TYPE_ALL = 2;
        public static final int SLIDER_TYPE_NONE = 0;
        public static final int SLIDER_TYPE_PART = 1;
        public final boolean immersive;
        public final boolean suppress;
        public final int useSliderType;
        public final int visibility;

        public ViewSpec(int i, boolean z, boolean z2, int i2) {
            this.visibility = i;
            this.suppress = z;
            this.immersive = z2;
            this.useSliderType = i2;
        }
    }

    public ZoomRatioToggleView(@NonNull Context context) {
        this(context, null);
    }

    public ZoomRatioToggleView(@NonNull Context context, @Nullable AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public ZoomRatioToggleView(@NonNull Context context, @Nullable AttributeSet attributeSet, int i) {
        this(context, attributeSet, i, 0);
    }

    public ZoomRatioToggleView(@NonNull Context context, @Nullable AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
        this.mTouchState = 0;
        this.mCurrentModule = 163;
        this.mUseSliderType = 0;
        this.mIsImmersive = false;
        this.mIsSuppressed = false;
        this.mHandler = new Handler();
        this.mIndexUpdater = new Runnable() {
            public void run() {
                String str = ZoomRatioToggleView.TAG;
                ZoomRatioToggleView.debugUi(str, "Macro mode not change");
                ZoomRatioToggleView.this.setSelectedChildIndex();
                ZoomRatioToggleView.debugUi(str, "run index updater ");
            }
        };
        this.mZoomTipAnnounceRunnable = new Runnable() {
            public void run() {
                String valueOf = String.valueOf(HybridZoomingSystem.toDecimal(ZoomRatioToggleView.this.mZoomRatio));
                ZoomRatioToggleView zoomRatioToggleView = ZoomRatioToggleView.this;
                zoomRatioToggleView.setContentDescription(zoomRatioToggleView.getString(R.string.accessibility_focus_status, valueOf));
                if (Util.isAccessible()) {
                    ZoomRatioToggleView zoomRatioToggleView2 = ZoomRatioToggleView.this;
                    zoomRatioToggleView2.announceForAccessibility(zoomRatioToggleView2.getString(R.string.accessibility_focus_status, valueOf));
                }
            }
        };
        init();
    }

    static /* synthetic */ void O000000o(AnimatorSet animatorSet) {
        if (animatorSet != null) {
            animatorSet.cancel();
            animatorSet.removeAllListeners();
        }
    }

    private void announceCurrentZoomRatioForAccessibility() {
        this.mHandler.removeCallbacks(this.mZoomTipAnnounceRunnable);
        this.mHandler.postDelayed(this.mZoomTipAnnounceRunnable, 500);
    }

    private void clickChildAt(int i, int i2) {
        clickChildAtIndex(getContainingChildIndex(i, i2));
    }

    /* access modifiers changed from: private */
    public void clickChildAtIndex(int i) {
        if (i != -1) {
            StringBuilder sb = new StringBuilder();
            sb.append("clickChildAtIndex: ");
            sb.append(i);
            com.android.camera.log.Log.u(TAG, sb.toString());
            if (i != this.mCurrentSelectedChildIndex) {
                showZoomChildView(true, i, 1);
            } else if (this.mIsSuppressed && this.mCurrentModule != 188) {
                ZoomRatioView zoomRatioView = (ZoomRatioView) getChildAt(i);
                if (zoomRatioView != null) {
                    this.mZoomInOutAnimatorSet.setTarget(zoomRatioView);
                    this.mZoomInOutAnimatorSet.start();
                    zoomRatioView.setZoomRatio(this.mZoomRatio);
                    ToggleStateListener toggleStateListener = this.mActionListener;
                    if (toggleStateListener != null) {
                        toggleStateListener.onClick(zoomRatioView);
                    }
                }
            } else if (this.mActionListener != null && isSupportUseSlider(i) && this.mActionListener.toShowSlideView(null)) {
                performHapticFeedback(0);
            }
        }
    }

    /* access modifiers changed from: private */
    public static void debugUi(String str, String str2) {
        if (UI_DEBUG_ENABLED || C0124O00000oO.O0o0O0 || C0124O00000oO.O0o0O0O || C0124O00000oO.O0o0O0o) {
            com.android.camera.log.Log.d(str, str2);
        }
    }

    private void endTouch(float f) {
        boolean isSupportUseSlider = isSupportUseSlider(getContainingChildIndex(this.mTouchStartX, this.mTouchStartY));
        if (this.mActionListener != null && Math.abs(f) >= 100.0f && !this.mIsImmersive && !this.mIsSuppressed && isSupportUseSlider && this.mActionListener.toShowSlideView(null)) {
            performHapticFeedback(0);
        }
        VelocityTracker velocityTracker = this.mVelocityTracker;
        if (velocityTracker != null) {
            velocityTracker.recycle();
            this.mVelocityTracker = null;
        }
        removeCallbacks(this.mLongPressRunnable);
        this.mTouchState = 0;
    }

    /* access modifiers changed from: private */
    public int getContainingChildIndex(int i, int i2) {
        if (this.mRect == null) {
            this.mRect = new Rect();
        }
        if (this.mIsSuppressed) {
            getChildAt(0).getHitRect(this.mRect);
            Rect rect = this.mRect;
            rect.left = (int) (((float) rect.left) - (((float) rect.width()) * 0.5f));
            Rect rect2 = this.mRect;
            rect2.right = (int) (((float) rect2.right) + (((float) rect2.width()) * 0.5f));
            if (this.mRect.contains(i, i2)) {
                return this.mCurrentSelectedChildIndex;
            }
        } else {
            int childCount = getChildCount();
            for (int i3 = 0; i3 < childCount; i3++) {
                getChildAt(i3).getHitRect(this.mRect);
                if (i3 == 0) {
                    Rect rect3 = this.mRect;
                    rect3.left = (int) (((float) rect3.left) - (((float) rect3.width()) * 0.5f));
                } else if (i3 == getChildCount() - 1) {
                    Rect rect4 = this.mRect;
                    rect4.right = (int) (((float) rect4.right) + (((float) rect4.width()) * 0.5f));
                }
                if (this.mRect.contains(i, i2)) {
                    return i3;
                }
            }
        }
        return -1;
    }

    private int getDimensionPixelSize(@DimenRes int i) {
        return getResources().getDimensionPixelSize(i);
    }

    /* access modifiers changed from: private */
    public String getString(@StringRes int i, Object... objArr) {
        return getResources().getString(i, objArr);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:10:0x016f, code lost:
        r3.add(android.animation.ObjectAnimator.ofFloat(r5, r0, r1));
        r3.add(android.animation.ObjectAnimator.ofFloat(r4.getTextView(), android.view.View.SCALE_X, new float[]{1.0f, 1.0f}));
        r3.add(android.animation.ObjectAnimator.ofFloat(r4.getIconView(), android.view.View.SCALE_X, new float[]{1.0f, 1.0f}));
        r5 = r4.getTextView();
        r0 = android.view.View.SCALE_Y;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:12:0x01e2, code lost:
        r3.add(android.animation.ObjectAnimator.ofFloat(r5, r0, r1));
        r5 = r4.getIconView();
        r0 = android.view.View.SCALE_X;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:13:0x01ef, code lost:
        r1 = new float[]{1.0f, 1.0f};
     */
    /* JADX WARNING: Code restructure failed: missing block: B:14:0x01f4, code lost:
        r3.add(android.animation.ObjectAnimator.ofFloat(r5, r0, r1));
        r4 = r4.getIconView();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:15:0x01ff, code lost:
        r3.add(android.animation.ObjectAnimator.ofFloat(r4, android.view.View.SCALE_Y, new float[]{1.0f, 1.0f}));
     */
    /* JADX WARNING: Code restructure failed: missing block: B:16:?, code lost:
        return;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:5:0x0088, code lost:
        r0 = android.view.View.ALPHA;
        r1 = new float[]{1.0f, 1.0f};
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void getTypeAnimator(List list, ZoomRatioView zoomRatioView, int i) {
        ZoomTextImageView zoomTextImageView;
        float[] fArr;
        Property property;
        ZoomTextImageView zoomTextImageView2;
        float[] fArr2;
        Property property2;
        switch (i) {
            case 0:
            case 4:
                list.add(ObjectAnimator.ofFloat(zoomRatioView.getIconView(), View.ALPHA, new float[]{0.0f, 0.0f}));
                list.add(ObjectAnimator.ofFloat(zoomRatioView.getTextView(), View.ALPHA, new float[]{0.0f, 1.0f}));
                list.add(ObjectAnimator.ofFloat(zoomRatioView.getTextView(), View.SCALE_X, new float[]{0.167f, 1.0f}));
                zoomTextImageView = zoomRatioView.getTextView();
                property = View.SCALE_Y;
                fArr = new float[]{0.167f, 1.0f};
                break;
            case 1:
                list.add(ObjectAnimator.ofFloat(zoomRatioView.getTextView(), View.ALPHA, new float[]{0.0f, 0.0f}));
                zoomTextImageView2 = zoomRatioView.getIconView();
                property2 = View.ALPHA;
                fArr2 = new float[]{0.0f, 1.0f};
                break;
            case 2:
                list.add(ObjectAnimator.ofFloat(zoomRatioView.getTextView(), View.ALPHA, new float[]{1.0f, 0.0f}));
                list.add(ObjectAnimator.ofFloat(zoomRatioView.getTextView(), View.SCALE_X, new float[]{1.0f, 0.167f}));
                list.add(ObjectAnimator.ofFloat(zoomRatioView.getTextView(), View.SCALE_Y, new float[]{1.0f, 0.167f}));
                zoomTextImageView = zoomRatioView.getIconView();
                property = View.ALPHA;
                fArr = new float[]{0.0f, 0.0f};
                break;
            case 3:
                list.add(ObjectAnimator.ofFloat(zoomRatioView.getTextView(), View.ALPHA, new float[]{0.0f, 0.0f}));
                zoomTextImageView2 = zoomRatioView.getIconView();
                property2 = View.ALPHA;
                fArr2 = new float[]{1.0f, 0.0f};
                break;
            case 5:
                list.add(ObjectAnimator.ofFloat(zoomRatioView.getTextView(), View.ALPHA, new float[]{0.0f, 0.0f}));
                list.add(ObjectAnimator.ofFloat(zoomRatioView.getIconView(), View.ALPHA, new float[]{1.0f, 1.0f}));
                list.add(ObjectAnimator.ofFloat(zoomRatioView.getIconView(), View.SCALE_X, new float[]{1.67f, 1.0f}));
                list.add(ObjectAnimator.ofFloat(zoomRatioView.getIconView(), View.SCALE_Y, new float[]{1.67f, 1.0f}));
                list.add(ObjectAnimator.ofFloat(zoomRatioView.getTextView(), View.SCALE_X, new float[]{1.0f, 1.0f}));
                ZoomTextImageView zoomTextImageView3 = zoomRatioView.getTextView();
                break;
            case 6:
                list.add(ObjectAnimator.ofFloat(zoomRatioView.getTextView(), View.ALPHA, new float[]{0.0f, 0.0f}));
                zoomTextImageView2 = zoomRatioView.getIconView();
                break;
            case 7:
                list.add(ObjectAnimator.ofFloat(zoomRatioView.getIconView(), View.ALPHA, new float[]{0.0f, 0.0f}));
                zoomTextImageView2 = zoomRatioView.getTextView();
                break;
            case 8:
                list.add(ObjectAnimator.ofFloat(zoomRatioView.getIconView(), View.ALPHA, new float[]{0.0f, 0.0f}));
                list.add(ObjectAnimator.ofFloat(zoomRatioView.getTextView(), View.ALPHA, new float[]{1.0f, 1.0f}));
                list.add(ObjectAnimator.ofFloat(zoomRatioView.getTextView(), View.SCALE_X, new float[]{1.1f, 1.0f}));
                list.add(ObjectAnimator.ofFloat(zoomRatioView.getIconView(), View.SCALE_X, new float[]{1.0f, 1.0f}));
                ZoomTextImageView zoomTextImageView4 = zoomRatioView.getTextView();
                Property property3 = View.SCALE_Y;
                float[] fArr3 = {1.1f, 1.0f};
                break;
            default:
                return;
        }
    }

    private void init() {
        setWillNotDraw(false);
        this.mPaint = new Paint();
        this.mPaint.setAntiAlias(true);
        this.mZoomShowAnimatorSet = new AnimatorSet();
        this.mZoomShowAnimatorSet.setInterpolator(new CubicEaseOutInterpolator());
        this.mZoomShowAnimatorSet.setDuration(400);
        this.mShowZoomChildAnimatorSet = new AnimatorSet();
        this.mShowZoomChildAnimatorSet.setDuration(200);
        this.mShowZoomChildAnimatorSet.setInterpolator(new CubicEaseOutInterpolator());
        this.mZoomInOutAnimatorSet = (AnimatorSet) AnimatorInflater.loadAnimator(getContext(), R.animator.zoom_button_zoom_in_out);
        this.mZoomInOutAnimatorSet.setTarget(this);
        this.mZoomInOutAnimatorSet.setInterpolator(new QuadraticEaseOutInterpolator());
        this.myAccessHelper = new MyAccessHelper(this);
        ViewCompat.setAccessibilityDelegate(this, this.myAccessHelper);
    }

    private boolean isLayoutRTL() {
        if (getContext() == null) {
            return false;
        }
        boolean z = true;
        if (getResources().getConfiguration().getLayoutDirection() != 1) {
            z = false;
        }
        return z;
    }

    private boolean isSupportUseSlider(int i) {
        boolean z = this.mUseSliderType == 1 && i != 0 && this.mZoomRatio >= 1.0f;
        boolean z2 = this.mUseSliderType == 2 && i != -1;
        return z || z2;
    }

    /* access modifiers changed from: private */
    public void longClickChild(int i) {
        com.android.camera.log.Log.u(TAG, "longClickChild");
        if (!isSupportUseSlider(i)) {
            return;
        }
        if ((!this.mIsImmersive || this.mIsSuppressed) && this.mActionListener != null) {
            View childAt = getChildAt(i);
            if (childAt != null && this.mActionListener.toShowSlideView((ZoomRatioView) childAt)) {
                performHapticFeedback(0);
            }
        }
    }

    private void resetAnimators() {
        debugUi(TAG, "resetAnimators");
        Arrays.asList(new AnimatorSet[]{this.mZoomInOutAnimatorSet, this.mShowZoomChildAnimatorSet, this.mZoomShowAnimatorSet}).forEach(O000000o.INSTANCE);
    }

    /* access modifiers changed from: private */
    public void setSelectedChildIndex() {
        int opticalZoomRatioIndex = HybridZoomingSystem.getOpticalZoomRatioIndex(this.mCurrentModule, this.mZoomRatio);
        if (isLayoutRTL()) {
            opticalZoomRatioIndex = (getChildCount() - 1) - opticalZoomRatioIndex;
        }
        boolean z = opticalZoomRatioIndex == this.mCurrentSelectedChildIndex || getVisibility() != 0;
        showZoomChildView(!z, opticalZoomRatioIndex, 0);
    }

    private void showZoomChildView(boolean z, int i, int i2) {
        int i3;
        removeCallbacks(this.mIndexUpdater);
        final ZoomRatioView zoomRatioView = (ZoomRatioView) getChildAt(i);
        final ZoomRatioView zoomRatioView2 = (ZoomRatioView) getChildAt(this.mCurrentSelectedChildIndex);
        float opticalZoomRatioAt = HybridZoomingSystem.getOpticalZoomRatioAt(this.mCurrentModule, i);
        float opticalZoomRatioAt2 = HybridZoomingSystem.getOpticalZoomRatioAt(this.mCurrentModule, this.mCurrentSelectedChildIndex);
        if (!z) {
            this.mCurrentSelectedChildIndex = i;
            if (zoomRatioView2 != null) {
                zoomRatioView2.setIconify(true);
                zoomRatioView2.setZoomRatio(opticalZoomRatioAt2);
            }
            if (zoomRatioView != null) {
                zoomRatioView.setIconify(false);
                if (i2 != 1) {
                    opticalZoomRatioAt = this.mZoomRatio;
                }
                zoomRatioView.setZoomRatio(opticalZoomRatioAt);
            }
        } else {
            ArrayList arrayList = new ArrayList();
            int childCount = getChildCount();
            for (int i4 = 0; i4 < childCount; i4++) {
                ZoomRatioView zoomRatioView3 = (ZoomRatioView) getChildAt(i4);
                if (i4 == i) {
                    zoomRatioView3.setZoomRatio(i2 == 1 ? opticalZoomRatioAt : this.mZoomRatio);
                    i3 = 4;
                } else if (i4 == this.mCurrentSelectedChildIndex) {
                    zoomRatioView3.setZoomRatio(opticalZoomRatioAt2);
                    i3 = 5;
                } else {
                    i3 = 6;
                }
                getTypeAnimator(arrayList, zoomRatioView3, i3);
            }
            this.mCurrentSelectedChildIndex = i;
            this.mShowZoomChildAnimatorSet.setDuration(200);
            this.mShowZoomChildAnimatorSet.cancel();
            this.mShowZoomChildAnimatorSet.removeAllListeners();
            this.mShowZoomChildAnimatorSet.addListener(new AnimatorListenerAdapter() {
                public void onAnimationEnd(Animator animator) {
                    super.onAnimationEnd(animator);
                    ZoomRatioView zoomRatioView = zoomRatioView2;
                    if (zoomRatioView != null) {
                        zoomRatioView.setIconify(true);
                        zoomRatioView2.resetScale();
                    }
                    ZoomRatioView zoomRatioView2 = zoomRatioView;
                    if (zoomRatioView2 != null) {
                        zoomRatioView2.setIconify(false);
                        zoomRatioView.resetScale();
                    }
                }
            });
            this.mShowZoomChildAnimatorSet.playTogether(arrayList);
            this.mShowZoomChildAnimatorSet.start();
        }
        if (i2 == 1) {
            ToggleStateListener toggleStateListener = this.mActionListener;
            if (toggleStateListener != null) {
                toggleStateListener.onClick(zoomRatioView);
            }
        }
        announceCurrentZoomRatioForAccessibility();
    }

    private void startLongPressCheck() {
        Runnable runnable = this.mLongPressRunnable;
        if (runnable == null) {
            this.mLongPressRunnable = new Runnable() {
                public void run() {
                    if (ZoomRatioToggleView.this.mTouchState == 1) {
                        ZoomRatioToggleView zoomRatioToggleView = ZoomRatioToggleView.this;
                        int access$700 = zoomRatioToggleView.getContainingChildIndex(zoomRatioToggleView.mTouchStartX, ZoomRatioToggleView.this.mTouchStartY);
                        if (access$700 != -1) {
                            ZoomRatioToggleView.this.longClickChild(access$700);
                        }
                        ZoomRatioToggleView.this.mTouchState = 3;
                    }
                }
            };
        } else {
            removeCallbacks(runnable);
        }
        postDelayed(this.mLongPressRunnable, (long) ViewConfiguration.getLongPressTimeout());
    }

    private boolean startScrollIfNeeded(MotionEvent motionEvent) {
        int x = (int) motionEvent.getX();
        int y = (int) motionEvent.getY();
        int i = this.mTouchStartX;
        if (x >= i - 10 && x <= i + 10) {
            int i2 = this.mTouchStartY;
            if (y >= i2 - 10 && y <= i2 + 10) {
                return false;
            }
        }
        removeCallbacks(this.mLongPressRunnable);
        this.mTouchState = 2;
        return true;
    }

    private void startTouch(MotionEvent motionEvent) {
        this.mTouchStartX = (int) motionEvent.getX();
        this.mTouchStartY = (int) motionEvent.getY();
        startLongPressCheck();
        this.mVelocityTracker = VelocityTracker.obtain();
        VelocityTracker velocityTracker = this.mVelocityTracker;
        if (velocityTracker != null) {
            velocityTracker.addMovement(motionEvent);
        }
        this.mTouchState = 1;
    }

    private void toShowView(float f) {
        boolean isSupportUseSlider = isSupportUseSlider(getContainingChildIndex(this.mTouchStartX, this.mTouchStartY));
        if (Math.abs(f) >= 100.0f && isSupportUseSlider) {
            ToggleStateListener toggleStateListener = this.mActionListener;
            if (toggleStateListener != null && toggleStateListener.toShowSlideView(null)) {
                performHapticFeedback(0);
                VelocityTracker velocityTracker = this.mVelocityTracker;
                if (velocityTracker != null) {
                    velocityTracker.recycle();
                    this.mVelocityTracker = null;
                }
                removeCallbacks(this.mLongPressRunnable);
                this.mTouchState = 4;
            }
        }
    }

    public boolean dispatchHoverEvent(MotionEvent motionEvent) {
        return this.myAccessHelper.dispatchHoverEvent(motionEvent) || super.dispatchHoverEvent(motionEvent);
    }

    public boolean isSuppressed() {
        return this.mIsSuppressed;
    }

    public void onClick(View view) {
        if (getVisibility() == 0) {
            debugUi(TAG, "UI AUTOMATIC TEST: CLICKED");
            clickChildAt(getWidth() / 2, getHeight() / 2);
        }
    }

    public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
        if (!isEnabled()) {
            return true;
        }
        int action = motionEvent.getAction();
        String str = TAG;
        if (action == 0) {
            StringBuilder sb = new StringBuilder();
            sb.append("onInterceptTouchEvent() DOWN: ");
            sb.append(this.mIsImmersive);
            debugUi(str, sb.toString());
            if ((this.mIsImmersive || this.mIsSuppressed) && getContainingChildIndex((int) motionEvent.getX(), (int) motionEvent.getY()) != this.mCurrentSelectedChildIndex) {
                return false;
            }
            ToggleStateListener toggleStateListener = this.mActionListener;
            if (toggleStateListener != null && !toggleStateListener.isInteractive()) {
                return false;
            }
            startTouch(motionEvent);
            return false;
        } else if (action != 2) {
            endTouch(0.0f);
            return false;
        } else {
            StringBuilder sb2 = new StringBuilder();
            sb2.append("onInterceptTouchEvent() MOVE: ");
            sb2.append(this.mIsImmersive);
            debugUi(str, sb2.toString());
            return startScrollIfNeeded(motionEvent);
        }
    }

    /* access modifiers changed from: protected */
    public void onLayout(boolean z, int i, int i2, int i3, int i4) {
        int childCount = getChildCount();
        if (childCount > 0) {
            int i5 = childCount == 0 ? 0 : childCount / 2;
            boolean isLayoutRTL = isLayoutRTL();
            int width = getWidth() / 2;
            int i6 = this.mItemWidth;
            int i7 = width - (i6 / 2);
            int i8 = i5 * i6;
            int i9 = !isLayoutRTL ? i7 - i8 : i7 + i8;
            int height = (getHeight() / 2) - (this.mItemHeight / 2);
            for (int i10 = 0; i10 < childCount; i10++) {
                getChildAt(i10).layout(i9, height, this.mItemWidth + i9, this.mItemHeight + height);
                int i11 = this.mItemWidth;
                i9 = !isLayoutRTL ? i9 + i11 : i9 - i11;
            }
        }
    }

    public boolean onLongClick(View view) {
        if (getVisibility() != 0) {
            return false;
        }
        debugUi(TAG, "UI AUTOMATIC TEST: LONGCLICKED");
        longClickChild(this.mCurrentSelectedChildIndex);
        return true;
    }

    /* access modifiers changed from: protected */
    public void onMeasure(int i, int i2) {
        measureChildren(i, i2);
        int childCount = getChildCount();
        int i3 = 0;
        int i4 = 0;
        int i5 = 0;
        for (int i6 = 0; i6 < childCount; i6++) {
            View childAt = getChildAt(i6);
            if (childAt.getVisibility() != 8) {
                i4 += childAt.getMeasuredWidth();
                i5 = Math.max(i5, childAt.getMeasuredHeight());
                i3++;
            }
        }
        this.mItemWidth = i3 == 0 ? 0 : i4 / i3;
        this.mItemHeight = i5;
        setMeasuredDimension(ViewGroup.resolveSizeAndState(Math.max(i4 + getPaddingLeft() + getPaddingRight(), getSuggestedMinimumWidth()), i, 0), ViewGroup.resolveSizeAndState(Math.max(i5 + getPaddingTop() + getPaddingBottom(), getSuggestedMinimumHeight()), i2, 0));
    }

    /* JADX WARNING: Code restructure failed: missing block: B:12:0x0021, code lost:
        if (r0 != 3) goto L_0x0023;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean onTouchEvent(MotionEvent motionEvent) {
        if (getChildCount() == 0 || !isEnabled()) {
            return false;
        }
        int action = motionEvent.getAction();
        String str = TAG;
        if (action != 0) {
            float f = 0.0f;
            if (action != 1) {
                if (action == 2) {
                    StringBuilder sb = new StringBuilder();
                    sb.append("onTouchEvent() MOVE: ");
                    sb.append(this.mIsImmersive);
                    debugUi(str, sb.toString());
                    if (this.mTouchState == 1) {
                        startScrollIfNeeded(motionEvent);
                    }
                    if (this.mTouchState == 2) {
                        VelocityTracker velocityTracker = this.mVelocityTracker;
                        if (velocityTracker == null) {
                            return false;
                        }
                        velocityTracker.addMovement(motionEvent);
                        this.mVelocityTracker.computeCurrentVelocity(1000);
                        toShowView(this.mVelocityTracker.getXVelocity());
                    }
                }
            }
            StringBuilder sb2 = new StringBuilder();
            sb2.append("onTouchEvent() UP: ");
            sb2.append(this.mIsImmersive);
            debugUi(str, sb2.toString());
            ToggleStateListener toggleStateListener = this.mActionListener;
            if (toggleStateListener != null && toggleStateListener.isInteractive() && this.mTouchState == 1) {
                clickChildAt((int) motionEvent.getX(), (int) motionEvent.getY());
            }
            if (this.mUseSliderType != 0 && this.mTouchState == 4) {
                return true;
            }
            if (this.mTouchState == 2) {
                VelocityTracker velocityTracker2 = this.mVelocityTracker;
                if (velocityTracker2 == null) {
                    return false;
                }
                velocityTracker2.addMovement(motionEvent);
                this.mVelocityTracker.computeCurrentVelocity(1000);
                f = this.mVelocityTracker.getXVelocity();
            }
            endTouch(f);
        } else {
            StringBuilder sb3 = new StringBuilder();
            sb3.append("onTouchEvent() DOWN: ");
            sb3.append(this.mIsImmersive);
            debugUi(str, sb3.toString());
            int x = (int) motionEvent.getX();
            int y = (int) motionEvent.getY();
            if (this.mIsImmersive || this.mIsSuppressed) {
                if (getContainingChildIndex(x, y) != this.mCurrentSelectedChildIndex) {
                    return false;
                }
            } else if (getContainingChildIndex(x, y) == -1) {
                return false;
            }
            ToggleStateListener toggleStateListener2 = this.mActionListener;
            if (toggleStateListener2 != null && !toggleStateListener2.isInteractive()) {
                return false;
            }
            startTouch(motionEvent);
        }
        return true;
    }

    public void setActionListener(ToggleStateListener toggleStateListener) {
        this.mActionListener = toggleStateListener;
    }

    public void setBackgroundColor(int i) {
        for (int i2 = 0; i2 < getChildCount(); i2++) {
            ((ZoomRatioView) getChildAt(i2)).setBackgroundColor(i);
        }
    }

    public void setCaptureCount(int i) {
        ZoomRatioView zoomRatioView;
        if (this.mIsImmersive || this.mIsSuppressed) {
            zoomRatioView = (ZoomRatioView) getChildAt(this.mIsSuppressed ? 0 : getChildCount() / 2);
        } else {
            zoomRatioView = (ZoomRatioView) getChildAt(this.mCurrentSelectedChildIndex);
            if (zoomRatioView == null) {
                return;
            }
        }
        zoomRatioView.setCaptureCount(i);
    }

    public boolean setCapturingMode(int i, boolean z) {
        this.mCurrentModule = i;
        float[] supportedOpticalZoomRatios = HybridZoomingSystem.getSupportedOpticalZoomRatios(this.mCurrentModule);
        if (z) {
            supportedOpticalZoomRatios = new float[]{1.0f};
        }
        int length = supportedOpticalZoomRatios.length;
        if (length <= 0) {
            return false;
        }
        float[] fArr = this.mZoomArray;
        if (fArr == null || !Arrays.equals(fArr, supportedOpticalZoomRatios)) {
            this.mZoomArray = supportedOpticalZoomRatios;
            int defaultOpticalZoomRatioIndex = z ? 0 : HybridZoomingSystem.getDefaultOpticalZoomRatioIndex(this.mCurrentModule);
            resetAnimators();
            removeAllViews();
            int i2 = 0;
            while (i2 < length) {
                ZoomRatioView zoomRatioView = (ZoomRatioView) LayoutInflater.from(getContext()).inflate(R.layout.zoom_ratio_item_view, null);
                zoomRatioView.getIconView().setVisibility(0);
                zoomRatioView.getTextView().setVisibility(0);
                zoomRatioView.setZoomRatio(supportedOpticalZoomRatios[i2]);
                zoomRatioView.setZoomRatioIndex(i2);
                zoomRatioView.setIconify(i2 != defaultOpticalZoomRatioIndex);
                addView(zoomRatioView, new LayoutParams(-2, -2));
                i2++;
            }
            this.mCurrentSelectedChildIndex = defaultOpticalZoomRatioIndex;
            this.mZoomRatio = supportedOpticalZoomRatios[defaultOpticalZoomRatioIndex];
            setSuppressed(z, true);
            return true;
        }
        setSuppressed(z, false);
        return false;
    }

    public void setEnabled(boolean z) {
        super.setEnabled(z);
        StringBuilder sb = new StringBuilder();
        sb.append("setEnabled(): ");
        sb.append(z);
        debugUi(TAG, sb.toString());
    }

    public void setImmersive(boolean z, boolean z2) {
        setImmersive(z, z2, true);
    }

    public void setImmersive(boolean z, boolean z2, final boolean z3) {
        StringBuilder sb = new StringBuilder();
        sb.append("setImmersive(): ");
        sb.append(z);
        String sb2 = sb.toString();
        String str = TAG;
        debugUi(str, sb2);
        if (z != this.mIsImmersive || z2) {
            this.mIsImmersive = z;
            ArrayList arrayList = new ArrayList();
            final int opticalZoomRatioIndex = HybridZoomingSystem.getOpticalZoomRatioIndex(this.mCurrentModule, this.mZoomRatio);
            if (!this.mIsImmersive) {
                if (!this.mIsSuppressed) {
                    if (opticalZoomRatioIndex == getChildCount() / 2) {
                        int childCount = getChildCount();
                        for (int i = 0; i < childCount; i++) {
                            ZoomRatioView zoomRatioView = (ZoomRatioView) getChildAt(i);
                            zoomRatioView.setVisibility(0);
                            if (i == opticalZoomRatioIndex) {
                                zoomRatioView.setZoomRatio(this.mZoomRatio);
                                getTypeAnimator(arrayList, zoomRatioView, 8);
                            } else {
                                getTypeAnimator(arrayList, zoomRatioView, 1);
                            }
                        }
                        this.mShowZoomChildAnimatorSet.setDuration(400);
                    } else {
                        int childCount2 = getChildCount();
                        for (int i2 = 0; i2 < childCount2; i2++) {
                            ZoomRatioView zoomRatioView2 = (ZoomRatioView) getChildAt(i2);
                            zoomRatioView2.setVisibility(0);
                            if (i2 == childCount2 / 2) {
                                getTypeAnimator(arrayList, zoomRatioView2, 5);
                            } else if (i2 == opticalZoomRatioIndex) {
                                zoomRatioView2.setZoomRatio(this.mZoomRatio);
                                getTypeAnimator(arrayList, zoomRatioView2, 0);
                            } else {
                                getTypeAnimator(arrayList, zoomRatioView2, 1);
                            }
                        }
                        this.mShowZoomChildAnimatorSet.setDuration(200);
                    }
                    this.mShowZoomChildAnimatorSet.cancel();
                    this.mShowZoomChildAnimatorSet.removeAllListeners();
                    this.mShowZoomChildAnimatorSet.addListener(new AnimatorListenerAdapter() {
                        public void onAnimationEnd(Animator animator) {
                            super.onAnimationEnd(animator);
                            int childCount = ZoomRatioToggleView.this.getChildCount();
                            for (int i = 0; i < childCount; i++) {
                                ZoomRatioView zoomRatioView = (ZoomRatioView) ZoomRatioToggleView.this.getChildAt(i);
                                zoomRatioView.setVisibility(0);
                                zoomRatioView.resetScale();
                                if (i == opticalZoomRatioIndex) {
                                    zoomRatioView.setZoomRatio(ZoomRatioToggleView.this.mZoomRatio);
                                    zoomRatioView.setIconify(false);
                                } else {
                                    zoomRatioView.setIconify(true);
                                }
                            }
                        }
                    });
                }
                return;
            } else if (!this.mIsSuppressed) {
                if (opticalZoomRatioIndex == getChildCount() / 2) {
                    int childCount3 = getChildCount();
                    for (int i3 = 0; i3 < childCount3; i3++) {
                        ZoomRatioView zoomRatioView3 = (ZoomRatioView) getChildAt(i3);
                        zoomRatioView3.setVisibility(0);
                        if (i3 == opticalZoomRatioIndex) {
                            getTypeAnimator(arrayList, zoomRatioView3, 7);
                        } else {
                            getTypeAnimator(arrayList, zoomRatioView3, 3);
                        }
                    }
                } else {
                    int childCount4 = getChildCount();
                    for (int i4 = 0; i4 < childCount4; i4++) {
                        ZoomRatioView zoomRatioView4 = (ZoomRatioView) getChildAt(i4);
                        zoomRatioView4.setVisibility(0);
                        if (i4 == childCount4 / 2) {
                            getTypeAnimator(arrayList, zoomRatioView4, 4);
                        } else if (i4 == opticalZoomRatioIndex) {
                            getTypeAnimator(arrayList, zoomRatioView4, 2);
                        } else {
                            getTypeAnimator(arrayList, zoomRatioView4, 3);
                        }
                    }
                }
                this.mShowZoomChildAnimatorSet.setDuration(200);
                this.mShowZoomChildAnimatorSet.cancel();
                this.mShowZoomChildAnimatorSet.removeAllListeners();
                this.mShowZoomChildAnimatorSet.addListener(new AnimatorListenerAdapter() {
                    public void onAnimationEnd(Animator animator) {
                        super.onAnimationEnd(animator);
                        int childCount = ZoomRatioToggleView.this.getChildCount();
                        int i = 0;
                        while (i < childCount) {
                            ZoomRatioView zoomRatioView = (ZoomRatioView) ZoomRatioToggleView.this.getChildAt(i);
                            zoomRatioView.resetScale();
                            if (childCount == 1 || i == childCount / 2) {
                                zoomRatioView.setVisibility(0);
                                zoomRatioView.setIconify(false);
                            } else {
                                zoomRatioView.setVisibility(4);
                                zoomRatioView.setIconify(true);
                            }
                            i++;
                        }
                    }

                    public void onAnimationStart(Animator animator) {
                        super.onAnimationStart(animator);
                        if (!z3) {
                            int i = 0;
                            int childCount = ZoomRatioToggleView.this.getChildCount();
                            while (i < childCount) {
                                ZoomRatioView zoomRatioView = (ZoomRatioView) ZoomRatioToggleView.this.getChildAt(i);
                                if (!(childCount == 1 || i == childCount / 2)) {
                                    zoomRatioView.setVisibility(4);
                                    zoomRatioView.setIconify(true);
                                }
                                i++;
                            }
                        }
                    }
                });
            } else {
                return;
            }
            this.mShowZoomChildAnimatorSet.playTogether(arrayList);
            this.mShowZoomChildAnimatorSet.start();
            return;
        }
        StringBuilder sb3 = new StringBuilder();
        sb3.append("setImmersive() ignored: ");
        sb3.append(z);
        debugUi(str, sb3.toString());
    }

    public void setRotation(float f) {
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childAt = getChildAt(i);
            if (childAt != null) {
                childAt.setRotation(f);
            }
        }
    }

    public void setSuppressed(boolean z, boolean z2) {
        StringBuilder sb = new StringBuilder();
        sb.append("setSuppressed(): ");
        sb.append(z);
        String sb2 = sb.toString();
        String str = TAG;
        debugUi(str, sb2);
        if (z != this.mIsSuppressed || z2) {
            this.mIsSuppressed = z;
            this.mIsImmersive = z;
            if (this.mIsSuppressed) {
                getChildAt(0).setVisibility(0);
            } else if (!this.mIsImmersive) {
                int opticalZoomRatioIndex = HybridZoomingSystem.getOpticalZoomRatioIndex(this.mCurrentModule, this.mZoomRatio);
                int childCount = getChildCount();
                for (int i = 0; i < childCount; i++) {
                    ZoomRatioView zoomRatioView = (ZoomRatioView) getChildAt(i);
                    if (i == opticalZoomRatioIndex) {
                        zoomRatioView.setZoomRatio(this.mZoomRatio);
                        zoomRatioView.setIconify(false);
                    } else {
                        zoomRatioView.setIconify(true);
                    }
                    zoomRatioView.setVisibility(0);
                }
            }
            return;
        }
        StringBuilder sb3 = new StringBuilder();
        sb3.append("setSuppressed() ignored: ");
        sb3.append(z);
        debugUi(str, sb3.toString());
    }

    public void setUseSliderAllowed(int i) {
        this.mUseSliderType = i;
    }

    public void setVisibility(int i) {
        super.setVisibility(i);
        StringBuilder sb = new StringBuilder();
        sb.append("setVisibility(): ");
        sb.append(Util.viewVisibilityToString(i));
        debugUi(TAG, sb.toString());
        if (i != 0) {
            resetAnimators();
        }
    }

    public void setZoomRatio(float f, int i) {
        if (Thread.currentThread().equals(Looper.getMainLooper().getThread())) {
            int opticalZoomRatioIndex = HybridZoomingSystem.getOpticalZoomRatioIndex(this.mCurrentModule, f);
            StringBuilder sb = new StringBuilder();
            sb.append("setZoomRatio(): zooming action = ");
            sb.append(ZoomingAction.toString(i));
            String sb2 = sb.toString();
            String str = TAG;
            com.android.camera.log.Log.u(str, sb2);
            StringBuilder sb3 = new StringBuilder();
            sb3.append("setZoomRatio():  current index = ");
            sb3.append(this.mCurrentSelectedChildIndex);
            com.android.camera.log.Log.u(str, sb3.toString());
            StringBuilder sb4 = new StringBuilder();
            sb4.append("setZoomRatio():   current zoom = ");
            sb4.append(this.mZoomRatio);
            com.android.camera.log.Log.u(str, sb4.toString());
            StringBuilder sb5 = new StringBuilder();
            sb5.append("setZoomRatio():   target index = ");
            sb5.append(opticalZoomRatioIndex);
            com.android.camera.log.Log.u(str, sb5.toString());
            StringBuilder sb6 = new StringBuilder();
            sb6.append("setZoomRatio():    target zoom = ");
            sb6.append(f);
            com.android.camera.log.Log.u(str, sb6.toString());
            this.mZoomRatio = f;
            if (this.mIsSuppressed) {
                ((ZoomRatioView) getChildAt(0)).setZoomRatio(this.mZoomRatio);
                announceCurrentZoomRatioForAccessibility();
                debugUi(str, "setZoomRatio(): mIsSuppressed");
            } else if (this.mIsImmersive) {
                ((ZoomRatioView) getChildAt(getChildCount() / 2)).setZoomRatio(this.mZoomRatio);
                announceCurrentZoomRatioForAccessibility();
                debugUi(str, "setZoomRatio(): mIsImmersive");
            } else if (i == 0) {
                debugUi(str, "setZoomRatio(): ignored as source is toggle button");
            } else {
                boolean z = UI_DEBUG_ENABLED;
                removeCallbacks(this.mIndexUpdater);
                post(this.mIndexUpdater);
            }
        } else {
            throw new IllegalStateException("setZoomRatio() must be called on main ui thread.");
        }
    }

    public void startTranslationAnimationShow() {
        ObjectAnimator objectAnimator;
        ArrayList arrayList = new ArrayList();
        int childCount = getChildCount();
        int i = 0;
        while (i < childCount) {
            final ZoomRatioView zoomRatioView = (ZoomRatioView) getChildAt(i);
            final float translationX = zoomRatioView.getTranslationX();
            if (i != this.mCurrentSelectedChildIndex) {
                ViewCompat.setAlpha(zoomRatioView, 1.0f);
                objectAnimator = ObjectAnimator.ofFloat(zoomRatioView, View.TRANSLATION_X, new float[]{((float) ((int) (((float) ((isLayoutRTL() ? this.mCurrentSelectedChildIndex - i : i - this.mCurrentSelectedChildIndex) * this.mItemWidth)) * 0.1f))) + translationX, translationX});
            } else {
                ViewCompat.setAlpha(zoomRatioView, 0.0f);
                objectAnimator = ObjectAnimator.ofFloat(zoomRatioView, View.TRANSLATION_X, new float[]{translationX, translationX});
            }
            arrayList.add(objectAnimator);
            objectAnimator.addListener(new AnimatorListenerAdapter() {
                public void onAnimationEnd(Animator animator) {
                    super.onAnimationEnd(animator);
                    ViewCompat.setTranslationX(zoomRatioView, translationX);
                }
            });
            i++;
        }
        this.mZoomShowAnimatorSet.cancel();
        this.mZoomShowAnimatorSet.removeAllListeners();
        this.mZoomShowAnimatorSet.playTogether(arrayList);
        this.mZoomShowAnimatorSet.start();
    }
}
