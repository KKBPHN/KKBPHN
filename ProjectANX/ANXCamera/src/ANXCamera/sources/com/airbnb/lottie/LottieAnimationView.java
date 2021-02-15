package com.airbnb.lottie;

import android.animation.Animator.AnimatorListener;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Build.VERSION;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.View.BaseSavedState;
import android.widget.ImageView.ScaleType;
import androidx.annotation.DrawableRes;
import androidx.annotation.FloatRange;
import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RawRes;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.view.ViewCompat;
import com.airbnb.lottie.O00000o.O0000OOo;
import com.airbnb.lottie.O00000oO.C0060O0000Ooo;
import com.airbnb.lottie.O00000oO.O0000Oo;
import com.airbnb.lottie.model.C0102O00000oO;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class LottieAnimationView extends AppCompatImageView {
    /* access modifiers changed from: private */
    public static final C0082O000OoO O000o00 = new C0061O00000oo();
    private static final String TAG = "LottieAnimationView";
    @Nullable
    private C0064O0000o0O O00000oo;
    private String O000OOo;
    @RawRes
    private int O000OOoO;
    private final C0082O000OoO O000Oo0 = new O0000OOo(this);
    private final C0082O000OoO O000Oo00 = new O0000O0o(this);
    /* access modifiers changed from: private */
    @Nullable
    public C0082O000OoO O000Oo0O;
    /* access modifiers changed from: private */
    @DrawableRes
    public int O000Oo0o = 0;
    private boolean O000OoO = false;
    private final C0083O000OoO0 O000OoO0 = new C0083O000OoO0();
    private boolean O000OoOO = false;
    private boolean O000OoOo = false;
    private RenderMode O000Ooo = RenderMode.AUTOMATIC;
    private boolean O000Ooo0 = true;
    private Set O000OooO = new HashSet();
    private int O000Oooo = 0;
    @Nullable
    private O000o000 O000o000;
    private boolean O00O0Oo;

    class SavedState extends BaseSavedState {
        public static final Creator CREATOR = new C0062O0000OoO();
        String O0000o00;
        String O000OOo;
        int O000OOoO;
        boolean O000OOoo;
        float progress;
        int repeatCount;
        int repeatMode;

        private SavedState(Parcel parcel) {
            super(parcel);
            this.O000OOo = parcel.readString();
            this.progress = parcel.readFloat();
            boolean z = true;
            if (parcel.readInt() != 1) {
                z = false;
            }
            this.O000OOoo = z;
            this.O0000o00 = parcel.readString();
            this.repeatMode = parcel.readInt();
            this.repeatCount = parcel.readInt();
        }

        /* synthetic */ SavedState(Parcel parcel, C0061O00000oo o00000oo) {
            this(parcel);
        }

        SavedState(Parcelable parcelable) {
            super(parcelable);
        }

        public void writeToParcel(Parcel parcel, int i) {
            super.writeToParcel(parcel, i);
            parcel.writeString(this.O000OOo);
            parcel.writeFloat(this.progress);
            parcel.writeInt(this.O000OOoo ? 1 : 0);
            parcel.writeString(this.O0000o00);
            parcel.writeInt(this.repeatMode);
            parcel.writeInt(this.repeatCount);
        }
    }

    public LottieAnimationView(Context context) {
        super(context);
        O000000o((AttributeSet) null);
    }

    public LottieAnimationView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        O000000o(attributeSet);
    }

    public LottieAnimationView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        O000000o(attributeSet);
    }

    private void O000000o(@Nullable AttributeSet attributeSet) {
        TypedArray obtainStyledAttributes = getContext().obtainStyledAttributes(attributeSet, R$styleable.LottieAnimationView);
        boolean z = false;
        if (!isInEditMode()) {
            this.O000Ooo0 = obtainStyledAttributes.getBoolean(R$styleable.LottieAnimationView_lottie_cacheComposition, true);
            boolean hasValue = obtainStyledAttributes.hasValue(R$styleable.LottieAnimationView_lottie_rawRes);
            boolean hasValue2 = obtainStyledAttributes.hasValue(R$styleable.LottieAnimationView_lottie_fileName);
            boolean hasValue3 = obtainStyledAttributes.hasValue(R$styleable.LottieAnimationView_lottie_url);
            if (!hasValue || !hasValue2) {
                if (hasValue) {
                    int resourceId = obtainStyledAttributes.getResourceId(R$styleable.LottieAnimationView_lottie_rawRes, 0);
                    if (resourceId != 0) {
                        O0000O0o(resourceId);
                    }
                } else if (hasValue2) {
                    String string = obtainStyledAttributes.getString(R$styleable.LottieAnimationView_lottie_fileName);
                    if (string != null) {
                        O0000Oo(string);
                    }
                } else if (hasValue3) {
                    String string2 = obtainStyledAttributes.getString(R$styleable.LottieAnimationView_lottie_url);
                    if (string2 != null) {
                        O0000Ooo(string2);
                    }
                }
                O0000OOo(obtainStyledAttributes.getResourceId(R$styleable.LottieAnimationView_lottie_fallbackRes, 0));
            } else {
                throw new IllegalArgumentException("lottie_rawRes and lottie_fileName cannot be used at the same time. Please use only one at once.");
            }
        }
        if (obtainStyledAttributes.getBoolean(R$styleable.LottieAnimationView_lottie_autoPlay, false)) {
            this.O000OoOO = true;
            this.O000OoOo = true;
        }
        if (obtainStyledAttributes.getBoolean(R$styleable.LottieAnimationView_lottie_loop, false)) {
            this.O000OoO0.setRepeatCount(-1);
        }
        if (obtainStyledAttributes.hasValue(R$styleable.LottieAnimationView_lottie_repeatMode)) {
            setRepeatMode(obtainStyledAttributes.getInt(R$styleable.LottieAnimationView_lottie_repeatMode, 1));
        }
        if (obtainStyledAttributes.hasValue(R$styleable.LottieAnimationView_lottie_repeatCount)) {
            setRepeatCount(obtainStyledAttributes.getInt(R$styleable.LottieAnimationView_lottie_repeatCount, -1));
        }
        if (obtainStyledAttributes.hasValue(R$styleable.LottieAnimationView_lottie_speed)) {
            setSpeed(obtainStyledAttributes.getFloat(R$styleable.LottieAnimationView_lottie_speed, 1.0f));
        }
        O0000o00(obtainStyledAttributes.getString(R$styleable.LottieAnimationView_lottie_imageAssetsFolder));
        setProgress(obtainStyledAttributes.getFloat(R$styleable.LottieAnimationView_lottie_progress, 0.0f));
        O00000oo(obtainStyledAttributes.getBoolean(R$styleable.LottieAnimationView_lottie_enableMergePathsForKitKatAndAbove, false));
        if (obtainStyledAttributes.hasValue(R$styleable.LottieAnimationView_lottie_colorFilter)) {
            C0092O000o0O o000o0O = new C0092O000o0O(obtainStyledAttributes.getColor(R$styleable.LottieAnimationView_lottie_colorFilter, 0));
            O000000o(new C0102O00000oO("**"), (Object) C0087O000Ooo0.OO00ooO, new O0000Oo(o000o0O));
        }
        if (obtainStyledAttributes.hasValue(R$styleable.LottieAnimationView_lottie_scale)) {
            this.O000OoO0.setScale(obtainStyledAttributes.getFloat(R$styleable.LottieAnimationView_lottie_scale, 1.0f));
        }
        if (obtainStyledAttributes.hasValue(R$styleable.LottieAnimationView_lottie_renderMode)) {
            int i = obtainStyledAttributes.getInt(R$styleable.LottieAnimationView_lottie_renderMode, RenderMode.AUTOMATIC.ordinal());
            if (i >= RenderMode.values().length) {
                i = RenderMode.AUTOMATIC.ordinal();
            }
            O000000o(RenderMode.values()[i]);
        }
        if (getScaleType() != null) {
            this.O000OoO0.setScaleType(getScaleType());
        }
        obtainStyledAttributes.recycle();
        C0083O000OoO0 o000OoO0 = this.O000OoO0;
        if (O0000OOo.O00000Oo(getContext()) != 0.0f) {
            z = true;
        }
        o000OoO0.O000000o(Boolean.valueOf(z));
        Oo0oO();
        this.O00O0Oo = true;
    }

    private void O00000Oo(O000o000 o000o000) {
        O0000o0();
        Oo0oO0o();
        this.O000o000 = o000o000.O00000o0(this.O000Oo00).O00000Oo(this.O000Oo0);
    }

    private void O0000o0() {
        this.O00000oo = null;
        this.O000OoO0.O0000o0();
    }

    /* JADX WARNING: Code restructure failed: missing block: B:19:0x003b, code lost:
        if (r3 != false) goto L_0x003d;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void Oo0oO() {
        int i = O0000Oo.O000o0OO[this.O000Ooo.ordinal()];
        int i2 = 2;
        if (i != 1) {
            if (i != 2 && i == 3) {
                C0064O0000o0O o0000o0O = this.O00000oo;
                boolean z = false;
                if (o0000o0O == null || !o0000o0O.O00O0oOo() || VERSION.SDK_INT >= 28) {
                    C0064O0000o0O o0000o0O2 = this.O00000oo;
                    if ((o0000o0O2 == null || o0000o0O2.O00O0oO0() <= 4) && VERSION.SDK_INT >= 21) {
                        z = true;
                    }
                }
            }
            i2 = 1;
        }
        if (i2 != getLayerType()) {
            setLayerType(i2, null);
        }
    }

    private void Oo0oO0o() {
        O000o000 o000o000 = this.O000o000;
        if (o000o000 != null) {
            o000o000.O00000oO(this.O000Oo00);
            this.O000o000.O00000o(this.O000Oo0);
        }
    }

    @Nullable
    public Bitmap O000000o(String str, @Nullable Bitmap bitmap) {
        return this.O000OoO0.O000000o(str, bitmap);
    }

    public List O000000o(C0102O00000oO o00000oO) {
        return this.O000OoO0.O000000o(o00000oO);
    }

    public void O000000o(int i) {
        this.O000OoO0.O000000o(i);
    }

    public void O000000o(AnimatorListener animatorListener) {
        this.O000OoO0.O000000o(animatorListener);
    }

    public void O000000o(AnimatorUpdateListener animatorUpdateListener) {
        this.O000OoO0.O000000o(animatorUpdateListener);
    }

    public void O000000o(O00000o0 o00000o0) {
        this.O000OoO0.O000000o(o00000o0);
    }

    public void O000000o(O00000o o00000o) {
        this.O000OoO0.O000000o(o00000o);
    }

    public void O000000o(@Nullable C0082O000OoO o000OoO) {
        this.O000Oo0O = o000OoO;
    }

    public void O000000o(C0094O000o0OO o000o0OO) {
        this.O000OoO0.O000000o(o000o0OO);
    }

    public void O000000o(RenderMode renderMode) {
        this.O000Ooo = renderMode;
        Oo0oO();
    }

    public void O000000o(C0102O00000oO o00000oO, Object obj, O0000Oo o0000Oo) {
        this.O000OoO0.O000000o(o00000oO, obj, o0000Oo);
    }

    public void O000000o(C0102O00000oO o00000oO, Object obj, C0060O0000Ooo o0000Ooo) {
        this.O000OoO0.O000000o(o00000oO, obj, (O0000Oo) new O0000Oo0(this, o0000Ooo));
    }

    public void O000000o(InputStream inputStream, @Nullable String str) {
        O00000Oo(C0096O00oOooo.O00000Oo(inputStream, str));
    }

    public void O000000o(String str, String str2, boolean z) {
        this.O000OoO0.O000000o(str, str2, z);
    }

    public boolean O000000o(@NonNull C0085O000OoOo o000OoOo) {
        C0064O0000o0O o0000o0O = this.O00000oo;
        if (o0000o0O != null) {
            o000OoOo.O000000o(o0000o0O);
        }
        return this.O000OooO.add(o000OoOo);
    }

    public void O00000Oo(@FloatRange(from = 0.0d, to = 1.0d) float f, @FloatRange(from = 0.0d, to = 1.0d) float f2) {
        this.O000OoO0.O00000Oo(f, f2);
    }

    public void O00000Oo(AnimatorListener animatorListener) {
        this.O000OoO0.O00000Oo(animatorListener);
    }

    public void O00000Oo(String str, @Nullable String str2) {
        O000000o((InputStream) new ByteArrayInputStream(str.getBytes()), str2);
    }

    public boolean O00000Oo(@NonNull C0085O000OoOo o000OoOo) {
        return this.O000OooO.remove(o000OoOo);
    }

    public void O00000o(String str) {
        this.O000OoO0.O00000o(str);
    }

    public void O00000o0(@NonNull C0064O0000o0O o0000o0O) {
        if (C0053O00000oO.DBG) {
            String str = TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("Set Composition \n");
            sb.append(o0000o0O);
            Log.v(str, sb.toString());
        }
        this.O000OoO0.setCallback(this);
        this.O00000oo = o0000o0O;
        boolean O00000o02 = this.O000OoO0.O00000o0(o0000o0O);
        Oo0oO();
        if (getDrawable() != this.O000OoO0 || O00000o02) {
            setImageDrawable(null);
            setImageDrawable(this.O000OoO0);
            onVisibilityChanged(this, getVisibility());
            requestLayout();
            for (C0085O000OoOo O000000o2 : this.O000OooO) {
                O000000o2.O000000o(o0000o0O);
            }
        }
    }

    public void O00000o0(String str) {
        this.O000OoO0.O00000o0(str);
    }

    public void O00000oO(String str) {
        this.O000OoO0.O00000oO(str);
    }

    public void O00000oo(@FloatRange(from = 0.0d, to = 1.0d) float f) {
        this.O000OoO0.O00000oo(f);
    }

    public void O00000oo(int i) {
        this.O000OoO0.O00000oo(i);
    }

    public void O00000oo(int i, int i2) {
        this.O000OoO0.O00000oo(i, i2);
    }

    public void O00000oo(boolean z) {
        this.O000OoO0.O00000oo(z);
    }

    public void O0000O0o(float f) {
        this.O000OoO0.O0000O0o(f);
    }

    public void O0000O0o(@RawRes int i) {
        this.O000OOoO = i;
        this.O000OOo = null;
        O00000Oo(this.O000Ooo0 ? C0096O00oOooo.O000000o(getContext(), i) : C0096O00oOooo.O000000o(getContext(), i, (String) null));
    }

    @Deprecated
    public void O0000O0o(boolean z) {
        this.O000OoO0.setRepeatCount(z ? -1 : 0);
    }

    public void O0000OOo(@DrawableRes int i) {
        this.O000Oo0o = i;
    }

    public void O0000OOo(boolean z) {
        this.O000OoO0.O0000OOo(z);
    }

    public void O0000Oo(String str) {
        this.O000OOo = str;
        this.O000OOoO = 0;
        O00000Oo(this.O000Ooo0 ? C0096O00oOooo.O00000Oo(getContext(), str) : C0096O00oOooo.O000000o(getContext(), str, (String) null));
    }

    public void O0000Oo(boolean z) {
        this.O000OoO0.O0000Oo(z);
    }

    public void O0000Oo0(boolean z) {
        this.O000OoO0.O0000Oo0(z);
    }

    @Deprecated
    public void O0000OoO(String str) {
        O00000Oo(str, (String) null);
    }

    public void O0000OoO(boolean z) {
        this.O000Ooo0 = z;
    }

    public void O0000Ooo(String str) {
        O00000Oo(this.O000Ooo0 ? C0096O00oOooo.O00000o(getContext(), str) : C0096O00oOooo.O00000o0(getContext(), str, null));
    }

    public float O0000o() {
        return this.O000OoO0.O0000o();
    }

    public void O0000o00(String str) {
        this.O000OoO0.O00000Oo(str);
    }

    public float O0000o0o() {
        return this.O000OoO0.O0000o0o();
    }

    @MainThread
    public void O0000oO() {
        if (isShown()) {
            this.O000OoO0.O0000oO();
            Oo0oO();
            return;
        }
        this.O000OoO = true;
    }

    @MainThread
    public void O0000oO0() {
        this.O000OoOo = false;
        this.O000OoOO = false;
        this.O000OoO = false;
        this.O000OoO0.O0000oO0();
        Oo0oO();
    }

    @MainThread
    public void O0000oOo() {
        if (isShown()) {
            this.O000OoO0.O0000oOo();
            Oo0oO();
            return;
        }
        this.O000OoO = true;
    }

    public void O0000oo0() {
        this.O000OoO0.O0000oo0();
    }

    @Nullable
    public C0064O0000o0O O000O0OO() {
        return this.O00000oo;
    }

    @Nullable
    public String O000O0Oo() {
        return this.O000OoO0.O000O0Oo();
    }

    public boolean O000O0o() {
        return this.O000OoO0.O000O0o();
    }

    public boolean O000O0oO() {
        return this.O000OoO0.O000O0oO();
    }

    public boolean O000OO00() {
        return this.O000OoO0.O000OO00();
    }

    public void O000OO0o() {
        this.O000OoO0.O000OO0o();
    }

    public void O000Oo0() {
        this.O000OooO.clear();
    }

    @Nullable
    public O000o0 O00oOoOo() {
        return this.O000OoO0.O00oOoOo();
    }

    public void O00oOooo() {
        this.O000OoO0.O00oOooo();
    }

    public void buildDrawingCache(boolean z) {
        String str = "buildDrawingCache";
        C0053O00000oO.beginSection(str);
        this.O000Oooo++;
        super.buildDrawingCache(z);
        if (this.O000Oooo == 1 && getWidth() > 0 && getHeight() > 0 && getLayerType() == 1 && getDrawingCache(z) == null) {
            O000000o(RenderMode.HARDWARE);
        }
        this.O000Oooo--;
        C0053O00000oO.O0000oOo(str);
    }

    @MainThread
    public void cancelAnimation() {
        this.O000OoO = false;
        this.O000OoO0.cancelAnimation();
        Oo0oO();
    }

    public long getDuration() {
        C0064O0000o0O o0000o0O = this.O00000oo;
        if (o0000o0O != null) {
            return (long) o0000o0O.getDuration();
        }
        return 0;
    }

    public int getFrame() {
        return this.O000OoO0.getFrame();
    }

    @FloatRange(from = 0.0d, to = 1.0d)
    public float getProgress() {
        return this.O000OoO0.getProgress();
    }

    public int getRepeatCount() {
        return this.O000OoO0.getRepeatCount();
    }

    public int getRepeatMode() {
        return this.O000OoO0.getRepeatMode();
    }

    public float getScale() {
        return this.O000OoO0.getScale();
    }

    public float getSpeed() {
        return this.O000OoO0.getSpeed();
    }

    public void invalidateDrawable(@NonNull Drawable drawable) {
        Drawable drawable2 = getDrawable();
        C0083O000OoO0 o000OoO0 = this.O000OoO0;
        if (drawable2 == o000OoO0) {
            super.invalidateDrawable(o000OoO0);
        } else {
            super.invalidateDrawable(drawable);
        }
    }

    public boolean isAnimating() {
        return this.O000OoO0.isAnimating();
    }

    /* access modifiers changed from: protected */
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (this.O000OoOo || this.O000OoOO) {
            O0000oO();
            this.O000OoOo = false;
            this.O000OoOO = false;
        }
        if (VERSION.SDK_INT < 23) {
            onVisibilityChanged(this, getVisibility());
        }
    }

    /* access modifiers changed from: protected */
    public void onDetachedFromWindow() {
        if (isAnimating()) {
            cancelAnimation();
            this.O000OoOO = true;
        }
        super.onDetachedFromWindow();
    }

    /* access modifiers changed from: protected */
    public void onRestoreInstanceState(Parcelable parcelable) {
        if (!(parcelable instanceof SavedState)) {
            super.onRestoreInstanceState(parcelable);
            return;
        }
        SavedState savedState = (SavedState) parcelable;
        super.onRestoreInstanceState(savedState.getSuperState());
        this.O000OOo = savedState.O000OOo;
        if (!TextUtils.isEmpty(this.O000OOo)) {
            O0000Oo(this.O000OOo);
        }
        this.O000OOoO = savedState.O000OOoO;
        int i = this.O000OOoO;
        if (i != 0) {
            O0000O0o(i);
        }
        setProgress(savedState.progress);
        if (savedState.O000OOoo) {
            O0000oO();
        }
        this.O000OoO0.O00000Oo(savedState.O0000o00);
        setRepeatMode(savedState.repeatMode);
        setRepeatCount(savedState.repeatCount);
    }

    /* access modifiers changed from: protected */
    public Parcelable onSaveInstanceState() {
        SavedState savedState = new SavedState(super.onSaveInstanceState());
        savedState.O000OOo = this.O000OOo;
        savedState.O000OOoO = this.O000OOoO;
        savedState.progress = this.O000OoO0.getProgress();
        boolean z = this.O000OoO0.isAnimating() || (!ViewCompat.isAttachedToWindow(this) && this.O000OoOO);
        savedState.O000OOoo = z;
        savedState.O0000o00 = this.O000OoO0.O000O0Oo();
        savedState.repeatMode = this.O000OoO0.getRepeatMode();
        savedState.repeatCount = this.O000OoO0.getRepeatCount();
        return savedState;
    }

    /* access modifiers changed from: protected */
    public void onVisibilityChanged(@NonNull View view, int i) {
        boolean z;
        if (this.O00O0Oo) {
            if (isShown()) {
                if (this.O000OoO) {
                    O0000oOo();
                    z = false;
                }
            }
            if (isAnimating()) {
                O0000oO0();
                z = true;
            }
            this.O000OoO = z;
        }
    }

    public void removeAllUpdateListeners() {
        this.O000OoO0.removeAllUpdateListeners();
    }

    public void removeUpdateListener(AnimatorUpdateListener animatorUpdateListener) {
        this.O000OoO0.O00000Oo(animatorUpdateListener);
    }

    public void setFrame(int i) {
        this.O000OoO0.setFrame(i);
    }

    public void setImageBitmap(Bitmap bitmap) {
        Oo0oO0o();
        super.setImageBitmap(bitmap);
    }

    public void setImageDrawable(Drawable drawable) {
        Oo0oO0o();
        super.setImageDrawable(drawable);
    }

    public void setImageResource(int i) {
        Oo0oO0o();
        super.setImageResource(i);
    }

    public void setProgress(@FloatRange(from = 0.0d, to = 1.0d) float f) {
        this.O000OoO0.setProgress(f);
    }

    public void setRepeatCount(int i) {
        this.O000OoO0.setRepeatCount(i);
    }

    public void setRepeatMode(int i) {
        this.O000OoO0.setRepeatMode(i);
    }

    public void setScale(float f) {
        this.O000OoO0.setScale(f);
        if (getDrawable() == this.O000OoO0) {
            setImageDrawable(null);
            setImageDrawable(this.O000OoO0);
        }
    }

    public void setScaleType(ScaleType scaleType) {
        super.setScaleType(scaleType);
        C0083O000OoO0 o000OoO0 = this.O000OoO0;
        if (o000OoO0 != null) {
            o000OoO0.setScaleType(scaleType);
        }
    }

    public void setSpeed(float f) {
        this.O000OoO0.setSpeed(f);
    }
}
