package com.android.camera.timerburst;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.drawable.GradientDrawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import com.android.camera.R;
import com.android.camera.Util;
import com.android.camera.animation.FolmeUtils;
import com.android.camera.animation.FolmeUtils.PhyAnimatorListener;
import com.android.camera.animation.folme.FolmeTranslateYOnSubscribe;
import com.android.camera.customization.TintColor;
import com.android.camera.log.Log;
import io.reactivex.Completable;
import miuix.animation.Folme;
import miuix.animation.base.AnimConfig;
import miuix.animation.controller.AnimState;
import miuix.animation.property.FloatProperty;
import miuix.animation.property.ViewProperty;

public class CustomSeekBar extends RelativeLayout {
    private final String TAG = "CustomSeekBar";
    /* access modifiers changed from: private */
    public CircleFullImageView circleFullImageView;
    private float mCircleIndexX;
    /* access modifiers changed from: private */
    public int[] mConfigArray = {5, 100, 500, 1000};
    /* access modifiers changed from: private */
    public Context mContext;
    private int mCurTvLocation = -1;
    private int mCurWidth = -1;
    private int mCurrentValue;
    private float mCurrentX;
    private int mEventType;
    /* access modifiers changed from: private */
    public float mInnerMarginHorizontal;
    /* access modifiers changed from: private */
    public boolean mIsEnlargeState = false;
    private boolean mIsRTL;
    /* access modifiers changed from: private */
    public float mMarginBuffer;
    private float[] mRangeMax;
    /* access modifiers changed from: private */
    public float[] mRangeMin;
    /* access modifiers changed from: private */
    public RelativeLayout mRlSeekInnerContainer;
    /* access modifiers changed from: private */
    public RelativeLayout mRlSeekOutter;
    /* access modifiers changed from: private */
    public float mRlSeekOutterHeight;
    AddSeekBarValueListener mSeekBarValueListener;
    private Object mSeekStyleoObject = new Object();
    private Object mThumbStyleObject = new Object();
    /* access modifiers changed from: private */
    public int mTotalwidth;
    private TextView mTvCurrentValue;
    /* access modifiers changed from: private */
    public TextView mTvEndValue;
    /* access modifiers changed from: private */
    public float mTvEndX;
    private RelativeLayout mTvParentLayout;
    /* access modifiers changed from: private */
    public float mTvStartRightX;
    /* access modifiers changed from: private */
    public TextView mTvStartValue;
    /* access modifiers changed from: private */
    public float mTvWidth;
    private String mUnitString = "";
    private int mValueEnlargeTimes;
    /* access modifiers changed from: private */
    public int mXStartParent;
    /* access modifiers changed from: private */
    public int widthScreen;

    interface AddSeekBarValueListener {
        void currentSeekBarValue(View view, int i, int i2);
    }

    public class JudgeSegmentResult {
        private float bothSidesMarginBuffer;
        private int[] elementArray;
        private int judgeValue;
        private float perSegmentWidth;
        private int segmentCount;

        JudgeSegmentResult(float f, int[] iArr, float f2) {
            this.elementArray = iArr;
            this.segmentCount = iArr.length - 1;
            this.perSegmentWidth = f / ((float) this.segmentCount);
            this.bothSidesMarginBuffer = f2;
        }

        public float getJudgePosition(float f) {
            float f2;
            int i = 0;
            while (true) {
                if (i >= this.segmentCount) {
                    f2 = 0.0f;
                    i = 1;
                    break;
                }
                int[] iArr = this.elementArray;
                if (((float) iArr[i]) <= f) {
                    int i2 = i + 1;
                    if (f < ((float) iArr[i2])) {
                        f2 = (f - ((float) iArr[i])) / ((float) (iArr[i2] - iArr[i]));
                        break;
                    }
                }
                i++;
            }
            float f3 = (float) i;
            float f4 = this.perSegmentWidth;
            float f5 = (f3 * f4) + (f2 * f4);
            return f < ((float) CustomSeekBar.this.mConfigArray[CustomSeekBar.this.mConfigArray.length - 1]) ? f5 + (this.bothSidesMarginBuffer / 2.0f) : f5;
        }

