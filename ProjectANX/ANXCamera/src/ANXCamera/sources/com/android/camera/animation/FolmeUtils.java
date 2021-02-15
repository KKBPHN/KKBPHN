package com.android.camera.animation;

import android.view.View;
import android.view.View.OnClickListener;
import androidx.annotation.ColorRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.android.camera.R;
import com.android.camera.log.Log;
import miuix.animation.Folme;
import miuix.animation.IStateStyle;
import miuix.animation.ITouchStyle.TouchType;
import miuix.animation.IVisibleStyle;
import miuix.animation.IVisibleStyle.VisibleType;
import miuix.animation.base.AnimConfig;
import miuix.animation.controller.AnimState;
import miuix.animation.listener.TransitionListener;
import miuix.animation.listener.UpdateInfo;
import miuix.animation.property.FloatProperty;
import miuix.animation.property.ViewProperty;

public class FolmeUtils {
    public static final int ANIMATION_DURATION = 200;
    private static final boolean DEBUG = true;
    public static final int DIRECTION_B2T = 3;
    public static final int DIRECTION_L2R = 0;
    public static final int DIRECTION_R2L = 1;
    public static final int DIRECTION_T2B = 2;
    public static final int DIRECTION_UNKNOWN = -1;
    private static final float NORMAL_SPRING_DAMPING = 0.9f;
    private static final float NORMAL_SPRING_RESPONSE = 0.3f;
    private static final int STATE_SELECTED = 1;
    private static final int STATE_UNSELECTED = 2;
    private static final String TAG = "FolmeUtils";

    public interface IPhyAnimatorListener {
        void onCancel();

        void onEnd();

        void onStart();

        void onUpdate(float f);
    }

    public class PhyAnimatorListener extends TransitionListener {
        private IPhyAnimatorListener mListener;

        public PhyAnimatorListener() {
        }

        public PhyAnimatorListener(IPhyAnimatorListener iPhyAnimatorListener) {
            this.mListener = iPhyAnimatorListener;
        }

        public void onBegin(Object obj, UpdateInfo updateInfo) {
            super.onBegin(obj, updateInfo);
            IPhyAnimatorListener iPhyAnimatorListener = this.mListener;
            if (iPhyAnimatorListener != null) {
                iPhyAnimatorListener.onStart();
            }
        }

        public void onCancel(Object obj) {
            super.onCancel(obj);
            IPhyAnimatorListener iPhyAnimatorListener = this.mListener;
            if (iPhyAnimatorListener != null) {
                iPhyAnimatorListener.onCancel();
            }
        }

        public void onComplete(Object obj) {
            super.onComplete(obj);
            IPhyAnimatorListener iPhyAnimatorListener = this.mListener;
            if (iPhyAnimatorListener != null) {
                iPhyAnimatorListener.onEnd();
            }
        }

        public void onUpdate(Object obj, FloatProperty floatProperty, float f, float f2, boolean z) {
            super.onUpdate(obj, floatProperty, f, f2, z);
            IPhyAnimatorListener iPhyAnimatorListener = this.mListener;
            if (iPhyAnimatorListener != null) {
                iPhyAnimatorListener.onUpdate(f);
            }
        }
    }

    private static void LogD(String str) {
        Log.d(TAG, str);
    }

    public static void animateDeparture(View view) {
        animateDeparture(view, null);
    }

    public static void animateDeparture(final View view, final Runnable runnable) {
        clean(view);
        if (runnable == null) {
            Folme.useAt(view).visible().hide(new AnimConfig[0]);
            return;
        }
        Folme.useAt(view).visible().hide(new AnimConfig().addListeners(new TransitionListener() {
            public void onComplete(Object obj) {
                super.onComplete(obj);
                Runnable runnable = runnable;
                if (runnable != null) {
                    runnable.run();
                }
                FolmeUtils.clean(view);
            }
        }));
    }

    @Deprecated
    public static void animateEntrance(View view) {
        clean(view);
        animationSlide(view, 3, 60);
    }

    @Deprecated
    public static void animateEntrance4Filter(View view) {
        AnimState add = new AnimState(VisibleType.HIDE).add(ViewProperty.TRANSLATION_Y, 60, new long[0]).add(ViewProperty.AUTO_ALPHA, 0.0f, new long[0]);
        AnimState add2 = new AnimState(VisibleType.SHOW).add(ViewProperty.TRANSLATION_Y, 60, new long[0]).add(ViewProperty.AUTO_ALPHA, 0.1f, new long[0]);
        AnimState add3 = new AnimState("start").add(ViewProperty.TRANSLATION_Y, 60, new long[0]).add(ViewProperty.AUTO_ALPHA, 0.1f, new long[0]);
        AnimState add4 = new AnimState("to").add(ViewProperty.TRANSLATION_Y, 0, new long[0]).add(ViewProperty.AUTO_ALPHA, 1.0f, new long[0]);
        Folme.useAt(view).state().fromTo(add, add2, new AnimConfig[0]).fromTo(add3, add4, new AnimConfig().setEase(-2, 0.9f, NORMAL_SPRING_RESPONSE).setDelay(50));
    }

