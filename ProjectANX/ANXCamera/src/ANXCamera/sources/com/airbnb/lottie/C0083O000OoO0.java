package com.airbnb.lottie;

import android.animation.Animator.AnimatorListener;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Drawable.Callback;
import android.os.Build.VERSION;
import android.view.View;
import android.widget.ImageView.ScaleType;
import androidx.annotation.FloatRange;
import androidx.annotation.IntRange;
import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.airbnb.lottie.O00000Oo.O000000o;
import com.airbnb.lottie.O00000Oo.O00000Oo;
import com.airbnb.lottie.O00000o.C0032O00000oO;
import com.airbnb.lottie.O00000o.O00000o;
import com.airbnb.lottie.O00000o.O0000O0o;
import com.airbnb.lottie.O00000o0.C0042O0000oOO;
import com.airbnb.lottie.O00000oO.C0060O0000Ooo;
import com.airbnb.lottie.O00000oO.O0000Oo;
import com.airbnb.lottie.model.C0102O00000oO;
import com.airbnb.lottie.model.O0000OOo;
import com.airbnb.lottie.model.layer.C0111O00000oO;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/* renamed from: com.airbnb.lottie.O000OoO0 reason: case insensitive filesystem */
public class C0083O000OoO0 extends Drawable implements Callback, Animatable {
    public static final int INFINITE = -1;
    public static final int RESTART = 1;
    public static final int REVERSE = 2;
    private static final String TAG = "O000OoO0";
    private C0064O0000o0O O00000oo;
    private boolean O0000O0o = true;
    private boolean O0000OOo = false;
    private final ArrayList O0000Oo = new ArrayList();
    private final Set O0000Oo0 = new HashSet();
    private final AnimatorUpdateListener O0000OoO = new O000OO00(this);
    @Nullable
    private O00000Oo O0000Ooo;
    @Nullable
    C0094O000o0OO O0000o;
    @Nullable
    private O00000o O0000o0;
    @Nullable
    private String O0000o00;
    @Nullable
    private O000000o O0000o0O;
    @Nullable
    O00000o0 O0000o0o;
    /* access modifiers changed from: private */
    @Nullable
    public C0111O00000oO O0000oO;
    private boolean O0000oO0;
    private boolean O0000oOO;
    private boolean O0000oOo;
    private boolean O0000oo0 = true;
    private int alpha = 255;
    /* access modifiers changed from: private */
    public final C0032O00000oO animator = new C0032O00000oO();
    private boolean isDirty = false;
    private final Matrix matrix = new Matrix();
    private float scale = 1.0f;
    @Nullable
    private ScaleType scaleType;

    public C0083O000OoO0() {
        this.animator.addUpdateListener(this.O0000OoO);
    }

    private void O000000o(@NonNull Canvas canvas) {
        if (ScaleType.FIT_XY == this.scaleType) {
            O00000Oo(canvas);
        } else {
            O00000o0(canvas);
        }
    }

    private void O00000Oo(Canvas canvas) {
        float f;
        if (this.O0000oO != null) {
            int i = -1;
            Rect bounds = getBounds();
            float width = ((float) bounds.width()) / ((float) this.O00000oo.getBounds().width());
            float height = ((float) bounds.height()) / ((float) this.O00000oo.getBounds().height());
            if (this.O0000oo0) {
                float min = Math.min(width, height);
                if (min < 1.0f) {
                    f = 1.0f / min;
                    width /= f;
                    height /= f;
                } else {
                    f = 1.0f;
                }
                if (f > 1.0f) {
                    i = canvas.save();
                    float width2 = ((float) bounds.width()) / 2.0f;
                    float height2 = ((float) bounds.height()) / 2.0f;
                    float f2 = width2 * min;
                    float f3 = min * height2;
                    canvas.translate(width2 - f2, height2 - f3);
                    canvas.scale(f, f, f2, f3);
                }
            }
            this.matrix.reset();
            this.matrix.preScale(width, height);
            this.O0000oO.O000000o(canvas, this.matrix, this.alpha);
            if (i > 0) {
                canvas.restoreToCount(i);
            }
        }
    }