        public int getJudgeValue(float f) {
            int i;
            float f2 = f - CustomSeekBar.this.mRangeMin[0];
            if (f2 < 0.0f) {
                f2 = 0.0f;
            }
            float f3 = f2 / this.perSegmentWidth;
            int i2 = (int) f3;
            float f4 = f3 - ((float) i2);
            int i3 = i2 + 1;
            if (i3 < CustomSeekBar.this.mConfigArray.length) {
                int[] iArr = this.elementArray;
                int i4 = iArr[i2];
                i = ((int) (((float) (iArr[i3] - i4)) * f4)) + i4;
            } else {
                i = CustomSeekBar.this.mConfigArray[CustomSeekBar.this.mConfigArray.length - 1];
            }
            this.judgeValue = i;
            return this.judgeValue;
        }
    }

    public CustomSeekBar(Context context) {
        super(context);
        this.mContext = context;
        initViews();
    }

    public CustomSeekBar(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mContext = context;
        initViews();
    }

    public CustomSeekBar(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.mContext = context;
        initViews();
    }

    /* access modifiers changed from: private */
    public float getCircleDiameter(int i) {
        int i2;
        Resources resources;
        if (i == 1) {
            resources = this.mContext.getResources();
            i2 = R.dimen.seek_bar_timer_circle_low;
        } else if (i == 2) {
            resources = this.mContext.getResources();
            i2 = R.dimen.seek_bar_timer_inner_low_height;
        } else if (i == 3) {
            resources = this.mContext.getResources();
            i2 = R.dimen.seek_bar_timer_inner_high_height;
        } else if (i != 4) {
            return 0.0f;
        } else {
            resources = this.mContext.getResources();
            i2 = R.dimen.seek_bar_timer_outter_height;
        }
        return resources.getDimension(i2);
    }

    private String getUnitString() {
        return TextUtils.isEmpty(this.mUnitString) ? "" : this.mUnitString;
    }

    private void initViews() {
        View inflate = RelativeLayout.inflate(getContext(), R.layout.layout_custom_seek_bar, this);
        this.mTvParentLayout = (RelativeLayout) inflate.findViewById(R.id.rl_seek_scale);
        this.mTvCurrentValue = (TextView) inflate.findViewById(R.id.tv_current);
        this.mTvCurrentValue.setTextColor(TintColor.tintColor());
        this.mTvStartValue = (TextView) inflate.findViewById(R.id.tv_start);
        this.mTvEndValue = (TextView) inflate.findViewById(R.id.tv_end);
        this.circleFullImageView = (CircleFullImageView) inflate.findViewById(R.id.iv_circle);
        this.circleFullImageView.updateView(1, this.mContext.getColor(R.color.color_white));
        this.mRlSeekOutter = (RelativeLayout) inflate.findViewById(R.id.rl_seek_container);
        this.mRlSeekOutterHeight = getResources().getDimension(R.dimen.seek_bar_timer_outter_height);
        this.mRlSeekInnerContainer = (RelativeLayout) inflate.findViewById(R.id.rl_seek_inner_container);
    }

