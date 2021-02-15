package com.android.camera;

import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader.TileMode;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import com.android.camera.log.Log;
import miuix.view.animation.CubicEaseInInterpolator;
import miuix.view.animation.CubicEaseOutInterpolator;

public class AudioMapMove extends View {
    public static final int DEFAULT_ANIM_TIME_ONE = 150;
    public static final int DEFAULT_ANIM_TIME_SECOND = 80;
    private static final String TAG = "AudioMapMove";
    private boolean antiAlias;
    private float lastY;
    private LinearGradient linearGradient1 = null;
    private LinearGradient linearGradient2 = null;
    private long mAnimTimeAudioMap;
    private long mAnimTimeLine;
    private ValueAnimator mAnimatorAudioMap;
    private ValueAnimator mAnimatorLine;
    private float mAudioMapHeight;
    private Paint mDialPaint;
    /* access modifiers changed from: private */
    public float mEndL;
    /* access modifiers changed from: private */
    public float mEndR;
    private float mFirstRectLeft;
    private float mFirstRectRight;
    private int mGradientsColor;
    private int mGreenColor;
    /* access modifiers changed from: private */
    public float mLValue;
    /* access modifiers changed from: private */
    public float mLeftCurrentVolume;
    /* access modifiers changed from: private */
    public float mLeftLineValue;
    /* access modifiers changed from: private */
    public float mLeftMaxVolumeValue;
    private float mLeftRectTop;
    private Paint mLinePaint;
    private OnAudioMapPressAnimatorListener mOnAudioMapPressAnimatorListener;
    private int mOrangeColor;
    /* access modifiers changed from: private */
    public float mPercent1;
    /* access modifiers changed from: private */
    public float mPercent2;
    /* access modifiers changed from: private */
    public float mRValue;
    private float mRectBottom;
    /* access modifiers changed from: private */
    public float mRightCurrentVolume;
    /* access modifiers changed from: private */
    public float mRightLineValue;
    /* access modifiers changed from: private */
    public float mRightMaxVolumeValue;
    private float mRightRectTop;
    private float mSecondRectLeft;
    private float mSecondRectRight;
    /* access modifiers changed from: private */
    public float mStartL = 0.0f;
    /* access modifiers changed from: private */
    public float mStartR = 0.0f;
    private int mYellowColor;
    private float offsetY;

    public interface OnAudioMapPressAnimatorListener {
        void setPressAudioMapPressAnimator();

        void setUpAudioMapPressAnimator();

        void setVolumeControlValue(float f);
    }

    public AudioMapMove(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init(context, attributeSet);
    }

    private void drawRect(Canvas canvas) {
        canvas.save();
        float f = this.mFirstRectLeft;
        float f2 = f;
        LinearGradient linearGradient = new LinearGradient(f2, this.mRectBottom, this.mLeftRectTop, f, new int[]{this.mGreenColor, this.mGradientsColor, this.mYellowColor, this.mOrangeColor}, null, TileMode.REPEAT);
        this.linearGradient1 = linearGradient;
        this.mDialPaint.setShader(this.linearGradient1);
        float f3 = this.mLeftCurrentVolume;
        float f4 = this.mLeftRectTop;
        if (f3 < f4) {
            this.mLeftCurrentVolume = f4;
        }
        float f5 = this.mRightCurrentVolume;
        float f6 = this.mRightRectTop;
        if (f5 < f6) {
            this.mRightCurrentVolume = f6;
        }
        canvas.drawRect(this.mFirstRectLeft, this.mLeftCurrentVolume, this.mFirstRectRight, this.mRectBottom, this.mDialPaint);
        canvas.drawRect(this.mSecondRectLeft, this.mRightCurrentVolume, this.mSecondRectRight, this.mRectBottom, this.mDialPaint);
        float f7 = this.mFirstRectLeft;
        float f8 = this.mRectBottom;
        LinearGradient linearGradient3 = new LinearGradient(f7, f8, this.mLeftRectTop, f7, new int[]{this.mGreenColor, this.mGradientsColor, this.mYellowColor, this.mOrangeColor}, null, TileMode.REPEAT);
        this.linearGradient2 = linearGradient3;
        this.mLinePaint.setShader(this.linearGradient2);
        float f9 = this.mLeftLineValue;
        float f10 = this.mLeftCurrentVolume;
        if (f9 >= f10) {
            this.mLeftMaxVolumeValue = f10;
            canvas.drawLine(this.mFirstRectLeft, f10, this.mFirstRectRight, f10, this.mLinePaint);
        } else {
            canvas.drawLine(this.mFirstRectLeft, f9, this.mFirstRectRight, f9, this.mLinePaint);
        }
        float f11 = this.mRightLineValue;
        float f12 = this.mRightCurrentVolume;
        if (f11 >= f12) {
            this.mRightMaxVolumeValue = f12;
            canvas.drawLine(this.mSecondRectLeft, f12, this.mSecondRectRight, f12, this.mLinePaint);
        } else {
            canvas.drawLine(this.mSecondRectLeft, f11, this.mSecondRectRight, f11, this.mLinePaint);
        }
        canvas.restore();
    }

