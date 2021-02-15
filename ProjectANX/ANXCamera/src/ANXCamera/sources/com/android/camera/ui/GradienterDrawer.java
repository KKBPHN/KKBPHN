package com.android.camera.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.os.Looper;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import com.android.camera.R;
import com.android.camera.customization.TintColor;
import com.android.camera.dualvideo.render.RenderUtil;
import com.android.camera.effect.EffectController;
import com.android.camera.log.Log;

public class GradienterDrawer extends RelativeLayout {
    public static final int COLOR_NORMAL = -1711276033;
    public static final int COLOR_SELECTED = -13840129;
    public static final String TAG = "GradienterDrawer";
    private boolean isReferenceLineEnabled;
    private boolean isSquareModule;
    private Direct mCurrentDirect = Direct.NONE;
    private float mDeviceRotation = 0.0f;
    private View mLineLeftView;
    private int mLineLongColor = RenderUtil.GRID_LINE_COLOR;
    private int mLineLongWidth = 1;
    private View mLineRightView;
    private int mLineShortColor = COLOR_SELECTED;
    private View mLineShortView;
    private int mLineShortWidth = 6;
    private int mParentHeighth = 0;
    private int mParentWidth = 0;
    private LinearLayout mRootView;

    /* renamed from: com.android.camera.ui.GradienterDrawer$1 reason: invalid class name */
    /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$com$android$camera$ui$GradienterDrawer$Direct = new int[Direct.values().length];

