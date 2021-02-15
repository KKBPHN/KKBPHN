package com.android.camera.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import com.android.camera.Util;
import com.android.camera.animation.FolmeUtils;
import com.android.camera.animation.FolmeUtils.PhyAnimatorListener;
import com.android.camera.customization.TintColor;
import com.android.camera.lib.compatibility.related.vibrator.ViberatorContext;
import com.android.camera.log.Log;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import miuix.animation.Folme;
import miuix.animation.IStateStyle;
import miuix.animation.base.AnimConfig;
import miuix.animation.property.FloatProperty;

public class SeekBarCompat extends SeekBar implements OnSeekBarChangeListener, OnTouchListener {
    public static final int CENTER_SEEKBAR = 2;
    private static final int INTERVAL = 5;
    public static final int NORMAL_SEEKBAR = 1;
    private static final String TAG = "SeekBarCompat";
    private float mAvaliableWidth;
    private Paint mBackgroundPaint;
    private float mBigCircleRadius;
    private boolean mCenterTwoWayMode;
    private Paint mCirclePaint;
    private float mCircleRadius;
    private int mClearGap;
    private Paint mClearPaint;
    /* access modifiers changed from: private */
    public float mCurrentCircleRadius;
    private float mDownX;
    private boolean mIsRTL;
    private float mLineWidth;
    private OnSeekBarCompatTouchListener mOnSeekBarCompatTouchListener;
    private Paint mPinPointPaint;
    private int mPinProgress;
    private Paint mPinProgressPaint;
    private RectF mPinProgressRectF;
    private float mPinRadius;
    private Object mScaleObject;
    private OnSeekBarCompatChangeListener mSeekBarCompatChangeListener;
    private IStateStyle mStyle;
    private Rect mTouchRect;
    private int mUserLastSeekPoint;
    private boolean mUserSeek;
    /* access modifiers changed from: private */
    public float mVisualProgress;

    public interface OnSeekBarCompatChangeListener {
        void onProgressChanged(SeekBar seekBar, int i, boolean z);

        void onStartTrackingTouch(SeekBar seekBar);

        void onStopTrackingTouch(SeekBar seekBar);
    }

    public interface OnSeekBarCompatTouchListener {
        boolean onTouch(View view, MotionEvent motionEvent);
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface SeekBarMode {
    }

    public SeekBarCompat(Context context) {
        this(context, null, -1, 0);
    }

    public SeekBarCompat(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, -1, 0);
    }

    public SeekBarCompat(Context context, AttributeSet attributeSet, int i) {
        this(context, attributeSet, i, 0);
    }

    public SeekBarCompat(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
        this.mScaleObject = new Object();
        this.mVisualProgress = 0.0f;
        this.mTouchRect = new Rect();
        init(context);
    }

    private boolean contains(int i, int i2, int i3) {
        boolean z = false;
        if (i2 == i3) {
            return false;
        }
        if (i > Math.min(i2, i3) && i < Math.max(i2, i3)) {
            z = true;
        }
        return z;
    }

    private void init(Context context) {
        this.mIsRTL = Util.isLayoutRTL(context);
        setThumb(null);
        setOnSeekBarChangeListener(this);
        setOnTouchListener(this);
        setLayerType(2, null);
        this.mLineWidth = (float) Util.dpToPixel(1.818f);
        this.mBackgroundPaint = new Paint();
        this.mBackgroundPaint.setAntiAlias(true);
        this.mBackgroundPaint.setStyle(Style.FILL);
        this.mBackgroundPaint.setColor(-1);
        this.mBackgroundPaint.setShadowLayer(1.0f, 0.0f, 0.0f, Integer.MIN_VALUE);
        this.mClearPaint = new Paint();
        this.mClearPaint.setAntiAlias(true);
        this.mClearPaint.setXfermode(new PorterDuffXfermode(Mode.CLEAR));
        this.mCirclePaint = new Paint();
        this.mCirclePaint.setStyle(Style.STROKE);
        this.mCirclePaint.setColor(-1);
        this.mCirclePaint.setAntiAlias(true);
        this.mCirclePaint.setStrokeWidth(this.mLineWidth);
        this.mCircleRadius = (float) Util.dpToPixel(11.27f);
        this.mBigCircleRadius = (float) Util.dpToPixel(16.36f);
        this.mCurrentCircleRadius = this.mCircleRadius;
        this.mCirclePaint.setShadowLayer(1.0f, 0.0f, 0.0f, Integer.MIN_VALUE);
        this.mPinProgressPaint = new Paint();
        this.mPinProgressPaint.setStyle(Style.FILL);
        this.mPinProgressPaint.setColor(TintColor.tintColor());
        this.mPinProgressPaint.setAntiAlias(true);
        this.mPinPointPaint = new Paint();
        this.mPinPointPaint.setStyle(Style.FILL);
        this.mPinPointPaint.setColor(-1);
        this.mPinPointPaint.setAntiAlias(true);
        this.mPinPointPaint.setShadowLayer(1.0f, 0.0f, 0.0f, Integer.MIN_VALUE);
        this.mPinProgressRectF = new RectF();
        this.mPinRadius = this.mLineWidth + 1.0f;
        this.mClearGap = Util.dpToPixel(2.181f);
    }