    public static IVisibleStyle animateHide(View view) {
        view.setVisibility(8);
        IVisibleStyle show = Folme.useAt(view).visible().setShow();
        show.hide(new AnimConfig[0]);
        return show;
    }

    @Deprecated
    public static IVisibleStyle animateShow(View view) {
        return animateShow(view, null);
    }

    @Deprecated
    public static IVisibleStyle animateShow(View view, final Runnable runnable) {
        view.setVisibility(0);
        IVisibleStyle hide = Folme.useAt(view).visible().setHide();
        if (runnable != null) {
            hide.show(new AnimConfig().addListeners(new TransitionListener() {
                public void onComplete(Object obj) {
                    super.onComplete(obj);
                    Runnable runnable = runnable;
                    if (runnable != null) {
                        runnable.run();
                    }
                }
            }));
        } else {
            hide.show(new AnimConfig[0]);
        }
        return hide;
    }

    public static void animateShrink(final View view, final Runnable runnable) {
        AnimState add = new AnimState(VisibleType.SHOW).add(ViewProperty.SCALE_X, 1.0f, new long[0]).add(ViewProperty.SCALE_Y, 1.0f, new long[0]);
        AnimState add2 = new AnimState(VisibleType.HIDE).add(ViewProperty.SCALE_X, 0.0f, new long[0]).add(ViewProperty.SCALE_Y, 0.0f, new long[0]);
        AnimState add3 = new AnimState(VisibleType.SHOW).add(ViewProperty.AUTO_ALPHA, 1.0f, new long[0]);
        AnimState add4 = new AnimState(VisibleType.HIDE).add(ViewProperty.AUTO_ALPHA, 0.0f, new long[0]);
        Folme.useAt(view).state().fromTo(add, add2, new AnimConfig().setEase(-2, 0.9f, NORMAL_SPRING_RESPONSE).setDelay(1000)).fromTo(add3, add4, new AnimConfig().setEase(-2, 0.99f, 0.2f).setDelay(1000).addListeners(new TransitionListener() {
            public void onComplete(Object obj) {
                super.onComplete(obj);
                Runnable runnable = runnable;
                if (runnable != null) {
                    runnable.run();
                }
                FolmeUtils.clean(view);
            }
        }));
    }

    public static void animationScale(@NonNull View view, float f, float f2) {
        AnimState add = new AnimState("from").add(ViewProperty.SCALE_X, f, new long[0]).add(ViewProperty.SCALE_Y, f, new long[0]);
        AnimState add2 = new AnimState("to").add(ViewProperty.SCALE_X, f2, new long[0]).add(ViewProperty.SCALE_Y, f2, new long[0]);
        Folme.useAt(view).state().fromTo(add, add2, new AnimConfig().setEase(-2, 0.9f, NORMAL_SPRING_RESPONSE));
    }

    public static void animationSlide(@NonNull View view, int i, int i2) {
        boolean z;
        boolean z2;
        AnimState animState;
        AnimState animState2;
        ViewProperty viewProperty;
        if (i != 0) {
            if (i == 1) {
                z = true;
            } else if (i == 2) {
                z = false;
            } else if (i == 3) {
                z2 = true;
                z = false;
            } else {
                throw new IllegalArgumentException("unknown direction.");
            }
            z2 = z;
        } else {
            z = true;
            z2 = false;
        }
        if (z) {
            AnimState animState3 = new AnimState(VisibleType.HIDE);
            ViewProperty viewProperty2 = ViewProperty.TRANSLATION_X;
            if (!z2) {
                i2 = -i2;
            }
            animState2 = animState3.add(viewProperty2, i2, new long[0]).add(ViewProperty.AUTO_ALPHA, 0.0f, new long[0]);
            animState = new AnimState(VisibleType.SHOW);
            viewProperty = ViewProperty.TRANSLATION_X;
        } else {
            AnimState animState4 = new AnimState(VisibleType.HIDE);
            ViewProperty viewProperty3 = ViewProperty.TRANSLATION_Y;
            if (!z2) {
                i2 = -i2;
            }
            animState2 = animState4.add(viewProperty3, i2, new long[0]).add(ViewProperty.AUTO_ALPHA, 0.0f, new long[0]);
            animState = new AnimState(VisibleType.SHOW);
            viewProperty = ViewProperty.TRANSLATION_Y;
        }
        AnimState add = animState.add(viewProperty, 0, new long[0]).add(ViewProperty.AUTO_ALPHA, 1.0f, new long[0]);
        view.setAlpha(0.0f);
        Folme.useAt(view).state().fromTo(animState2, add, new AnimConfig().setEase(-2, 0.9f, NORMAL_SPRING_RESPONSE));
    }

