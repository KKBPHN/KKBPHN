package com.android.camera.fragment.mode;

import android.graphics.Rect;
import android.view.View;
import android.view.animation.Interpolator;
import android.widget.LinearLayout;
import androidx.core.view.ViewCompat;
import androidx.core.view.ViewPropertyAnimatorCompat;
import com.android.camera.R;
import com.android.camera.log.Log;
import com.android.camera.ui.NormalRoundView;
import com.android.camera.ui.SmoothRoundLayout;
import miuix.animation.Folme;
import miuix.animation.base.AnimConfig;
import miuix.animation.controller.AnimState;
import miuix.animation.listener.TransitionListener;
import miuix.animation.property.FloatProperty;
import miuix.animation.property.ViewProperty;
import miuix.view.animation.CubicEaseOutInterpolator;

public class MoreModeListAnimation {
    public static final int DIRECTION_B2T = 3;
    public static final int DIRECTION_L2R = 0;
    public static final int DIRECTION_R2L = 1;
    public static final int DIRECTION_T2B = 2;
    public static final int DIRECTION_UNKNOWN = -1;
    private static final int DISTANCE = 300;
    private static final String TAG = "MoreModeAnimation";
    private static MoreModeListAnimation sAnimation;
    private Interpolator mInterpolator = new CubicEaseOutInterpolator();
    private IMoreMode mMoreModeNew;
    private IMoreMode mMoreModeOld;
    ViewProperty mRadiusProp = new ViewProperty("cornerRadius") {
        public float getValue(View view) {
            if (view instanceof SmoothRoundLayout) {
                return ((SmoothRoundLayout) view).getCornerRadius();
            }
            if (view instanceof LinearLayout) {
                NormalRoundView normalRoundView = (NormalRoundView) view.findViewById(R.id.mode_icon_layout);
                if (normalRoundView != null) {
                    return normalRoundView.getCornerRadius();
                }
            }
            return 0.0f;
        }

        public void setValue(View view, float f) {
            if (view instanceof SmoothRoundLayout) {
                ((SmoothRoundLayout) view).setCornerRadius(f);
            } else if (view instanceof LinearLayout) {
                NormalRoundView normalRoundView = (NormalRoundView) view.findViewById(R.id.mode_icon_layout);
                if (normalRoundView != null) {
                    normalRoundView.setCornerRadius(f);
                }
            }
        }
    };
    private int mSize;
    private SpringState mSpringState;

    public interface OnSpringUpdateListener {
        boolean canScrollDown();

        boolean canScrollUp();

        float getOverScrollX();

        float getOverScrollY();

        float getRotate();

        void onUpdate(float f, float f2);
    }

