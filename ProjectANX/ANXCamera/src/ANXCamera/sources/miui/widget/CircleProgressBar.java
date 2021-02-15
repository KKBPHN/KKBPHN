package miui.widget;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.NinePatchDrawable;
import android.util.AttributeSet;
import android.view.animation.LinearInterpolator;
import android.widget.ProgressBar;
import androidx.core.app.NotificationCompat;
import com.miui.internal.R;
import miui.util.AttributeResolver;

public class CircleProgressBar extends ProgressBar {
    private static final int ALPHA_NEED_DRAW_MIN_VALUE = 10;
    private static final int DEFAULT_FADE_OUT_DURATION = 300;
    private static final int DEFAULT_ROTATE_VELOCITY = 300;
    private Path mArcPath;
    private RectF mArcRect;
    private Bitmap mBitmapForSoftLayer;
    private Canvas mCanvasForSoftLayer;
    private Animator mChangeProgressAnimator;
    private int mCurrentLevel;
    private Drawable[] mLevelsBackDrawable;
    private Drawable[] mLevelsForeDrawable;
    private Drawable[] mLevelsMiddleDrawable;
    private Paint mPaint;
    private int mPrevAlpha;
    private int mPrevLevel;
    private OnProgressChangedListener mProgressChangedListener;
    private int[] mProgressLevels;
    private int mRotateVelocity;
    private Drawable mThumb;

    public interface OnProgressChangedListener {
        void onProgressChanged();
    }

    public CircleProgressBar(Context context) {
        this(context, null);
    }