    private float O00000o(@NonNull Canvas canvas) {
        return Math.min(((float) canvas.getWidth()) / ((float) this.O00000oo.getBounds().width()), ((float) canvas.getHeight()) / ((float) this.O00000oo.getBounds().height()));
    }

    private void O00000o0(Canvas canvas) {
        float f;
        if (this.O0000oO != null) {
            float f2 = this.scale;
            float O00000o2 = O00000o(canvas);
            if (f2 > O00000o2) {
                f = this.scale / O00000o2;
            } else {
                O00000o2 = f2;
                f = 1.0f;
            }
            int i = -1;
            if (f > 1.0f) {
                i = canvas.save();
                float width = ((float) this.O00000oo.getBounds().width()) / 2.0f;
                float height = ((float) this.O00000oo.getBounds().height()) / 2.0f;
                float f3 = width * O00000o2;
                float f4 = height * O00000o2;
                canvas.translate((getScale() * width) - f3, (getScale() * height) - f4);
                canvas.scale(f, f, f3, f4);
            }
            this.matrix.reset();
            this.matrix.preScale(O00000o2, O00000o2);
            this.O0000oO.O000000o(canvas, this.matrix, this.alpha);
            if (i > 0) {
                canvas.restoreToCount(i);
            }
        }
    }

    private O000000o Oo0oO0() {
        if (getCallback() == null) {
            return null;
        }
        if (this.O0000o0O == null) {
            this.O0000o0O = new O000000o(getCallback(), this.O0000o0o);
        }
        return this.O0000o0O;
    }

    private O00000Oo Oo0oO0O() {
        if (getCallback() == null) {
            return null;
        }
        O00000Oo o00000Oo = this.O0000Ooo;
        if (o00000Oo != null && !o00000Oo.O000000o(getContext())) {
            this.O0000Ooo = null;
        }
        if (this.O0000Ooo == null) {
            this.O0000Ooo = new O00000Oo(getCallback(), this.O0000o00, this.O0000o0, this.O00000oo.O00O0o0o());
        }
        return this.O0000Ooo;
    }

    private void Oo0ooo() {
        this.O0000oO = new C0111O00000oO(this, C0042O0000oOO.O00000oO(this.O00000oo), this.O00000oo.getLayers(), this.O00000oo);
    }

    @Nullable
    private Context getContext() {
        Callback callback = getCallback();
        if (callback != null && (callback instanceof View)) {
            return ((View) callback).getContext();
        }
        return null;
    }

    private void updateBounds() {
        if (this.O00000oo != null) {
            float scale2 = getScale();
            setBounds(0, 0, (int) (((float) this.O00000oo.getBounds().width()) * scale2), (int) (((float) this.O00000oo.getBounds().height()) * scale2));
        }
    }

    @Nullable
    public Bitmap O000000o(String str) {
        O00000Oo Oo0oO0O = Oo0oO0O();
        if (Oo0oO0O != null) {
            return Oo0oO0O.O000O0OO(str);
        }
        return null;
    }

    @Nullable
    public Bitmap O000000o(String str, @Nullable Bitmap bitmap) {
        O00000Oo Oo0oO0O = Oo0oO0O();
        if (Oo0oO0O == null) {
            O00000o.warning("Cannot update bitmap. Most likely the drawable is not added to a View which prevents Lottie from getting a Context.");
            return null;
        }
        Bitmap O000000o2 = Oo0oO0O.O000000o(str, bitmap);
        invalidateSelf();
        return O000000o2;
    }

    @Nullable
    public Typeface O000000o(String str, String str2) {
        O000000o Oo0oO0 = Oo0oO0();
        if (Oo0oO0 != null) {
            return Oo0oO0.O000000o(str, str2);
        }
        return null;
    }

    public List O000000o(C0102O00000oO o00000oO) {
        if (this.O0000oO == null) {
            O00000o.warning("Cannot resolve KeyPath. Composition is not set yet.");
            return Collections.emptyList();
        }
        ArrayList arrayList = new ArrayList();
        this.O0000oO.O000000o(o00000oO, 0, arrayList, new C0102O00000oO(new String[0]));
        return arrayList;
    }