    public static IStateStyle basePhysicsAnimation(Object obj, float f, float f2, float f3, PhyAnimatorListener phyAnimatorListener) {
        IStateStyle iStateStyle;
        if (obj != null) {
            iStateStyle = Folme.useValue(obj);
        } else {
            iStateStyle = Folme.useValue(new Object[0]);
        }
        iStateStyle.setTo((Object) Float.valueOf(f)).to(Float.valueOf(f2), new AnimConfig().setFromSpeed(f3).setEase(-2, 0.9f, NORMAL_SPRING_RESPONSE).addListeners(phyAnimatorListener));
        return iStateStyle;
    }

    public static void clean(View view) {
        if (view != null) {
            Folme.clean(view);
        }
    }

    public static void handleAdapterItemSwitchState(boolean z, @Nullable View view) {
        if (view != null) {
            if (view.getVisibility() != 0) {
                view.setVisibility(0);
            }
            int i = 2;
            if (view.getTag() == null) {
                if (z) {
                    i = 1;
                }
                view.setTag(Integer.valueOf(i));
                if (z) {
                    Folme.useAt(view).visible().setShow();
                } else {
                    Folme.useAt(view).visible().setHide();
                }
                return;
            }
            if (((Integer) view.getTag()).intValue() != (z ? 1 : 2)) {
                if (z) {
                    i = 1;
                }
                view.setTag(Integer.valueOf(i));
                Folme.clean(view);
                if (z) {
                    Folme.useAt(view).visible().show(new AnimConfig().setEase(6, 200.0f));
                } else {
                    Folme.useAt(view).visible().hide(new AnimConfig().setEase(6, 200.0f));
                }
            }
        }
    }

    public static void handleListItemTouch(View view) {
        Folme.useAt(view).touch().handleTouchOf(view, new AnimConfig[0]);
    }

    public static void handleListItemTouch(View view, int i) {
        Folme.useAt(view).touch().handleTouchOf(view, new AnimConfig().setEase(i, new float[0]));
    }

    public static void handleTouchScale(final View view, final OnClickListener onClickListener) {
        Folme.useAt(view).touch().handleTouchOf(view, new AnimConfig().addListeners(new TransitionListener() {
            public void onComplete(Object obj) {
                super.onComplete(obj);
                if (obj == TouchType.UP) {
                    onClickListener.onClick(view);
                }
            }
        }));
    }

    public static IStateStyle popup(View view, float f, float f2, float f3, final IPhyAnimatorListener iPhyAnimatorListener) {
        IStateStyle useValue = Folme.useValue(view);
        useValue.setTo((Object) Float.valueOf(f / 100.0f)).to(Float.valueOf(f2 / 100.0f), new AnimConfig().setFromSpeed(f3 / 100.0f).setEase(-2, 0.7f, NORMAL_SPRING_RESPONSE).addListeners(new PhyAnimatorListener(iPhyAnimatorListener) {
            public void onUpdate(Object obj, FloatProperty floatProperty, float f, float f2, boolean z) {
                IPhyAnimatorListener iPhyAnimatorListener = iPhyAnimatorListener;
                if (iPhyAnimatorListener != null) {
                    iPhyAnimatorListener.onUpdate(f * 100.0f);
                }
            }
        }));
        return useValue;
    }

    public static void setupAdapterItemBackgroundColor(int i, int i2, View view) {
        Folme.useAt(view).state().setup("show").add((FloatProperty) ViewProperty.BACKGROUND, (Object) Integer.valueOf(i2), new long[0]).setup("hide").add((FloatProperty) ViewProperty.BACKGROUND, (Object) Integer.valueOf(i), new long[0]);
    }

    public static void touchButtonTint(@ColorRes int i, View... viewArr) {
        for (View view : viewArr) {
            touchScaleTint(view, 1.0f, view.getResources().getColor(i), null);
        }
    }

    public static void touchDialogButtonTint(View... viewArr) {
        for (View view : viewArr) {
            touchScaleTint(view, 1.0f, view.getResources().getColor(R.color.dialog_button_bg_pressed), null);
        }
    }

    public static void touchItemScale(View view) {
        Folme.useAt(view).touch().handleTouchOf(view, new AnimConfig[0]);
    }

    public static void touchScale(View... viewArr) {
        for (View view : viewArr) {
            touchScaleTint(view, 0.9f, 0, null);
        }
    }

    public static void touchScaleTint(View view, float f, int i, AnimConfig animConfig) {
        Folme.useAt(view).touch().setScale(f, TouchType.DOWN).setTint(i).handleTouchOf(view, new AnimConfig[0]);
    }

    public static void touchScaleTint(View... viewArr) {
        for (View view : viewArr) {
            touchScaleTint(view, 0.9f, 855638016, null);
        }
    }

    public static void touchTint(View view) {
        touchScaleTint(view, 1.0f, 436207616, null);
    }

    public static void touchTint(View... viewArr) {
        for (View view : viewArr) {
            touchTint(view);
        }
    }

    public static void touchTintDefaultDayNight(View view) {
        Folme.useAt(view).touch().setScale(1.0f, TouchType.DOWN).handleTouchOf(view, new AnimConfig[0]);
    }
}