    private int judgeCorridinate(float f, float f2) {
        int[] iArr = new int[2];
        this.mRlSeekOutter.getLocationOnScreen(iArr);
        int circleDiameter = (int) (((float) iArr[0]) + (getCircleDiameter(4) / 2.0f));
        int i = iArr[1];
        this.mRangeMin = new float[]{(float) circleDiameter, (float) i};
        float circleDiameter2 = getCircleDiameter(4);
        this.mRangeMax = new float[]{((float) (this.mRlSeekOutter.getWidth() + iArr[0])) - (getCircleDiameter(4) / 2.0f), (float) (i + this.mRlSeekOutter.getHeight() + 50)};
        if (this.mIsRTL) {
            if (this.mRangeMin[0] > f || f > this.mRangeMax[0] + (getCircleDiameter(4) / 2.0f)) {
                return -1;
            }
        } else if (this.mRangeMin[0] - (getCircleDiameter(4) / 2.0f) > f || f > this.mRangeMax[0] + (getCircleDiameter(4) / 2.0f)) {
            return -1;
        }
        if ((this.mRangeMin[1] > f2 || f2 > this.mRangeMax[1]) && !this.mIsEnlargeState) {
            return -1;
        }
        int judgeValue = new JudgeSegmentResult(this.mRangeMax[0] - this.mRangeMin[0], this.mConfigArray, circleDiameter2).getJudgeValue(f);
        AddSeekBarValueListener addSeekBarValueListener = this.mSeekBarValueListener;
        if (addSeekBarValueListener != null) {
            addSeekBarValueListener.currentSeekBarValue(this, this.mValueEnlargeTimes, judgeValue);
        }
        int[] iArr2 = this.mConfigArray;
        if (judgeValue < iArr2[0]) {
            judgeValue = iArr2[0];
        }
        int[] iArr3 = this.mConfigArray;
        int i2 = iArr3[iArr3.length - 1];
        if (judgeValue <= i2) {
            i2 = judgeValue;
        }
        return i2;
    }

    /* access modifiers changed from: private */
    public float judgeCorridinatePosition(float f, float f2) {
        float judgePosition = new JudgeSegmentResult(f, this.mConfigArray, f2).getJudgePosition((float) this.mCurrentValue);
        updateInnerContainer((int) judgePosition);
        return judgePosition;
    }

    /* access modifiers changed from: private */
    public void reverseEnlager() {
        LayoutParams layoutParams = (LayoutParams) this.mRlSeekInnerContainer.getLayoutParams();
        this.mInnerMarginHorizontal = (this.mRlSeekOutterHeight - getCircleDiameter(2)) / 2.0f;
        layoutParams.height = (int) getCircleDiameter(2);
        if (this.mCurrentValue == this.mConfigArray[0]) {
            layoutParams.width = (int) getCircleDiameter(2);
        }
        float f = this.mInnerMarginHorizontal;
        layoutParams.setMargins((int) f, 0, (int) f, 0);
        this.mRlSeekInnerContainer.setLayoutParams(layoutParams);
        this.mRlSeekInnerContainer.setBackground(getResources().getDrawable(R.drawable.shape_inner_radius_low));
        ((GradientDrawable) this.mRlSeekInnerContainer.getBackground()).setColor(TintColor.tintColor());
        this.circleFullImageView.updateView(1, getResources().getColor(R.color.color_white));
    }

    @TargetApi(11)
    private void showOrHideAnim(View view, boolean z) {
        view.setVisibility(z ? 4 : 0);
    }

    private void startScaleAnim(boolean z) {
        float f;
        float f2;
        float f3;
        float f4;
        if (z) {
            f = getCircleDiameter(2);
            f3 = getCircleDiameter(3);
            f4 = getCircleDiameter(1) / 2.0f;
            f2 = getCircleDiameter(2) / 2.0f;
        } else {
            f = getCircleDiameter(3);
            f3 = getCircleDiameter(2);
            f2 = getCircleDiameter(1) / 2.0f;
            f4 = getCircleDiameter(2) / 2.0f;
        }
        Completable.create(new FolmeTranslateYOnSubscribe(this.mTvParentLayout, z ? -10 : 0)).subscribe();
        FolmeUtils.basePhysicsAnimation(this.mSeekStyleoObject, f, f3, 0.0f, new PhyAnimatorListener() {
            public void onUpdate(Object obj, FloatProperty floatProperty, float f, float f2, boolean z) {
                super.onUpdate(obj, floatProperty, f, f2, z);
                int i = (int) f;
                LayoutParams layoutParams = (LayoutParams) CustomSeekBar.this.mRlSeekInnerContainer.getLayoutParams();
                CustomSeekBar customSeekBar = CustomSeekBar.this;
                customSeekBar.mInnerMarginHorizontal = (customSeekBar.mRlSeekOutterHeight - ((float) i)) / 2.0f;
                layoutParams.setMargins((int) CustomSeekBar.this.mInnerMarginHorizontal, 0, (int) CustomSeekBar.this.mInnerMarginHorizontal, 0);
                layoutParams.height = i;
                layoutParams.width = Util.clamp(layoutParams.width, i, layoutParams.width);
                GradientDrawable gradientDrawable = (GradientDrawable) CustomSeekBar.this.mRlSeekInnerContainer.getBackground();
                gradientDrawable.setColor(TintColor.tintColor());
                float f3 = (float) (i >> 1);
                gradientDrawable.setCornerRadii(new float[]{f3, f3, f3, f3, f3, f3, f3, f3});
                CustomSeekBar.this.mRlSeekInnerContainer.setLayoutParams(layoutParams);
            }
        });
        FolmeUtils.basePhysicsAnimation(this.mThumbStyleObject, f4, f2, 0.0f, new PhyAnimatorListener() {
            public void onUpdate(Object obj, FloatProperty floatProperty, float f, float f2, boolean z) {
                super.onUpdate(obj, floatProperty, f, f2, z);
                CustomSeekBar.this.circleFullImageView.updateRadiusView(f, CustomSeekBar.this.getResources().getColor(R.color.color_white));
            }
        });
    }

