package com.miui.internal.widget;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.graphics.Region.Op;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewTreeObserver.OnPreDrawListener;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.miui.internal.R;
import miui.widget.GuidePopupWindow;

public class GuidePopupView extends FrameLayout implements OnTouchListener {
    public static final int ARROW_BOTTOM_LEFT_MODE = 7;
    public static final int ARROW_BOTTOM_MODE = 0;
    public static final int ARROW_BOTTOM_RIGHT_MODE = 5;
    public static final int ARROW_LEFT_MODE = 3;
    public static final int ARROW_NONE_MODE = -1;
    public static final int ARROW_RIGHT_MODE = 2;
    public static final int ARROW_TOP_LEFT_MODE = 4;
    public static final int ARROW_TOP_MODE = 1;
    public static final int ARROW_TOP_RIGHT_MODE = 6;
    private View mAnchor;
    private int mAnchorHeight;
    private int mAnchorLocationX;
    private int mAnchorLocationY;
    private int mAnchorWidth;
    /* access modifiers changed from: private */
    public ObjectAnimator mAnimator;
    private int mArrowMode;
    private int mColorBackground;
    private Context mContext;
    private int mDefaultOffset;
    /* access modifiers changed from: private */
    public GuidePopupWindow mGuidePopupWindow;
    private AnimatorListener mHideAnimatorListener;
    /* access modifiers changed from: private */
    public boolean mIsDismissing;
    private boolean mIsMirrored;
    private float mLineLength;
    private int mMinBorder;
    private LinearLayout mMirroredTextGroup;
    private int mOffsetX;
    private int mOffsetY;
    private final Paint mPaint;
    /* access modifiers changed from: private */
    public AnimatorListener mShowAnimatorListener;
    private float mStartPointRadius;
    private float mTextCircleRadius;
    private ColorStateList mTextColor;
    private LinearLayout mTextGroup;
    private int mTextSize;
    private OnTouchListener mTouchInterceptor;
    private boolean mUseDefaultOffset;

    class WrapperOnClickListener implements OnClickListener {
        public OnClickListener mOnClickListener;

        WrapperOnClickListener() {
        }

        public void onClick(View view) {
            OnClickListener onClickListener = this.mOnClickListener;
            if (onClickListener != null) {
                onClickListener.onClick(view);
            }
            GuidePopupView.this.mGuidePopupWindow.dismiss(true);
        }

        public void setOnClickListener(OnClickListener onClickListener) {
            this.mOnClickListener = onClickListener;
        }
    }

    public GuidePopupView(Context context) {
        this(context, null);
    }