    class SpringState {
        private boolean mDragging;
        private AnimConfig mEnterConfig = new AnimConfig().setEase(6, 500.0f).addListeners(new TransitionListener() {
            public void onComplete(Object obj) {
                super.onComplete(obj);
                SpringState.this.mTranX = 0.0f;
            }

            public void onUpdate(Object obj, FloatProperty floatProperty, float f, float f2, boolean z) {
                OnSpringUpdateListener onSpringUpdateListener;
                float f3;
                OnSpringUpdateListener onSpringUpdateListener2;
                float f4;
                super.onUpdate(obj, floatProperty, f, f2, z);
                SpringState.this.mTranX = f;
                int rotate = (int) SpringState.this.mListener.getRotate();
                if (rotate != 0) {
                    if (rotate == 90) {
                        onSpringUpdateListener2 = SpringState.this.mListener;
                        f4 = -SpringState.this.mTranX;
                    } else if (rotate == 180) {
                        onSpringUpdateListener = SpringState.this.mListener;
                        f3 = -SpringState.this.mTranX;
                    } else if (rotate == 270) {
                        onSpringUpdateListener2 = SpringState.this.mListener;
                        f4 = SpringState.this.mTranX;
                    } else {
                        return;
                    }
                    onSpringUpdateListener2.onUpdate(0.0f, f4);
                    return;
                }
                onSpringUpdateListener = SpringState.this.mListener;
                f3 = SpringState.this.mTranX;
                onSpringUpdateListener.onUpdate(f3, 0.0f);
            }
        });
        /* access modifiers changed from: private */
        public float mFollowY;
        private boolean mIsStart;
        private float mLeadY;
        /* access modifiers changed from: private */
        public OnSpringUpdateListener mListener;
        /* access modifiers changed from: private */
        public float mTranX;
        /* access modifiers changed from: private */
        public float mTranY;
        private AnimConfig mUpdateYConfig = new AnimConfig().setEase(18, 130.0f).addListeners(new TransitionListener() {
            public void onUpdate(Object obj, FloatProperty floatProperty, float f, float f2, boolean z) {
                OnSpringUpdateListener onSpringUpdateListener;
                float f3;
                OnSpringUpdateListener onSpringUpdateListener2;
                float f4;
                super.onUpdate(obj, floatProperty, f, f2, z);
                SpringState.this.mFollowY = f;
                SpringState springState = SpringState.this;
                springState.mTranY = springState.mListener.getOverScrollY() - SpringState.this.mFollowY;
                if (SpringState.this.mListener != null) {
                    int rotate = (int) SpringState.this.mListener.getRotate();
                    if (rotate != 0) {
                        if (rotate == 90) {
                            onSpringUpdateListener2 = SpringState.this.mListener;
                            f4 = SpringState.this.mTranY;
                        } else if (rotate == 180) {
                            onSpringUpdateListener = SpringState.this.mListener;
                            f3 = -SpringState.this.mTranY;
                        } else if (rotate == 270) {
                            onSpringUpdateListener2 = SpringState.this.mListener;
                            f4 = -SpringState.this.mTranY;
                        } else {
                            return;
                        }
                        onSpringUpdateListener2.onUpdate(f4, 0.0f);
                        return;
                    }
                    onSpringUpdateListener = SpringState.this.mListener;
                    f3 = SpringState.this.mTranY;
                    onSpringUpdateListener.onUpdate(0.0f, f3);
                }
            }
        });

        public SpringState(OnSpringUpdateListener onSpringUpdateListener) {
            this.mListener = onSpringUpdateListener;
        }

        public void startEnter() {
            AnimState add = new AnimState("from").add(ViewProperty.TRANSLATION_X, 50.0f, new long[0]);
            AnimState add2 = new AnimState("to").add(ViewProperty.TRANSLATION_X, 0.0f, new long[0]);
            Folme.useValue(new Object[0]).fromTo(add, add2, this.mEnterConfig);
        }

        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("SpringState{mTranX=");
            sb.append(this.mTranX);
            sb.append(", mTranY=");
            sb.append(this.mTranY);
            sb.append(", mLeadY=");
            sb.append(this.mLeadY);
            sb.append(", mFollowY=");
            sb.append(this.mFollowY);
            sb.append(", mDragging=");
            sb.append(this.mDragging);
            sb.append(", mIsStart=");
            sb.append(this.mIsStart);
            sb.append('}');
            return sb.toString();
        }