    public CircleProgressBar(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public CircleProgressBar(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.mArcPath = new Path();
        this.mRotateVelocity = 300;
        setIndeterminate(false);
        int resolveColor = AttributeResolver.resolveColor(context, R.attr.circleProgressBarColor);
        this.mPaint = new Paint();
        this.mPaint.setColor(resolveColor);
    }

    private int calcDuration(int i) {
        return (i * 1000) / this.mRotateVelocity;
    }

    private void drawLayer(Canvas canvas, Drawable drawable, Drawable drawable2, Drawable drawable3, float f, int i) {
        if (drawable != null) {
            drawable.setAlpha(i);
            drawable.draw(canvas);
        }
        if (canvas.isHardwareAccelerated()) {
            canvas.saveLayer((float) drawable3.getBounds().left, (float) drawable3.getBounds().top, (float) drawable3.getBounds().right, (float) drawable3.getBounds().bottom, null, 31);
            this.mPaint.setStyle(Style.STROKE);
            this.mPaint.setStrokeWidth((float) drawable3.getBounds().width());
            this.mArcPath.reset();
            this.mArcPath.addArc(this.mArcRect, -90.0f, f * 360.0f);
            canvas.drawPath(this.mArcPath, this.mPaint);
            this.mPaint.setStyle(Style.FILL);
            this.mPaint.setStrokeWidth(0.0f);
            drawable3.setAlpha(i);
            drawable3.draw(canvas);
            canvas.restore();
        } else {
            if (this.mBitmapForSoftLayer == null) {
                this.mBitmapForSoftLayer = Bitmap.createBitmap(drawable3.getBounds().width(), drawable3.getBounds().height(), Config.ARGB_8888);
                this.mCanvasForSoftLayer = new Canvas(this.mBitmapForSoftLayer);
            }
            this.mBitmapForSoftLayer.eraseColor(0);
            this.mCanvasForSoftLayer.save();
            this.mCanvasForSoftLayer.translate((float) (-drawable3.getBounds().left), (float) (-drawable3.getBounds().top));
            this.mCanvasForSoftLayer.drawArc(this.mArcRect, -90.0f, f * 360.0f, true, this.mPaint);
            drawable3.setAlpha(i);
            drawable3.draw(this.mCanvasForSoftLayer);
            this.mCanvasForSoftLayer.restore();
            canvas.drawBitmap(this.mBitmapForSoftLayer, (float) drawable3.getBounds().left, (float) drawable3.getBounds().top, null);
        }
        Drawable drawable4 = this.mThumb;
        if (drawable4 != null) {
            canvas.save();
            int width = ((getWidth() - getPaddingLeft()) - getPaddingRight()) / 2;
            int height = ((getHeight() - getPaddingTop()) - getPaddingBottom()) / 2;
            int intrinsicWidth = drawable4.getIntrinsicWidth();
            int intrinsicHeight = drawable4.getIntrinsicHeight();
            canvas.rotate((((float) getProgress()) * 360.0f) / ((float) getMax()), (float) width, (float) height);
            int i2 = intrinsicWidth / 2;
            int i3 = intrinsicHeight / 2;
            drawable4.setBounds(width - i2, height - i3, width + i2, height + i3);
            drawable4.draw(canvas);
            canvas.restore();
        }
        if (drawable2 != null) {
            drawable2.setAlpha(i);
            drawable2.draw(canvas);
        }
    }

    private Drawable getBackDrawable(int i) {
        Drawable[] drawableArr = this.mLevelsBackDrawable;
        if (drawableArr == null) {
            return null;
        }
        return drawableArr[i];
    }

    private Drawable[] getDrawables(int[] iArr) {
        if (iArr == null) {
            return null;
        }
        Resources resources = getContext().getResources();
        Drawable[] drawableArr = new Drawable[iArr.length];
        for (int i = 0; i < iArr.length; i++) {
            drawableArr[i] = resources.getDrawable(iArr[i]);
            drawableArr[i].setBounds(0, 0, drawableArr[i].getIntrinsicWidth(), drawableArr[i].getIntrinsicHeight());
        }
        return drawableArr;
    }

    private Drawable getForeDrawable(int i) {
        Drawable[] drawableArr = this.mLevelsForeDrawable;
        if (drawableArr == null) {
            return null;
        }
        return drawableArr[i];
    }

    private int getIntrinsicHeight() {
        int intrinsicHeight = getMiddleDrawable(0).getIntrinsicHeight();
        Drawable[] drawableArr = this.mLevelsForeDrawable;
        if (drawableArr != null) {
            intrinsicHeight = Math.max(intrinsicHeight, drawableArr[0].getIntrinsicHeight());
        }
        Drawable[] drawableArr2 = this.mLevelsBackDrawable;
        return drawableArr2 != null ? Math.max(intrinsicHeight, drawableArr2[0].getIntrinsicHeight()) : intrinsicHeight;
    }

    private int getIntrinsicWidth() {
        int intrinsicWidth = getMiddleDrawable(0).getIntrinsicWidth();
        Drawable[] drawableArr = this.mLevelsForeDrawable;
        if (drawableArr != null) {
            intrinsicWidth = Math.max(intrinsicWidth, drawableArr[0].getIntrinsicWidth());
        }
        Drawable[] drawableArr2 = this.mLevelsBackDrawable;
        return drawableArr2 != null ? Math.max(intrinsicWidth, drawableArr2[0].getIntrinsicWidth()) : intrinsicWidth;
    }

    private Drawable getMiddleDrawable(int i) {
        Drawable[] drawableArr = this.mLevelsMiddleDrawable;
        if (drawableArr == null) {
            return null;
        }
        return drawableArr[i];
    }

    private float getRate() {
        return ((float) getProgress()) / ((float) getMax());
    }

    /* access modifiers changed from: protected */
    public void drawableStateChanged() {
        super.drawableStateChanged();
        int progressLevelCount = getProgressLevelCount();
        for (int i = 0; i < progressLevelCount; i++) {
            Drawable[] drawableArr = this.mLevelsBackDrawable;
            if (drawableArr != null) {
                drawableArr[i].setState(getDrawableState());
            }
            Drawable[] drawableArr2 = this.mLevelsMiddleDrawable;
            if (drawableArr2 != null) {
                drawableArr2[i].setState(getDrawableState());
            }
            Drawable[] drawableArr3 = this.mLevelsForeDrawable;
            if (drawableArr3 != null) {
                drawableArr3[i].setState(getDrawableState());
            }
        }
        invalidate();
    }

    public int getPrevAlpha() {
        return this.mPrevAlpha;
    }

    public int getProgressLevelCount() {
        int[] iArr = this.mProgressLevels;
        if (iArr == null) {
            return 1;
        }
        return 1 + iArr.length;
    }

    /* access modifiers changed from: protected */
    public synchronized void onDraw(Canvas canvas) {
        drawLayer(canvas, getBackDrawable(this.mCurrentLevel), getForeDrawable(this.mCurrentLevel), getMiddleDrawable(this.mCurrentLevel), getRate(), 255 - this.mPrevAlpha);
        if (this.mPrevAlpha >= 10) {
            drawLayer(canvas, getBackDrawable(this.mPrevLevel), getForeDrawable(this.mPrevLevel), getMiddleDrawable(this.mPrevLevel), getRate(), this.mPrevAlpha);
        }
    }

    /* access modifiers changed from: protected */
    public synchronized void onMeasure(int i, int i2) {
        setMeasuredDimension(getIntrinsicWidth(), getIntrinsicHeight());
    }

    public void setDrawablesForLevels(int[] iArr, int[] iArr2, int[] iArr3) {
        setDrawablesForLevels(getDrawables(iArr), getDrawables(iArr2), getDrawables(iArr3));
    }

    public void setDrawablesForLevels(Drawable[] drawableArr, Drawable[] drawableArr2, Drawable[] drawableArr3) {
        Paint paint;
        PorterDuffXfermode porterDuffXfermode;
        this.mLevelsBackDrawable = drawableArr;
        this.mLevelsMiddleDrawable = drawableArr2;
        this.mLevelsForeDrawable = drawableArr3;
        if (drawableArr != null) {
            for (Drawable mutate : drawableArr) {
                mutate.mutate();
            }
        }
        if (drawableArr2 != null) {
            for (Drawable mutate2 : drawableArr2) {
                mutate2.mutate();
            }
        }
        if (drawableArr3 != null) {
            for (Drawable mutate3 : drawableArr3) {
                mutate3.mutate();
            }
        }
        if (drawableArr2 != null) {
            for (BitmapDrawable bitmapDrawable : drawableArr2) {
                if (bitmapDrawable instanceof BitmapDrawable) {
                    paint = bitmapDrawable.getPaint();
                    porterDuffXfermode = new PorterDuffXfermode(Mode.SRC_IN);
                } else if (bitmapDrawable instanceof NinePatchDrawable) {
                    paint = ((NinePatchDrawable) bitmapDrawable).getPaint();
                    porterDuffXfermode = new PorterDuffXfermode(Mode.SRC_IN);
                } else {
                    throw new IllegalArgumentException("'middles' must a bitmap or nine patch drawable.");
                }
                paint.setXfermode(porterDuffXfermode);
            }
            this.mArcRect = new RectF((float) (drawableArr2[0].getBounds().left - 5), (float) (drawableArr2[0].getBounds().top - 5), (float) (drawableArr2[0].getBounds().right + 5), (float) (drawableArr2[0].getBounds().bottom + 5));
        }
    }

    public void setOnProgressChangedListener(OnProgressChangedListener onProgressChangedListener) {
        this.mProgressChangedListener = onProgressChangedListener;
    }

    public void setPrevAlpha(int i) {
        this.mPrevAlpha = i;
        invalidate();
    }

    public synchronized void setProgress(int i) {
        int i2;
        super.setProgress(i);
        if (this.mProgressLevels == null) {
            i2 = 0;
        } else {
            i2 = this.mProgressLevels.length;
            int i3 = 0;
            while (true) {
                if (i3 >= i2) {
                    i3 = -1;
                    break;
                } else if (i < this.mProgressLevels[i3]) {
                    break;
                } else {
                    i3++;
                }
            }
            if (i3 != -1) {
                i2 = i3;
            }
        }
        if (i2 != this.mCurrentLevel) {
            this.mPrevLevel = this.mCurrentLevel;
            this.mCurrentLevel = i2;
            setPrevAlpha(255);
            ObjectAnimator ofInt = ObjectAnimator.ofInt(this, "prevAlpha", new int[]{0});
            ofInt.setDuration(300);
            ofInt.setInterpolator(new LinearInterpolator());
            ofInt.start();
        }
        if (this.mProgressChangedListener != null) {
            this.mProgressChangedListener.onProgressChanged();
        }
    }

    public void setProgressByAnimator(int i) {
        setProgressByAnimator(i, null);
    }

    public void setProgressByAnimator(int i, AnimatorListener animatorListener) {
        stopProgressAnimator();
        int abs = Math.abs((int) ((((float) (i - getProgress())) / ((float) getMax())) * 360.0f));
        this.mChangeProgressAnimator = ObjectAnimator.ofInt(this, NotificationCompat.CATEGORY_PROGRESS, new int[]{i});
        this.mChangeProgressAnimator.setDuration((long) calcDuration(abs));
        this.mChangeProgressAnimator.setInterpolator(getInterpolator());
        if (animatorListener != null) {
            this.mChangeProgressAnimator.addListener(animatorListener);
        }
        this.mChangeProgressAnimator.start();
    }

    public void setProgressLevels(int[] iArr) {
        this.mProgressLevels = iArr;
    }

    public void setRotateVelocity(int i) {
        this.mRotateVelocity = i;
    }

    public void setThumb(int i) {
        setThumb(getResources().getDrawable(i));
    }

    public void setThumb(Drawable drawable) {
        this.mThumb = drawable;
    }

    public void stopProgressAnimator() {
        Animator animator = this.mChangeProgressAnimator;
        if (animator != null && animator.isRunning()) {
            this.mChangeProgressAnimator.cancel();
        }
    }
}
