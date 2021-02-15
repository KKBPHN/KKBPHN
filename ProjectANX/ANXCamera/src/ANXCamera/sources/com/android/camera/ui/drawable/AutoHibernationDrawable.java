package com.android.camera.ui.drawable;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.drawable.Drawable;
import android.view.animation.LinearInterpolator;
import androidx.annotation.Nullable;
import com.android.camera.R;

public class AutoHibernationDrawable extends Drawable {
    private static int ALPHA_HINT = 102;
    private static int ALPHA_OUTSTANDING = 255;
    public float mBaseRadius;
    private Context mContext;
    private long mDuration = 0;
    boolean mIsClockwise = true;
    private int mLastDrawAlpha = ALPHA_OUTSTANDING;
    private float mLineHeight;
    private float mLineWidth;
    public float mMiddleX;
    public float mMiddleY;
    /* access modifiers changed from: private */
    public boolean mOnce = true;
    public Paint mPaint = new Paint();
    private ValueAnimator mPreTimeAnimator;
    private float mStart = 0.0f;
    public float mTimeAngle = 0.0f;
    private ValueAnimator mTimeAnimator;

    public AutoHibernationDrawable(Context context) {
        this.mContext = context;
        this.mLineWidth = context.getResources().getDimension(R.dimen.auto_hibernation_recording_time_line_width);
        this.mLineHeight = context.getResources().getDimension(R.dimen.auto_hibernation_recording_time_line_height);
        this.mPaint.setAntiAlias(true);
        this.mPaint.setStyle(Style.STROKE);
        this.mPaint.setStrokeWidth(this.mLineWidth);
        this.mPaint.setColor(-1);
    }

    public void cancelAnimation() {
        ValueAnimator valueAnimator = this.mPreTimeAnimator;
        if (valueAnimator != null) {
            valueAnimator.removeAllUpdateListeners();
            this.mPreTimeAnimator.removeAllListeners();
            this.mPreTimeAnimator.cancel();
            this.mPreTimeAnimator = null;
        }
        ValueAnimator valueAnimator2 = this.mTimeAnimator;
        if (valueAnimator2 != null) {
            this.mIsClockwise = true;
            this.mTimeAngle = 0.0f;
            valueAnimator2.removeAllUpdateListeners();
            this.mTimeAnimator.removeAllListeners();
            this.mTimeAnimator.cancel();
            this.mTimeAnimator = null;
        }
    }

    public void draw(Canvas canvas) {
        if (canvas != null) {
            canvas.save();
            float f = this.mBaseRadius;
            int i = 0;
            float f2 = 0.0f;
            while (i < 180) {
                canvas.save();
                f2 += i == 0 ? 0.0f : 2.0f;
                canvas.rotate(f2, this.mMiddleX, this.mMiddleY);
                int i2 = ALPHA_OUTSTANDING;
                if (f2 < this.mTimeAngle) {
                    if (!this.mIsClockwise) {
                        i2 = ALPHA_HINT;
                    }
                    float f3 = this.mTimeAngle;
                    if (f3 > 350.0f && f3 < 355.0f) {
                        this.mLastDrawAlpha = i2;
                    }
                    if (this.mTimeAngle > 355.0f) {
                        i2 = this.mLastDrawAlpha;
                    }
                } else if (this.mIsClockwise) {
                    i2 = ALPHA_HINT;
                }
                this.mPaint.setAlpha(i2);
                float f4 = this.mMiddleX;
                float f5 = this.mMiddleY;
                canvas.drawLine(f4, f5 - f, f4, (f5 - f) + this.mLineHeight, this.mPaint);
                canvas.restore();
                i++;
            }
            canvas.restore();
        }
    }

    public int getOpacity() {
        return -1;
    }

    public void reset() {
        this.mTimeAngle = 0.0f;
        this.mIsClockwise = true;
        invalidateSelf();
    }

    public void setAlpha(int i) {
    }

    public void setColorFilter(@Nullable ColorFilter colorFilter) {
    }

    public void setWidthHeight(float f, float f2) {
        this.mMiddleX = f / 2.0f;
        this.mMiddleY = f2 / 2.0f;
        this.mBaseRadius = Math.min(f, f2) / 2.0f;
    }

    public void startRecord(long j, float f, boolean z) {
        ValueAnimator valueAnimator;
        if (j == this.mDuration && f == this.mStart && z == this.mOnce) {
            valueAnimator = this.mTimeAnimator;
            if (valueAnimator == null) {
                return;
            }
        } else {
            if (this.mTimeAnimator != null) {
                cancelAnimation();
            }
            this.mDuration = j;
            this.mStart = f;
            this.mOnce = z;
            this.mTimeAnimator = ValueAnimator.ofFloat(new float[]{f, 1.0f});
            this.mTimeAnimator.setDuration(j);
            this.mTimeAnimator.setInterpolator(new LinearInterpolator());
            this.mTimeAnimator.addUpdateListener(new AnimatorUpdateListener() {
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    float floatValue = ((Float) valueAnimator.getAnimatedValue()).floatValue();
                    AutoHibernationDrawable autoHibernationDrawable = AutoHibernationDrawable.this;
                    autoHibernationDrawable.mTimeAngle = floatValue * 360.0f;
                    autoHibernationDrawable.invalidateSelf();
                }
            });
            this.mTimeAnimator.addListener(new AnimatorListener() {
                public void onAnimationCancel(Animator animator) {
                }

                public void onAnimationEnd(Animator animator) {
                }

                public void onAnimationRepeat(Animator animator) {
                    if (!AutoHibernationDrawable.this.mOnce) {
                        AutoHibernationDrawable autoHibernationDrawable = AutoHibernationDrawable.this;
                        autoHibernationDrawable.mIsClockwise = !autoHibernationDrawable.mIsClockwise;
                    }
                }

                public void onAnimationStart(Animator animator) {
                }
            });
            if (!z) {
                this.mTimeAnimator.setRepeatMode(1);
                this.mTimeAnimator.setRepeatCount(-1);
            }
            valueAnimator = this.mTimeAnimator;
        }
        valueAnimator.start();
    }

    public void startRecordForFastmotion(final int i, float f, boolean z) {
        this.mIsClockwise = z;
        this.mTimeAngle = f;
        this.mOnce = false;
        this.mPreTimeAnimator = ValueAnimator.ofFloat(new float[]{f / 360.0f, 1.0f});
        this.mPreTimeAnimator.setDuration((long) (((360.0f - f) / 360.0f) * ((float) i)));
        this.mPreTimeAnimator.setInterpolator(new LinearInterpolator());
        this.mPreTimeAnimator.addUpdateListener(new AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float floatValue = ((Float) valueAnimator.getAnimatedValue()).floatValue();
                AutoHibernationDrawable autoHibernationDrawable = AutoHibernationDrawable.this;
                autoHibernationDrawable.mTimeAngle = floatValue * 360.0f;
                autoHibernationDrawable.invalidateSelf();
            }
        });
        this.mPreTimeAnimator.addListener(new AnimatorListener() {
            public void onAnimationCancel(Animator animator) {
            }

            public void onAnimationEnd(Animator animator) {
                AutoHibernationDrawable autoHibernationDrawable = AutoHibernationDrawable.this;
                autoHibernationDrawable.mIsClockwise = !autoHibernationDrawable.mIsClockwise;
                autoHibernationDrawable.startRecord((long) i, 0.0f, false);
            }

            public void onAnimationRepeat(Animator animator) {
            }

            public void onAnimationStart(Animator animator) {
            }
        });
        this.mPreTimeAnimator.start();
    }
}
