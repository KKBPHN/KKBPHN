package com.android.camera.ui;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.Style;
import android.graphics.Typeface;
import android.text.TextPaint;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.StyleSpan;
import android.text.style.TypefaceSpan;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import com.android.camera.R;
import com.android.camera.Util;
import com.android.camera.constant.ColorConstant;
import com.android.camera.customization.TintColor;
import com.android.camera.log.Log;
import com.android.camera.protocol.ModeCoordinatorImpl;
import com.android.camera.protocol.ModeProtocol.CameraAction;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public abstract class BaseHorizontalZoomView extends View {
    private static final String TAG = "BaseHorizontalZoomView";
    public static final int TOUCH_STATE_CLICK = 1;
    public static final int TOUCH_STATE_IDLE = 0;
    public static final int TOUCH_STATE_SCROLL = 2;

    @Retention(RetentionPolicy.SOURCE)
    public @interface AnimateState {
    }

    public abstract class HorizontalDrawAdapter {
        protected float mCurrentLineHalfHeight;
        protected float mCurrentLineIllegalHalfHeight;
        protected float mCurrentLineSelectHalfHeight;
        protected String mCurrentValue;
        protected float mDegree;
        private float mIllegalLineHalfHeight;
        protected Paint mIllegalLinePaint;
        protected boolean mIsRSL;
        protected float mItemGap;
        protected int mLineColorDefault;
        protected int mLineColorIllegal;
        protected int mLineColorSelect;
        protected float mLineHalfHeight;
        protected float mLineIllegalMovingHalfHeight;
        protected float mLineMovingHalfHeight;
        protected float mLineSelectHalfHeight;
        protected float mLineSelectMovingHalfHeight;
        protected int mLineStopPointWidth;
        protected int mLineTextGap;
        protected int mLineWidth;
        protected boolean mNeedIllegalLine;
        protected Paint mNormalPaint;
        protected float mRotateRate;
        protected Paint mSelectPaint;
        protected AbsoluteSizeSpan mSpaceTextStyle;
        protected Paint mStopPointPaint;
        protected StyleSpan mTextBoldStyle;
        protected int mTextColor;
        protected TypefaceSpan mTextFontStyle;
        protected TextPaint mTextPaint;
        protected int mTextSize;
        protected int mViewCurrentState = 0;
        protected int mViewLastState = 0;

        public void draw(int i, Canvas canvas, boolean z) {
        }

        public void draw(int i, Canvas canvas, boolean z, int i2, float f) {
            float f2;
            float f3;
            this.mViewCurrentState = i2;
            if (i2 == 0) {
                f3 = this.mLineSelectHalfHeight;
            } else if (i2 == 1) {
                float f4 = this.mLineSelectHalfHeight;
                f3 = f4 + (f * (this.mLineSelectMovingHalfHeight - f4));
            } else if (i2 == 2) {
                float f5 = this.mLineSelectHalfHeight;
                this.mCurrentLineSelectHalfHeight = f5 + ((this.mLineSelectMovingHalfHeight - f5) * f);
                float f6 = this.mLineHalfHeight;
                this.mCurrentLineHalfHeight = f6 + ((this.mLineMovingHalfHeight - f6) * f);
                if (this.mNeedIllegalLine) {
                    float f7 = this.mIllegalLineHalfHeight;
                    f2 = f7 + (f * (this.mLineIllegalMovingHalfHeight - f7));
                    this.mCurrentLineIllegalHalfHeight = f2;
                }
                return;
            } else {
                return;
            }
            this.mCurrentLineSelectHalfHeight = f3;
            this.mCurrentLineHalfHeight = this.mLineHalfHeight;
            f2 = this.mIllegalLineHalfHeight;
            this.mCurrentLineIllegalHalfHeight = f2;
        }

        public abstract Align getAlign(int i);

        public abstract int getCount();

        /* access modifiers changed from: protected */
        public void initStyle(Context context) {
            this.mIsRSL = Util.isLayoutRTL(context);
            Resources resources = context.getResources();
            this.mLineSelectMovingHalfHeight = ((float) resources.getDimensionPixelSize(R.dimen.bokeh_line_selected_moving_height)) / 2.0f;
            this.mLineSelectHalfHeight = ((float) resources.getDimensionPixelSize(R.dimen.bokeh_line_selected_height)) / 2.0f;
            this.mLineMovingHalfHeight = ((float) resources.getDimensionPixelSize(R.dimen.bokeh_line_moving_height)) / 2.0f;
            this.mLineHalfHeight = ((float) resources.getDimensionPixelSize(R.dimen.bokeh_line_height)) / 2.0f;
            this.mLineWidth = resources.getDimensionPixelSize(R.dimen.bokeh_line_width);
            this.mLineStopPointWidth = resources.getDimensionPixelSize(R.dimen.bokeh_line_flag_width);
            this.mLineColorSelect = TintColor.tintColor();
            this.mLineColorDefault = resources.getColor(R.color.zoom_popup_color_new_default);
            this.mLineColorIllegal = ColorConstant.COLOR_COMMON_NORMAL;
            this.mSpaceTextStyle = new AbsoluteSizeSpan(18, true);
            this.mTextFontStyle = new TypefaceSpan(Util.getMiuiTypeface());
            this.mTextBoldStyle = new StyleSpan(1);
            this.mTextSize = resources.getDimensionPixelSize(R.dimen.zoom_popup_text_size);
            this.mTextColor = resources.getColor(R.color.zoom_popup_color_new_default);
            this.mLineTextGap = resources.getDimensionPixelSize(R.dimen.zoom_popup_line_text_margin);
            this.mNormalPaint = new Paint(1);
            this.mNormalPaint.setStyle(Style.FILL);
            this.mNormalPaint.setColor(this.mLineColorDefault);
            this.mNormalPaint.setShadowLayer(1.0f, 0.0f, 0.0f, Integer.MIN_VALUE);
            this.mStopPointPaint = new Paint(1);
            this.mStopPointPaint.setStyle(Style.FILL);
            this.mStopPointPaint.setColor(this.mLineColorDefault);
            this.mStopPointPaint.setShadowLayer(1.0f, 0.0f, 0.0f, Integer.MIN_VALUE);
            this.mSelectPaint = new Paint(1);
            this.mSelectPaint.setStyle(Style.FILL);
            this.mSelectPaint.setColor(this.mLineColorSelect);
            this.mSelectPaint.setShadowLayer(1.0f, 0.0f, 0.0f, Integer.MIN_VALUE);
            if (this.mNeedIllegalLine) {
                this.mIllegalLinePaint = new Paint(1);
                this.mIllegalLinePaint.setStyle(Style.FILL);
                this.mIllegalLinePaint.setColor(this.mLineColorIllegal);
                this.mIllegalLinePaint.setShadowLayer(1.0f, 0.0f, 0.0f, Integer.MIN_VALUE);
                this.mIllegalLineHalfHeight = ((float) resources.getDimensionPixelSize(R.dimen.bokeh_line_height)) / 4.0f;
                this.mLineIllegalMovingHalfHeight = ((float) resources.getDimensionPixelSize(R.dimen.illegal_line_moving_height)) / 2.0f;
            }
            this.mTextPaint = new TextPaint(1);
            this.mTextPaint.setAntiAlias(true);
            this.mTextPaint.setTextSize((float) this.mTextSize);
            this.mTextPaint.setColor(this.mTextColor);
            this.mTextPaint.setTypeface(Util.getMiuiTypeface());
            this.mTextPaint.setShadowLayer(1.0f, 0.0f, 0.0f, Integer.MIN_VALUE);
            this.mTextPaint.setTypeface(Typeface.defaultFromStyle(1));
        }

        public boolean isSingleValueLine(int i) {
            return true;
        }

        public float measureGap(int i) {
            return 0.0f;
        }

        public abstract float measureWidth(int i);

        public void setCurrentValue(String str) {
            if (str != null) {
                this.mCurrentValue = str;
            }
        }

        public void setItemGap(float f) {
            this.mItemGap = f;
        }

        public void setRotate(int i) {
            this.mDegree = (float) i;
        }

        public void updateSelectColor() {
            this.mLineColorSelect = TintColor.tintColor();
            this.mSelectPaint.setColor(this.mLineColorSelect);
        }
    }

    public interface OnPositionSelectListener {
        void onChangeValue(String str);

        void onPositionSelect(View view, int i, float f);
    }

    public interface onTouchUpStateListener {
        void onScrollEnd();

        void onScrollStart(float f);

        void onTouchUpState();
    }

    public BaseHorizontalZoomView(Context context) {
        super(context);
    }

    public BaseHorizontalZoomView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public BaseHorizontalZoomView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }

    public boolean canPositionScroll() {
        CameraAction cameraAction = (CameraAction) ModeCoordinatorImpl.getInstance().getAttachProtocol(161);
        return cameraAction == null || !cameraAction.isDoingAction();
    }

    public boolean isIdle() {
        return true;
    }

    public boolean onTouchEvent(MotionEvent motionEvent) {
        if (motionEvent.getAction() != 0 || canPositionScroll()) {
            return true;
        }
        Log.d(TAG, "cannot scroll to apply zoom value, do not process the down event.");
        return false;
    }

    public abstract void setDrawAdapter(HorizontalDrawAdapter horizontalDrawAdapter, int i, boolean z);

    public void setEvent(MotionEvent motionEvent) {
        onTouchEvent(motionEvent);
    }

    public abstract void setListener(OnPositionSelectListener onPositionSelectListener, onTouchUpStateListener ontouchupstatelistener);

    public abstract void setSelection(float f);

    public abstract void setSelection(int i, boolean z);
}