    private void startAnimator(boolean z) {
        FolmeUtils.basePhysicsAnimation(this.mScaleObject, this.mCurrentCircleRadius, z ? this.mBigCircleRadius : this.mCircleRadius, 0.0f, new PhyAnimatorListener() {
            public void onUpdate(Object obj, FloatProperty floatProperty, float f, float f2, boolean z) {
                super.onUpdate(obj, floatProperty, f, f2, z);
                SeekBarCompat.this.mCurrentCircleRadius = f;
                SeekBarCompat.this.invalidate();
            }
        });
    }

    private void startScroll(int i) {
        this.mStyle = Folme.useValue(this).setTo((Object) Float.valueOf(this.mVisualProgress)).to(Float.valueOf((float) i), new AnimConfig().setEase(-2, 0.9f, 0.15f).setFromSpeed(0.0f).addListeners(new PhyAnimatorListener() {
            public void onUpdate(Object obj, FloatProperty floatProperty, float f, float f2, boolean z) {
                super.onUpdate(obj, floatProperty, f, f2, z);
                SeekBarCompat.this.mVisualProgress = f;
                SeekBarCompat.this.invalidate();
            }
        }));
    }

    /* JADX WARNING: Code restructure failed: missing block: B:12:0x0027, code lost:
        if (r3.mIsRTL != false) goto L_0x001c;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:6:0x0015, code lost:
        if (r3.mIsRTL != false) goto L_0x0017;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:8:0x001c, code lost:
        r4 = getMin();
     */
    /* JADX WARNING: Removed duplicated region for block: B:18:0x0044  */
    /* JADX WARNING: Removed duplicated region for block: B:19:0x0054  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public int getNextProgress(float f) {
        int i;
        float f2 = this.mAvaliableWidth;
        if (f2 == 0.0f) {
            f2 = (float) getWidth();
        }
        float f3 = f - this.mBigCircleRadius;
        if (f3 >= 0.0f) {
            if (f3 <= f2) {
                i = Math.round((f3 / f2) * ((float) getMax()));
                if (this.mIsRTL) {
                    i = getMax() - i;
                }
                if (this.mCenterTwoWayMode) {
                    return Util.clamp(i, 0, getMax());
                }
                int i2 = this.mPinProgress;
                return Util.clamp(i - i2, 0 - i2, getMax() - this.mPinProgress);
            }
        }
        i = getMax();
        if (this.mCenterTwoWayMode) {
        }
    }

    public int getPinProgress() {
        return this.mPinProgress;
    }

    public Rect getTouchRect() {
        return this.mTouchRect;
    }

    public boolean isCenterTwoWayMode() {
        return this.mCenterTwoWayMode;
    }

    /* access modifiers changed from: protected */
    public synchronized void onDraw(Canvas canvas) {
        float max;
        float strokeWidth;
        Paint paint;
        Paint paint2;
        Paint paint3;
        Canvas canvas2 = canvas;
        synchronized (this) {
            int height = getHeight() / 2;
            if (this.mAvaliableWidth == 0.0f) {
                this.mAvaliableWidth = ((float) getWidth()) - (this.mBigCircleRadius * 2.0f);
            }
            float f = this.mCenterTwoWayMode ? this.mVisualProgress + ((float) this.mPinProgress) : this.mVisualProgress;
            float max2 = (this.mAvaliableWidth * (((float) (this.mIsRTL ? getMax() - this.mPinProgress : this.mPinProgress)) / ((float) getMax()))) + this.mBigCircleRadius;
            int saveLayer = canvas.saveLayer(0.0f, 0.0f, (float) getWidth(), (float) getHeight(), null, 31);
            float f2 = (float) height;
            canvas.drawRoundRect(this.mBigCircleRadius - this.mCircleRadius, f2 - (this.mLineWidth / 2.0f), this.mCircleRadius + (((float) getWidth()) - this.mBigCircleRadius), f2 + (this.mLineWidth / 2.0f), 1.0f, 1.0f, this.mBackgroundPaint);
            int i = -1;
            if (this.mIsRTL) {
                this.mPinProgressRectF.left = (((float) getWidth()) - this.mBigCircleRadius) + this.mCircleRadius;
                if (this.mCenterTwoWayMode) {
                    if (f <= ((float) this.mPinProgress)) {
                        this.mPinProgressRectF.left = this.mPinRadius + max2;
                    } else if (f > ((float) this.mPinProgress)) {
                        this.mPinProgressRectF.left = max2 - this.mPinRadius;
                    }
                    this.mPinPointPaint.setColor(-1);
                    String str = TAG;
                    StringBuilder sb = new StringBuilder();
                    sb.append("current progress:");
                    sb.append(f);
                    Log.d(str, sb.toString());
                } else {
                    if (f <= ((float) this.mPinProgress)) {
                        paint3 = this.mPinPointPaint;
                    } else {
                        paint3 = this.mPinPointPaint;
                        i = TintColor.tintColor();
                    }
                    paint3.setColor(i);
                }
                this.mPinProgressRectF.right = this.mBigCircleRadius + (((((float) getMax()) - f) * this.mAvaliableWidth) / ((float) getMax()));
                this.mPinProgressRectF.top = f2 - (this.mLineWidth / 2.0f);
                this.mPinProgressRectF.bottom = (this.mLineWidth / 2.0f) + f2;
                canvas2.drawRoundRect(this.mPinProgressRectF, 0.0f, 0.0f, this.mPinProgressPaint);
                if (this.mPinProgress != 0) {
                    canvas2.drawCircle(max2, f2, this.mPinRadius, this.mPinPointPaint);
                }
                if (this.mTouchRect == null) {
                    this.mTouchRect = new Rect();
                }
                this.mTouchRect.set((int) (((double) this.mBigCircleRadius) + Math.floor((double) (((((((float) getMax()) - f) * this.mAvaliableWidth) / ((float) getMax())) - this.mCurrentCircleRadius) - ((float) this.mClearGap)))), 0, (int) (((double) this.mBigCircleRadius) + Math.ceil((double) ((((((float) getMax()) - f) * this.mAvaliableWidth) / ((float) getMax())) + this.mCurrentCircleRadius + ((float) this.mClearGap)))), getHeight());
                canvas.drawRect((float) ((int) (((double) this.mBigCircleRadius) + Math.floor((double) (((((((float) getMax()) - f) * this.mAvaliableWidth) / ((float) getMax())) - this.mCurrentCircleRadius) - ((float) this.mClearGap))))), f2 - this.mCurrentCircleRadius, (float) ((int) (((double) this.mBigCircleRadius) + Math.ceil((double) ((((((float) getMax()) - f) * this.mAvaliableWidth) / ((float) getMax())) + this.mCurrentCircleRadius + ((float) this.mClearGap))))), f2 + this.mCurrentCircleRadius, this.mClearPaint);
                max = this.mBigCircleRadius + (((((float) getMax()) - f) * this.mAvaliableWidth) / ((float) getMax()));
                strokeWidth = this.mCurrentCircleRadius - (this.mCirclePaint.getStrokeWidth() / 2.0f);
                paint = this.mCirclePaint;
            } else {
                this.mPinProgressRectF.left = this.mBigCircleRadius - this.mCircleRadius;
                if (this.mCenterTwoWayMode) {
                    if (f <= ((float) this.mPinProgress)) {
                        this.mPinProgressRectF.left = max2 - this.mPinRadius;
                    } else if (f > ((float) this.mPinProgress)) {
                        this.mPinProgressRectF.left = this.mPinRadius + max2;
                    }
                    this.mPinPointPaint.setColor(-1);
                    String str2 = TAG;
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append("current progress:");
                    sb2.append(f);
                    Log.d(str2, sb2.toString());
                } else {
                    if (f <= ((float) this.mPinProgress)) {
                        paint2 = this.mPinPointPaint;
                    } else {
                        paint2 = this.mPinPointPaint;
                        i = TintColor.tintColor();
                    }
                    paint2.setColor(i);
                }
                this.mPinProgressRectF.right = this.mBigCircleRadius + ((this.mAvaliableWidth * f) / ((float) getMax()));
                this.mPinProgressRectF.top = f2 - (this.mLineWidth / 2.0f);
                this.mPinProgressRectF.bottom = (this.mLineWidth / 2.0f) + f2;
                canvas2.drawRoundRect(this.mPinProgressRectF, 0.0f, 0.0f, this.mPinProgressPaint);
                if (this.mPinProgress != 0) {
                    canvas2.drawCircle(max2, f2, this.mPinRadius, this.mPinPointPaint);
                }
                if (this.mTouchRect == null) {
                    this.mTouchRect = new Rect();
                }
                this.mTouchRect.set((int) (((double) this.mBigCircleRadius) + Math.floor((double) ((((this.mAvaliableWidth * f) / ((float) getMax())) - this.mCurrentCircleRadius) - ((float) this.mClearGap)))), 0, (int) (((double) this.mBigCircleRadius) + Math.ceil((double) (((this.mAvaliableWidth * f) / ((float) getMax())) + this.mCurrentCircleRadius + ((float) this.mClearGap)))), getHeight());
                canvas.drawRect((float) ((int) (((double) this.mBigCircleRadius) + Math.floor((double) ((((this.mAvaliableWidth * f) / ((float) getMax())) - this.mCurrentCircleRadius) - ((float) this.mClearGap))))), f2 - this.mCurrentCircleRadius, (float) ((int) (((double) this.mBigCircleRadius) + Math.ceil((double) (((this.mAvaliableWidth * f) / ((float) getMax())) + this.mCurrentCircleRadius + ((float) this.mClearGap))))), f2 + this.mCurrentCircleRadius, this.mClearPaint);
                max = this.mBigCircleRadius + ((f * this.mAvaliableWidth) / ((float) getMax()));
                strokeWidth = this.mCurrentCircleRadius - (this.mCirclePaint.getStrokeWidth() / 2.0f);
                paint = this.mCirclePaint;
            }
            canvas2.drawCircle(max, f2, strokeWidth, paint);
            canvas2.restoreToCount(saveLayer);
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:9:0x0026, code lost:
        if (contains(r2.mPinProgress, r4, r2.mUserLastSeekPoint) != false) goto L_0x0008;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
        int i2 = this.mPinProgress;
        if (i2 != 0) {
            if (!z || i != i2) {
                if (this.mUserSeek) {
                }
            }
            ViberatorContext.getInstance(getContext().getApplicationContext()).performModeSwitch();
            this.mUserLastSeekPoint = i;
        }
        if (this.mSeekBarCompatChangeListener != null) {
            if (this.mCenterTwoWayMode) {
                i -= this.mPinProgress;
            }
            this.mSeekBarCompatChangeListener.onProgressChanged(seekBar, i, z);
        }
    }

    public void onStartTrackingTouch(SeekBar seekBar) {
        OnSeekBarCompatChangeListener onSeekBarCompatChangeListener = this.mSeekBarCompatChangeListener;
        if (onSeekBarCompatChangeListener != null) {
            onSeekBarCompatChangeListener.onStartTrackingTouch(seekBar);
        }
    }

    public void onStopTrackingTouch(SeekBar seekBar) {
        OnSeekBarCompatChangeListener onSeekBarCompatChangeListener = this.mSeekBarCompatChangeListener;
        if (onSeekBarCompatChangeListener != null) {
            onSeekBarCompatChangeListener.onStopTrackingTouch(seekBar);
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:5:0x000e, code lost:
        if (r0 != 3) goto L_0x005c;
     */
    /* JADX WARNING: Removed duplicated region for block: B:19:0x0060  */
    /* JADX WARNING: Removed duplicated region for block: B:21:0x0065  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean onTouch(View view, MotionEvent motionEvent) {
        OnSeekBarCompatTouchListener onSeekBarCompatTouchListener;
        int action = motionEvent.getAction();
        if (action != 0) {
            if (action != 1) {
                if (action == 2) {
                    if (this.mDownX == motionEvent.getX()) {
                        return true;
                    }
                    if (!this.mUserSeek && this.mUserLastSeekPoint != getProgress()) {
                        this.mUserLastSeekPoint = getProgress();
                        this.mUserSeek = true;
                    }
                }
            }
            this.mUserSeek = false;
            startScroll(getNextProgress(motionEvent.getX()));
            startAnimator(false);
            onSeekBarCompatTouchListener = this.mOnSeekBarCompatTouchListener;
            if (onSeekBarCompatTouchListener == null) {
                return onSeekBarCompatTouchListener.onTouch(view, motionEvent);
            }
            int action2 = motionEvent.getAction();
            if (action2 == 0) {
                if (!getTouchRect().contains((int) motionEvent.getX(), (int) motionEvent.getY())) {
                    return true;
                }
            } else if (!(action2 == 1 || action2 == 2)) {
                return false;
            }
            setProgress(getNextProgress(motionEvent.getX()), true);
            return true;
        }
        startAnimator(true);
        this.mUserLastSeekPoint = getProgress();
        this.mDownX = motionEvent.getX();
        startScroll(getNextProgress(motionEvent.getX()));
        onSeekBarCompatTouchListener = this.mOnSeekBarCompatTouchListener;
        if (onSeekBarCompatTouchListener == null) {
        }
    }

    public void setCenterTwoWayMode(boolean z) {
        this.mCenterTwoWayMode = z;
        if (this.mCenterTwoWayMode) {
            invalidate();
        }
    }

    public void setOnSeekBarChangeListener(OnSeekBarCompatChangeListener onSeekBarCompatChangeListener) {
        this.mSeekBarCompatChangeListener = onSeekBarCompatChangeListener;
    }

    public void setOnSeekBarCompatTouchListener(OnSeekBarCompatTouchListener onSeekBarCompatTouchListener) {
        this.mOnSeekBarCompatTouchListener = onSeekBarCompatTouchListener;
    }

    public synchronized void setProgress(int i) {
        setProgress(i, false);
    }

    public synchronized void setProgress(int i, boolean z) {
        if (!z) {
            if (this.mStyle != null) {
                this.mStyle.cancel();
            }
            this.mVisualProgress = (float) i;
            invalidate();
        }
        if (this.mCenterTwoWayMode) {
            i += this.mPinProgress;
        }
        if (!z) {
            super.setProgress(i);
        } else {
            try {
                Method declaredMethod = ProgressBar.class.getDeclaredMethod("setProgressInternal", new Class[]{Integer.TYPE, Boolean.TYPE, Boolean.TYPE});
                declaredMethod.setAccessible(true);
                declaredMethod.invoke(this, new Object[]{Integer.valueOf(i), Boolean.valueOf(true), Boolean.valueOf(false)});
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e2) {
                e2.printStackTrace();
            } catch (InvocationTargetException e3) {
                e3.printStackTrace();
            }
        }
    }

    public void setSeekBarPinProgress(int i) {
        int max = getMax();
        int min = getMin();
        if (i == max || i == min) {
            this.mPinProgress = 0;
        } else {
            this.mPinProgress = i;
        }
    }

    public void setVisibility(int i) {
        super.setVisibility(i);
        if (i == 0) {
            Paint paint = this.mPinProgressPaint;
            if (paint != null) {
                paint.setColor(TintColor.tintColor());
            }
        }
    }
}
