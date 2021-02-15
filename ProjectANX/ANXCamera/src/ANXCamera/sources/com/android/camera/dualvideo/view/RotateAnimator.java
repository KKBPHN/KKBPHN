package com.android.camera.dualvideo.view;

import android.animation.ValueAnimator;
import android.view.View;
import android.view.animation.LinearInterpolator;
import androidx.annotation.Nullable;
import com.android.camera.log.Log;
import com.ss.android.vesdk.VEResult;
import java.util.ArrayList;
import java.util.List;

public class RotateAnimator {
    private static final String TAG = "RotateAnimator";
    private int mCurrentDegree;
    private ValueAnimator mRotationAnimator;
    private int mViewOrientation;
    final List mViews = new ArrayList();

    public RotateAnimator(int i) {
        this.mViewOrientation = (i + m.cQ) % m.cQ;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:24:0x0034, code lost:
        if (r8 == 0) goto L_0x0041;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void disposeRotation(int i) {
        int i2;
        int i3 = m.cQ;
        int i4 = i >= 0 ? i % m.cQ : (i % m.cQ) + m.cQ;
        int i5 = this.mViewOrientation;
        if (i5 != i4) {
            int i6 = this.mCurrentDegree;
            int i7 = i4 - i5;
            if (i7 < 0) {
                i7 += m.cQ;
            }
            if (i7 > 180) {
                i7 += VEResult.TER_EGL_BAD_MATCH;
            }
            boolean z = i7 >= 0;
            this.mViewOrientation = i4;
            int i8 = i4 % m.cQ;
            ValueAnimator valueAnimator = this.mRotationAnimator;
            if (valueAnimator != null) {
                valueAnimator.cancel();
            }
            if (z) {
                i2 = i6 == 360 ? 0 : i6;
            } else {
                i2 = i6 == 0 ? 360 : i6;
                if (i8 == 360) {
                    i3 = 0;
                    StringBuilder sb = new StringBuilder();
                    String str = "disposeRotation: ";
                    sb.append(str);
                    sb.append(i6);
                    String str2 = "-> ";
                    sb.append(str2);
                    sb.append(i8);
                    String sb2 = sb.toString();
                    String str3 = TAG;
                    Log.d(str3, sb2);
                    StringBuilder sb3 = new StringBuilder();
                    sb3.append(str);
                    sb3.append(i2);
                    sb3.append(str2);
                    sb3.append(i3);
                    Log.d(str3, sb3.toString());
                    this.mRotationAnimator = ValueAnimator.ofInt(new int[]{i2, i3});
                    this.mRotationAnimator.setInterpolator(new LinearInterpolator());
                    this.mRotationAnimator.removeAllUpdateListeners();
                    this.mRotationAnimator.addUpdateListener(new O00000o(this));
                    this.mRotationAnimator.start();
                }
            }
            i3 = i8;
            StringBuilder sb4 = new StringBuilder();
            String str4 = "disposeRotation: ";
            sb4.append(str4);
            sb4.append(i6);
            String str22 = "-> ";
            sb4.append(str22);
            sb4.append(i8);
            String sb22 = sb4.toString();
            String str32 = TAG;
            Log.d(str32, sb22);
            StringBuilder sb32 = new StringBuilder();
            sb32.append(str4);
            sb32.append(i2);
            sb32.append(str22);
            sb32.append(i3);
            Log.d(str32, sb32.toString());
            this.mRotationAnimator = ValueAnimator.ofInt(new int[]{i2, i3});
            this.mRotationAnimator.setInterpolator(new LinearInterpolator());
            this.mRotationAnimator.removeAllUpdateListeners();
            this.mRotationAnimator.addUpdateListener(new O00000o(this));
            this.mRotationAnimator.start();
        }
    }

    public /* synthetic */ void O00000o0(ValueAnimator valueAnimator) {
        this.mCurrentDegree = ((Integer) valueAnimator.getAnimatedValue()).intValue();
        for (View view : this.mViews) {
            if (view != null) {
                view.setRotation((float) this.mCurrentDegree);
            }
        }
    }

    public void addView(@Nullable View view) {
        if (view != null) {
            view.setRotation((float) this.mViewOrientation);
            this.mCurrentDegree = this.mViewOrientation;
            this.mViews.add(view);
        }
    }

    public void clear() {
        this.mViews.clear();
    }

    public void onOrientationChanged(int i) {
        disposeRotation(360 - i);
    }
}