    private void init(Context context, AttributeSet attributeSet) {
        initConfig(context, attributeSet);
        initPaint();
    }

    private void initConfig(Context context, AttributeSet attributeSet) {
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R.styleable.AudioMap);
        this.antiAlias = obtainStyledAttributes.getBoolean(0, true);
        this.mAnimTimeAudioMap = (long) obtainStyledAttributes.getInt(3, 150);
        this.mAnimTimeLine = (long) obtainStyledAttributes.getInt(3, 80);
        this.mRValue = (float) getResources().getDimensionPixelSize(R.dimen.audio_map_rect_bottom);
        this.mLValue = (float) getResources().getDimensionPixelSize(R.dimen.audio_map_rect_bottom);
        this.mLeftLineValue = obtainStyledAttributes.getFloat(5, this.mLValue);
        this.mRightLineValue = obtainStyledAttributes.getFloat(5, this.mRValue);
        this.mAudioMapHeight = (float) getResources().getDimensionPixelSize(R.dimen.audio_map_rect_height_size);
        this.mFirstRectLeft = (float) getResources().getDimensionPixelSize(R.dimen.audio_map_first_rect_left);
        this.mFirstRectRight = (float) getResources().getDimensionPixelSize(R.dimen.audio_map_first_rect_right);
        this.mSecondRectLeft = (float) getResources().getDimensionPixelSize(R.dimen.audio_map_second_rect_left);
        this.mSecondRectRight = (float) getResources().getDimensionPixelSize(R.dimen.audio_map_second_rect_right);
        this.mLeftRectTop = (float) getResources().getDimensionPixelSize(R.dimen.audio_map_rect_top);
        this.mRightRectTop = (float) getResources().getDimensionPixelSize(R.dimen.audio_map_rect_top);
        this.mRectBottom = (float) getResources().getDimensionPixelSize(R.dimen.audio_map_rect_bottom);
        this.mGreenColor = getResources().getColor(R.color.audio_map_green_color);
        this.mGradientsColor = getResources().getColor(R.color.audio_map_gradients_color);
        this.mYellowColor = getResources().getColor(R.color.audio_map_yellow_color);
        this.mOrangeColor = getResources().getColor(R.color.audio_map_orange_color);
    }

    private void initPaint() {
        this.mDialPaint = new Paint();
        this.mDialPaint.setAntiAlias(this.antiAlias);
        this.mDialPaint.setStrokeWidth(1.0f);
        this.mLinePaint = new Paint();
        this.mLinePaint.setAntiAlias(this.antiAlias);
        this.mLinePaint.setStrokeWidth(2.0f);
    }

    private void startAnimatorAudioMap() {
        ValueAnimator valueAnimator = this.mAnimatorAudioMap;
        if (valueAnimator != null && valueAnimator.isRunning()) {
            this.mAnimatorAudioMap.cancel();
        }
        if (this.mAnimatorAudioMap == null) {
            this.mAnimatorAudioMap = ValueAnimator.ofFloat(new float[]{0.0f, 1.0f});
            this.mAnimatorAudioMap.setDuration(this.mAnimTimeAudioMap);
            this.mAnimatorAudioMap.setInterpolator(new CubicEaseOutInterpolator());
            this.mAnimatorAudioMap.addUpdateListener(new AnimatorUpdateListener() {
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    AudioMapMove.this.mPercent1 = ((Float) valueAnimator.getAnimatedValue()).floatValue();
                    AudioMapMove audioMapMove = AudioMapMove.this;
                    audioMapMove.mLValue = audioMapMove.mStartL + (AudioMapMove.this.mPercent1 * (AudioMapMove.this.mEndL - AudioMapMove.this.mStartL));
                    AudioMapMove audioMapMove2 = AudioMapMove.this;
                    audioMapMove2.mRValue = audioMapMove2.mStartR + (AudioMapMove.this.mPercent1 * (AudioMapMove.this.mEndR - AudioMapMove.this.mStartR));
                    AudioMapMove audioMapMove3 = AudioMapMove.this;
                    audioMapMove3.mLeftCurrentVolume = audioMapMove3.mLValue;
                    AudioMapMove audioMapMove4 = AudioMapMove.this;
                    audioMapMove4.mRightCurrentVolume = audioMapMove4.mRValue;
                    AudioMapMove.this.invalidate();
                }
            });
        }
        this.mAnimatorAudioMap.start();
    }

    private void startAnimatorLine() {
        ValueAnimator valueAnimator = this.mAnimatorLine;
        if (valueAnimator != null && valueAnimator.isRunning()) {
            this.mAnimatorLine.cancel();
        }
        if (this.mAnimatorLine == null) {
            this.mAnimatorLine = ValueAnimator.ofFloat(new float[]{0.0f, 1.0f});
            this.mAnimatorLine.setDuration(this.mAnimTimeLine);
            this.mAnimatorLine.setInterpolator(new CubicEaseInInterpolator());
            this.mAnimatorLine.addUpdateListener(new AnimatorUpdateListener() {
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    AudioMapMove.this.mPercent2 = ((Float) valueAnimator.getAnimatedValue()).floatValue();
                    AudioMapMove audioMapMove = AudioMapMove.this;
                    audioMapMove.mLeftLineValue = audioMapMove.mLeftMaxVolumeValue + (AudioMapMove.this.mPercent2 * (AudioMapMove.this.mLeftCurrentVolume - AudioMapMove.this.mLeftMaxVolumeValue));
                    AudioMapMove audioMapMove2 = AudioMapMove.this;
                    audioMapMove2.mRightLineValue = audioMapMove2.mRightMaxVolumeValue + (AudioMapMove.this.mPercent2 * (AudioMapMove.this.mRightCurrentVolume - AudioMapMove.this.mRightMaxVolumeValue));
                    AudioMapMove.this.invalidate();
                }
            });
        }
        this.mAnimatorLine.start();
    }

    /* access modifiers changed from: protected */
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawRect(canvas);
    }

    /* access modifiers changed from: protected */
    public void onMeasure(int i, int i2) {
        super.onMeasure(i, i2);
    }

    /* access modifiers changed from: protected */
    public void onSizeChanged(int i, int i2, int i3, int i4) {
        super.onSizeChanged(i, i2, i3, i4);
    }

    public boolean onTouchEvent(MotionEvent motionEvent) {
        float y = motionEvent.getY();
        int action = motionEvent.getAction();
        if (action != 0) {
            if (action == 1) {
                this.mOnAudioMapPressAnimatorListener.setUpAudioMapPressAnimator();
            } else if (action == 2) {
                this.offsetY = this.lastY - y;
                this.lastY = y;
                this.mOnAudioMapPressAnimatorListener.setVolumeControlValue(this.offsetY);
            }
            return super.onTouchEvent(motionEvent);
        }
        this.lastY = y;
        this.mOnAudioMapPressAnimatorListener.setPressAudioMapPressAnimator();
        return true;
    }

    public void setOnAudioMapPressAnimatorListener(OnAudioMapPressAnimatorListener onAudioMapPressAnimatorListener) {
        Log.d(TAG, "setOnAudioMapPressAnimatorListener()");
        this.mOnAudioMapPressAnimatorListener = onAudioMapPressAnimatorListener;
    }

    public void setValue(float f, float f2) {
        float f3 = this.mAudioMapHeight / 32.0f;
        int[] iArr = {(int) (f * f3), (int) (f2 * f3)};
        this.mStartR = this.mRValue;
        this.mStartL = this.mLValue;
        float f4 = this.mRectBottom;
        this.mEndL = f4 - ((float) iArr[0]);
        this.mEndR = f4 - ((float) iArr[1]);
        this.mLeftMaxVolumeValue = this.mLeftLineValue;
        this.mLeftCurrentVolume = (float) iArr[0];
        this.mRightMaxVolumeValue = this.mRightLineValue;
        this.mRightCurrentVolume = (float) iArr[1];
        startAnimatorAudioMap();
        startAnimatorLine();
    }
}
