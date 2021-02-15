package com.airbnb.lottie.O00000o;

import android.view.Choreographer;
import android.view.Choreographer.FrameCallback;
import androidx.annotation.FloatRange;
import androidx.annotation.MainThread;
import androidx.annotation.Nullable;
import androidx.annotation.VisibleForTesting;
import com.airbnb.lottie.C0053O00000oO;
import com.airbnb.lottie.C0064O0000o0O;

/* renamed from: com.airbnb.lottie.O00000o.O00000oO reason: case insensitive filesystem */
public class C0032O00000oO extends O000000o implements FrameCallback {

    /* renamed from: O00000Oo reason: collision with root package name */
    private boolean f6O00000Oo = false;

    /* renamed from: O00000o reason: collision with root package name */
    private float f7O00000o = -2.14748365E9f;

    /* renamed from: O00000o0 reason: collision with root package name */
    private long f8O00000o0 = 0;
    private float O00000oO = 2.14748365E9f;
    @Nullable
    private C0064O0000o0O O00000oo;
    private float frame = 0.0f;
    private int repeatCount = 0;
    @VisibleForTesting
    protected boolean running = false;
    private float speed = 1.0f;

    private float Oo0oOO() {
        C0064O0000o0O o0000o0O = this.O00000oo;
        if (o0000o0O == null) {
            return Float.MAX_VALUE;
        }
        return (1.0E9f / o0000o0O.getFrameRate()) / Math.abs(this.speed);
    }

    private void Oo0oo0() {
        if (this.O00000oo != null) {
            float f = this.frame;
            if (f < this.f7O00000o || f > this.O00000oO) {
                throw new IllegalStateException(String.format("Frame must be [%f,%f]. It is %f", new Object[]{Float.valueOf(this.f7O00000o), Float.valueOf(this.O00000oO), Float.valueOf(this.frame)}));
            }
        }
    }

    private boolean isReversed() {
        return getSpeed() < 0.0f;
    }

    public void O000000o(float f, float f2) {
        if (f <= f2) {
            C0064O0000o0O o0000o0O = this.O00000oo;
            float O00O0oOO = o0000o0O == null ? -3.4028235E38f : o0000o0O.O00O0oOO();
            C0064O0000o0O o0000o0O2 = this.O00000oo;
            float O00O0o0O = o0000o0O2 == null ? Float.MAX_VALUE : o0000o0O2.O00O0o0O();
            this.f7O00000o = O0000O0o.clamp(f, O00O0oOO, O00O0o0O);
            this.O00000oO = O0000O0o.clamp(f2, O00O0oOO, O00O0o0O);
            O00000o((float) ((int) O0000O0o.clamp(this.frame, f, f2)));
            return;
        }
        throw new IllegalArgumentException(String.format("minFrame (%s) must be <= maxFrame (%s)", new Object[]{Float.valueOf(f), Float.valueOf(f2)}));
    }

    public void O000000o(int i) {
        O000000o((float) i, (float) ((int) this.O00000oO));
    }

    public void O00000o(float f) {
        if (this.frame != f) {
            this.frame = O0000O0o.clamp(f, O0000o(), O0000o0o());
            this.f8O00000o0 = 0;
            O0000o00();
        }
    }

    public void O00000o0(C0064O0000o0O o0000o0O) {
        float f;
        float f2;
        boolean z = this.O00000oo == null;
        this.O00000oo = o0000o0O;
        if (z) {
            f2 = (float) ((int) Math.max(this.f7O00000o, o0000o0O.O00O0oOO()));
            f = Math.min(this.O00000oO, o0000o0O.O00O0o0O());
        } else {
            f2 = (float) ((int) o0000o0O.O00O0oOO());
            f = o0000o0O.O00O0o0O();
        }
        O000000o(f2, (float) ((int) f));
        float f3 = this.frame;
        this.frame = 0.0f;
        O00000o((float) ((int) f3));
    }

    /* access modifiers changed from: protected */
    @MainThread
    public void O00000o0(boolean z) {
        Choreographer.getInstance().removeFrameCallback(this);
        if (z) {
            this.running = false;
        }
    }

    public void O00000oO(float f) {
        O000000o(this.f7O00000o, f);
    }

    public float O0000o() {
        C0064O0000o0O o0000o0O = this.O00000oo;
        if (o0000o0O == null) {
            return 0.0f;
        }
        float f = this.f7O00000o;
        if (f == -2.14748365E9f) {
            f = o0000o0O.O00O0oOO();
        }
        return f;
    }

    public void O0000o0() {
        this.O00000oo = null;
        this.f7O00000o = -2.14748365E9f;
        this.O00000oO = 2.14748365E9f;
    }