        /* JADX WARNING: Can't wrap try/catch for region: R(10:0|1|2|3|4|5|6|7|8|10) */
        /* JADX WARNING: Can't wrap try/catch for region: R(8:0|1|2|3|4|5|6|(3:7|8|10)) */
        /* JADX WARNING: Failed to process nested try/catch */
        /* JADX WARNING: Missing exception handler attribute for start block: B:3:0x0014 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:5:0x001f */
        /* JADX WARNING: Missing exception handler attribute for start block: B:7:0x002a */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        static {
            $SwitchMap$com$android$camera$ui$GradienterDrawer$Direct[Direct.TOP.ordinal()] = 1;
            $SwitchMap$com$android$camera$ui$GradienterDrawer$Direct[Direct.BOTTOM.ordinal()] = 2;
            $SwitchMap$com$android$camera$ui$GradienterDrawer$Direct[Direct.RIGHT.ordinal()] = 3;
            try {
                $SwitchMap$com$android$camera$ui$GradienterDrawer$Direct[Direct.LEFT.ordinal()] = 4;
            } catch (NoSuchFieldError unused) {
            }
        }
    }

    enum Direct {
        NONE,
        TOP,
        BOTTOM,
        LEFT,
        RIGHT
    }

    public GradienterDrawer(Context context) {
        super(context);
        init(context);
    }

    public GradienterDrawer(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init(context);
    }

    public GradienterDrawer(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        init(context);
    }

    private void init(Context context) {
        if (getChildCount() > 0) {
            removeAllViews();
        }
        this.mRootView = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.v6_preview_gradienter, this, false);
        addView(this.mRootView, new LayoutParams(-1, -1));
        this.mLineShortView = this.mRootView.findViewById(R.id.view_line_short);
        this.mLineLeftView = this.mRootView.findViewById(R.id.view_line_left);
        this.mLineRightView = this.mRootView.findViewById(R.id.view_line_right);
    }

    private void invalidateView() {
        if (Looper.getMainLooper() == Looper.myLooper()) {
            invalidate();
        } else {
            postInvalidate();
        }
    }

    private void resetMargin() {
        int i;
        int i2;
        int i3;
        int i4;
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) this.mLineShortView.getLayoutParams();
        if (this.isReferenceLineEnabled) {
            resetParams(layoutParams);
            setViewVisible(this.mLineRightView, 8);
            setViewVisible(this.mLineLeftView, 8);
            int i5 = AnonymousClass1.$SwitchMap$com$android$camera$ui$GradienterDrawer$Direct[this.mCurrentDirect.ordinal()];
            if (i5 == 1) {
                if (!this.isSquareModule) {
                    i2 = this.mParentHeighth / 3;
                } else {
                    int i6 = this.mParentWidth;
                    i2 = (i6 / 3) + ((this.mParentHeighth - i6) / 2);
                }
                i = this.mParentWidth / 3;
                this.mRootView.setOrientation(0);
                this.mRootView.setGravity(80);
                int i7 = this.mLineShortWidth;
                layoutParams.height = i7;
                layoutParams.width = -1;
                layoutParams.bottomMargin = i2 - (i7 / 2);
            } else if (i5 != 2) {
                if (i5 != 3) {
                    if (i5 == 4) {
                        int i8 = this.mParentWidth;
                        int i9 = i8 / 3;
                        i4 = !this.isSquareModule ? this.mParentHeighth / 3 : (i8 / 3) + ((this.mParentHeighth - i8) / 2);
                        this.mRootView.setOrientation(1);
                        this.mRootView.setGravity(5);
                        int i10 = this.mLineShortWidth;
                        layoutParams.width = i10;
                        layoutParams.height = -1;
                        layoutParams.rightMargin = i9 - (i10 / 2);
                    }
                    this.mLineShortView.setLayoutParams(layoutParams);
                    return;
                }
                int i11 = this.mParentWidth;
                int i12 = i11 / 3;
                i4 = !this.isSquareModule ? this.mParentHeighth / 3 : (i11 / 3) + ((this.mParentHeighth - i11) / 2);
                this.mRootView.setOrientation(1);
                this.mRootView.setGravity(3);
                int i13 = this.mLineShortWidth;
                layoutParams.width = i13;
                layoutParams.height = -1;
                layoutParams.leftMargin = i12 - (i13 / 2);
                layoutParams.topMargin = i4;
                layoutParams.bottomMargin = i4;
                this.mLineShortView.setLayoutParams(layoutParams);
                return;
            } else {
                if (!this.isSquareModule) {
                    i3 = this.mParentHeighth / 3;
                } else {
                    int i14 = this.mParentWidth;
                    i3 = (i14 / 3) + ((this.mParentHeighth - i14) / 2);
                }
                i = this.mParentWidth / 3;
                this.mRootView.setOrientation(0);
                this.mRootView.setGravity(48);
                int i15 = this.mLineShortWidth;
                layoutParams.height = i15;
                layoutParams.width = -1;
                layoutParams.topMargin = i3 - (i15 / 2);
            }
            layoutParams.leftMargin = i;
            layoutParams.rightMargin = i;
            this.mLineShortView.setLayoutParams(layoutParams);
            return;
        }
        LinearLayout.LayoutParams layoutParams2 = (LinearLayout.LayoutParams) this.mLineLeftView.getLayoutParams();
        LinearLayout.LayoutParams layoutParams3 = (LinearLayout.LayoutParams) this.mLineRightView.getLayoutParams();
        resetParams(layoutParams, layoutParams2, layoutParams3);
        setViewVisible(this.mLineRightView, 0);
        setViewVisible(this.mLineLeftView, 0);
        int i16 = AnonymousClass1.$SwitchMap$com$android$camera$ui$GradienterDrawer$Direct[this.mCurrentDirect.ordinal()];
        if (i16 == 1 || i16 == 2) {
            int i17 = this.mParentHeighth / 2;
            this.mRootView.setOrientation(0);
            this.mRootView.setGravity(48);
            int i18 = this.mLineShortWidth;
            layoutParams.height = i18;
            int i19 = this.mLineLongWidth;
            layoutParams2.height = i19;
            layoutParams3.height = i19;
            layoutParams.width = -1;
            layoutParams2.width = -1;
            layoutParams3.width = -1;
            layoutParams.topMargin = i17 - (i18 / 2);
            layoutParams2.topMargin = i17 - (i19 / 2);
            layoutParams3.topMargin = i17 - (i19 / 2);
        } else if (i16 == 3 || i16 == 4) {
            int i20 = this.mParentWidth / 2;
            this.mRootView.setOrientation(1);
            this.mRootView.setGravity(5);
            layoutParams.width = this.mLineShortWidth;
            int i21 = this.mLineLongWidth;
            layoutParams2.width = i21;
            layoutParams3.width = i21;
            if (!this.isSquareModule) {
                layoutParams.height = -1;
                layoutParams2.height = -1;
                layoutParams3.height = -1;
            } else {
                int i22 = this.mParentWidth;
                layoutParams.height = i22 / 3;
                int i23 = this.mParentHeighth;
                layoutParams2.height = (i23 - (i22 / 3)) / 2;
                layoutParams3.height = (i23 - (i22 / 3)) / 2;
            }
            layoutParams.rightMargin = i20 - (this.mLineShortWidth / 2);
            int i24 = this.mLineLongWidth;
            layoutParams2.rightMargin = i20 - (i24 / 2);
            layoutParams3.rightMargin = i20 - (i24 / 2);
        }
        this.mLineShortView.setLayoutParams(layoutParams);
        this.mLineLeftView.setLayoutParams(layoutParams2);
        this.mLineRightView.setLayoutParams(layoutParams3);
    }

    private void resetParams(LinearLayout.LayoutParams... layoutParamsArr) {
        for (LinearLayout.LayoutParams layoutParams : layoutParamsArr) {
            layoutParams.rightMargin = 0;
            layoutParams.leftMargin = 0;
            layoutParams.topMargin = 0;
            layoutParams.bottomMargin = 0;
        }
    }

    private void setViewVisible(View view, int i) {
        if (view.getVisibility() != i) {
            view.setVisibility(i);
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:24:0x0065  */
    /* JADX WARNING: Removed duplicated region for block: B:27:0x0070  */
    /* JADX WARNING: Removed duplicated region for block: B:30:0x007f  */
    /* JADX WARNING: Removed duplicated region for block: B:31:0x0084  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void updateView() {
        Direct direct;
        float f;
        this.mDeviceRotation = EffectController.getInstance().getDeviceRotation();
        float f2 = this.mDeviceRotation;
        if (f2 <= 45.0f || f2 >= 135.0f) {
            f2 = this.mDeviceRotation;
            if (f2 < 135.0f || f2 >= 225.0f) {
                f2 = this.mDeviceRotation;
                if (f2 <= 225.0f || f2 >= 315.0f) {
                    direct = Direct.BOTTOM;
                    if (this.mDeviceRotation == -1.0f) {
                        this.mDeviceRotation = -5.0f;
                    }
                    f2 = this.mDeviceRotation;
                    if (f2 > 300.0f) {
                        f = 360.0f;
                    }
                    if (Math.abs(f2) <= 3.0f) {
                        f2 = 0.0f;
                    }
                    setViewVisible(this.mLineShortView, 0);
                    if (direct != this.mCurrentDirect) {
                        setViewVisible(this.mLineShortView, 4);
                        this.mCurrentDirect = direct;
                        resetMargin();
                    }
                    setLineShortColor(f2 != 0.0f ? TintColor.tintColor() : -1711276033);
                    this.mLineShortView.setRotation(-f2);
                    String str = TAG;
                    StringBuilder sb = new StringBuilder();
                    sb.append("updateView  rotationOffset : ");
                    sb.append(f2);
                    sb.append(";  mDeviceRotation :");
                    sb.append(this.mDeviceRotation);
                    Log.i(str, sb.toString());
                    invalidateView();
                }
                direct = Direct.LEFT;
                f = 270.0f;
            } else {
                direct = Direct.TOP;
                f = 180.0f;
            }
        } else {
            direct = Direct.RIGHT;
            f = 90.0f;
        }
        f2 -= f;
        if (Math.abs(f2) <= 3.0f) {
        }
        setViewVisible(this.mLineShortView, 0);
        if (direct != this.mCurrentDirect) {
        }
        setLineShortColor(f2 != 0.0f ? TintColor.tintColor() : -1711276033);
        this.mLineShortView.setRotation(-f2);
        String str2 = TAG;
        StringBuilder sb2 = new StringBuilder();
        sb2.append("updateView  rotationOffset : ");
        sb2.append(f2);
        sb2.append(";  mDeviceRotation :");
        sb2.append(this.mDeviceRotation);
        Log.i(str2, sb2.toString());
        invalidateView();
    }

    /* access modifiers changed from: protected */
    public void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        updateView();
    }

    /* access modifiers changed from: protected */
    public void onVisibilityChanged(View view, int i) {
        super.onVisibilityChanged(view, i);
        if (i == 0) {
            this.mCurrentDirect = Direct.NONE;
        }
    }

    public void setConfigInfo(int i, int i2, boolean z) {
        this.mParentWidth = i;
        this.mParentHeighth = i2;
        this.isSquareModule = z;
        this.mCurrentDirect = Direct.NONE;
        updateView();
    }

    public void setLineShortColor(int i) {
        if (this.mLineShortColor != i) {
            this.mLineShortColor = i;
            this.mLineShortView.setBackgroundColor(i);
        }
    }

    public void setReferenceLineEnabled(boolean z) {
        this.mCurrentDirect = Direct.NONE;
        this.isReferenceLineEnabled = z;
    }

    public void setlineWidth(int i, int i2) {
        this.mLineLongWidth = i;
        this.mLineShortWidth = i2;
        this.mCurrentDirect = Direct.NONE;
    }
}
