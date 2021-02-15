package com.android.camera.effect.draw_mode;

import com.android.gallery3d.ui.ExtTexture;
import java.util.Arrays;

public class DrawExtTexAttribute extends DrawRectShapeAttributeBase {
    public boolean mEffectPopup;
    public ExtTexture mExtTexture;
    public int mRotation;
    public float[] mTextureTransform;

    public DrawExtTexAttribute() {
        this.mEffectPopup = false;
        this.mRotation = 0;
        this.mTarget = 8;
    }

    public DrawExtTexAttribute(ExtTexture extTexture, float[] fArr, int i, int i2, int i3, int i4) {
        this.mEffectPopup = false;
        this.mRotation = 0;
        this.mX = i;
        this.mY = i2;
        this.mWidth = i3;
        this.mHeight = i4;
        this.mExtTexture = extTexture;
        this.mTextureTransform = fArr;
        this.mTarget = 8;
    }

    public DrawExtTexAttribute(boolean z) {
        this.mEffectPopup = false;
        this.mRotation = 0;
        this.mEffectPopup = z;
        this.mTarget = 8;
    }

    public DrawExtTexAttribute init(ExtTexture extTexture, float[] fArr, int i, int i2, int i3, int i4) {
        this.mX = i;
        this.mY = i2;
        this.mWidth = i3;
        this.mHeight = i4;
        this.mExtTexture = extTexture;
        this.mTextureTransform = fArr;
        return this;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("DrawExtTexAttribute{mX=");
        sb.append(this.mX);
        sb.append(", mY=");
        sb.append(this.mY);
        sb.append(", mWidth=");
        sb.append(this.mWidth);
        sb.append(", mHeight=");
        sb.append(this.mHeight);
        sb.append(", mTextureTransform=");
        sb.append(Arrays.toString(this.mTextureTransform));
        sb.append(", mExtTexture=");
        sb.append(this.mExtTexture);
        sb.append(", mEffectPopup=");
        sb.append(this.mEffectPopup);
        sb.append(", mRotation = ");
        sb.append(this.mRotation);
        sb.append('}');
        return sb.toString();
    }
}