        public void updateInnerSpringAnim() {
            if (!this.mListener.canScrollUp() || !this.mListener.canScrollDown()) {
                if (!this.mListener.canScrollUp() || !this.mListener.canScrollDown()) {
                    if (!this.mIsStart) {
                        this.mLeadY = 0.0f;
                        this.mFollowY = 0.0f;
                        this.mTranY = 0.0f;
                        this.mDragging = true;
                        this.mIsStart = true;
                    } else if (this.mDragging) {
                        Folme.useValue(this).setTo((Object) Float.valueOf(this.mFollowY)).to(Float.valueOf(this.mListener.getOverScrollY()), this.mUpdateYConfig);
                    }
                }
                return;
            }
            this.mListener.onUpdate(0.0f, 0.0f);
            this.mLeadY = 0.0f;
            this.mFollowY = 0.0f;
            this.mTranY = 0.0f;
            this.mIsStart = false;
            this.mDragging = false;
        }
    }

    private int getCol(int i) {
        return 0;
    }

    public static MoreModeListAnimation getInstance() {
        if (sAnimation == null) {
            sAnimation = new MoreModeListAnimation();
        }
        return sAnimation;
    }

    private int getRow(int i) {
        return 0;
    }

    public void clearSpring() {
        this.mSpringState = null;
        Log.d(TAG, "clearSpring");
    }

    public void initSpring(OnSpringUpdateListener onSpringUpdateListener) {
        this.mSpringState = new SpringState(onSpringUpdateListener);
        Log.d(TAG, "initSpring");
    }

    public void initSwitchAnimation(IMoreMode iMoreMode, IMoreMode iMoreMode2, int i) {
        this.mMoreModeNew = iMoreMode2;
        this.mMoreModeOld = iMoreMode;
        this.mSize = i;
    }

    public void releaseSwitchAnimation() {
        this.mMoreModeNew = null;
        this.mMoreModeOld = null;
    }

    @Deprecated
    public void startAlphaAnimation(View view, int i) {
        view.setAlpha(0.0f);
        ViewCompat.animate(view).setInterpolator(this.mInterpolator).alpha(1.0f).setDuration(200).start();
    }

    public void startInnerEnterAnim() {
        SpringState springState = this.mSpringState;
        if (springState != null) {
            springState.startEnter();
        }
    }

    public void startInnerSpringAnim() {
        SpringState springState = this.mSpringState;
        if (springState != null) {
            springState.updateInnerSpringAnim();
        }
    }

    public void startShowNewAnimation(View view, int i) {
        if (this.mMoreModeNew != null && this.mMoreModeOld != null) {
            int i2 = i;
            Rect region = MoreModeHelper.getRegion(view.getContext(), this.mMoreModeOld.getType(), this.mMoreModeOld.getCountPerLine(), this.mMoreModeNew.getCountPerLine(), i2, this.mSize);
            Rect region2 = MoreModeHelper.getRegion(view.getContext(), this.mMoreModeNew.getType(), this.mMoreModeNew.getCountPerLine(), this.mMoreModeNew.getCountPerLine(), i2, this.mSize);
            StringBuilder sb = new StringBuilder();
            sb.append("start new region ");
            sb.append(region);
            sb.append(", end region ");
            sb.append(region2);
            Log.d(TAG, sb.toString());
            float width = ((((float) (region.width() - region2.width())) / 2.0f) + ((float) region.left)) - ((float) region2.left);
            float width2 = ((((float) (region.width() - region2.width())) / 2.0f) + ((float) region.top)) - ((float) region2.top);
            float width3 = ((float) region.width()) / ((float) region2.width());
            float width4 = ((float) region.width()) / 2.0f;
            float dimension = view.getResources().getDimension(R.dimen.mode_item_radius_new);
            AnimState add = new AnimState("from").add(ViewProperty.TRANSLATION_X, width, new long[0]).add(ViewProperty.TRANSLATION_Y, width2, new long[0]).add(ViewProperty.SCALE_X, width3, new long[0]).add(ViewProperty.SCALE_Y, width3, new long[0]).add(ViewProperty.ALPHA, 0.0f, new long[0]).add(this.mRadiusProp, width4, new long[0]);
            AnimState add2 = new AnimState("to").add(ViewProperty.TRANSLATION_X, 0.0f, new long[0]).add(ViewProperty.TRANSLATION_Y, 0.0f, new long[0]).add(ViewProperty.SCALE_X, 1.0f, new long[0]).add(ViewProperty.SCALE_Y, 1.0f, new long[0]).add(ViewProperty.ALPHA, 1.0f, new long[0]).add(this.mRadiusProp, dimension, new long[0]);
            AnimConfig ease = new AnimConfig().setEase(-2, 0.9f, 0.3f);
            Folme.useAt(view).state().setTo((Object) add).to(add2, ease);
        }
    }

    public void startShowOldAnimation(View view, int i) {
        if (this.mMoreModeNew != null && this.mMoreModeOld != null) {
            int i2 = i;
            Rect region = MoreModeHelper.getRegion(view.getContext(), this.mMoreModeNew.getType(), this.mMoreModeNew.getCountPerLine(), this.mMoreModeOld.getCountPerLine(), i2, this.mSize);
            Rect region2 = MoreModeHelper.getRegion(view.getContext(), this.mMoreModeOld.getType(), this.mMoreModeOld.getCountPerLine(), this.mMoreModeOld.getCountPerLine(), i2, this.mSize);
            StringBuilder sb = new StringBuilder();
            sb.append("start old region ");
            sb.append(region);
            sb.append(", end region ");
            sb.append(region2);
            Log.d(TAG, sb.toString());
            float width = ((((float) (region.width() - region2.width())) / 2.0f) + ((float) region.left)) - ((float) region2.left);
            float width2 = ((((float) (region.width() - region2.width())) / 2.0f) + ((float) region.top)) - ((float) region2.top);
            float width3 = ((float) region.width()) / ((float) region2.width());
            float dimension = (view.getResources().getDimension(R.dimen.mode_item_radius_new) * ((float) region2.width())) / ((float) region.width());
            float width4 = ((float) region2.width()) / 2.0f;
            AnimState add = new AnimState("from").add(ViewProperty.TRANSLATION_X, width, new long[0]).add(ViewProperty.TRANSLATION_Y, width2, new long[0]).add(ViewProperty.SCALE_X, width3, new long[0]).add(ViewProperty.SCALE_Y, width3, new long[0]).add(ViewProperty.ALPHA, 0.0f, new long[0]).add(this.mRadiusProp, dimension, new long[0]);
            AnimState add2 = new AnimState("to").add(ViewProperty.TRANSLATION_X, 0.0f, new long[0]).add(ViewProperty.TRANSLATION_Y, 0.0f, new long[0]).add(ViewProperty.SCALE_X, 1.0f, new long[0]).add(ViewProperty.SCALE_Y, 1.0f, new long[0]).add(ViewProperty.ALPHA, 1.0f, new long[0]).add(this.mRadiusProp, width4, new long[0]);
            AnimConfig ease = new AnimConfig().setEase(-2, 0.9f, 0.3f);
            ease.addListeners(new TransitionListener() {
                public void onUpdate(Object obj, FloatProperty floatProperty, float f, float f2, boolean z) {
                    super.onUpdate(obj, floatProperty, f, f2, z);
                }
            });
            Folme.useAt(view).state().setTo((Object) add).to(add2, ease);
        }
    }

    @Deprecated
    public void startTranAnimation(View view, int i, int i2) {
        boolean z = false;
        boolean z2 = true;
        if (i2 == 0) {
            getCol(i);
            z2 = false;
            z = true;
        } else if (i2 == 1) {
            getCol(i);
            z = true;
        } else if (i2 == 2) {
            getRow(i);
            z2 = false;
        } else if (i2 == 3) {
            getRow(i);
        } else {
            throw new IllegalArgumentException("unknown direction.");
        }
        StringBuilder sb = new StringBuilder();
        sb.append("position = ");
        sb.append(i);
        Log.d(TAG, sb.toString());
        ViewPropertyAnimatorCompat duration = ViewCompat.animate(view).setInterpolator(this.mInterpolator).setDuration(200);
        float f = 300.0f;
        if (z) {
            if (!z2) {
                f = -300.0f;
            }
            view.setTranslationX(f);
            duration.translationX(0.0f);
        } else {
            if (!z2) {
                f = -300.0f;
            }
            view.setTranslationY(f);
            duration.translationY(0.0f);
        }
        duration.start();
    }
}
