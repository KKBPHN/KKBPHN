package com.android.camera;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import com.android.camera.data.DataRepository;
import com.android.camera.log.Log;
import com.android.camera.protocol.ModeCoordinatorImpl;
import com.android.camera.protocol.ModeProtocol.ChangeGainProtocol;
import com.android.camera.statistic.MistatsConstants.Manual;
import com.android.camera.statistic.MistatsWrapper;
import com.xiaomi.camera.ui.drawable.RoundRectDrawable;
import java.util.HashMap;

public class VolumeControlPanel extends View {
    private static final float GAIN_MAX_VALUE = 100.0f;
    private static final String TAG = "VolumeControlPanel";
    private static final float mDefaultGainValue = 50.0f;
    private boolean antiAlias;
    private float lastY;
    private int mDialColor;
    private Paint mDialPaint;
    private OnVolumeControlListener mOnVolumeControlListener;
    private OnVolumePressAnimatorListener mOnVolumePressAnimatorListener;
    private float mRectBottom;
    private float mRectLeft;
    private float mRectRight;
    private float mRectTop;
    private boolean mSwitchAnimator;
    private float mTemporaryValue;
    private float mUnitLength;
    private float offsetY;

    public interface OnVolumeControlListener {
        void putVolumeControlValue(float f);

        void removePostDelayedTime();

        void setPostDelayedTime();

        void setSilenceUpSwitchTarget(boolean z);
    }

    public interface OnVolumePressAnimatorListener {
        void setPressAnimatorX();

        void setPressAnimatorY();

        void setUpAnimatorX();

        void setUpAnimatorY();
    }

    public VolumeControlPanel(Context context) {
        this(context, null);
    }

    public VolumeControlPanel(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mSwitchAnimator = true;
        Init(context, attributeSet);
    }

    private void Init(Context context, AttributeSet attributeSet) {
        setClipToOutline(true);
        initConfig(context, attributeSet);
        initPaint();
    }

    private void drawRect(Canvas canvas) {
        int save = canvas.save();
        canvas.drawRect(this.mRectLeft, this.mRectTop, this.mRectRight, this.mRectBottom, this.mDialPaint);
        canvas.restoreToCount(save);
    }

    private void initConfig(Context context, AttributeSet attributeSet) {
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R.styleable.VolumeControl);
        this.antiAlias = obtainStyledAttributes.getBoolean(3, true);
        this.mDialColor = obtainStyledAttributes.getColor(0, -1);
        this.mRectLeft = (float) getResources().getDimensionPixelSize(R.dimen.volume_control_rect_left);
        this.mRectRight = (float) getResources().getDimensionPixelSize(R.dimen.volume_control_rect_right);
        this.mRectBottom = (float) getResources().getDimensionPixelSize(R.dimen.volume_control_rect_bottom);
        this.mUnitLength = this.mRectBottom / GAIN_MAX_VALUE;
        this.mRectTop = this.mUnitLength * 50.0f;
    }

    private void initPaint() {
        this.mDialPaint = new Paint();
        this.mDialPaint.setAntiAlias(this.antiAlias);
        this.mDialPaint.setColor(this.mDialColor);
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
                HashMap hashMap = new HashMap();
                hashMap.put(Manual.PARAM_PRO_MODE_ADJUST_VOLUME_CONTROL_CLICK, Integer.valueOf(1));
                hashMap.put(Manual.PARAM_PRO_MODE_ADJUST_VOLUME_CONTROL_VALUE, Float.valueOf(this.mTemporaryValue));
                MistatsWrapper.mistatEvent("M_proVideo_", hashMap);
                this.mOnVolumeControlListener.setPostDelayedTime();
            } else if (action == 2) {
                this.offsetY = this.lastY - y;
                this.lastY = y;
                if (this.mRectTop - this.offsetY >= this.mRectBottom && this.mSwitchAnimator) {
                    this.mSwitchAnimator = false;
                    this.mOnVolumeControlListener.setSilenceUpSwitchTarget(true);
                } else if (this.mRectTop - this.offsetY < this.mRectBottom && !this.mSwitchAnimator) {
                    this.mSwitchAnimator = true;
                    this.mOnVolumeControlListener.setSilenceUpSwitchTarget(false);
                }
                setValue(this.offsetY);
            }
            return super.onTouchEvent(motionEvent);
        }
        Log.u(TAG, "onTouchEvent:ACTION_DOWN");
        this.lastY = y;
        this.mOnVolumeControlListener.removePostDelayedTime();
        return true;
    }

    public void setGainValueReset(float f) {
        this.mRectTop = f * this.mUnitLength;
        invalidate();
    }

    public void setOnVolumeControlListener(OnVolumeControlListener onVolumeControlListener) {
        Log.d(TAG, "setOnVolumeListener()");
        this.mOnVolumeControlListener = onVolumeControlListener;
    }

    public void setOnVolumePressAnimatorListener(OnVolumePressAnimatorListener onVolumePressAnimatorListener) {
        Log.d(TAG, "setOnVolumePressAnimatorListener()");
        this.mOnVolumePressAnimatorListener = onVolumePressAnimatorListener;
    }

    public void setValue(float f) {
        this.mRectTop -= f;
        this.mOnVolumeControlListener.putVolumeControlValue(this.mRectTop);
        float f2 = this.mRectTop;
        float f3 = this.mRectBottom;
        if (f2 > f3) {
            this.mRectTop = f3;
        } else if (f2 <= 0.0f) {
            this.mRectTop = 0.0f;
        }
        this.mTemporaryValue = GAIN_MAX_VALUE - (this.mRectTop / this.mUnitLength);
        if (((double) this.mTemporaryValue) <= 0.1d) {
            this.mTemporaryValue = 0.0f;
        }
        String valueOf = String.valueOf(this.mTemporaryValue);
        ChangeGainProtocol changeGainProtocol = (ChangeGainProtocol) ModeCoordinatorImpl.getInstance().getAttachProtocol(935);
        if (changeGainProtocol != null && DataRepository.dataItemGlobal().getCurrentMode() == 180) {
            StringBuilder sb = new StringBuilder();
            sb.append("mGainNum");
            sb.append(valueOf);
            Log.d("GainValue10-21", sb.toString());
            changeGainProtocol.setGainValue(valueOf);
        }
        invalidate();
    }

    public Drawable setVolumeControlBackGround() {
        return new RoundRectDrawable(getResources().getColor(R.color.volume_control_background), (float) getResources().getDimensionPixelSize(R.dimen.audio_map_rect_radius));
    }
}