    @FloatRange(from = 0.0d, to = 1.0d)
    public float O0000o0O() {
        C0064O0000o0O o0000o0O = this.O00000oo;
        if (o0000o0O == null) {
            return 0.0f;
        }
        return (this.frame - o0000o0O.O00O0oOO()) / (this.O00000oo.O00O0o0O() - this.O00000oo.O00O0oOO());
    }

    public float O0000o0o() {
        C0064O0000o0O o0000o0O = this.O00000oo;
        if (o0000o0O == null) {
            return 0.0f;
        }
        float f = this.O00000oO;
        if (f == 2.14748365E9f) {
            f = o0000o0O.O00O0o0O();
        }
        return f;
    }

    @MainThread
    public void O0000oO() {
        this.running = true;
        O00000Oo(isReversed());
        O00000o((float) ((int) (isReversed() ? O0000o0o() : O0000o())));
        this.f8O00000o0 = 0;
        this.repeatCount = 0;
        postFrameCallback();
    }

    @MainThread
    public void O0000oO0() {
        O0000oOO();
    }

    /* access modifiers changed from: protected */
    @MainThread
    public void O0000oOO() {
        O00000o0(true);
    }

    @MainThread
    public void O0000oOo() {
        float O0000o;
        this.running = true;
        postFrameCallback();
        this.f8O00000o0 = 0;
        if (isReversed() && getFrame() == O0000o()) {
            O0000o = O0000o0o();
        } else if (!isReversed() && getFrame() == O0000o0o()) {
            O0000o = O0000o();
        } else {
            return;
        }
        this.frame = O0000o;
    }

    public void O0000oo0() {
        setSpeed(-getSpeed());
    }

    @MainThread
    public void cancel() {
        O0000OoO();
        O0000oOO();
    }

    public void doFrame(long j) {
        postFrameCallback();
        if (this.O00000oo != null && isRunning()) {
            String str = "LottieValueAnimator#doFrame";
            C0053O00000oO.beginSection(str);
            long j2 = this.f8O00000o0;
            long j3 = 0;
            if (j2 != 0) {
                j3 = j - j2;
            }
            float Oo0oOO = ((float) j3) / Oo0oOO();
            float f = this.frame;
            if (isReversed()) {
                Oo0oOO = -Oo0oOO;
            }
            this.frame = f + Oo0oOO;
            boolean z = !O0000O0o.O000000o(this.frame, O0000o(), O0000o0o());
            this.frame = O0000O0o.clamp(this.frame, O0000o(), O0000o0o());
            this.f8O00000o0 = j;
            O0000o00();
            if (z) {
                if (getRepeatCount() == -1 || this.repeatCount < getRepeatCount()) {
                    O0000Ooo();
                    this.repeatCount++;
                    if (getRepeatMode() == 2) {
                        this.f6O00000Oo = !this.f6O00000Oo;
                        O0000oo0();
                    } else {
                        this.frame = isReversed() ? O0000o0o() : O0000o();
                    }
                    this.f8O00000o0 = j;
                } else {
                    this.frame = this.speed < 0.0f ? O0000o() : O0000o0o();
                    O0000oOO();
                    O000000o(isReversed());
                }
            }
            Oo0oo0();
            C0053O00000oO.O0000oOo(str);
        }
    }

    @MainThread
    public void endAnimation() {
        O0000oOO();
        O000000o(isReversed());
    }

    @FloatRange(from = 0.0d, to = 1.0d)
    public float getAnimatedFraction() {
        float f;
        float O0000o;
        if (this.O00000oo == null) {
            return 0.0f;
        }
        if (isReversed()) {
            f = O0000o0o();
            O0000o = this.frame;
        } else {
            f = this.frame;
            O0000o = O0000o();
        }
        return (f - O0000o) / (O0000o0o() - O0000o());
    }

    public Object getAnimatedValue() {
        return Float.valueOf(O0000o0O());
    }

    public long getDuration() {
        C0064O0000o0O o0000o0O = this.O00000oo;
        if (o0000o0O == null) {
            return 0;
        }
        return (long) o0000o0O.getDuration();
    }

    public float getFrame() {
        return this.frame;
    }

    public float getSpeed() {
        return this.speed;
    }

    public boolean isRunning() {
        return this.running;
    }

    /* access modifiers changed from: protected */
    public void postFrameCallback() {
        if (isRunning()) {
            O00000o0(false);
            Choreographer.getInstance().postFrameCallback(this);
        }
    }

    public void setRepeatMode(int i) {
        super.setRepeatMode(i);
        if (i != 2 && this.f6O00000Oo) {
            this.f6O00000Oo = false;
            O0000oo0();
        }
    }

    public void setSpeed(float f) {
        this.speed = f;
    }
}