    public GuidePopupView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, R.attr.guidePopupViewStyle);
    }

    public GuidePopupView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.mUseDefaultOffset = true;
        this.mTextColor = null;
        this.mPaint = new Paint();
        this.mShowAnimatorListener = new AnimatorListenerAdapter() {
            private boolean mCancel;

            public void onAnimationCancel(Animator animator) {
                this.mCancel = true;
            }

            public void onAnimationEnd(Animator animator) {
                if (!this.mCancel) {
                    GuidePopupView.this.mAnimator = null;
                }
            }

            public void onAnimationStart(Animator animator) {
                this.mCancel = false;
            }
        };
        this.mHideAnimatorListener = new AnimatorListenerAdapter() {
            private boolean mCancel;

            public void onAnimationCancel(Animator animator) {
                this.mCancel = true;
            }

            public void onAnimationEnd(Animator animator) {
                if (!this.mCancel) {
                    GuidePopupView.this.mIsDismissing = false;
                    GuidePopupView.this.mAnimator = null;
                    GuidePopupView.this.mGuidePopupWindow.dismiss();
                    GuidePopupView.this.setArrowMode(-1);
                }
            }

            public void onAnimationStart(Animator animator) {
                this.mCancel = false;
                GuidePopupView.this.mIsDismissing = true;
            }
        };
        this.mArrowMode = -1;
        this.mContext = context;
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R.styleable.GuidePopupView, i, 0);
        this.mStartPointRadius = obtainStyledAttributes.getDimension(R.styleable.GuidePopupView_startPointRadius, 0.0f);
        this.mLineLength = obtainStyledAttributes.getDimension(R.styleable.GuidePopupView_lineLength, 0.0f);
        this.mTextCircleRadius = obtainStyledAttributes.getDimension(R.styleable.GuidePopupView_textCircleRadius, 0.0f);
        this.mColorBackground = obtainStyledAttributes.getColor(R.styleable.GuidePopupView_android_colorBackground, 0);
        this.mPaint.setColor(obtainStyledAttributes.getColor(R.styleable.GuidePopupView_paintColor, -1));
        this.mTextSize = obtainStyledAttributes.getDimensionPixelSize(R.styleable.GuidePopupView_android_textSize, 15);
        this.mTextColor = obtainStyledAttributes.getColorStateList(R.styleable.GuidePopupView_android_textColor);
        obtainStyledAttributes.recycle();
        this.mMinBorder = (int) (this.mLineLength + (this.mTextCircleRadius * 2.5f));
    }

    /* JADX WARNING: Code restructure failed: missing block: B:17:0x0058, code lost:
        if (((float) (r1 - r4)) < r3) goto L_0x0081;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:22:0x0068, code lost:
        if (((float) (r1 - r4)) < r2) goto L_0x0089;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:26:0x0077, code lost:
        if (((float) (r0 - r6)) < r3) goto L_0x0062;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:31:0x0087, code lost:
        if (((float) (r0 - r6)) < r2) goto L_0x0089;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:32:0x0089, code lost:
        r2 = 5;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void adjustArrowMode() {
        int width = getWidth();
        int height = getHeight();
        int i = 4;
        int i2 = this.mAnchorLocationY;
        int i3 = 0;
        int i4 = height - i2;
        int i5 = this.mAnchorHeight;
        int i6 = this.mAnchorLocationX;
        int i7 = width - i6;
        int i8 = this.mAnchorWidth;
        int[] iArr = {i2, i4 - i5, i6, i7 - i8};
        int i9 = i6 + (i8 / 2);
        int i10 = i2 + (i5 / 2);
        int i11 = Integer.MIN_VALUE;
        int i12 = 0;
        while (true) {
            if (i3 >= 4) {
                i3 = i12;
                break;
            } else if (iArr[i3] >= this.mMinBorder) {
                break;
            } else {
                if (iArr[i3] > i11) {
                    i11 = iArr[i3];
                    i12 = i3;
                }
                i3++;
            }
        }
        if (i3 != 0) {
            if (i3 != 1) {
                if (i3 == 2) {
                    float f = (float) i10;
                    float f2 = this.mTextCircleRadius;
                    if (f >= f2) {
                    }
                } else if (i3 == 3) {
                    float f3 = (float) i10;
                    float f4 = this.mTextCircleRadius;
                    if (f3 >= f4) {
                    }
                    setArrowMode(i);
                }
                i = i3;
                setArrowMode(i);
            }
            float f5 = (float) i9;
            float f6 = this.mTextCircleRadius;
            if (f5 >= f6) {
            }
            setArrowMode(i);
            i = 6;
            setArrowMode(i);
        }
        float f7 = (float) i9;
        float f8 = this.mTextCircleRadius;
        if (f7 >= f8) {
        }
        i = 7;
        setArrowMode(i);
    }

    private void arrowLayout() {
        caculateDefaultOffset();
        drawText(this.mArrowMode, this.mTextGroup, this.mOffsetX, this.mOffsetY);
        if (this.mIsMirrored) {
            drawText(getMirroredMode(), this.mMirroredTextGroup, -this.mOffsetX, -this.mOffsetY);
        }
    }

    private void caculateDefaultOffset() {
        if (!this.mUseDefaultOffset) {
            this.mDefaultOffset = 0;
            return;
        }
        int i = this.mAnchorWidth / 2;
        int i2 = this.mAnchorHeight / 2;
        int sqrt = (int) Math.sqrt(Math.pow((double) i, 2.0d) + Math.pow((double) i2, 2.0d));
        int i3 = this.mArrowMode;
        if (i3 == 0 || i3 == 1) {
            this.mDefaultOffset = i2;
        } else if (i3 == 2 || i3 == 3) {
            this.mDefaultOffset = i;
        } else {
            this.mDefaultOffset = sqrt;
        }
    }

    private void drawPopup(Canvas canvas, int i, int i2, int i3) {
        float f;
        this.mPaint.setAntiAlias(true);
        this.mPaint.setStyle(Style.FILL);
        float f2 = (float) (this.mAnchorLocationX + (this.mAnchorWidth / 2) + i2);
        float f3 = (float) (this.mAnchorLocationY + (this.mAnchorHeight / 2) + i3);
        switch (i) {
            case 0:
                f = 180.0f;
                break;
            case 2:
                f = 90.0f;
                break;
            case 3:
                f = -90.0f;
                break;
            case 4:
                f = -45.0f;
                break;
            case 5:
                f = 135.0f;
                break;
            case 6:
                f = 45.0f;
                break;
            case 7:
                f = -135.0f;
                break;
            default:
                f = 0.0f;
                break;
        }
        canvas.save();
        canvas.rotate(f, f2, f3);
        canvas.translate(0.0f, (float) this.mDefaultOffset);
        int save = canvas.save();
        float f4 = f3;
        canvas.clipRect(f2 - 2.0f, f4, f2 + 2.0f, f3 + this.mStartPointRadius, Op.DIFFERENCE);
        canvas.drawCircle(f2, f3, this.mStartPointRadius, this.mPaint);
        canvas.restoreToCount(save);
        this.mPaint.setStyle(Style.STROKE);
        this.mPaint.setStrokeWidth(4.0f);
        canvas.drawLine(f2, f4, f2, f3 + this.mLineLength, this.mPaint);
        float f5 = f3 + this.mLineLength + this.mTextCircleRadius;
        this.mPaint.setStyle(Style.STROKE);
        this.mPaint.setStrokeWidth(4.0f);
        canvas.drawCircle(f2, f5, this.mTextCircleRadius, this.mPaint);
        canvas.restore();
    }

    /* JADX WARNING: Code restructure failed: missing block: B:10:0x0062, code lost:
        if (r7 == 4) goto L_0x0075;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:12:0x0065, code lost:
        if (r7 == 5) goto L_0x0072;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:14:0x0068, code lost:
        if (r7 == 6) goto L_0x0070;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:16:0x006b, code lost:
        if (r7 == 7) goto L_0x006e;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:17:0x006e, code lost:
        r6 = r6 + r2;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:18:0x0070, code lost:
        r6 = r6 - r2;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:19:0x0072, code lost:
        r6 = r6 - r2;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:20:0x0073, code lost:
        r1 = r1 + r0;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:21:0x0075, code lost:
        r6 = r6 + r2;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:22:0x0076, code lost:
        r1 = r1 - r0;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:23:0x0077, code lost:
        r6 = r6 + r9;
        r1 = r1 + r10;
        r8.layout(r6, r1, r8.getMeasuredWidth() + r6, r8.getMeasuredHeight() + r1);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:24:0x0086, code lost:
        return;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:5:0x0022, code lost:
        r6 = (int) (r6 - ((float) (r8.getMeasuredWidth() / 2)));
        r1 = r2 - (r8.getMeasuredHeight() / 2);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:8:0x0049, code lost:
        r1 = (int) (r1 - ((float) (r8.getMeasuredHeight() / 2)));
     */
    /* JADX WARNING: Code restructure failed: missing block: B:9:0x0052, code lost:
        r2 = (int) (((double) r0) * java.lang.Math.sin(0.7853981633974483d));
        r0 = (int) (r0 - ((float) r2));
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void drawText(int i, LinearLayout linearLayout, int i2, int i3) {
        float f;
        float f2;
        float f3 = ((float) this.mDefaultOffset) + this.mLineLength + this.mTextCircleRadius;
        int i4 = this.mAnchorLocationX + (this.mAnchorWidth / 2);
        int i5 = this.mAnchorLocationY + (this.mAnchorHeight / 2);
        int i6 = 0;
        switch (i) {
            case 0:
            case 5:
            case 7:
                i6 = i4 - (linearLayout.getMeasuredWidth() / 2);
                f = ((float) i5) - f3;
                break;
            case 1:
            case 4:
            case 6:
                i6 = i4 - (linearLayout.getMeasuredWidth() / 2);
                f = ((float) i5) + f3;
                break;
            case 2:
                f2 = ((float) i4) - f3;
                break;
            case 3:
                f2 = ((float) i4) + f3;
                break;
            default:
                int i7 = 0;
                break;
        }
    }

    private int getMirroredMode() {
        int i = this.mArrowMode;
        if (i == -1) {
            return -1;
        }
        return i % 2 == 0 ? i + 1 : i - 1;
    }

    public void addGuideTextView(LinearLayout linearLayout, String str) {
        if (!TextUtils.isEmpty(str)) {
            String[] split = str.split("\n");
            if (!(split == null || split.length == 0)) {
                for (String text : split) {
                    TextView textView = (TextView) FrameLayout.inflate(this.mContext, R.layout.guide_popup_text_view, null);
                    textView.setText(text);
                    textView.setTextSize(0, (float) this.mTextSize);
                    ColorStateList colorStateList = this.mTextColor;
                    if (colorStateList != null) {
                        textView.setTextColor(colorStateList);
                    }
                    linearLayout.addView(textView);
                }
            }
        }
    }

    public void animateToDismiss() {
        if (!this.mIsDismissing) {
            ObjectAnimator objectAnimator = this.mAnimator;
            if (objectAnimator != null) {
                objectAnimator.cancel();
            }
            this.mAnimator = ObjectAnimator.ofFloat(this, View.ALPHA, new float[]{0.0f});
            this.mAnimator.setDuration(200);
            this.mAnimator.addListener(this.mHideAnimatorListener);
            this.mAnimator.start();
        }
    }

    public void animateToShow() {
        setAlpha(0.0f);
        invalidate();
        getViewTreeObserver().addOnPreDrawListener(new OnPreDrawListener() {
            public boolean onPreDraw() {
                GuidePopupView.this.getViewTreeObserver().removeOnPreDrawListener(this);
                if (GuidePopupView.this.mAnimator != null) {
                    GuidePopupView.this.mAnimator.cancel();
                }
                GuidePopupView guidePopupView = GuidePopupView.this;
                guidePopupView.mAnimator = ObjectAnimator.ofFloat(guidePopupView, View.ALPHA, new float[]{1.0f});
                GuidePopupView.this.mAnimator.setDuration(300);
                GuidePopupView.this.mAnimator.addListener(GuidePopupView.this.mShowAnimatorListener);
                GuidePopupView.this.mAnimator.start();
                return true;
            }
        });
    }

    public void clearOffset() {
        setOffset(0, 0);
        this.mUseDefaultOffset = true;
    }

    /* access modifiers changed from: protected */
    public void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        canvas.save();
        canvas.translate((float) this.mAnchorLocationX, (float) this.mAnchorLocationY);
        this.mAnchor.setDrawingCacheEnabled(true);
        this.mAnchor.buildDrawingCache();
        canvas.drawBitmap(this.mAnchor.getDrawingCache(), 0.0f, 0.0f, null);
        this.mAnchor.setDrawingCacheEnabled(false);
        canvas.restore();
        drawPopup(canvas, this.mArrowMode, this.mOffsetX, this.mOffsetY);
        if (this.mIsMirrored) {
            drawPopup(canvas, getMirroredMode(), -this.mOffsetX, -this.mOffsetY);
        }
    }

    public int getArrowMode() {
        return this.mArrowMode;
    }

    public int getColorBackground() {
        return this.mColorBackground;
    }

    /* access modifiers changed from: protected */
    public void onFinishInflate() {
        super.onFinishInflate();
        this.mTextGroup = (LinearLayout) findViewById(R.id.text_group);
        this.mMirroredTextGroup = (LinearLayout) findViewById(R.id.mirrored_text_group);
    }

    /* access modifiers changed from: protected */
    public void onLayout(boolean z, int i, int i2, int i3, int i4) {
        if (this.mAnchorWidth == 0 || this.mAnchorHeight == 0) {
            setAnchor(this.mAnchor);
        }
        this.mTextCircleRadius = (float) Math.max(Math.sqrt(Math.pow((double) this.mTextGroup.getMeasuredWidth(), 2.0d) + Math.pow((double) this.mTextGroup.getMeasuredHeight(), 2.0d)) / 2.0d, (double) this.mTextCircleRadius);
        if (this.mIsMirrored) {
            this.mTextCircleRadius = (float) Math.max(Math.sqrt(Math.pow((double) this.mMirroredTextGroup.getMeasuredWidth(), 2.0d) + Math.pow((double) this.mMirroredTextGroup.getMeasuredHeight(), 2.0d)) / 2.0d, (double) this.mTextCircleRadius);
        }
        if (this.mArrowMode == -1) {
            adjustArrowMode();
        } else {
            arrowLayout();
        }
    }

    public boolean onTouch(View view, MotionEvent motionEvent) {
        int x = (int) motionEvent.getX();
        int y = (int) motionEvent.getY();
        int i = this.mAnchorLocationX;
        Rect rect = new Rect(i, this.mAnchorLocationY, this.mAnchor.getWidth() + i, this.mAnchorLocationY + this.mAnchor.getHeight());
        if (motionEvent.getAction() != 0 || !rect.contains(x, y)) {
            this.mGuidePopupWindow.dismiss(true);
            return true;
        }
        this.mAnchor.callOnClick();
        return true;
    }

    public void setAnchor(View view) {
        this.mAnchor = view;
        this.mAnchorWidth = this.mAnchor.getWidth();
        this.mAnchorHeight = this.mAnchor.getHeight();
        int[] iArr = new int[2];
        this.mAnchor.getLocationInWindow(iArr);
        this.mAnchorLocationX = iArr[0];
        this.mAnchorLocationY = iArr[1];
    }

    public void setArrowMode(int i) {
        this.mArrowMode = i;
    }

    public void setArrowMode(int i, boolean z) {
        setArrowMode(i);
        this.mIsMirrored = z;
        this.mMirroredTextGroup.setVisibility(this.mIsMirrored ? 0 : 8);
    }

    public void setGuidePopupWindow(GuidePopupWindow guidePopupWindow) {
        this.mGuidePopupWindow = guidePopupWindow;
    }

    public void setOffset(int i, int i2) {
        this.mOffsetX = i;
        this.mOffsetY = i2;
        this.mUseDefaultOffset = false;
    }

    public void setTouchInterceptor(OnTouchListener onTouchListener) {
        this.mTouchInterceptor = onTouchListener;
    }
}
