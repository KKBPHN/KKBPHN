package com.android.camera.effect.renders;

import O00000Oo.O00000oO.O000000o.C0122O00000o;
import com.android.camera.Util;
import com.android.camera.log.Log;
import com.android.gallery3d.ui.BasicTexture;
import com.android.gallery3d.ui.StringTexture;

public class NewStyleTextWaterMark extends WaterMark {
    private static final float RATIO = 0.87f;
    private static final String TAG = "NewStyleTextWaterMark";
    public static final int TEXT_COLOR = -1;
    public static final float TEXT_PIXEL_SIZE = 92.0f;
    private int mCenterX;
    private int mCenterY;
    private int mCharMargin;
    private boolean mIsCinematicAspectRatio;
    private int mPaddingX;
    private int mPaddingY;
    private int mWaterHeight;
    private String mWaterText;
    private BasicTexture mWaterTexture;
    private int mWaterWidth;

    public NewStyleTextWaterMark(String str, int i, int i2, int i3, boolean z) {
        super(i, i2, i3);
        this.mIsCinematicAspectRatio = z;
        float watermarkRatio = Util.getWatermarkRatio(i, i2);
        float textSize = getTextSize() * watermarkRatio;
        this.mPaddingX = Math.round(Util.getWaterMarkPaddingX() * watermarkRatio) & -2;
        this.mPaddingY = Math.round(Util.getWaterMarkPaddingY() * watermarkRatio) & -2;
        if (this.mIsCinematicAspectRatio) {
            textSize *= Util.getCinematicAspectWaterMarkRatio();
            this.mPaddingX = (int) (((float) this.mPaddingX) * Util.getCinematicAspectWaterMarkRatio());
            this.mPaddingY = (int) (((float) this.mPaddingY) * Util.getCinematicAspectWaterMarkRatio());
            if ((i3 == 90 || i3 == 270) && i > i2) {
                int i4 = i2;
                i2 = i;
                i = i4;
            }
            int watermarkCinematicAspectMargin = Util.getWatermarkCinematicAspectMargin(i, i2);
            if (i < i2) {
                this.mPaddingX += watermarkCinematicAspectMargin;
            } else {
                this.mPaddingY += watermarkCinematicAspectMargin;
            }
        }
        String str2 = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("ImageWaterMark: textSize = ");
        sb.append(textSize);
        String str3 = ", mPaddingX = ";
        sb.append(str3);
        sb.append(this.mPaddingX);
        String str4 = ", mPaddingY = ";
        sb.append(str4);
        sb.append(this.mPaddingY);
        Log.d(str2, sb.toString());
        this.mWaterText = str;
        this.mWaterTexture = StringTexture.newInstance(this.mWaterText, textSize, -1, 2);
        this.mWaterWidth = this.mWaterTexture.getWidth();
        this.mWaterHeight = this.mWaterTexture.getHeight();
        this.mCharMargin = (int) ((((float) this.mWaterHeight) * (1.0f - getTextMargin())) / 2.0f);
        if (!C0122O00000o.instance().OO0oo()) {
            if (i3 == 90 || i3 == 270) {
                this.mPaddingX = (this.mPaddingX - this.mCharMargin) & -2;
            } else {
                this.mPaddingY = (this.mPaddingY - this.mCharMargin) & -2;
            }
        }
        String str5 = TAG;
        StringBuilder sb2 = new StringBuilder();
        sb2.append("ImageWaterMark: after mWaterWidth = ");
        sb2.append(this.mWaterWidth);
        sb2.append(", mWaterHeight = ");
        sb2.append(this.mWaterHeight);
        sb2.append(", mCharMargin = ");
        sb2.append(this.mCharMargin);
        sb2.append(str3);
        sb2.append(this.mPaddingX);
        sb2.append(str4);
        sb2.append(this.mPaddingY);
        Log.d(str5, sb2.toString());
        calcCenterAxis();
        if (Util.sIsDumpLog) {
            print();
        }
    }

    private void calcCenterAxis() {
        int i;
        int i2;
        int i3;
        int i4;
        int i5;
        int i6 = this.mOrientation;
        if (i6 != 0) {
            if (i6 == 90) {
                this.mCenterX = (this.mPictureWidth - this.mPaddingY) - (this.mWaterHeight / 2);
                i5 = this.mPaddingX;
                i4 = this.mWaterWidth;
            } else if (i6 == 180) {
                this.mCenterX = this.mPaddingX + (this.mWaterWidth / 2);
                i5 = this.mPaddingY;
                i4 = this.mWaterHeight;
            } else if (i6 == 270) {
                this.mCenterX = this.mPaddingY + (this.mWaterHeight / 2);
                i3 = this.mPictureHeight - this.mPaddingX;
                i2 = this.mWaterWidth;
            } else {
                return;
            }
            i = i5 + (i4 / 2);
            this.mCenterY = i;
        }
        this.mCenterX = (this.mPictureWidth - this.mPaddingX) - (this.mWaterWidth / 2);
        i3 = this.mPictureHeight - this.mPaddingY;
        i2 = this.mWaterHeight;
        i = i3 - (i2 / 2);
        this.mCenterY = i;
    }

    private float getTextMargin() {
        if (C0122O00000o.instance().OO0oo()) {
            return 0.6f;
        }
        return RATIO;
    }

    private float getTextSize() {
        return C0122O00000o.instance().OO0oo() ? 75.0f : 92.0f;
    }

    private void print() {
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("WaterMark pictureWidth=");
        sb.append(this.mPictureWidth);
        sb.append(" pictureHeight=");
        sb.append(this.mPictureHeight);
        sb.append(" waterText=");
        sb.append(this.mWaterText);
        sb.append(" centerX=");
        sb.append(this.mCenterX);
        sb.append(" centerY=");
        sb.append(this.mCenterY);
        sb.append(" waterWidth=");
        sb.append(this.mWaterWidth);
        sb.append(" waterHeight=");
        sb.append(this.mWaterHeight);
        sb.append(" paddingX=");
        sb.append(this.mPaddingX);
        sb.append(" paddingY=");
        sb.append(this.mPaddingY);
        Log.v(str, sb.toString());
    }

    public int getCenterX() {
        return this.mCenterX;
    }

    public int getCenterY() {
        return this.mCenterY;
    }

    public int getHeight() {
        return this.mWaterHeight;
    }

    public /* bridge */ /* synthetic */ int getLeft() {
        return super.getLeft();
    }

    public int getPaddingX() {
        return this.mPaddingX;
    }

    public int getPaddingY() {
        return this.mPaddingY;
    }

    public BasicTexture getTexture() {
        return this.mWaterTexture;
    }

    public /* bridge */ /* synthetic */ int getTop() {
        return super.getTop();
    }

    public int getWidth() {
        return this.mWaterWidth;
    }
}
