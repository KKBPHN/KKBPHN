package com.miui.internal.widget;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ClipDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.NinePatchDrawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Build.VERSION;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ProgressBar;
import androidx.core.view.GravityCompat;
import com.miui.internal.variable.Android_Graphics_Drawable_AnimatedRotateDrawable_class;
import com.miui.internal.variable.Android_Graphics_Drawable_AnimatedRotateDrawable_class.Factory;
import com.miui.internal.variable.Android_Graphics_Drawable_StateListDrawable_class;
import miui.R;
import miui.reflect.Method;
import miui.reflect.NoSuchMethodException;
import miui.util.ViewUtils;

public class ProgressBarDelegate {
    private static final Android_Graphics_Drawable_AnimatedRotateDrawable_class AnimatedRotateDrawableClass = Factory.getInstance().get();
    private static final int DRAWABLE_MAX_LEVEL = 10000;
    private static final Method ON_SIZE_CHANGED = Method.of(ProgressBar.class, "onSizeChanged", "(IIII)V");
    private static final Method SET_INDETERMINATE = Method.of(ProgressBar.class, "setIndeterminate", "(Z)V");
    private static final Method SET_INDETERMINATE_DRAWABLE;
    private static final Method SET_PROGRESS_DRAWABLE;
    private static final Android_Graphics_Drawable_StateListDrawable_class StateListDrawableClass = Android_Graphics_Drawable_StateListDrawable_class.Factory.getInstance().get();
    static final String TAG = "ProgressBarDelegate";
    private static Method sGetLayerGravity;
    private static Method sSetLayerGravity;
    private final ProgressBar mActual;
    private float mDisabledAlpha;
    private boolean mIndeterminateDrawableChanged;
    private Drawable mIndeterminateDrawableOrignal;
    private boolean mInitialized = false;
    private final Class mProgressBarClass;
    private boolean mProgressDrawableChanged;
    private Drawable mProgressDrawableOrignal;
    private Drawable mProgressMask;

    static {
        String str = "(Landroid/graphics/drawable/Drawable;)V";
        SET_INDETERMINATE_DRAWABLE = Method.of(ProgressBar.class, "setIndeterminateDrawable", str);
        SET_PROGRESS_DRAWABLE = Method.of(ProgressBar.class, "setProgressDrawable", str);
        if (VERSION.SDK_INT >= 23) {
            try {
                sGetLayerGravity = Method.of(LayerDrawable.class, "getLayerGravity", "(I)I");
                sSetLayerGravity = Method.of(LayerDrawable.class, "setLayerGravity", "(II)V");
            } catch (NoSuchMethodException e) {
                Log.e(TAG, "Fail to find method in LayerDrawable class", e);
            }
        }
    }

    private ProgressBarDelegate(ProgressBar progressBar, Class cls) {
        this.mActual = progressBar;
        this.mProgressBarClass = cls;
    }

    private static Drawable addMask(Resources resources, Drawable drawable, Drawable drawable2, int i, int i2) {
        if (drawable == null || drawable2 == null || i <= 0 || i2 <= 0) {
            return drawable;
        }
        Bitmap createBitmap = Bitmap.createBitmap(i, i2, Config.ARGB_8888);
        Canvas canvas = new Canvas(createBitmap);
        canvas.setDrawFilter(new PaintFlagsDrawFilter(0, 3));
        drawable.setLevel(10000);
        drawable.setBounds(0, 0, i, i2);
        drawable.draw(canvas);
        drawable2.draw(canvas);
        return new BitmapDrawable(resources, createBitmap);
    }

    public static ProgressBarDelegate create(ProgressBar progressBar, Class cls) {
        return new ProgressBarDelegate(progressBar, cls);
    }