    public void O000000o(int i) {
        if (this.O00000oo == null) {
            this.O0000Oo.add(new O000OOOo(this, i));
        } else {
            this.animator.O000000o(i);
        }
    }

    public void O000000o(AnimatorListener animatorListener) {
        this.animator.addListener(animatorListener);
    }

    public void O000000o(AnimatorUpdateListener animatorUpdateListener) {
        this.animator.addUpdateListener(animatorUpdateListener);
    }

    public void O000000o(O00000o0 o00000o0) {
        this.O0000o0o = o00000o0;
        O000000o o000000o = this.O0000o0O;
        if (o000000o != null) {
            o000000o.O00000Oo(o00000o0);
        }
    }

    public void O000000o(O00000o o00000o) {
        this.O0000o0 = o00000o;
        O00000Oo o00000Oo = this.O0000Ooo;
        if (o00000Oo != null) {
            o00000Oo.O00000Oo(o00000o);
        }
    }

    public void O000000o(C0094O000o0OO o000o0OO) {
        this.O0000o = o000o0OO;
    }

    public void O000000o(C0102O00000oO o00000oO, Object obj, O0000Oo o0000Oo) {
        if (this.O0000oO == null) {
            this.O0000Oo.add(new C0075O000O0oO(this, o00000oO, obj, o0000Oo));
            return;
        }
        boolean z = true;
        if (o00000oO.O00OoO0() != null) {
            o00000oO.O00OoO0().O000000o(obj, o0000Oo);
        } else {
            List O000000o2 = O000000o(o00000oO);
            for (int i = 0; i < O000000o2.size(); i++) {
                ((C0102O00000oO) O000000o2.get(i)).O00OoO0().O000000o(obj, o0000Oo);
            }
            z = true ^ O000000o2.isEmpty();
        }
        if (z) {
            invalidateSelf();
            if (obj == C0087O000Ooo0.OO00oo) {
                setProgress(getProgress());
            }
        }
    }

    public void O000000o(C0102O00000oO o00000oO, Object obj, C0060O0000Ooo o0000Ooo) {
        O000000o(o00000oO, obj, (O0000Oo) new C0076O000O0oo(this, o0000Ooo));
    }

    /* access modifiers changed from: 0000 */
    public void O000000o(Boolean bool) {
        this.O0000O0o = bool.booleanValue();
    }

    public void O000000o(String str, String str2, boolean z) {
        C0064O0000o0O o0000o0O = this.O00000oo;
        if (o0000o0O == null) {
            this.O0000Oo.add(new O000O0OO(this, str, str2, z));
            return;
        }
        O0000OOo O0000ooO = o0000o0O.O0000ooO(str);
        String str3 = ".";
        String str4 = "Cannot find marker with name ";
        if (O0000ooO != null) {
            int i = (int) O0000ooO.O000oO0O;
            O0000OOo O0000ooO2 = this.O00000oo.O0000ooO(str2);
            if (str2 != null) {
                O00000oo(i, (int) (O0000ooO2.O000oO0O + (z ? 1.0f : 0.0f)));
                return;
            }
            StringBuilder sb = new StringBuilder();
            sb.append(str4);
            sb.append(str2);
            sb.append(str3);
            throw new IllegalArgumentException(sb.toString());
        }
        StringBuilder sb2 = new StringBuilder();
        sb2.append(str4);
        sb2.append(str);
        sb2.append(str3);
        throw new IllegalArgumentException(sb2.toString());
    }

    public void O00000Oo(@FloatRange(from = 0.0d, to = 1.0d) float f, @FloatRange(from = 0.0d, to = 1.0d) float f2) {
        C0064O0000o0O o0000o0O = this.O00000oo;
        if (o0000o0O == null) {
            this.O0000Oo.add(new O00oOoOo(this, f, f2));
        } else {
            O00000oo((int) O0000O0o.lerp(o0000o0O.O00O0oOO(), this.O00000oo.O00O0o0O(), f), (int) O0000O0o.lerp(this.O00000oo.O00O0oOO(), this.O00000oo.O00O0o0O(), f2));
        }
    }

    public void O00000Oo(AnimatorListener animatorListener) {
        this.animator.removeListener(animatorListener);
    }

