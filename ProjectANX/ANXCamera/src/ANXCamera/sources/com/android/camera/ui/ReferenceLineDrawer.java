package com.android.camera.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.util.AttributeSet;
import android.view.View;
import com.android.camera.dualvideo.render.RenderUtil;
import com.android.camera.effect.EffectController;

public class ReferenceLineDrawer extends View {
    private static final int BORDER = 1;
    public static final String TAG = "ReferenceLineDrawer";
    private boolean isGradienterEnabled;
    private boolean mBottomVisible = true;
    private int mColumnCount = 1;
    private Direct mCurrentDirect = Direct.NONE;
    private float mDeviceRotation = 0.0f;
    private int mFrameColor = 402653184;
    private Paint mFramePaint;
    private int mLineColor = RenderUtil.GRID_LINE_COLOR;
    private Paint mLinePaint;
    private int mRowCount = 1;
    private boolean mTopVisible = true;

    public ReferenceLineDrawer(Context context) {
        super(context);
    }

    public ReferenceLineDrawer(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public ReferenceLineDrawer(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }

    private void resetline(Canvas canvas) {
        float f;
        float f2;
        float f3;
        int i;
        int i2;
        float f4;
        float f5;
        float f6;
        int i3;
        int i4;
        int width = getWidth() - 1;
        int height = getHeight() - 1;
        int i5 = 1;
        while (true) {
            int i6 = this.mColumnCount;
            if (i5 >= i6) {
                break;
            }
            if (this.isGradienterEnabled && this.mCurrentDirect == Direct.LEFT && i5 == 2) {
                int i7 = i5 * width;
                canvas.drawRect((float) (i7 / i6), 1.0f, (float) ((i7 / i6) + 3), (float) (height / this.mRowCount), this.mFramePaint);
                int i8 = this.mColumnCount;
                f6 = (float) (i7 / i8);
                int i9 = this.mRowCount;
                f5 = (float) (((height / i9) * (i9 - 1)) + 1);
                f4 = (float) ((i7 / i8) + 3);
            } else {
                if (this.isGradienterEnabled && this.mCurrentDirect == Direct.RIGHT && i5 == 1) {
                    i4 = i5 * width;
                    int i10 = this.mColumnCount;
                    canvas.drawRect((float) (i4 / i10), 1.0f, (float) ((i4 / i10) + 3), (float) (height / this.mRowCount), this.mFramePaint);
                    i3 = this.mColumnCount;
                    f6 = (float) (i4 / i3);
                    int i11 = this.mRowCount;
                    f5 = (float) (((height / i11) * (i11 - 1)) + 1);
                } else {
                    i4 = i5 * width;
                    i3 = this.mColumnCount;
                    f6 = (float) (i4 / i3);
                    f5 = 1.0f;
                }
                f4 = (float) ((i4 / i3) + 3);
            }
            canvas.drawRect(f6, f5, f4, (float) (height - 1), this.mFramePaint);
            i5++;
        }
        boolean z = !this.mBottomVisible;
        int i12 = 0;
        int i13 = 0;
        while (true) {
            int i14 = this.mRowCount;
            if (i13 > i14) {
                break;
            }
            if (!(i13 == 0 || i13 == i14) || ((i13 == 0 && this.mTopVisible) || (i13 == this.mRowCount && this.mBottomVisible))) {
                if ((this.isGradienterEnabled && this.mCurrentDirect == Direct.BOTTOM && i13 == 1) || (this.isGradienterEnabled && this.mCurrentDirect == Direct.TOP && i13 == 2)) {
                    float f7 = z ? 1.0f : 0.0f;
                    int i15 = i13 * height;
                    int i16 = this.mRowCount;
                    canvas.drawRect(f7, (float) (i15 / i16), (float) (width / this.mColumnCount), (float) ((i15 / i16) + 3), this.mFramePaint);
                    int i17 = this.mColumnCount;
                    float f8 = (float) (((width / i17) * (i17 - 1)) + z);
                    int i18 = this.mRowCount;
                    canvas.drawRect(f8, (float) (i15 / i18), (float) (width - z), (float) ((i15 / i18) + 3), this.mFramePaint);
                } else {
                    float f9 = z ? 1.0f : 0.0f;
                    int i19 = i13 * height;
                    int i20 = this.mRowCount;
                    canvas.drawRect(f9, (float) (i19 / i20), (float) (width - z), (float) ((i19 / i20) + 3), this.mFramePaint);
                }
            }
            i13++;
        }
        int i21 = 1;
        while (true) {
            int i22 = this.mColumnCount;
            if (i21 >= i22) {
                break;
            }
            if (this.isGradienterEnabled && this.mCurrentDirect == Direct.RIGHT && i21 == 1) {
                int i23 = i21 * width;
                canvas.drawRect((float) (i23 / i22), 1.0f, (float) ((i23 / i22) + 2), (float) (height / this.mRowCount), this.mLinePaint);
                int i24 = this.mColumnCount;
                f3 = (float) (i23 / i24);
                int i25 = this.mRowCount;
                f2 = (float) (((height / i25) * (i25 - 1)) + 1);
                f = (float) ((i23 / i24) + 2);
            } else {
                if (this.isGradienterEnabled && this.mCurrentDirect == Direct.LEFT && i21 == 2) {
                    i2 = i21 * width;
                    int i26 = this.mColumnCount;
                    canvas.drawRect((float) (i2 / i26), 1.0f, (float) ((i2 / i26) + 2), (float) (height / this.mRowCount), this.mLinePaint);
                    i = this.mColumnCount;
                    f3 = (float) (i2 / i);
                    int i27 = this.mRowCount;
                    f2 = (float) (((height / i27) * (i27 - 1)) + 1);
                } else {
                    i2 = i21 * width;
                    i = this.mColumnCount;
                    f3 = (float) (i2 / i);
                    f2 = 1.0f;
                }
                f = (float) ((i2 / i) + 2);
            }
            canvas.drawRect(f3, f2, f, (float) (height - 1), this.mLinePaint);
            i21++;
        }
        while (true) {
            int i28 = this.mRowCount;
            if (i12 <= i28) {
                if (!(i12 == 0 || i12 == i28) || ((i12 == 0 && this.mTopVisible) || (i12 == this.mRowCount && this.mBottomVisible))) {
                    if ((this.isGradienterEnabled && this.mCurrentDirect == Direct.BOTTOM && i12 == 1) || (this.isGradienterEnabled && this.mCurrentDirect == Direct.TOP && i12 == 2)) {
                        float f10 = z ? 1.0f : 0.0f;
                        int i29 = i12 * height;
                        int i30 = this.mRowCount;
                        canvas.drawRect(f10, (float) (i29 / i30), (float) (width / this.mColumnCount), (float) ((i29 / i30) + 2), this.mLinePaint);
                        int i31 = this.mColumnCount;
                        float f11 = (float) (((width / i31) * (i31 - 1)) + z);
                        int i32 = this.mRowCount;
                        canvas.drawRect(f11, (float) (i29 / i32), (float) (width - z), (float) ((i29 / i32) + 2), this.mLinePaint);
                    } else {
                        float f12 = z ? 1.0f : 0.0f;
                        int i33 = i12 * height;
                        int i34 = this.mRowCount;
                        canvas.drawRect(f12, (float) (i33 / i34), (float) (width - z), (float) ((i33 / i34) + 2), this.mLinePaint);
                    }
                }
                i12++;
            } else {
                return;
            }
        }
    }

    private void updateView(Canvas canvas) {
        Direct direct;
        this.mDeviceRotation = EffectController.getInstance().getDeviceRotation();
        float f = this.mDeviceRotation;
        if (f <= 45.0f || f >= 135.0f) {
            float f2 = this.mDeviceRotation;
            if (f2 < 135.0f || f2 >= 225.0f) {
                float f3 = this.mDeviceRotation;
                if (f3 <= 225.0f || f3 >= 315.0f) {
                    direct = Direct.BOTTOM;
                    int i = (this.mDeviceRotation > 300.0f ? 1 : (this.mDeviceRotation == 300.0f ? 0 : -1));
                } else {
                    direct = Direct.LEFT;
                }
            } else {
                direct = Direct.TOP;
            }
        } else {
            direct = Direct.RIGHT;
        }
        if (direct != this.mCurrentDirect) {
            this.mCurrentDirect = direct;
        }
        resetline(canvas);
        if (this.isGradienterEnabled) {
            invalidate();
        }
    }

    public void initialize(int i, int i2) {
        this.mColumnCount = Math.max(i2, 1);
        this.mRowCount = Math.max(i, 1);
        this.mLinePaint = new Paint();
        this.mFramePaint = new Paint();
        this.mLinePaint.setStrokeWidth(1.0f);
        this.mFramePaint.setStrokeWidth(1.0f);
        this.mLinePaint.setStyle(Style.FILL);
        this.mFramePaint.setStyle(Style.STROKE);
        this.mLinePaint.setColor(this.mLineColor);
        this.mFramePaint.setColor(this.mFrameColor);
    }

    /* access modifiers changed from: protected */
    public void onDraw(Canvas canvas) {
        updateView(canvas);
        super.onDraw(canvas);
    }

    /* access modifiers changed from: protected */
    public void onVisibilityChanged(View view, int i) {
        super.onVisibilityChanged(view, i);
        if (i == 0) {
            this.mCurrentDirect = Direct.NONE;
        }
    }

    public void setBorderVisible(boolean z, boolean z2) {
        if (this.mTopVisible != z || this.mBottomVisible != z2) {
            this.mTopVisible = z;
            this.mBottomVisible = z2;
            invalidate();
        }
    }

    public void setGradienterEnabled(boolean z) {
        this.isGradienterEnabled = z;
        if (getVisibility() == 0) {
            this.mCurrentDirect = Direct.NONE;
            invalidate();
        }
    }

    public void setLineColor(int i) {
        this.mLineColor = i;
    }
}