    private void updateInnerContainer(int i) {
        updateInnerContainer(i, false);
    }

    private void updateInnerContainer(int i, boolean z) {
        int circleDiameter = (int) (this.mIsEnlargeState ? getCircleDiameter(3) : getCircleDiameter(2));
        boolean z2 = this.mCurrentValue == this.mConfigArray[0];
        if (z) {
            if (this.mCurWidth == -1) {
                this.mCurWidth = this.mRlSeekInnerContainer.getWidth();
            }
            AnimState add = new AnimState("from").add(ViewProperty.WIDTH, this.mCurWidth, new long[0]);
            if (!z2) {
                circleDiameter += i;
            }
            this.mCurWidth = circleDiameter;
            Folme.useAt(this.mRlSeekInnerContainer).state().setTo((Object) add).to(new AnimState("to").add(ViewProperty.WIDTH, this.mCurWidth, new long[0]), new AnimConfig[0]).setEase(-2, 0.9f, 0.15f);
            return;
        }
        LayoutParams layoutParams = (LayoutParams) this.mRlSeekInnerContainer.getLayoutParams();
        if (!z2) {
            circleDiameter += i;
        }
        layoutParams.width = circleDiameter;
        this.mRlSeekInnerContainer.setLayoutParams(layoutParams);
    }