    public void O00000Oo(AnimatorUpdateListener animatorUpdateListener) {
        this.animator.removeUpdateListener(animatorUpdateListener);
    }

    public void O00000Oo(@Nullable String str) {
        this.O0000o00 = str;
    }

    public void O00000o(String str) {
        C0064O0000o0O o0000o0O = this.O00000oo;
        if (o0000o0O == null) {
            this.O0000Oo.add(new O000O00o(this, str));
            return;
        }
        O0000OOo O0000ooO = o0000o0O.O0000ooO(str);
        if (O0000ooO != null) {
            int i = (int) O0000ooO.O000oO0O;
            O00000oo(i, ((int) O0000ooO.O00oO0) + i);
            return;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Cannot find marker with name ");
        sb.append(str);
        sb.append(".");
        throw new IllegalArgumentException(sb.toString());
    }

    public void O00000o0(String str) {
        C0064O0000o0O o0000o0O = this.O00000oo;
        if (o0000o0O == null) {
            this.O0000Oo.add(new C0079O000Oo00(this, str));
            return;
        }
        O0000OOo O0000ooO = o0000o0O.O0000ooO(str);
        if (O0000ooO != null) {
            O00000oo((int) (O0000ooO.O000oO0O + O0000ooO.O00oO0));
            return;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Cannot find marker with name ");
        sb.append(str);
        sb.append(".");
        throw new IllegalArgumentException(sb.toString());
    }

    public boolean O00000o0(C0064O0000o0O o0000o0O) {
        if (this.O00000oo == o0000o0O) {
            return false;
        }
        this.isDirty = false;
        O0000o0();
        this.O00000oo = o0000o0O;
        Oo0ooo();
        this.animator.O00000o0(o0000o0O);
        setProgress(this.animator.getAnimatedFraction());
        setScale(this.scale);
        updateBounds();
        Iterator it = new ArrayList(this.O0000Oo).iterator();
        while (it.hasNext()) {
            ((C0080O000Oo0O) it.next()).O00000Oo(o0000o0O);
            it.remove();
        }
        this.O0000Oo.clear();
        o0000o0O.O0000Oo0(this.O0000oOO);
        return true;
    }

    public void O00000oO(String str) {
        C0064O0000o0O o0000o0O = this.O00000oo;
        if (o0000o0O == null) {
            this.O0000Oo.add(new C0078O000OOoo(this, str));
            return;
        }
        O0000OOo O0000ooO = o0000o0O.O0000ooO(str);
        if (O0000ooO != null) {
            O000000o((int) O0000ooO.O000oO0O);
            return;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Cannot find marker with name ");
        sb.append(str);
        sb.append(".");
        throw new IllegalArgumentException(sb.toString());
    }

    public void O00000oo(@FloatRange(from = 0.0d, to = 1.0d) float f) {
        C0064O0000o0O o0000o0O = this.O00000oo;
        if (o0000o0O == null) {
            this.O0000Oo.add(new C0077O000OOoO(this, f));
        } else {
            O00000oo((int) O0000O0o.lerp(o0000o0O.O00O0oOO(), this.O00000oo.O00O0o0O(), f));
        }
    }

    public void O00000oo(int i) {
        if (this.O00000oo == null) {
            this.O0000Oo.add(new O000OOo(this, i));
        } else {
            this.animator.O00000oO(((float) i) + 0.99f);
        }
    }

    public void O00000oo(int i, int i2) {
        if (this.O00000oo == null) {
            this.O0000Oo.add(new C0074O000O0Oo(this, i, i2));
        } else {
            this.animator.O000000o((float) i, ((float) i2) + 0.99f);
        }
    }

    public void O00000oo(boolean z) {
        if (this.O0000oO0 != z) {
            if (VERSION.SDK_INT < 19) {
                O00000o.warning("Merge paths are not supported pre-Kit Kat.");
                return;
            }
            this.O0000oO0 = z;
            if (this.O00000oo != null) {
                Oo0ooo();
            }
        }
    }

    public void O0000O0o(float f) {
        C0064O0000o0O o0000o0O = this.O00000oo;
        if (o0000o0O == null) {
            this.O0000Oo.add(new O000OOo0(this, f));
        } else {
            O000000o((int) O0000O0o.lerp(o0000o0O.O00O0oOO(), this.O00000oo.O00O0o0O(), f));
        }
    }

    @Deprecated
    public void O0000O0o(boolean z) {
        this.animator.setRepeatCount(z ? -1 : 0);
    }

    public void O0000OOo(boolean z) {
        this.O0000oOo = z;
    }

    public void O0000Oo(boolean z) {
        this.O0000OOo = z;
    }

    public void O0000Oo0(boolean z) {
        this.O0000oOO = z;
        C0064O0000o0O o0000o0O = this.O00000oo;
        if (o0000o0O != null) {
            o0000o0O.O0000Oo0(z);
        }
    }

    public float O0000o() {
        return this.animator.O0000o();
    }

    public void O0000o0() {
        if (this.animator.isRunning()) {
            this.animator.cancel();
        }
        this.O00000oo = null;
        this.O0000oO = null;
        this.O0000Ooo = null;
        this.animator.O0000o0();
        invalidateSelf();
    }

    public float O0000o0o() {
        return this.animator.O0000o0o();
    }

    @MainThread
    public void O0000oO() {
        if (this.O0000oO == null) {
            this.O0000Oo.add(new O000OO0o(this));
            return;
        }
        if (this.O0000O0o || getRepeatCount() == 0) {
            this.animator.O0000oO();
        }
        if (!this.O0000O0o) {
            setFrame((int) (getSpeed() < 0.0f ? O0000o() : O0000o0o()));
            this.animator.endAnimation();
        }
    }

    public void O0000oO0() {
        this.O0000Oo.clear();
        this.animator.O0000oO0();
    }

    @MainThread
    public void O0000oOo() {
        if (this.O0000oO == null) {
            this.O0000Oo.add(new O000OO(this));
            return;
        }
        if (this.O0000O0o || getRepeatCount() == 0) {
            this.animator.O0000oOo();
        }
        if (!this.O0000O0o) {
            setFrame((int) (getSpeed() < 0.0f ? O0000o() : O0000o0o()));
            this.animator.endAnimation();
        }
    }

    public void O0000oo0() {
        this.animator.O0000oo0();
    }

    public boolean O000O00o() {
        return this.O0000oO0;
    }

    public C0064O0000o0O O000O0OO() {
        return this.O00000oo;
    }

    @Nullable
    public String O000O0Oo() {
        return this.O0000o00;
    }

    public boolean O000O0o() {
        C0111O00000oO o00000oO = this.O0000oO;
        return o00000oO != null && o00000oO.O000O0o();
    }

    @Nullable
    public C0094O000o0OO O000O0o0() {
        return this.O0000o;
    }

    public boolean O000O0oO() {
        C0111O00000oO o00000oO = this.O0000oO;
        return o00000oO != null && o00000oO.O000O0oO();
    }

    public boolean O000O0oo() {
        return this.O0000oOo;
    }

    public boolean O000OO() {
        return this.O0000o == null && this.O00000oo.getCharacters().size() > 0;
    }

    public boolean O000OO00() {
        return this.O0000oO0;
    }

    public void O000OO0o() {
        this.animator.removeAllListeners();
    }

    @Nullable
    public O000o0 O00oOoOo() {
        C0064O0000o0O o0000o0O = this.O00000oo;
        if (o0000o0O != null) {
            return o0000o0O.O00oOoOo();
        }
        return null;
    }

    public void O00oOooo() {
        this.O0000oo0 = false;
    }

    public void cancelAnimation() {
        this.O0000Oo.clear();
        this.animator.cancel();
    }

    public void draw(@NonNull Canvas canvas) {
        this.isDirty = false;
        String str = "Drawable#draw";
        C0053O00000oO.beginSection(str);
        if (this.O0000OOo) {
            try {
                O000000o(canvas);
            } catch (Throwable th) {
                O00000o.O000000o("Lottie crashed in draw!", th);
            }
        } else {
            O000000o(canvas);
        }
        C0053O00000oO.O0000oOo(str);
    }

    @MainThread
    public void endAnimation() {
        this.O0000Oo.clear();
        this.animator.endAnimation();
    }

    public int getAlpha() {
        return this.alpha;
    }

    public int getFrame() {
        return (int) this.animator.getFrame();
    }

    public int getIntrinsicHeight() {
        C0064O0000o0O o0000o0O = this.O00000oo;
        if (o0000o0O == null) {
            return -1;
        }
        return (int) (((float) o0000o0O.getBounds().height()) * getScale());
    }

    public int getIntrinsicWidth() {
        C0064O0000o0O o0000o0O = this.O00000oo;
        if (o0000o0O == null) {
            return -1;
        }
        return (int) (((float) o0000o0O.getBounds().width()) * getScale());
    }

    public int getOpacity() {
        return -3;
    }

    @FloatRange(from = 0.0d, to = 1.0d)
    public float getProgress() {
        return this.animator.O0000o0O();
    }

    public int getRepeatCount() {
        return this.animator.getRepeatCount();
    }

    public int getRepeatMode() {
        return this.animator.getRepeatMode();
    }

    public float getScale() {
        return this.scale;
    }

    public float getSpeed() {
        return this.animator.getSpeed();
    }

    public void invalidateDrawable(@NonNull Drawable drawable) {
        Callback callback = getCallback();
        if (callback != null) {
            callback.invalidateDrawable(this);
        }
    }

    public void invalidateSelf() {
        if (!this.isDirty) {
            this.isDirty = true;
            Callback callback = getCallback();
            if (callback != null) {
                callback.invalidateDrawable(this);
            }
        }
    }

    public boolean isAnimating() {
        C0032O00000oO o00000oO = this.animator;
        if (o00000oO == null) {
            return false;
        }
        return o00000oO.isRunning();
    }

    public boolean isLooping() {
        return this.animator.getRepeatCount() == -1;
    }

    public boolean isRunning() {
        return isAnimating();
    }

    public void removeAllUpdateListeners() {
        this.animator.removeAllUpdateListeners();
        this.animator.addUpdateListener(this.O0000OoO);
    }

    public void scheduleDrawable(@NonNull Drawable drawable, @NonNull Runnable runnable, long j) {
        Callback callback = getCallback();
        if (callback != null) {
            callback.scheduleDrawable(this, runnable, j);
        }
    }

    public void setAlpha(@IntRange(from = 0, to = 255) int i) {
        this.alpha = i;
        invalidateSelf();
    }

    public void setColorFilter(@Nullable ColorFilter colorFilter) {
        O00000o.warning("Use addColorFilter instead.");
    }

    public void setFrame(int i) {
        if (this.O00000oo == null) {
            this.O0000Oo.add(new O000O0o0(this, i));
        } else {
            this.animator.O00000o((float) i);
        }
    }

    public void setProgress(@FloatRange(from = 0.0d, to = 1.0d) float f) {
        if (this.O00000oo == null) {
            this.O0000Oo.add(new O000O0o(this, f));
            return;
        }
        String str = "Drawable#setProgress";
        C0053O00000oO.beginSection(str);
        this.animator.O00000o(O0000O0o.lerp(this.O00000oo.O00O0oOO(), this.O00000oo.O00O0o0O(), f));
        C0053O00000oO.O0000oOo(str);
    }

    public void setRepeatCount(int i) {
        this.animator.setRepeatCount(i);
    }

    public void setRepeatMode(int i) {
        this.animator.setRepeatMode(i);
    }

    public void setScale(float f) {
        this.scale = f;
        updateBounds();
    }

    /* access modifiers changed from: 0000 */
    public void setScaleType(ScaleType scaleType2) {
        this.scaleType = scaleType2;
    }

    public void setSpeed(float f) {
        this.animator.setSpeed(f);
    }

    @MainThread
    public void start() {
        O0000oO();
    }

    @MainThread
    public void stop() {
        endAnimation();
    }

    public void unscheduleDrawable(@NonNull Drawable drawable, @NonNull Runnable runnable) {
        Callback callback = getCallback();
        if (callback != null) {
            callback.unscheduleDrawable(this, runnable);
        }
    }
}
