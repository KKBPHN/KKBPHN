package miui.widget;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import miui.animation.physics.DynamicAnimation;
import miui.animation.physics.DynamicAnimation.OnAnimationEndListener;
import miui.animation.physics.SpringAnimation;
import miui.animation.property.FloatProperty;

public class SeekBarContainerLinearLayout extends LinearLayout {
    public static final String FROM_TOUCH_ANIM = "from_touch_anim";
    private int mCurrentProgress;
    private boolean mIsInClickableRect = false;
    private boolean mIsInThumb = false;
    private float mProgress;
    private SpringAnimation mProgressAnim;
    private FloatProperty mProgressProperty = new FloatProperty("Progress") {
        public float getValue(SeekBarContainerLinearLayout seekBarContainerLinearLayout) {
            return SeekBarContainerLinearLayout.this.getProgress();
        }

        public void setValue(SeekBarContainerLinearLayout seekBarContainerLinearLayout, float f) {
            SeekBarContainerLinearLayout.this.setProgress(Math.round(f));
        }
    };
    /* access modifiers changed from: private */
    public SeekBar mSeekBar;

    public SeekBarContainerLinearLayout(Context context) {
        super(context);
        initAnim();
    }

    public SeekBarContainerLinearLayout(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        initAnim();
    }

    public SeekBarContainerLinearLayout(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        initAnim();
    }

    private void attemptClaimDrag() {
        ViewParent parent = getParent();
        if (parent != null) {
            parent.requestDisallowInterceptTouchEvent(true);
        }
    }

    private int getThumbLeft() {
        Drawable thumb = this.mSeekBar.getThumb();
        return ((thumb.getBounds().left + (this.mSeekBar.getPaddingLeft() - this.mSeekBar.getThumbOffset())) - (thumb.getIntrinsicWidth() / 2)) + this.mSeekBar.getLeft();
    }

    private int getThumbRight() {
        Drawable thumb = this.mSeekBar.getThumb();
        return thumb.getBounds().right + (this.mSeekBar.getPaddingLeft() - this.mSeekBar.getThumbOffset()) + (thumb.getIntrinsicWidth() / 2) + this.mSeekBar.getLeft();
    }

    private void initAnim() {
        this.mProgressAnim = new SpringAnimation(this, this.mProgressProperty, -1.0f);
        this.mProgressAnim.getSpring().setStiffness(986.96f);
        this.mProgressAnim.getSpring().setDampingRatio(0.6f);
        this.mProgressAnim.setMinimumVisibleChange(0.002f);
        this.mProgressAnim.addEndListener(new OnAnimationEndListener() {
            public void onAnimationEnd(DynamicAnimation dynamicAnimation, boolean z, float f, float f2) {
                SeekBarContainerLinearLayout.this.mSeekBar.setTag(null);
            }
        });
    }

    private boolean isInExtendThumb(float f, float f2, int i) {
        Rect rect = new Rect();
        this.mSeekBar.getHitRect(rect);
        return f > ((float) (getThumbLeft() - i)) && f < ((float) (getThumbRight() + i)) && f2 > ((float) (rect.top - i)) && f2 < ((float) (rect.bottom + i));
    }

    private boolean isInThumbXRange(float f, int i) {
        boolean z = false;
        if (this.mSeekBar.getThumb() == null) {
            return false;
        }
        int thumbLeft = getThumbLeft();
        int thumbRight = getThumbRight();
        if (f > ((float) (thumbLeft - i)) && f < ((float) (thumbRight + i))) {
            z = true;
        }
        return z;
    }

    private void onTouchSeekBar(MotionEvent motionEvent) {
        motionEvent.setLocation(motionEvent.getX() - ((float) this.mSeekBar.getLeft()), motionEvent.getY());
        this.mSeekBar.onTouchEvent(motionEvent);
    }

