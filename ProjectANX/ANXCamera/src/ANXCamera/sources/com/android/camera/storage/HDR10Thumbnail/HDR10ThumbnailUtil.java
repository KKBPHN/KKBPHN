package com.android.camera.storage.HDR10Thumbnail;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.opengl.EGLContext;
import android.opengl.GLES20;
import com.android.camera.effect.framework.gles.EglCore;
import com.android.camera.effect.framework.gles.EglSurfaceBase;
import com.android.camera.effect.framework.gles.PbufferSurface;
import com.android.camera.log.Log;
import com.android.camera.storage.HDR10Thumbnail.gles.LutRenderX;
import com.android.camera.storage.HDR10Thumbnail.gles.OpenGLUtils;
import java.nio.IntBuffer;

public class HDR10ThumbnailUtil {
    private static final String TAG = "HDR10ThumbnailUtil";
    private static Bitmap mBitmap;
    private static EglCore mEglCore;
    private static LutRenderX mLutRender;
    private static EglSurfaceBase mRenderSurface;

    public static Bitmap getHdr10Bitmap(EGLContext eGLContext, Bitmap bitmap) {
        mBitmap = bitmap;
        long currentTimeMillis = System.currentTimeMillis();
        mEglCore = new EglCore(eGLContext, 2);
        mRenderSurface = new PbufferSurface(mEglCore, mBitmap.getWidth(), mBitmap.getHeight());
        mRenderSurface.makeCurrent();
        mLutRender = new LutRenderX();
        long currentTimeMillis2 = System.currentTimeMillis();
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("getHdr10Bitmap initGL cost ");
        sb.append(currentTimeMillis2 - currentTimeMillis);
        Log.i(str, sb.toString());
        int loadTexture = OpenGLUtils.loadTexture(mBitmap, -1, true);
        long currentTimeMillis3 = System.currentTimeMillis();
        String str2 = TAG;
        StringBuilder sb2 = new StringBuilder();
        sb2.append("getHdr10Bitmap upload to GPU cost ");
        sb2.append(currentTimeMillis3 - currentTimeMillis2);
        Log.i(str2, sb2.toString());
        mLutRender.draw(loadTexture, OpenGLUtils.loadTexture1d());
        long currentTimeMillis4 = System.currentTimeMillis();
        String str3 = TAG;
        StringBuilder sb3 = new StringBuilder();
        sb3.append("getHdr10Bitmap render cost ");
        sb3.append(currentTimeMillis4 - currentTimeMillis3);
        Log.i(str3, sb3.toString());
        Bitmap pixelsFromBuffer = getPixelsFromBuffer(0, 0, mBitmap.getWidth(), mBitmap.getHeight());
        long currentTimeMillis5 = System.currentTimeMillis();
        String str4 = TAG;
        StringBuilder sb4 = new StringBuilder();
        sb4.append("getHdr10Bitmap read gpu out cost ");
        sb4.append(currentTimeMillis5 - currentTimeMillis4);
        Log.i(str4, sb4.toString());
        GLES20.glDeleteTextures(1, new int[]{loadTexture}, 0);
        mRenderSurface.makeNothingCurrent();
        mRenderSurface.releaseEglSurface();
        mEglCore.release();
        long currentTimeMillis6 = System.currentTimeMillis();
        String str5 = TAG;
        StringBuilder sb5 = new StringBuilder();
        sb5.append("getHdr10Bitmap release gl cost ");
        sb5.append(currentTimeMillis6 - currentTimeMillis5);
        Log.i(str5, sb5.toString());
        String str6 = TAG;
        StringBuilder sb6 = new StringBuilder();
        sb6.append("getHdr10Bitmap total cost ");
        sb6.append(currentTimeMillis6 - currentTimeMillis);
        Log.d(str6, sb6.toString());
        return pixelsFromBuffer;
    }

    private static Bitmap getPixelsFromBuffer(int i, int i2, int i3, int i4) {
        int[] iArr = new int[((i2 + i4) * i3)];
        int[] iArr2 = new int[(i3 * i4)];
        IntBuffer wrap = IntBuffer.wrap(iArr);
        wrap.position(0);
        GLES20.glReadPixels(i, i2, i3, i4, 6408, 5121, wrap);
        int i5 = 0;
        int i6 = 0;
        while (i5 < i4) {
            for (int i7 = 0; i7 < i3; i7++) {
                int i8 = iArr[(i5 * i3) + i7];
                iArr2[(((i4 - i6) - 1) * i3) + i7] = (i8 & -16711936) | ((i8 << 16) & 16711680) | ((i8 >> 16) & 255);
            }
            i5++;
            i6++;
        }
        return Bitmap.createBitmap(iArr2, i3, i4, Config.ARGB_8888);
    }
}