    /* access modifiers changed from: private */
    /* JADX WARNING: Code restructure failed: missing block: B:26:0x0095, code lost:
        if (r11.mTvStartValue.getVisibility() == 4) goto L_0x0097;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:27:0x0097, code lost:
        r4 = r11.mTvStartValue;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:32:0x00ad, code lost:
        if (r11.mTvStartValue.getVisibility() != 4) goto L_0x00b0;
     */
    /* JADX WARNING: Removed duplicated region for block: B:30:0x00a2  */
    /* JADX WARNING: Removed duplicated region for block: B:35:0x00c3  */
    /* JADX WARNING: Removed duplicated region for block: B:36:0x00c8  */
    /* JADX WARNING: Removed duplicated region for block: B:39:0x00d4  */
    /* JADX WARNING: Removed duplicated region for block: B:40:0x00d6  */
    /* JADX WARNING: Removed duplicated region for block: B:43:0x00db  */
    /* JADX WARNING: Removed duplicated region for block: B:48:0x00f0  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void updateTvCurrent(boolean z, float f) {
        float f2;
        float f3;
        AnimState animState;
        AnimState animState2;
        TextView textView;
        if (this.mCurrentValue == this.mConfigArray[0]) {
            f = 0.0f;
        }
        int[] iArr = new int[2];
        this.mTvEndValue.getLocationOnScreen(iArr);
        this.mTvEndX = (float) iArr[0];
        int[] iArr2 = new int[2];
        this.mTvStartValue.getLocationOnScreen(iArr2);
        int[] iArr3 = new int[2];
        this.mRlSeekInnerContainer.getLocationOnScreen(iArr3);
        if (this.mIsRTL) {
            f3 = ((float) iArr2[0]) + f;
            f2 = getCircleDiameter(3);
        } else {
            f3 = ((float) iArr3[0]) + f;
            f2 = getCircleDiameter(2) / 2.0f;
        }
        this.mCircleIndexX = f3 + f2;
        if (this.mCurrentValue < 3) {
            if (this.mTvStartValue.getVisibility() == 0) {
                showOrHideAnim(this.mTvStartValue, true);
            }
            if (this.mTvEndValue.getVisibility() == 4) {
                textView = this.mTvEndValue;
            }
            LayoutParams layoutParams = (LayoutParams) this.mTvCurrentValue.getLayoutParams();
            int circleDiameter = (((int) (this.mCircleIndexX - (this.mTvWidth / 2.0f))) - iArr2[0]) + (f != 0.0f ? 0 : ((int) (!this.mIsEnlargeState ? getCircleDiameter(3) : getCircleDiameter(2))) / 4);
            if (!z) {
                if (this.mIsRTL) {
                    layoutParams.rightMargin = circleDiameter;
                } else {
                    layoutParams.leftMargin = circleDiameter;
                }
                this.mTvCurrentValue.setTranslationX(0.0f);
                this.mTvCurrentValue.setLayoutParams(layoutParams);
                return;
            }
            String str = "to";
            String str2 = "from";
            if (this.mIsRTL) {
                float f4 = (float) iArr2[0];
                float f5 = this.mTvEndX;
                if (f4 - f5 < ((float) circleDiameter)) {
                    circleDiameter = (int) (((float) iArr2[0]) - f5);
                }
                if (this.mCurTvLocation == -1) {
                    this.mCurTvLocation = layoutParams.rightMargin - circleDiameter;
                }
                animState = new AnimState(str2).add(ViewProperty.TRANSLATION_X, this.mCurTvLocation, new long[0]);
                StringBuilder sb = new StringBuilder();
                sb.append("from : ");
                sb.append(this.mCurTvLocation);
                sb.append(", to : ");
                sb.append(layoutParams.rightMargin - circleDiameter);
                Log.d("CustomSeekBar", sb.toString());
                this.mCurTvLocation = layoutParams.rightMargin - circleDiameter;
                animState2 = new AnimState(str);
            } else {
                int[] iArr4 = new int[2];
                this.mTvParentLayout.getLocationOnScreen(iArr4);
                float f6 = this.mTvEndX;
                if (f6 - ((float) iArr4[0]) < ((float) circleDiameter)) {
                    circleDiameter = (int) (f6 - ((float) iArr4[0]));
                }
                if (this.mCurTvLocation == -1) {
                    this.mCurTvLocation = circleDiameter - layoutParams.leftMargin;
                }
                animState = new AnimState(str2).add(ViewProperty.TRANSLATION_X, this.mCurTvLocation, new long[0]);
                this.mCurTvLocation = circleDiameter - layoutParams.leftMargin;
                animState2 = new AnimState(str);
            }
            Folme.useAt(this.mTvCurrentValue).state().setTo((Object) animState).to(animState2.add(ViewProperty.TRANSLATION_X, this.mCurTvLocation, new long[0]), new AnimConfig[0]).setEase(-2, 0.9f, 0.15f);
            return;
        }
        if (!this.mIsRTL) {
            if (this.mTvEndValue.getVisibility() == 4) {
                showOrHideAnim(this.mTvEndValue, false);
            }
        } else if (this.mTvEndValue.getVisibility() == 4) {
        }
        if (this.mTvEndValue.getVisibility() == 0) {
            showOrHideAnim(this.mTvEndValue, true);
        }
        showOrHideAnim(textView, false);
        LayoutParams layoutParams2 = (LayoutParams) this.mTvCurrentValue.getLayoutParams();
        int circleDiameter2 = (((int) (this.mCircleIndexX - (this.mTvWidth / 2.0f))) - iArr2[0]) + (f != 0.0f ? 0 : ((int) (!this.mIsEnlargeState ? getCircleDiameter(3) : getCircleDiameter(2))) / 4);
        if (!z) {
        }
    }

    @SuppressLint({"ResourceType"})
    public void initSeekBarConfig(int[] iArr, int i, int i2, String str, AddSeekBarValueListener addSeekBarValueListener) {
        this.mIsRTL = Util.isLayoutRTL(getContext());
        this.mConfigArray = iArr;
        this.mCurrentValue = i;
        this.mValueEnlargeTimes = i2;
        this.mUnitString = str;
        StringBuilder sb = new StringBuilder();
        sb.append("initSeekBarConfig  mInialValue: ");
        sb.append(this.mCurrentValue);
        sb.append("\n valueEnlargeTimes:");
        sb.append(i2);
        sb.append("\n");
        String str2 = "CustomSeekBar";
        Log.i(str2, sb.toString());
        for (int i3 = 0; i3 < iArr.length; i3++) {
            StringBuilder sb2 = new StringBuilder();
            sb2.append("index:  ");
            sb2.append(i3);
            sb2.append("   config:");
            sb2.append(iArr[i3]);
            Log.i(str2, sb2.toString());
        }
        this.mSeekBarValueListener = addSeekBarValueListener;
        StringBuilder sb3 = new StringBuilder();
        sb3.append(String.valueOf(this.mConfigArray[0] * this.mValueEnlargeTimes));
        sb3.append(getUnitString());
        this.mTvStartValue.setText(sb3.toString());
        StringBuilder sb4 = new StringBuilder();
        int[] iArr2 = this.mConfigArray;
        sb4.append(String.valueOf(iArr2[iArr2.length - 1] * this.mValueEnlargeTimes));
        sb4.append(getUnitString());
        this.mTvEndValue.setText(sb4.toString());
        TextView textView = this.mTvCurrentValue;
        StringBuilder sb5 = new StringBuilder();
        sb5.append(String.valueOf(this.mCurrentValue * this.mValueEnlargeTimes));
        sb5.append(getUnitString());
        textView.setText(sb5.toString());
        this.mTvCurrentValue.setTextColor(TintColor.tintColor());
        getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
            public void onGlobalLayout() {
                CustomSeekBar.this.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                CustomSeekBar.this.mIsEnlargeState = false;
                CustomSeekBar customSeekBar = CustomSeekBar.this;
                customSeekBar.mMarginBuffer = customSeekBar.getCircleDiameter(4);
                CustomSeekBar.this.widthScreen = ((WindowManager) CustomSeekBar.this.mContext.getSystemService("window")).getDefaultDisplay().getWidth();
                CustomSeekBar customSeekBar2 = CustomSeekBar.this;
                customSeekBar2.mTotalwidth = (int) (((float) customSeekBar2.mRlSeekOutter.getWidth()) - CustomSeekBar.this.mMarginBuffer);
                CustomSeekBar customSeekBar3 = CustomSeekBar.this;
                customSeekBar3.mTvStartRightX = (float) (customSeekBar3.mRlSeekInnerContainer.getPaddingLeft() + CustomSeekBar.this.mTvStartValue.getWidth());
                CustomSeekBar customSeekBar4 = CustomSeekBar.this;
                customSeekBar4.mTvEndX = (float) ((customSeekBar4.widthScreen - CustomSeekBar.this.mRlSeekInnerContainer.getPaddingRight()) - CustomSeekBar.this.mTvEndValue.getWidth());
                CustomSeekBar customSeekBar5 = CustomSeekBar.this;
                customSeekBar5.mTvWidth = customSeekBar5.getResources().getDimension(R.dimen.seek_bar_timer_circle_current_tv);
                CustomSeekBar customSeekBar6 = CustomSeekBar.this;
                float access$1300 = customSeekBar6.judgeCorridinatePosition((float) customSeekBar6.mTotalwidth, CustomSeekBar.this.mMarginBuffer);
                CustomSeekBar.this.reverseEnlager();
                int[] iArr = new int[2];
                CustomSeekBar.this.mRlSeekOutter.getLocationOnScreen(iArr);
                CustomSeekBar.this.mXStartParent = iArr[0];
                CustomSeekBar.this.updateTvCurrent(true, access$1300);
            }
        });
    }

    /* access modifiers changed from: protected */
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    /* JADX WARNING: Removed duplicated region for block: B:12:0x0069  */
    /* JADX WARNING: Removed duplicated region for block: B:22:0x008f  */
    /* JADX WARNING: Removed duplicated region for block: B:23:0x0094  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean onTouchEvent(MotionEvent motionEvent) {
        boolean z;
        int judgeCorridinate;
        float f;
        float[] fArr;
        int i;
        String str;
        this.mRlSeekOutterHeight = (float) this.mRlSeekOutter.getHeight();
        this.mCurrentX = motionEvent.getRawX();
        float rawY = motionEvent.getRawY();
        this.mEventType = motionEvent.getAction();
        int action = motionEvent.getAction();
        String str2 = "CustomSeekBar";
        if (action != 0) {
            if (action == 1) {
                Log.i(str2, "MotionEvent.ACTION_UP");
            } else if (action == 2) {
                str = "MotionEvent.ACTION_MOVE";
            }
            z = false;
            judgeCorridinate = judgeCorridinate(this.mCurrentX, rawY);
            StringBuilder sb = new StringBuilder();
            sb.append(" isEnlargeStateTemp : ");
            sb.append(z);
            sb.append("\n  mIsEnlargeState:");
            sb.append(this.mIsEnlargeState);
            sb.append("\n  typeCor: ");
            sb.append(judgeCorridinate);
            Log.i(str2, sb.toString());
            if (z != this.mIsEnlargeState) {
                this.mIsEnlargeState = z;
                startScaleAnim(this.mIsEnlargeState);
            }
            if (this.mIsRTL && judgeCorridinate > 0) {
                judgeCorridinate = (this.mConfigArray[1] + 1) - judgeCorridinate;
            }
            if (this.mCurrentValue != judgeCorridinate && judgeCorridinate > 0) {
                this.mCurrentValue = judgeCorridinate;
                f = this.mCurrentX;
                fArr = this.mRangeMin;
                if (f > fArr[0]) {
                    updateInnerContainer(0, true);
                    i = 0;
                } else {
                    i = this.mIsRTL ? (int) (((float) this.mTotalwidth) - (f - fArr[0])) : (int) (f - fArr[0]);
                    updateInnerContainer(i, true);
                }
                updateTvCurrent(false, (float) i);
                TextView textView = this.mTvCurrentValue;
                StringBuilder sb2 = new StringBuilder();
                sb2.append(String.valueOf(this.mCurrentValue * this.mValueEnlargeTimes));
                sb2.append(getUnitString());
                textView.setText(sb2.toString());
            }
            return true;
        }
        str = "MotionEvent.ACTION_DOWN";
        Log.i(str2, str);
        z = true;
        judgeCorridinate = judgeCorridinate(this.mCurrentX, rawY);
        StringBuilder sb3 = new StringBuilder();
        sb3.append(" isEnlargeStateTemp : ");
        sb3.append(z);
        sb3.append("\n  mIsEnlargeState:");
        sb3.append(this.mIsEnlargeState);
        sb3.append("\n  typeCor: ");
        sb3.append(judgeCorridinate);
        Log.i(str2, sb3.toString());
        if (z != this.mIsEnlargeState) {
        }
        judgeCorridinate = (this.mConfigArray[1] + 1) - judgeCorridinate;
        this.mCurrentValue = judgeCorridinate;
        f = this.mCurrentX;
        fArr = this.mRangeMin;
        if (f > fArr[0]) {
        }
        updateTvCurrent(false, (float) i);
        TextView textView2 = this.mTvCurrentValue;
        StringBuilder sb22 = new StringBuilder();
        sb22.append(String.valueOf(this.mCurrentValue * this.mValueEnlargeTimes));
        sb22.append(getUnitString());
        textView2.setText(sb22.toString());
        return true;
    }
}