    private void releaseClaimDrag() {
        ViewParent parent = getParent();
        if (parent != null) {
            parent.requestDisallowInterceptTouchEvent(false);
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:18:0x009b  */
    /* JADX WARNING: Removed duplicated region for block: B:19:0x00ac  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void trackTouchEvent(MotionEvent motionEvent) {
        if (this.mIsInThumb) {
            onTouchSeekBar(motionEvent);
            return;
        }
        float x = motionEvent.getX();
        int width = (this.mSeekBar.getWidth() - this.mSeekBar.getPaddingLeft()) - this.mSeekBar.getPaddingRight();
        float f = 1.0f;
        if (getLayoutDirection() == 1) {
            int left = this.mSeekBar.getLeft() + this.mSeekBar.getPaddingLeft();
            if (x <= ((float) (this.mSeekBar.getRight() - this.mSeekBar.getPaddingRight()))) {
                float f2 = (float) left;
                if (x >= f2) {
                    float f3 = (float) width;
                    f = ((f3 - x) + f2) / f3;
                }
                float range = 0.0f + (f * ((float) ProgressBarHelper.getRange(this.mSeekBar))) + ((float) ProgressBarHelper.getMin(this.mSeekBar));
                if (isInThumbXRange(x, 0)) {
                    this.mProgressAnim.cancel();
                    this.mSeekBar.setProgress(Math.round(range));
                    this.mIsInThumb = true;
                    return;
                }
                this.mProgressAnim.getSpring().setFinalPosition(range);
                if (!this.mProgressAnim.isRunning()) {
                    this.mProgressAnim.start();
                    this.mSeekBar.setTag(FROM_TOUCH_ANIM);
                    return;
                }
                return;
            }
        } else {
            int right = this.mSeekBar.getRight() - this.mSeekBar.getPaddingRight();
            float left2 = (float) (this.mSeekBar.getLeft() + this.mSeekBar.getPaddingLeft());
            if (x >= left2) {
                if (x <= ((float) right)) {
                    f = (x - left2) / ((float) width);
                }
                float range2 = 0.0f + (f * ((float) ProgressBarHelper.getRange(this.mSeekBar))) + ((float) ProgressBarHelper.getMin(this.mSeekBar));
                if (isInThumbXRange(x, 0)) {
                }
            }
        }
        f = 0.0f;
        float range22 = 0.0f + (f * ((float) ProgressBarHelper.getRange(this.mSeekBar))) + ((float) ProgressBarHelper.getMin(this.mSeekBar));
        if (isInThumbXRange(x, 0)) {
        }
    }

    public float getProgress() {
        return this.mProgress;
    }

    /* access modifiers changed from: protected */
    public void onFinishInflate() {
        super.onFinishInflate();
        int i = 0;
        while (true) {
            if (i >= getChildCount()) {
                break;
            }
            View childAt = getChildAt(i);
            if (childAt instanceof SeekBar) {
                this.mSeekBar = (SeekBar) childAt;
                break;
            }
            i++;
        }
        if (this.mSeekBar == null) {
            throw new IllegalStateException("fail to get seek bar");
        }
    }

    public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
        Drawable thumb = this.mSeekBar.getThumb();
        if (thumb == null) {
            return false;
        }
        return isInExtendThumb(motionEvent.getX(), motionEvent.getY(), thumb.getIntrinsicWidth() * 2);
    }

    /* access modifiers changed from: protected */
    public void onMeasure(int i, int i2) {
        super.onMeasure(i, i2);
        setParentClipChildren();
    }

    /* JADX WARNING: Code restructure failed: missing block: B:15:0x0035, code lost:
        if (r6.mIsInClickableRect != false) goto L_0x0037;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:7:0x0014, code lost:
        if (r6.mIsInClickableRect == false) goto L_0x00a4;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean onTouchEvent(MotionEvent motionEvent) {
        int action = motionEvent.getAction();
        if (action != 0) {
            if (action != 1) {
                if (action != 2) {
                    if (action == 3) {
                    }
                } else if (this.mIsInClickableRect) {
                    Drawable background = this.mSeekBar.getBackground();
                    if (background != null) {
                        background.setHotspot(motionEvent.getX(), motionEvent.getY());
                    }
                    trackTouchEvent(motionEvent);
                }
            }
            this.mSeekBar.setPressed(false);
            this.mSeekBar.invalidate();
            releaseClaimDrag();
            onTouchSeekBar(motionEvent);
        } else {
            Drawable thumb = this.mSeekBar.getThumb();
            if (thumb != null) {
                float x = motionEvent.getX();
                float y = motionEvent.getY();
                boolean isInExtendThumb = isInExtendThumb(x, y, thumb.getIntrinsicWidth() * 2);
                this.mIsInThumb = isInExtendThumb(x, y, 0);
                if (!isInExtendThumb || !this.mSeekBar.isEnabled()) {
                    this.mIsInClickableRect = false;
                } else {
                    this.mIsInClickableRect = true;
                    this.mSeekBar.setPressed(true);
                    this.mCurrentProgress = this.mSeekBar.getProgress();
                    this.mProgress = (float) this.mCurrentProgress;
                    if (this.mIsInThumb) {
                        onTouchSeekBar(motionEvent);
                    } else {
                        this.mProgressAnim.getSpring().setFinalPosition((float) this.mCurrentProgress);
                    }
                    attemptClaimDrag();
                }
            } else {
                this.mIsInClickableRect = false;
                this.mIsInThumb = false;
            }
        }
        return true;
    }

    public void setParentClipChildren() {
        setClipChildren(false);
        ViewParent parent = getParent();
        if (parent != null && (parent instanceof ViewGroup)) {
            ((ViewGroup) parent).setClipChildren(false);
        }
    }

    public void setProgress(int i) {
        this.mProgress = (float) i;
        this.mSeekBar.setProgress(i);
    }

    public boolean shouldDelayChildPressedState() {
        return true;
    }
}