    private void initMaskPaint(Drawable drawable) {
        Paint paint = drawable instanceof NinePatchDrawable ? ((NinePatchDrawable) drawable).getPaint() : drawable instanceof BitmapDrawable ? ((BitmapDrawable) drawable).getPaint() : null;
        if (paint != null) {
            paint.setXfermode(new PorterDuffXfermode(Mode.DST_IN));
            return;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("The drawable should be NinePatchDrawable or BitmapDrawable. drawable=");
        sb.append(drawable);
        Log.w(TAG, sb.toString());
    }

    private void superOnSizeChanged(int i, int i2, int i3, int i4) {
        ON_SIZE_CHANGED.invoke(this.mProgressBarClass, this.mActual, Integer.valueOf(i), Integer.valueOf(i2), Integer.valueOf(i3), Integer.valueOf(i4));
    }

    private void superSetIndeterminate(boolean z) {
        SET_INDETERMINATE.invoke(this.mProgressBarClass, this.mActual, Boolean.valueOf(z));
    }

    private void superSetIndeterminateDrawable(Drawable drawable) {
        SET_INDETERMINATE_DRAWABLE.invoke(this.mProgressBarClass, this.mActual, drawable);
    }

    private void superSetProgressDrawable(Drawable drawable) {
        SET_PROGRESS_DRAWABLE.invoke(this.mProgressBarClass, this.mActual, drawable);
    }

    private static Drawable tileifyForProgressBar(Resources resources, Drawable drawable, Drawable drawable2, int i, int i2, boolean z) {
        Drawable drawable3 = drawable;
        if ((drawable3 instanceof NinePatchDrawable) || (drawable3 instanceof BitmapDrawable)) {
            return new ClipDrawable(addMask(resources, drawable, drawable2, i, i2), GravityCompat.START, 1);
        }
        if (drawable3 instanceof ClipDrawable) {
            Drawable addMask = addMask(resources, drawable, drawable2, i, i2);
            if (addMask == drawable3) {
                return addMask;
            }
            drawable3 = new ClipDrawable(addMask, GravityCompat.START, 1);
        } else {
            int i3 = 0;
            if (drawable3 instanceof LayerDrawable) {
                LayerDrawable layerDrawable = (LayerDrawable) drawable3;
                int numberOfLayers = layerDrawable.getNumberOfLayers();
                Drawable[] drawableArr = new Drawable[numberOfLayers];
                for (int i4 = 0; i4 < numberOfLayers; i4++) {
                    int id = layerDrawable.getId(i4);
                    Drawable drawable4 = layerDrawable.getDrawable(i4);
                    if (id == 16908301 || id == 16908303) {
                        drawableArr[i4] = tileifyForProgressBar(resources, drawable4, drawable2, i, i2, z);
                    } else {
                        drawableArr[i4] = drawable4;
                    }
                }
                LayerDrawable layerDrawable2 = new LayerDrawable(drawableArr);
                for (int i5 = 0; i5 < numberOfLayers; i5++) {
                    layerDrawable2.setId(i5, layerDrawable.getId(i5));
                    Method method = sGetLayerGravity;
                    if (!(method == null || sSetLayerGravity == null)) {
                        sSetLayerGravity.invoke(LayerDrawable.class, layerDrawable2, Integer.valueOf(i5), Integer.valueOf(method.invokeInt(LayerDrawable.class, layerDrawable, Integer.valueOf(i5))));
                    }
                    ProgressBarDelegateHelper.setOutLayerParams(layerDrawable2, layerDrawable, i5);
                }
                return layerDrawable2;
            } else if (drawable3 instanceof StateListDrawable) {
                StateListDrawable stateListDrawable = (StateListDrawable) drawable3;
                StateListDrawable stateListDrawable2 = new StateListDrawable();
                int stateCount = StateListDrawableClass.getStateCount(stateListDrawable);
                while (i3 < stateCount) {
                    stateListDrawable2.addState(StateListDrawableClass.getStateSet(stateListDrawable, i3), tileifyForProgressBar(resources, StateListDrawableClass.getStateDrawable(stateListDrawable, i3), drawable2, i, i2, z));
                    i3++;
                }
                return stateListDrawable2;
            } else if (drawable3 instanceof AnimationDrawable) {
                AnimationDrawable animationDrawable = (AnimationDrawable) drawable3;
                int numberOfFrames = animationDrawable.getNumberOfFrames();
                AnimationDrawable animationDrawable2 = new AnimationDrawable();
                animationDrawable2.setOneShot(animationDrawable.isOneShot());
                while (i3 < numberOfFrames) {
                    Drawable tileifyForProgressBar = tileifyForProgressBar(resources, animationDrawable.getFrame(i3), drawable2, i, i2, z);
                    tileifyForProgressBar.setLevel(10000);
                    animationDrawable2.addFrame(tileifyForProgressBar, animationDrawable.getDuration(i3));
                    i3++;
                }
                if (z) {
                    animationDrawable2.setLevel(10000);
                } else {
                    animationDrawable2.setLevel(animationDrawable.getLevel());
                }
                return animationDrawable2;
            } else {
                StringBuilder sb = new StringBuilder();
                sb.append("Unknown Drawable subclass, src=");
                sb.append(drawable);
                Log.w(TAG, sb.toString());
            }
        }
        return drawable3;
    }

    private void updateDrawable() {
        updateDrawable(this.mActual.isIndeterminate());
    }

    private void updateDrawable(boolean z) {
        int i;
        if (this.mInitialized) {
            ProgressBar progressBar = this.mActual;
            int height = (progressBar.getHeight() - progressBar.getPaddingTop()) - progressBar.getPaddingBottom();
            int width = (progressBar.getWidth() - progressBar.getPaddingLeft()) - progressBar.getPaddingRight();
            if (height < width) {
                int i2 = width;
                width = height;
                height = i2;
            }
            Drawable drawable = this.mProgressMask;
            if (drawable != null) {
                drawable.setBounds(0, 0, height, width);
            }
            if (z && this.mIndeterminateDrawableChanged) {
                this.mIndeterminateDrawableChanged = true;
                Drawable tileifyForProgressBar = tileifyForProgressBar(progressBar.getResources(), this.mIndeterminateDrawableOrignal, this.mProgressMask, height, width, true);
                if (tileifyForProgressBar != null && height > 0 && width > 0) {
                    if (ViewUtils.isLayoutRtl(this.mActual)) {
                        i = height - height;
                        height += 0;
                    } else {
                        i = 0;
                    }
                    tileifyForProgressBar.setBounds(i, 0, height, width);
                }
                superSetIndeterminateDrawable(tileifyForProgressBar);
            } else if (!z && this.mProgressDrawableChanged) {
                this.mProgressDrawableChanged = true;
                superSetProgressDrawable(tileifyForProgressBar(progressBar.getResources(), this.mProgressDrawableOrignal, this.mProgressMask, height, width, false));
                ProgressBarDelegateHelper.setProgress(this.mActual);
            }
        }
    }

    public void drawableStateChanged() {
        boolean isEnabled = this.mActual.isEnabled();
        if (!isEnabled) {
            this.mActual.getProgressDrawable().setAlpha(255);
        }
        this.mActual.setAlpha(isEnabled ? 1.0f : this.mDisabledAlpha);
    }

    public Drawable getProgressMaskDrawable() {
        return this.mProgressMask;
    }

    public void onSizeChanged(int i, int i2, int i3, int i4) {
        this.mProgressDrawableChanged = true;
        this.mIndeterminateDrawableChanged = true;
        updateDrawable();
        superOnSizeChanged(i, i2, i3, i4);
    }

    public void postConstruct(AttributeSet attributeSet, int i) {
        Context context = this.mActual.getContext();
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R.styleable.ProgressBar, i, 0);
        Drawable drawable = this.mIndeterminateDrawableOrignal;
        if (drawable != null && drawable.getClass().getName().equals(Android_Graphics_Drawable_AnimatedRotateDrawable_class.NAME)) {
            AnimatedRotateDrawableClass.setFramesCount(drawable, obtainStyledAttributes.getInt(com.miui.internal.R.styleable.ProgressBar_indeterminateFramesCount, 48));
            AnimatedRotateDrawableClass.setFramesDuration(drawable, obtainStyledAttributes.getInt(com.miui.internal.R.styleable.ProgressBar_indeterminateFramesDuration, 25));
        }
        this.mInitialized = true;
        Drawable drawable2 = obtainStyledAttributes.getDrawable(com.miui.internal.R.styleable.ProgressBar_progressMask);
        if (drawable2 != null) {
            initMaskPaint(drawable2);
            setProgressMaskDrawable(drawable2);
        } else {
            updateDrawable();
        }
        obtainStyledAttributes.recycle();
        TypedArray obtainStyledAttributes2 = context.getTheme().obtainStyledAttributes(attributeSet, new int[]{16842803}, 0, 0);
        this.mDisabledAlpha = obtainStyledAttributes2.getFloat(0, 0.5f);
        obtainStyledAttributes2.recycle();
    }

    public void setIndeterminate(boolean z) {
        if (z != this.mActual.isIndeterminate()) {
            updateDrawable(z);
            superSetIndeterminate(z);
        }
    }

    public void setIndeterminateDrawable(Drawable drawable) {
        if (this.mIndeterminateDrawableOrignal != drawable) {
            this.mIndeterminateDrawableOrignal = drawable;
            this.mIndeterminateDrawableChanged = true;
            updateDrawable();
        }
    }

    public void setProgressDrawable(Drawable drawable) {
        if (this.mProgressDrawableOrignal != drawable) {
            this.mProgressDrawableOrignal = drawable;
            this.mProgressDrawableChanged = true;
            updateDrawable();
        }
    }

    public synchronized void setProgressMaskDrawable(Drawable drawable) {
        if (this.mProgressMask != drawable) {
            this.mProgressMask = drawable;
            this.mProgressDrawableChanged = true;
            this.mIndeterminateDrawableChanged = true;
            updateDrawable();
        }
    }
}
