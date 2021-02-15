package com.android.camera.effect.renders;

import O00000Oo.O00000oO.O000000o.C0122O00000o;
import O00000Oo.O00000oO.O000000o.C0124O00000oO;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Rect;
import android.graphics.RectF;
import android.media.Image;
import android.media.Image.Plane;
import android.opengl.EGLContext;
import android.opengl.GLES20;
import android.os.ConditionVariable;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.util.Size;
import com.android.camera.CameraAppImpl;
import com.android.camera.CameraSettings;
import com.android.camera.Util;
import com.android.camera.aiwatermark.data.WatermarkItem;
import com.android.camera.aiwatermark.util.WatermarkConstant;
import com.android.camera.data.data.runing.ComponentRunningTiltValue;
import com.android.camera.effect.FilterInfo;
import com.android.camera.effect.FrameBuffer;
import com.android.camera.effect.MiYuvImage;
import com.android.camera.effect.ShaderNativeUtil;
import com.android.camera.effect.SnapshotCanvas;
import com.android.camera.effect.draw_mode.DrawBasicTexAttribute;
import com.android.camera.effect.draw_mode.DrawYuvAttribute;
import com.android.camera.effect.framework.gles.EglCore;
import com.android.camera.effect.framework.gles.PbufferSurface;
import com.android.camera.effect.framework.graphics.Block;
import com.android.camera.effect.framework.graphics.Splitter;
import com.android.camera.effect.framework.image.MemYuvImage;
import com.android.camera.effect.framework.utils.CounterUtil;
import com.android.camera.log.Log;
import com.android.camera.statistic.CameraStatUtils;
import com.android.camera.watermark.WaterMarkBitmap;
import com.android.camera.watermark.WaterMarkData;
import com.arcsoft.supernight.SuperNightProcess;
import com.xiaomi.camera.base.ImageUtil;
import com.xiaomi.camera.core.FilterProcessor.YuvAttributeWrapper;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class SnapshotRender {
    private static final boolean DUMP_TEXTURE = false;
    private static final int EGL_CONTEXT_CLIENT_VERSION = 12440;
    private static final int QUEUE_LIMIT = 7;
    /* access modifiers changed from: private */
    public static final String TAG = "SnapshotRender";
    /* access modifiers changed from: private */
    public int mAdjHeight;
    /* access modifiers changed from: private */
    public int mAdjWidth;
    /* access modifiers changed from: private */
    public int mBlockHeight;
    /* access modifiers changed from: private */
    public int mBlockWidth;
    /* access modifiers changed from: private */
    public Bitmap mCinematicRatioWaterMarkBitmap;
    /* access modifiers changed from: private */
    public DeviceWatermarkParam mDeviceWaterMarkParam;
    /* access modifiers changed from: private */
    public Bitmap mDualCameraWaterMarkBitmap;
    /* access modifiers changed from: private */
    public EglCore mEglCore;
    private EGLHandler mEglHandler;
    private HandlerThread mEglThread;
    /* access modifiers changed from: private */
    public CounterUtil mFrameCounter;
    /* access modifiers changed from: private */
    public Bitmap mFrontCameraWaterMarkBitmap;
    /* access modifiers changed from: private */
    public volatile int mImageQueueSize;
    /* access modifiers changed from: private */
    public final Object mLock;
    /* access modifiers changed from: private */
    public MemYuvImage mMemImage;
    /* access modifiers changed from: private */
    public int mQuality;
    private boolean mRelease;
    /* access modifiers changed from: private */
    public boolean mReleasePending;
    /* access modifiers changed from: private */
    public PbufferSurface mRenderSurface;
    /* access modifiers changed from: private */
    public Splitter mSplitter;
    private int mTextureId;
    /* access modifiers changed from: private */
    public CounterUtil mTotalCounter;

    class EGLHandler extends Handler {
        public static final int MSG_DRAW_MAIN_ASYNC = 1;
        public static final int MSG_DRAW_MAIN_SYNC = 2;
        public static final int MSG_INIT_EGL_SYNC = 0;
        public static final int MSG_PREPARE_EFFECT_RENDER = 6;
        public static final int MSG_RELEASE = 5;
        private FrameBuffer mFrameBuffer;
        private SnapshotCanvas mGLCanvas;
        private FrameBuffer mWatermarkFrameBuffer;

        public EGLHandler(Looper looper) {
            super(looper);
        }

        /* JADX WARNING: Removed duplicated region for block: B:33:0x01fd  */
        /* JADX WARNING: Removed duplicated region for block: B:54:0x038b  */
        /* JADX WARNING: Removed duplicated region for block: B:57:0x03a2  */
        /* JADX WARNING: Removed duplicated region for block: B:59:0x03ab  */
        /* JADX WARNING: Removed duplicated region for block: B:60:0x03ad  */
        /* JADX WARNING: Removed duplicated region for block: B:63:0x0436  */
        /* JADX WARNING: Removed duplicated region for block: B:64:0x046b  */
        /* JADX WARNING: Removed duplicated region for block: B:67:0x04a0  */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        private byte[] applyEffect(DrawYuvAttribute drawYuvAttribute) {
            byte[] bArr;
            boolean z;
            String str;
            DrawYuvAttribute drawYuvAttribute2;
            boolean z2;
            int[] iArr;
            boolean z3;
            int i;
            int i2;
            int i3;
            int i4;
            int i5;
            int i6;
            boolean z4;
            DrawYuvAttribute drawYuvAttribute3;
            byte[] bArr2;
            int[] iArr2;
            int i7;
            int i8;
            byte[] bArr3;
            int i9;
            int i10;
            char c;
            int i11;
            int i12;
            boolean z5;
            Size size;
            DrawYuvAttribute drawYuvAttribute4 = drawYuvAttribute;
            PipeRender effectRender = getEffectRender(drawYuvAttribute);
            if (effectRender == null) {
                Log.w(SnapshotRender.TAG, "init render failed");
                return null;
            }
            int width = drawYuvAttribute4.mPictureSize.getWidth();
            int height = drawYuvAttribute4.mPictureSize.getHeight();
            long currentTimeMillis = System.currentTimeMillis();
            drawYuvAttribute4.mImage.getTimestamp();
            Plane plane = drawYuvAttribute4.mImage.getPlanes()[0];
            Plane plane2 = drawYuvAttribute4.mImage.getPlanes()[1];
            int rowStride = plane.getRowStride();
            int rowStride2 = plane2.getRowStride();
            String access$400 = SnapshotRender.TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("plane0 stride = ");
            sb.append(plane.getRowStride());
            sb.append(", plane1 stride = ");
            sb.append(plane2.getRowStride());
            Log.d(access$400, sb.toString());
            boolean isOutputSquare = drawYuvAttribute.isOutputSquare();
            String str2 = ", watermarkRange = ";
            if (drawYuvAttribute4.mEffectIndex != FilterInfo.FILTER_ID_NONE || CameraSettings.isAgeGenderAndMagicMirrorWaterOpen() || isOutputSquare || CameraSettings.isTiltShiftOn() || (!drawYuvAttribute4.mApplyWaterMark && drawYuvAttribute4.mTimeWatermark == null)) {
                z = isOutputSquare;
                str = str2;
                z3 = false;
                z2 = false;
                drawYuvAttribute2 = drawYuvAttribute4;
                iArr = null;
                bArr = null;
            } else {
                str = str2;
                z = isOutputSquare;
                z3 = false;
                iArr = Util.getWatermarkRange(width, height, drawYuvAttribute4.mJpegRotation, drawYuvAttribute4.mHasDualWaterMark, drawYuvAttribute4.mHasFrontWaterMark, drawYuvAttribute4.mTimeWatermark, drawYuvAttribute4.mDeviceWatermarkParam);
                String access$4002 = SnapshotRender.TAG;
                StringBuilder sb2 = new StringBuilder();
                sb2.append("applyEffect onlyDrawWatermarkRange: rotation = ");
                sb2.append(drawYuvAttribute4.mJpegRotation);
                sb2.append(str);
                sb2.append(Arrays.toString(iArr));
                Log.d(access$4002, sb2.toString());
                byte[] yuvData = ImageUtil.getYuvData(drawYuvAttribute4.mImage);
                MiYuvImage subYuvImage = Util.getSubYuvImage(yuvData, width, height, rowStride, rowStride2, iArr);
                String access$4003 = SnapshotRender.TAG;
                StringBuilder sb3 = new StringBuilder();
                sb3.append("get sub range data spend total tome =");
                sb3.append(System.currentTimeMillis() - currentTimeMillis);
                Log.d(access$4003, sb3.toString());
                Image image = drawYuvAttribute4.mImage;
                Size size2 = drawYuvAttribute4.mPreviewSize;
                Size size3 = r4;
                Size size4 = new Size(iArr[2], iArr[3]);
                DrawYuvAttribute drawYuvAttribute5 = new DrawYuvAttribute(image, size2, size3, drawYuvAttribute4.mEffectIndex, drawYuvAttribute4.mOrientation, drawYuvAttribute4.mJpegRotation, drawYuvAttribute4.mShootRotation, drawYuvAttribute4.mDate, drawYuvAttribute4.mMirror, drawYuvAttribute4.mApplyWaterMark, drawYuvAttribute4.mIsHeif, drawYuvAttribute4.mTiltShiftMode, drawYuvAttribute4.mTimeWatermark, drawYuvAttribute4.mHasDualWaterMark, drawYuvAttribute4.mHasFrontWaterMark, drawYuvAttribute4.mDeviceWatermarkParam, drawYuvAttribute4.mAttribute, drawYuvAttribute4.mWaterInfos, drawYuvAttribute4.mMajorAIWatermarkItem, drawYuvAttribute4.mMinorAIWatermarkItem);
                drawYuvAttribute5.mYuvImage = subYuvImage;
                drawYuvAttribute2 = drawYuvAttribute5;
                bArr = yuvData;
                z2 = true;
            }
            updateRenderParameters(effectRender, drawYuvAttribute2, z3);
            effectRender.setFrameBufferSize(drawYuvAttribute2.mPictureSize.getWidth(), drawYuvAttribute2.mPictureSize.getHeight());
            int i13 = z2 ? iArr[2] : width;
            int i14 = z2 ? iArr[3] : height;
            checkFrameBuffer(i13, i14);
            this.mGLCanvas.beginBindFrameBuffer(this.mFrameBuffer);
            long currentTimeMillis2 = System.currentTimeMillis();
            GLES20.glFlush();
            effectRender.setParentFrameBufferId(this.mFrameBuffer.getId());
            effectRender.draw(drawYuvAttribute2);
            String access$4004 = SnapshotRender.TAG;
            StringBuilder sb4 = new StringBuilder();
            sb4.append("drawTime=");
            sb4.append(System.currentTimeMillis() - currentTimeMillis2);
            Log.d(access$4004, sb4.toString());
            effectRender.deleteBuffer();
            drawYuvAttribute2.mOriginalSize = new Size(width, height);
            if (!z) {
                i4 = width;
                i3 = height;
                i2 = 0;
            } else if (width > height) {
                i2 = (width - height) / 2;
                i4 = height;
                i3 = i4;
            } else {
                i = (height - width) / 2;
                i4 = width;
                i3 = i4;
                i2 = 0;
                if (!drawYuvAttribute2.mApplyWaterMark) {
                    if (z2) {
                        c = 1;
                        i10 = -iArr[0];
                        i9 = -iArr[1];
                    } else {
                        c = 1;
                        i10 = 0;
                        i9 = 0;
                    }
                    long currentTimeMillis3 = System.currentTimeMillis();
                    if (!z2) {
                        iArr = Util.getWatermarkRange(i4, i3, drawYuvAttribute2.mJpegRotation, drawYuvAttribute2.mHasDualWaterMark, drawYuvAttribute2.mHasFrontWaterMark, drawYuvAttribute2.mTimeWatermark, drawYuvAttribute2.mDeviceWatermarkParam);
                        String access$4005 = SnapshotRender.TAG;
                        StringBuilder sb5 = new StringBuilder();
                        sb5.append("applyEffect !onlyDrawWatermarkRange: rotation = ");
                        sb5.append(drawYuvAttribute2.mJpegRotation);
                        sb5.append(str);
                        sb5.append(Arrays.toString(iArr));
                        Log.d(access$4005, sb5.toString());
                        z5 = false;
                        i12 = iArr[0];
                        i11 = iArr[c];
                    } else {
                        z5 = false;
                        i12 = 0;
                        i11 = 0;
                    }
                    int[] iArr3 = iArr;
                    int i15 = drawYuvAttribute2.mJpegQuality;
                    int i16 = (i15 <= 0 || i15 > 97) ? 97 : i15;
                    boolean z6 = z5;
                    int i17 = i14;
                    int i18 = i13;
                    int i19 = i16;
                    i6 = height;
                    z4 = z2;
                    i5 = width;
                    drawYuvAttribute3 = drawYuvAttribute2;
                    drawAgeGenderAndMagicMirrorWater(drawYuvAttribute2.mWaterInfos, i10, i9, width, height, drawYuvAttribute2.mPreviewSize.getWidth(), drawYuvAttribute2.mPreviewSize.getHeight(), drawYuvAttribute2.mJpegRotation, false, false);
                    if (!drawYuvAttribute3.mIsHeif) {
                        bArr2 = ShaderNativeUtil.getPicture(i12 + i2, i11 + i, iArr3[2], iArr3[3], i19);
                        String access$4006 = SnapshotRender.TAG;
                        StringBuilder sb6 = new StringBuilder();
                        sb6.append("for remove watermark spend more time = ");
                        sb6.append(System.currentTimeMillis() - currentTimeMillis3);
                        Log.d(access$4006, sb6.toString());
                    } else {
                        bArr2 = null;
                    }
                    drawWaterMark(i10 + i2, i9 + i, i4, i3, drawYuvAttribute3.mJpegRotation, drawYuvAttribute3.mTimeWatermark, false);
                    String access$4007 = SnapshotRender.TAG;
                    StringBuilder sb7 = new StringBuilder();
                    sb7.append("watermarkTime=");
                    sb7.append(System.currentTimeMillis() - currentTimeMillis3);
                    Log.d(access$4007, sb7.toString());
                    this.mGLCanvas.endBindFrameBuffer();
                    if (z4) {
                        i8 = i17;
                        i7 = i18;
                        size = new Size(i7, i8);
                    } else {
                        i8 = i17;
                        i7 = i18;
                        size = drawYuvAttribute3.mOriginalSize;
                    }
                    checkWatermarkFrameBuffer(size);
                    this.mGLCanvas.beginBindFrameBuffer(this.mWatermarkFrameBuffer);
                    long currentTimeMillis4 = System.currentTimeMillis();
                    RgbToYuvRender rgbToYuvRender = (RgbToYuvRender) fetchRender(FilterInfo.FILTER_ID_RGB2YUV);
                    updateRenderParameters(rgbToYuvRender, drawYuvAttribute3, false);
                    rgbToYuvRender.setParentFrameBufferId(this.mFrameBuffer.getId());
                    rgbToYuvRender.drawTexture(this.mFrameBuffer.getTexture().getId(), 0.0f, 0.0f, (float) size.getWidth(), (float) size.getHeight(), true);
                    String access$4008 = SnapshotRender.TAG;
                    StringBuilder sb8 = new StringBuilder();
                    sb8.append("rgb2YuvTime=");
                    sb8.append(System.currentTimeMillis() - currentTimeMillis4);
                    Log.d(access$4008, sb8.toString());
                    iArr2 = iArr3;
                } else {
                    i8 = i14;
                    i7 = i13;
                    i5 = width;
                    i6 = height;
                    z4 = z2;
                    drawYuvAttribute3 = drawYuvAttribute2;
                    bArr2 = null;
                }
                GLES20.glPixelStorei(SuperNightProcess.ASVL_PAF_RAW12_RGGB_12B, 1);
                long currentTimeMillis5 = System.currentTimeMillis();
                if (!z4) {
                    i7 = drawYuvAttribute3.mOriginalSize.getWidth();
                }
                int height2 = !z4 ? i8 : drawYuvAttribute3.mOriginalSize.getHeight();
                int ceil = (int) Math.ceil(((double) i7) / 2.0d);
                int ceil2 = (int) Math.ceil((((double) height2) * 3.0d) / 4.0d);
                ByteBuffer allocate = ByteBuffer.allocate(ceil * ceil2 * 4);
                GLES20.glReadPixels(0, 0, ceil, ceil2, 6408, 5121, allocate);
                allocate.rewind();
                Log.d(SnapshotRender.TAG, String.format(Locale.ENGLISH, "readSize=%dx%d imageSize=%dx%d", new Object[]{Integer.valueOf(ceil), Integer.valueOf(ceil2), Integer.valueOf(i7), Integer.valueOf(height2)}));
                String access$4009 = SnapshotRender.TAG;
                StringBuilder sb9 = new StringBuilder();
                sb9.append("readTime=");
                sb9.append(System.currentTimeMillis() - currentTimeMillis5);
                Log.d(access$4009, sb9.toString());
                byte[] array = allocate.array();
                if (!z4) {
                    long currentTimeMillis6 = System.currentTimeMillis();
                    Util.coverSubYuvImage(bArr, i4, i3, rowStride, rowStride2, allocate.array(), iArr2);
                    String access$40010 = SnapshotRender.TAG;
                    StringBuilder sb10 = new StringBuilder();
                    sb10.append("cover sub range data spend total time =");
                    sb10.append(System.currentTimeMillis() - currentTimeMillis6);
                    Log.d(access$40010, sb10.toString());
                    bArr3 = bArr;
                } else {
                    bArr3 = array;
                }
                long currentTimeMillis7 = System.currentTimeMillis();
                ImageUtil.updateYuvImage(drawYuvAttribute3.mImage, bArr3, z4);
                String access$40011 = SnapshotRender.TAG;
                StringBuilder sb11 = new StringBuilder();
                sb11.append("updateImageTime=");
                sb11.append(System.currentTimeMillis() - currentTimeMillis7);
                Log.d(access$40011, sb11.toString());
                this.mGLCanvas.endBindFrameBuffer();
                this.mGLCanvas.recycledResources();
                if (drawYuvAttribute4.mApplyWaterMark) {
                    int[] reverseCalculateRange = reverseCalculateRange(i5, i6, drawYuvAttribute4.mOutputSize.getWidth(), drawYuvAttribute4.mOutputSize.getHeight(), iArr2);
                    drawYuvAttribute4.mDataOfTheRegionUnderWatermarks = bArr2;
                    drawYuvAttribute4.mCoordinatesOfTheRegionUnderWatermarks = reverseCalculateRange;
                }
                return bArr3;
            }
            i = 0;
            if (!drawYuvAttribute2.mApplyWaterMark) {
            }
            GLES20.glPixelStorei(SuperNightProcess.ASVL_PAF_RAW12_RGGB_12B, 1);
            long currentTimeMillis52 = System.currentTimeMillis();
            if (!z4) {
            }
            if (!z4) {
            }
            int ceil3 = (int) Math.ceil(((double) i7) / 2.0d);
            int ceil22 = (int) Math.ceil((((double) height2) * 3.0d) / 4.0d);
            ByteBuffer allocate2 = ByteBuffer.allocate(ceil3 * ceil22 * 4);
            GLES20.glReadPixels(0, 0, ceil3, ceil22, 6408, 5121, allocate2);
            allocate2.rewind();
            Log.d(SnapshotRender.TAG, String.format(Locale.ENGLISH, "readSize=%dx%d imageSize=%dx%d", new Object[]{Integer.valueOf(ceil3), Integer.valueOf(ceil22), Integer.valueOf(i7), Integer.valueOf(height2)}));
            String access$40092 = SnapshotRender.TAG;
            StringBuilder sb92 = new StringBuilder();
            sb92.append("readTime=");
            sb92.append(System.currentTimeMillis() - currentTimeMillis52);
            Log.d(access$40092, sb92.toString());
            byte[] array2 = allocate2.array();
            if (!z4) {
            }
            long currentTimeMillis72 = System.currentTimeMillis();
            ImageUtil.updateYuvImage(drawYuvAttribute3.mImage, bArr3, z4);
            String access$400112 = SnapshotRender.TAG;
            StringBuilder sb112 = new StringBuilder();
            sb112.append("updateImageTime=");
            sb112.append(System.currentTimeMillis() - currentTimeMillis72);
            Log.d(access$400112, sb112.toString());
            this.mGLCanvas.endBindFrameBuffer();
            this.mGLCanvas.recycledResources();
            if (drawYuvAttribute4.mApplyWaterMark) {
            }
            return bArr3;
        }

        private byte[] applyEffectForAIWatermark(DrawYuvAttribute drawYuvAttribute, boolean z) {
            DrawYuvAttribute drawYuvAttribute2 = drawYuvAttribute;
            Log.k(3, SnapshotRender.TAG, "apply effect For AIWatermark start");
            PipeRender effectRenderForAI = getEffectRenderForAI(drawYuvAttribute);
            if (effectRenderForAI == null) {
                Log.w(SnapshotRender.TAG, "init render failed");
                return null;
            }
            int width = drawYuvAttribute2.mPictureSize.getWidth();
            int height = drawYuvAttribute2.mPictureSize.getHeight();
            long currentTimeMillis = System.currentTimeMillis();
            drawYuvAttribute2.mImage.getTimestamp();
            Plane plane = drawYuvAttribute2.mImage.getPlanes()[0];
            Plane plane2 = drawYuvAttribute2.mImage.getPlanes()[1];
            int rowStride = plane.getRowStride();
            int rowStride2 = plane2.getRowStride();
            int width2 = drawYuvAttribute2.mPictureSize.getWidth();
            int height2 = drawYuvAttribute2.mPictureSize.getHeight();
            Rect displayRect = Util.getDisplayRect();
            WatermarkItem watermarkItem = z ? drawYuvAttribute2.mMinorAIWatermarkItem : drawYuvAttribute2.mMajorAIWatermarkItem;
            if (watermarkItem == null) {
                Log.k(5, SnapshotRender.TAG, "watermark item is null");
                return null;
            }
            if (!(watermarkItem.getType() == 11 || watermarkItem.getType() == 12)) {
                CameraStatUtils.trackAIWatermarkCapture(String.valueOf(watermarkItem.getType()), watermarkItem.getKey(), String.valueOf(watermarkItem.hasMove()), String.valueOf(drawYuvAttribute2.mOrientation));
            }
            int[] location = getLocation(displayRect, watermarkItem);
            if (location[0] == location[2] || location[1] == location[3]) {
                Log.k(5, SnapshotRender.TAG, "bitmap size is error");
                return null;
            }
            float scale = getScale(width2, height2, displayRect);
            String access$400 = SnapshotRender.TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("plane0 stride = ");
            sb.append(plane.getRowStride());
            sb.append(", plane1 stride = ");
            sb.append(plane2.getRowStride());
            Log.d(access$400, sb.toString());
            int[] aIWatermarkRange = Util.getAIWatermarkRange(drawYuvAttribute2.mOrientation, drawYuvAttribute2.mJpegRotation, location, scale, displayRect);
            byte[] yuvData = ImageUtil.getYuvData(drawYuvAttribute2.mImage);
            WatermarkItem watermarkItem2 = watermarkItem;
            MiYuvImage subYuvImage = Util.getSubYuvImage(yuvData, width, height, rowStride, rowStride2, aIWatermarkRange);
            String access$4002 = SnapshotRender.TAG;
            StringBuilder sb2 = new StringBuilder();
            sb2.append("get sub range data spend total tome =");
            sb2.append(System.currentTimeMillis() - currentTimeMillis);
            Log.d(access$4002, sb2.toString());
            Image image = drawYuvAttribute2.mImage;
            Size size = drawYuvAttribute2.mPreviewSize;
            Size size2 = r5;
            Size size3 = new Size(aIWatermarkRange[2], aIWatermarkRange[3]);
            DrawYuvAttribute drawYuvAttribute3 = new DrawYuvAttribute(image, size, size2, drawYuvAttribute2.mEffectIndex, drawYuvAttribute2.mOrientation, drawYuvAttribute2.mJpegRotation, drawYuvAttribute2.mShootRotation, drawYuvAttribute2.mDate, drawYuvAttribute2.mMirror, drawYuvAttribute2.mApplyWaterMark, drawYuvAttribute2.mIsHeif, drawYuvAttribute2.mTiltShiftMode, drawYuvAttribute2.mTimeWatermark, drawYuvAttribute2.mHasDualWaterMark, drawYuvAttribute2.mHasFrontWaterMark, drawYuvAttribute2.mDeviceWatermarkParam, drawYuvAttribute2.mAttribute, drawYuvAttribute2.mWaterInfos, drawYuvAttribute2.mMajorAIWatermarkItem, drawYuvAttribute2.mMinorAIWatermarkItem);
            drawYuvAttribute3.mYuvImage = subYuvImage;
            updateRenderParameters(effectRenderForAI, drawYuvAttribute3, false);
            effectRenderForAI.setFrameBufferSize(drawYuvAttribute3.mPictureSize.getWidth(), drawYuvAttribute3.mPictureSize.getHeight());
            int i = aIWatermarkRange[2];
            int i2 = aIWatermarkRange[3];
            checkFrameBuffer(i, i2);
            this.mGLCanvas.beginBindFrameBuffer(this.mFrameBuffer);
            long currentTimeMillis2 = System.currentTimeMillis();
            GLES20.glFlush();
            effectRenderForAI.setParentFrameBufferId(this.mFrameBuffer.getId());
            effectRenderForAI.draw(drawYuvAttribute3);
            String access$4003 = SnapshotRender.TAG;
            StringBuilder sb3 = new StringBuilder();
            sb3.append("drawTime=");
            sb3.append(System.currentTimeMillis() - currentTimeMillis2);
            Log.d(access$4003, sb3.toString());
            effectRenderForAI.deleteBuffer();
            drawYuvAttribute3.mOriginalSize = new Size(width, height);
            int i3 = -aIWatermarkRange[0];
            int i4 = -aIWatermarkRange[1];
            Bitmap textBitmap = watermarkItem2.isTextWatermark() ? watermarkItem2.getTextBitmap() : Util.convertResToBitmap(watermarkItem2.getResId());
            if (textBitmap == null) {
                Log.w(SnapshotRender.TAG, "bitmap is null");
                return new byte[0];
            }
            float calcSuperMoonScale = WatermarkConstant.calcSuperMoonScale();
            if (watermarkItem2.getType() == 11) {
                scale *= calcSuperMoonScale;
                String access$4004 = SnapshotRender.TAG;
                StringBuilder sb4 = new StringBuilder();
                sb4.append("superMoonScale:");
                sb4.append(calcSuperMoonScale);
                sb4.append(" now scale:");
                sb4.append(scale);
                Log.d(access$4004, sb4.toString());
            }
            byte[] bArr = yuvData;
            int i5 = i2;
            float f = scale;
            int i6 = i;
            drawAIWaterMark(i3 + 0, i4 + 0, width, height, drawYuvAttribute3.mJpegRotation, textBitmap, aIWatermarkRange, f, false);
            this.mGLCanvas.endBindFrameBuffer();
            Size size4 = new Size(i6, i5);
            checkWatermarkFrameBuffer(size4);
            this.mGLCanvas.beginBindFrameBuffer(this.mWatermarkFrameBuffer);
            long currentTimeMillis3 = System.currentTimeMillis();
            RgbToYuvRender rgbToYuvRender = (RgbToYuvRender) fetchRender(FilterInfo.FILTER_ID_RGB2YUV);
            updateRenderParameters(rgbToYuvRender, drawYuvAttribute3, false);
            rgbToYuvRender.setParentFrameBufferId(this.mFrameBuffer.getId());
            rgbToYuvRender.drawTexture(this.mFrameBuffer.getTexture().getId(), 0.0f, 0.0f, (float) size4.getWidth(), (float) size4.getHeight(), true);
            String access$4005 = SnapshotRender.TAG;
            StringBuilder sb5 = new StringBuilder();
            sb5.append("rgb2YuvTime=");
            sb5.append(System.currentTimeMillis() - currentTimeMillis3);
            Log.d(access$4005, sb5.toString());
            GLES20.glPixelStorei(SuperNightProcess.ASVL_PAF_RAW12_RGGB_12B, 1);
            long currentTimeMillis4 = System.currentTimeMillis();
            int ceil = (int) Math.ceil(((double) i6) / 2.0d);
            int ceil2 = (int) Math.ceil((((double) i5) * 3.0d) / 4.0d);
            ByteBuffer allocate = ByteBuffer.allocate(ceil * ceil2 * 4);
            GLES20.glReadPixels(0, 0, ceil, ceil2, 6408, 5121, allocate);
            allocate.rewind();
            Log.d(SnapshotRender.TAG, String.format(Locale.ENGLISH, "readSize=%dx%d imageSize=%dx%d", new Object[]{Integer.valueOf(ceil), Integer.valueOf(ceil2), Integer.valueOf(i6), Integer.valueOf(i5)}));
            String access$4006 = SnapshotRender.TAG;
            StringBuilder sb6 = new StringBuilder();
            sb6.append("readTime=");
            sb6.append(System.currentTimeMillis() - currentTimeMillis4);
            Log.d(access$4006, sb6.toString());
            long currentTimeMillis5 = System.currentTimeMillis();
            byte[] array = allocate.array();
            DrawYuvAttribute drawYuvAttribute4 = drawYuvAttribute3;
            Util.coverSubYuvImage(bArr, width, height, rowStride, rowStride2, array, aIWatermarkRange);
            String access$4007 = SnapshotRender.TAG;
            StringBuilder sb7 = new StringBuilder();
            sb7.append("cover sub range data spend total time =");
            sb7.append(System.currentTimeMillis() - currentTimeMillis5);
            Log.d(access$4007, sb7.toString());
            long currentTimeMillis6 = System.currentTimeMillis();
            Image image2 = drawYuvAttribute4.mImage;
            byte[] bArr2 = bArr;
            ImageUtil.updateYuvImage(image2, bArr2, true);
            String access$4008 = SnapshotRender.TAG;
            StringBuilder sb8 = new StringBuilder();
            sb8.append("updateImageTime=");
            sb8.append(System.currentTimeMillis() - currentTimeMillis6);
            Log.d(access$4008, sb8.toString());
            this.mGLCanvas.endBindFrameBuffer();
            this.mGLCanvas.recycledResources();
            Log.k(3, SnapshotRender.TAG, "apply effect For AIWatermark end");
            return bArr2;
        }

        private byte[] blockSplitApplyEffect(DrawYuvAttribute drawYuvAttribute) {
            int i;
            int i2;
            int i3;
            int i4;
            int i5;
            int i6;
            int[] iArr;
            RectF rectF;
            int i7;
            int i8;
            int i9;
            int i10;
            String str;
            int i11;
            int i12;
            int i13;
            int i14;
            PipeRender pipeRender;
            List list;
            int[] iArr2;
            RectF rectF2;
            RectF rectF3;
            RectF rectF4;
            RectF rectF5;
            Block block;
            int i15;
            int i16;
            int i17;
            int i18;
            int i19;
            int i20;
            int i21;
            int i22;
            int i23;
            int i24;
            DrawYuvAttribute drawYuvAttribute2 = drawYuvAttribute;
            GLES20.glGetIntegerv(3379, IntBuffer.allocate(2));
            String str2 = "TOTAL";
            SnapshotRender.this.mTotalCounter.reset(str2);
            PipeRender effectRender = getEffectRender(drawYuvAttribute);
            if (effectRender == null) {
                Log.w(SnapshotRender.TAG, "init render failed");
                return null;
            }
            drawYuvAttribute2.mImage.getTimestamp();
            int width = drawYuvAttribute2.mPictureSize.getWidth();
            int height = drawYuvAttribute2.mPictureSize.getHeight();
            Plane plane = drawYuvAttribute2.mImage.getPlanes()[0];
            Plane plane2 = drawYuvAttribute2.mImage.getPlanes()[1];
            int rowStride = plane.getRowStride();
            int rowStride2 = plane2.getRowStride();
            SnapshotRender.this.mMemImage.mWidth = width;
            SnapshotRender.this.mMemImage.mHeight = height;
            SnapshotRender.this.mMemImage.parseImage(drawYuvAttribute2.mImage);
            String access$400 = SnapshotRender.TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("plane0 stride = ");
            sb.append(plane.getRowStride());
            sb.append(", plane1 stride = ");
            sb.append(plane2.getRowStride());
            Log.d(access$400, sb.toString());
            updateRenderParameters(effectRender, drawYuvAttribute2, true);
            Splitter access$2000 = SnapshotRender.this.mSplitter;
            int access$600 = SnapshotRender.this.mBlockWidth;
            int i25 = rowStride2;
            int access$700 = SnapshotRender.this.mBlockHeight;
            int i26 = rowStride;
            List split = access$2000.split(width, height, access$600, access$700, SnapshotRender.this.mAdjWidth, SnapshotRender.this.mAdjHeight);
            boolean isOutputSquare = drawYuvAttribute.isOutputSquare();
            if (isOutputSquare) {
                if (width > height) {
                    i2 = (width - height) / 2;
                    i = 0;
                    i4 = height;
                } else {
                    i = (height - width) / 2;
                    i2 = 0;
                    i4 = width;
                }
                i3 = i4;
            } else {
                i2 = 0;
                i = 0;
                i4 = height;
                i3 = width;
            }
            if (drawYuvAttribute2.mApplyWaterMark) {
                int[] watermarkRange = Util.getWatermarkRange(i3, i4, drawYuvAttribute2.mJpegRotation, drawYuvAttribute2.mHasDualWaterMark, drawYuvAttribute2.mHasFrontWaterMark, drawYuvAttribute2.mTimeWatermark, drawYuvAttribute2.mDeviceWatermarkParam);
                String access$4002 = SnapshotRender.TAG;
                StringBuilder sb2 = new StringBuilder();
                sb2.append("blockSplitApplyEffect: rotation = ");
                sb2.append(drawYuvAttribute2.mJpegRotation);
                sb2.append(", watermarkRange = ");
                sb2.append(Arrays.toString(watermarkRange));
                Log.d(access$4002, sb2.toString());
                i5 = i26;
                RectF rectF6 = new RectF((float) (watermarkRange[0] + i2), (float) (watermarkRange[1] + i), (float) (watermarkRange[0] + watermarkRange[2] + i2), (float) (watermarkRange[1] + watermarkRange[3] + i));
                i6 = i4;
                ShaderNativeUtil.genWaterMarkRange(watermarkRange[0] + i2, watermarkRange[1] + i, watermarkRange[2], watermarkRange[3], 4);
                iArr = watermarkRange;
                rectF = rectF6;
            } else {
                i5 = i26;
                i6 = i4;
                rectF = null;
                iArr = null;
            }
            RectF rectF7 = new RectF();
            boolean z = drawYuvAttribute2.mEffectIndex == FilterInfo.FILTER_ID_NONE && !CameraSettings.isAgeGenderAndMagicMirrorWaterOpen() && !isOutputSquare && !CameraSettings.isTiltShiftOn() && (drawYuvAttribute2.mApplyWaterMark || drawYuvAttribute2.mTimeWatermark != null);
            int i27 = drawYuvAttribute2.mBlockWidth;
            int i28 = drawYuvAttribute2.mBlockHeight;
            int i29 = 0;
            while (i29 < split.size()) {
                int i30 = i28;
                SnapshotRender.this.mFrameCounter.reset(String.format(Locale.ENGLISH, "[loop%d/%d]begin", new Object[]{Integer.valueOf(i29), Integer.valueOf(split.size())}));
                Block block2 = (Block) split.get(i29);
                int i31 = block2.mWidth;
                int i32 = block2.mHeight;
                int[] offset = block2.getOffset(width, height);
                int i33 = i29;
                rectF7.left = (float) offset[0];
                int i34 = i27;
                rectF7.top = (float) offset[1];
                rectF7.right = (float) (offset[0] + i31);
                rectF7.bottom = (float) (offset[1] + i32);
                if (z) {
                    block = block2;
                    str = str2;
                    i16 = i5;
                    iArr2 = iArr;
                    i17 = i32;
                    i18 = i31;
                    i10 = i30;
                    i9 = i34;
                    rectF5 = rectF7;
                    i8 = i6;
                    i15 = height;
                    Block block3 = block;
                    i7 = i3;
                    list = split;
                    rectF4 = rectF;
                    i19 = 0;
                    if (!rectangle_collision(rectF7.left, rectF7.top, rectF7.width(), rectF7.height(), rectF.left, rectF.top, rectF.width(), rectF.height())) {
                        drawYuvAttribute2 = drawYuvAttribute;
                        i12 = width;
                        i11 = i16;
                        pipeRender = effectRender;
                        i13 = i25;
                        i14 = i15;
                        rectF3 = rectF5;
                        rectF2 = rectF4;
                        i29 = i33 + 1;
                        rectF7 = rectF3;
                        rectF = rectF2;
                        iArr = iArr2;
                        split = list;
                        effectRender = pipeRender;
                        height = i14;
                        i25 = i13;
                        width = i12;
                        i5 = i11;
                        str2 = str;
                        i28 = i10;
                        i27 = i9;
                        i6 = i8;
                        i3 = i7;
                    }
                } else {
                    i18 = i31;
                    rectF5 = rectF7;
                    block = block2;
                    i7 = i3;
                    rectF4 = rectF;
                    str = str2;
                    i8 = i6;
                    i16 = i5;
                    i10 = i30;
                    i9 = i34;
                    list = split;
                    i15 = height;
                    iArr2 = iArr;
                    i19 = 0;
                    i17 = i32;
                }
                if (effectRender instanceof PipeRender) {
                    effectRender.setFrameBufferSize(i18, i17);
                }
                checkFrameBuffer(i18, i17);
                effectRender.setParentFrameBufferId(this.mFrameBuffer.getId());
                this.mGLCanvas.beginBindFrameBuffer(this.mFrameBuffer.getId(), i18, i17);
                GLES20.glViewport(i19, i19, i18, i17);
                int i35 = i25;
                int i36 = i15;
                int[] yUVOffset = block.getYUVOffset(i16, i35, width, i36);
                int i37 = i18;
                drawYuvAttribute2 = drawYuvAttribute;
                drawYuvAttribute2.mOffsetY = yUVOffset[i19];
                drawYuvAttribute2.mOffsetUV = yUVOffset[1];
                drawYuvAttribute2.mBlockWidth = i37;
                drawYuvAttribute2.mBlockHeight = i17;
                effectRender.draw(drawYuvAttribute2);
                Locale locale = Locale.ENGLISH;
                Object[] objArr = new Object[2];
                objArr[i19] = Integer.valueOf(i33);
                objArr[1] = Integer.valueOf(list.size());
                SnapshotRender.this.mFrameCounter.tick(String.format(locale, "[loop%d/%d]gl drawFrame", objArr));
                int i38 = i8;
                int i39 = i7;
                drawYuvAttribute2.mOriginalSize = new Size(i39, i38);
                if (drawYuvAttribute2.mApplyWaterMark) {
                    CounterUtil counterUtil = new CounterUtil();
                    String str3 = "drawWaterMark";
                    counterUtil.reset(str3);
                    int i40 = i2 - offset[i19];
                    int i41 = i - offset[1];
                    List list2 = drawYuvAttribute2.mWaterInfos;
                    String str4 = str3;
                    int i42 = -offset[i19];
                    int i43 = -offset[1];
                    int width2 = drawYuvAttribute2.mPreviewSize.getWidth();
                    i11 = i16;
                    String str5 = str4;
                    pipeRender = effectRender;
                    CounterUtil counterUtil2 = counterUtil;
                    i23 = i38;
                    i24 = i39;
                    int i44 = i37;
                    int i45 = width2;
                    i13 = i35;
                    int height2 = drawYuvAttribute2.mPreviewSize.getHeight();
                    int i46 = i36;
                    i14 = i46;
                    i20 = 0;
                    int i47 = i17;
                    i21 = i44;
                    i12 = width;
                    drawAgeGenderAndMagicMirrorWater(list2, i42, i43, width, i36, i45, height2, drawYuvAttribute2.mJpegRotation, false, false);
                    rectF3 = rectF5;
                    rectF2 = rectF4;
                    if (rectangle_collision(rectF3.left, rectF3.top, rectF3.width(), rectF3.height(), rectF2.left, rectF2.top, rectF2.width(), rectF2.height())) {
                        float[] intersectRect = getIntersectRect(rectF3.left, rectF3.top, rectF3.right, rectF3.bottom, rectF2.left, rectF2.top, rectF2.right, rectF2.bottom);
                        ShaderNativeUtil.mergeWaterMarkRange((int) intersectRect[0], (int) intersectRect[1], (int) (intersectRect[2] - intersectRect[0]), (int) (intersectRect[3] - intersectRect[1]), offset[0], offset[1], 4);
                        drawWaterMark(i40, i41, i24, i23, drawYuvAttribute2.mJpegRotation, drawYuvAttribute2.mTimeWatermark, false);
                    }
                    counterUtil2.tick(str5);
                    this.mGLCanvas.endBindFrameBuffer();
                    i22 = i47;
                    Size size = new Size(i21, i22);
                    checkWatermarkFrameBuffer(size);
                    this.mGLCanvas.beginBindFrameBuffer(this.mWatermarkFrameBuffer);
                    RgbToYuvRender rgbToYuvRender = (RgbToYuvRender) fetchRender(FilterInfo.FILTER_ID_RGB2YUV);
                    updateRenderParameters(rgbToYuvRender, drawYuvAttribute2, true);
                    rgbToYuvRender.setParentFrameBufferId(this.mFrameBuffer.getId());
                    rgbToYuvRender.drawTexture(this.mFrameBuffer.getTexture().getId(), 0.0f, 0.0f, (float) size.getWidth(), (float) size.getHeight(), true);
                    counterUtil2.tick("drawWaterMark rgb2yuv");
                } else {
                    i23 = i38;
                    i24 = i39;
                    i13 = i35;
                    i14 = i36;
                    i20 = i19;
                    i12 = width;
                    i22 = i17;
                    i11 = i16;
                    pipeRender = effectRender;
                    rectF3 = rectF5;
                    rectF2 = rectF4;
                    i21 = i37;
                }
                GLES20.glPixelStorei(SuperNightProcess.ASVL_PAF_RAW12_RGGB_12B, 1);
                ShaderNativeUtil.mergeYUV(i21, i22, yUVOffset[i20], yUVOffset[1]);
                Locale locale2 = Locale.ENGLISH;
                Object[] objArr2 = new Object[2];
                objArr2[i20] = Integer.valueOf(i33);
                objArr2[1] = Integer.valueOf(list.size());
                SnapshotRender.this.mFrameCounter.tick(String.format(locale2, "[loop%d/%d]gl mergeYUV", objArr2));
                i29 = i33 + 1;
                rectF7 = rectF3;
                rectF = rectF2;
                iArr = iArr2;
                split = list;
                effectRender = pipeRender;
                height = i14;
                i25 = i13;
                width = i12;
                i5 = i11;
                str2 = str;
                i28 = i10;
                i27 = i9;
                i6 = i8;
                i3 = i7;
            }
            int i48 = i28;
            int[] iArr3 = iArr;
            String str6 = str2;
            PipeRender pipeRender2 = effectRender;
            drawYuvAttribute2.mBlockWidth = i27;
            drawYuvAttribute2.mBlockHeight = i48;
            pipeRender2.deleteBuffer();
            this.mGLCanvas.endBindFrameBuffer();
            this.mGLCanvas.recycledResources();
            if (drawYuvAttribute2.mApplyWaterMark) {
                byte[] waterMarkRange = ShaderNativeUtil.getWaterMarkRange(SnapshotRender.this.mQuality, 4);
                int[] reverseCalculateRange = reverseCalculateRange(drawYuvAttribute2.mPictureSize.getWidth(), drawYuvAttribute2.mPictureSize.getHeight(), drawYuvAttribute2.mOutputSize.getWidth(), drawYuvAttribute2.mOutputSize.getHeight(), iArr3);
                drawYuvAttribute2.mDataOfTheRegionUnderWatermarks = waterMarkRange;
                drawYuvAttribute2.mCoordinatesOfTheRegionUnderWatermarks = reverseCalculateRange;
            }
            SnapshotRender.this.mTotalCounter.tick(str6);
            return null;
        }

        private void checkFrameBuffer(int i, int i2) {
            FrameBuffer frameBuffer = this.mFrameBuffer;
            if (frameBuffer == null || frameBuffer.getWidth() != i || this.mFrameBuffer.getHeight() != i2) {
                this.mFrameBuffer = new FrameBuffer(this.mGLCanvas, i, i2, 0);
            }
        }

        private void checkWatermarkFrameBuffer(Size size) {
            FrameBuffer frameBuffer = this.mWatermarkFrameBuffer;
            if (frameBuffer == null || frameBuffer.getWidth() < size.getWidth() || this.mWatermarkFrameBuffer.getHeight() < size.getHeight()) {
                this.mWatermarkFrameBuffer = new FrameBuffer(this.mGLCanvas, size.getWidth(), size.getHeight(), 0);
            }
        }

        private byte[] compress(Bitmap bitmap) {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(CompressFormat.JPEG, 100, byteArrayOutputStream);
            byte[] byteArray = byteArrayOutputStream.toByteArray();
            try {
                byteArrayOutputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return byteArray;
        }

        private void drawAgeGenderAndMagicMirrorWater(List list, int i, int i2, int i3, int i4, int i5, int i6, int i7, boolean z, boolean z2) {
            if (C0124O00000oO.OOoo0oo() && !z && CameraSettings.isAgeGenderAndMagicMirrorWaterOpen()) {
                List list2 = list;
                WaterMarkBitmap waterMarkBitmap = new WaterMarkBitmap(list);
                WaterMarkData waterMarkData = waterMarkBitmap.getWaterMarkData();
                if (waterMarkData != null) {
                    AgeGenderAndMagicMirrorWaterMark ageGenderAndMagicMirrorWaterMark = new AgeGenderAndMagicMirrorWaterMark(waterMarkData.getImage(), i3, i4, i7, i5, i6, 0.0f, 0.0f);
                    drawWaterMark(ageGenderAndMagicMirrorWaterMark, i, i2, i7 - waterMarkData.getOrientation(), z2);
                }
                waterMarkBitmap.releaseBitmap();
                Log.d(WaterMarkBitmap.class.getSimpleName(), "Draw age_gender_and_magic_mirror water mark");
            }
        }

        /* JADX WARNING: Code restructure failed: missing block: B:18:0x002c, code lost:
            if (r6.mMajorAIWatermarkItem == null) goto L_0x001e;
         */
        /* JADX WARNING: Removed duplicated region for block: B:20:0x0031  */
        /* JADX WARNING: Removed duplicated region for block: B:21:0x0037  */
        /* JADX WARNING: Removed duplicated region for block: B:45:0x0078  */
        /* JADX WARNING: Removed duplicated region for block: B:48:0x008c  */
        /* JADX WARNING: Removed duplicated region for block: B:49:0x008f  */
        /* JADX WARNING: Removed duplicated region for block: B:52:0x00aa  */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        private boolean drawImage(DrawYuvAttribute drawYuvAttribute, boolean z) {
            byte[] applyEffect;
            boolean z2 = (drawYuvAttribute.mMajorAIWatermarkItem == null && drawYuvAttribute.mMinorAIWatermarkItem == null) ? false : true;
            WatermarkItem watermarkItem = drawYuvAttribute.mMajorAIWatermarkItem;
            if (!(watermarkItem != null && watermarkItem.getType() == 11 && drawYuvAttribute.mMinorAIWatermarkItem == null)) {
                WatermarkItem watermarkItem2 = drawYuvAttribute.mMinorAIWatermarkItem;
                if (watermarkItem2 != null) {
                    if (watermarkItem2.getType() == 11) {
                    }
                }
                if (!z2) {
                    drawYuvAttribute.mApplyWaterMark = false;
                    drawYuvAttribute.mTimeWatermark = null;
                } else if (drawYuvAttribute.mMajorAIWatermarkItem != null) {
                    applyEffectForAIWatermark(drawYuvAttribute, false);
                }
                applyEffect = (!CameraSettings.isTiltShiftOn() || (drawYuvAttribute.mEffectIndex == FilterInfo.FILTER_ID_NONE && !CameraSettings.isAgeGenderAndMagicMirrorWaterOpen() && !CameraSettings.isTiltShiftOn() && !z && (drawYuvAttribute.mApplyWaterMark || drawYuvAttribute.mTimeWatermark != null))) ? applyEffect(drawYuvAttribute) : blockSplitApplyEffect(drawYuvAttribute);
                if (z2 && drawYuvAttribute.mMajorAIWatermarkItem != null) {
                    applyEffect = applyEffectForAIWatermark(drawYuvAttribute, false);
                }
                if (drawYuvAttribute.mMinorAIWatermarkItem != null) {
                    applyEffect = applyEffectForAIWatermark(drawYuvAttribute, true);
                }
                String access$400 = SnapshotRender.TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("mainLen=");
                sb.append(applyEffect != null ? "null" : Integer.valueOf(applyEffect.length));
                Log.d(access$400, sb.toString());
                if (SnapshotRender.this.mDeviceWaterMarkParam.isCinematicAspectRatio()) {
                    Util.drawMiMovieBlackBridge(drawYuvAttribute.mImage);
                }
                return true;
            }
            z2 = false;
            if (!z2) {
            }
            if (!CameraSettings.isTiltShiftOn()) {
            }
            applyEffect = applyEffectForAIWatermark(drawYuvAttribute, false);
            if (drawYuvAttribute.mMinorAIWatermarkItem != null) {
            }
            String access$4002 = SnapshotRender.TAG;
            StringBuilder sb2 = new StringBuilder();
            sb2.append("mainLen=");
            sb2.append(applyEffect != null ? "null" : Integer.valueOf(applyEffect.length));
            Log.d(access$4002, sb2.toString());
            if (SnapshotRender.this.mDeviceWaterMarkParam.isCinematicAspectRatio()) {
            }
            return true;
        }

        private void drawWaterMark(WaterMark waterMark, int i, int i2, int i3, boolean z) {
            this.mGLCanvas.getState().pushState();
            if (z) {
                int i4 = (i3 + m.cQ) % m.cQ;
                int i5 = waterMark.mPictureWidth;
                if (i4 == 90 || i4 == 270) {
                    int i6 = waterMark.mPictureHeight;
                    this.mGLCanvas.getState().translate(0.0f, (float) ((i6 / 2) + i2));
                    this.mGLCanvas.getState().rotate(180.0f, 1.0f, 0.0f, 0.0f);
                    this.mGLCanvas.getState().translate(0.0f, (float) (((-i6) / 2) - i2));
                } else {
                    this.mGLCanvas.getState().translate((float) (i5 / 2), 0.0f);
                    this.mGLCanvas.getState().rotate(180.0f, 0.0f, 1.0f, 0.0f);
                    this.mGLCanvas.getState().translate((float) ((-i5) / 2), 0.0f);
                }
            }
            if (i3 != 0) {
                this.mGLCanvas.getState().translate((float) (waterMark.getCenterX() + i), (float) (waterMark.getCenterY() + i2));
                this.mGLCanvas.getState().rotate((float) (-i3), 0.0f, 0.0f, 1.0f);
                this.mGLCanvas.getState().translate((float) ((-i) - waterMark.getCenterX()), (float) ((-i2) - waterMark.getCenterY()));
            }
            BasicRender basicRender = this.mGLCanvas.getBasicRender();
            DrawBasicTexAttribute drawBasicTexAttribute = new DrawBasicTexAttribute(waterMark.getTexture(), i + waterMark.getLeft(), i2 + waterMark.getTop(), waterMark.getWidth(), waterMark.getHeight());
            basicRender.draw(drawBasicTexAttribute);
            this.mGLCanvas.getState().popState();
        }

        private Render fetchRender(int i) {
            RenderGroup effectRenderGroup = this.mGLCanvas.getEffectRenderGroup();
            Render render = effectRenderGroup.getRender(i);
            if (render != null) {
                return render;
            }
            this.mGLCanvas.prepareEffectRenders(false, i);
            return effectRenderGroup.getRender(i);
        }

        /* JADX WARNING: Removed duplicated region for block: B:15:0x0042  */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        private PipeRender getEffectRender(DrawYuvAttribute drawYuvAttribute) {
            int i;
            PipeRender pipeRender = new PipeRender(this.mGLCanvas);
            pipeRender.addRender(fetchRender(FilterInfo.FILTER_ID_YUV2RGB));
            int i2 = drawYuvAttribute.mEffectIndex;
            if (i2 != FilterInfo.FILTER_ID_NONE) {
                Render fetchRender = fetchRender(i2);
                if (fetchRender != null) {
                    pipeRender.addRender(fetchRender);
                }
            }
            String str = drawYuvAttribute.mTiltShiftMode;
            if (str != null) {
                Render render = null;
                if (ComponentRunningTiltValue.TILT_CIRCLE.equals(str)) {
                    i = FilterInfo.FILTER_ID_GAUSSIAN;
                } else {
                    if (ComponentRunningTiltValue.TILT_PARALLEL.equals(drawYuvAttribute.mTiltShiftMode)) {
                        i = FilterInfo.FILTER_ID_TILTSHIFT;
                    }
                    if (render != null) {
                        pipeRender.addRender(render);
                    }
                }
                render = fetchRender(i);
                if (render != null) {
                }
            }
            if (!drawYuvAttribute.mApplyWaterMark) {
                pipeRender.addRender(fetchRender(FilterInfo.FILTER_ID_RGB2YUV));
            }
            return pipeRender;
        }

        private PipeRender getEffectRenderForAI(DrawYuvAttribute drawYuvAttribute) {
            PipeRender pipeRender = new PipeRender(this.mGLCanvas);
            pipeRender.addRender(fetchRender(FilterInfo.FILTER_ID_YUV2RGB));
            return pipeRender;
        }

        private Bitmap getGPURBGA(int i, int i2, int i3, int i4) {
            int i5 = i3 * i4;
            int[] iArr = new int[i5];
            int[] iArr2 = new int[i5];
            IntBuffer wrap = IntBuffer.wrap(iArr);
            wrap.position(0);
            GLES20.glReadPixels(i, i2, i3, i4, 6408, 5121, wrap);
            int i6 = 0;
            int i7 = 0;
            while (i6 < i4) {
                int i8 = i7;
                for (int i9 = 0; i9 < i3; i9++) {
                    int i10 = iArr[i8];
                    iArr2[i8] = (i10 & -16711936) | ((i10 << 16) & 16711680) | ((i10 >> 16) & 255);
                    i8++;
                }
                i6++;
                i7 = i8;
            }
            return Bitmap.createBitmap(iArr2, i3, i4, Config.ARGB_8888);
        }

        private Bitmap getGPUYYY(int i, int i2, int i3, int i4) {
            int i5 = i3 >> 1;
            int i6 = i4 >> 1;
            byte[] bArr = new byte[(i5 * i5 * 4)];
            int i7 = i3 * i4;
            int[] iArr = new int[i7];
            ByteBuffer wrap = ByteBuffer.wrap(bArr);
            wrap.position(0);
            GLES20.glReadPixels(i, i2, i5, i6, 6408, 5121, wrap);
            int i8 = 0;
            for (int i9 = 0; i9 < i7; i9++) {
                byte b = bArr[i9];
                iArr[i8] = (b & -1) | ((b << 8) & 0) | 0 | ((b << 16) & 0);
                i8++;
            }
            return Bitmap.createBitmap(iArr, i3, i4, Config.ARGB_8888);
        }

        private float[] getIntersectRect(float f, float f2, float f3, float f4, float f5, float f6, float f7, float f8) {
            float[] fArr = new float[4];
            if (f <= f5) {
                f = f5;
            }
            fArr[0] = f;
            if (f2 <= f6) {
                f2 = f6;
            }
            fArr[1] = f2;
            if (f3 >= f7) {
                f3 = f7;
            }
            fArr[2] = f3;
            if (f4 >= f8) {
                f4 = f8;
            }
            fArr[3] = f4;
            return fArr;
        }

        private int[] getLocation(Rect rect, WatermarkItem watermarkItem) {
            int[] captureCoordinate = watermarkItem.getCaptureCoordinate();
            int i = captureCoordinate[0];
            int i2 = rect.left;
            int i3 = captureCoordinate[1];
            int i4 = rect.top;
            return new int[]{i - i2, i3 - i4, captureCoordinate[2] - i2, captureCoordinate[3] - i4};
        }

        private float getScale(int i, int i2, Rect rect) {
            int abs = Math.abs(rect.bottom - rect.top);
            int abs2 = Math.abs(rect.right - rect.left);
            int max = Math.max(abs, abs2);
            int min = Math.min(abs, abs2);
            return Math.min(((float) Math.max(i, i2)) / ((float) max), ((float) Math.min(i, i2)) / ((float) min));
        }

        private void initEGL(EGLContext eGLContext, boolean z) {
            if (SnapshotRender.this.mEglCore == null) {
                SnapshotRender.this.mEglCore = new EglCore(eGLContext, 2);
            }
            if (z && SnapshotRender.this.mRenderSurface != null) {
                SnapshotRender.this.mRenderSurface.release();
                SnapshotRender.this.mRenderSurface = null;
            }
            SnapshotRender snapshotRender = SnapshotRender.this;
            snapshotRender.mRenderSurface = new PbufferSurface(snapshotRender.mEglCore, 1, 1);
            SnapshotRender.this.mRenderSurface.makeCurrent();
        }

        private boolean rectangle_collision(float f, float f2, float f3, float f4, float f5, float f6, float f7, float f8) {
            return f <= f7 + f5 && f + f3 >= f5 && f2 <= f8 + f6 && f2 + f4 >= f6;
        }

        private void release() {
            FrameBuffer frameBuffer = this.mFrameBuffer;
            if (frameBuffer != null) {
                frameBuffer.release();
                this.mFrameBuffer = null;
            }
            FrameBuffer frameBuffer2 = this.mWatermarkFrameBuffer;
            if (frameBuffer2 != null) {
                frameBuffer2.release();
                this.mWatermarkFrameBuffer = null;
            }
            if (SnapshotRender.this.mRenderSurface != null) {
                SnapshotRender.this.mRenderSurface.makeCurrent();
                this.mGLCanvas.recycledResources();
                SnapshotRender.this.mRenderSurface.makeNothingCurrent();
            }
            this.mGLCanvas = null;
            SnapshotRender.this.destroy();
        }

        private int[] reverseCalculateRange(int i, int i2, int i3, int i4, int[] iArr) {
            if (C0124O00000oO.isMTKPlatform() && (i == i3 || i2 == i4)) {
                return iArr;
            }
            float f = ((float) i4) / ((float) i2);
            if (((float) i3) / ((float) i) == f || i3 == i4) {
                return new int[]{(int) (((float) iArr[0]) * f), (int) (((float) iArr[1]) * f), (int) (((float) iArr[2]) * f), (int) (((float) iArr[3]) * f)};
            }
            String access$400 = SnapshotRender.TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("origin w:");
            sb.append(i3);
            sb.append(" origin h:");
            sb.append(i4);
            sb.append(" image w:");
            sb.append(i);
            sb.append(" image h:");
            sb.append(i2);
            sb.append(" in different ratio");
            Log.e(access$400, sb.toString());
            return null;
        }

        private void updateRenderParameters(Render render, DrawYuvAttribute drawYuvAttribute, boolean z) {
            if (z) {
                render.setViewportSize(SnapshotRender.this.mBlockWidth, SnapshotRender.this.mBlockHeight);
            } else {
                render.setViewportSize(drawYuvAttribute.mPictureSize.getWidth(), drawYuvAttribute.mPictureSize.getHeight());
            }
            render.setPreviewSize(drawYuvAttribute.mPreviewSize.getWidth(), drawYuvAttribute.mPreviewSize.getHeight());
            render.setEffectRangeAttribute(drawYuvAttribute.mAttribute);
            render.setMirror(drawYuvAttribute.mMirror);
            render.setSnapshotSize(drawYuvAttribute.mPictureSize.getWidth(), drawYuvAttribute.mPictureSize.getHeight());
            render.setOrientation(drawYuvAttribute.mOrientation);
            render.setShootRotation(drawYuvAttribute.mShootRotation);
            render.setJpegOrientation(drawYuvAttribute.mJpegRotation);
        }

        public void drawAIWaterMark(int i, int i2, int i3, int i4, int i5, Bitmap bitmap, int[] iArr, float f, boolean z) {
            if (bitmap != null) {
                int i6 = i5;
                AIImageWaterMark aIImageWaterMark = new AIImageWaterMark(bitmap, i3, i4, i6, iArr, f);
                drawWaterMark(aIImageWaterMark, i, i2, i6, z);
            }
        }

        public void drawWaterMark(int i, int i2, int i3, int i4, int i5, String str, boolean z) {
            int i6 = i5;
            if (str != null) {
                int i7 = i5;
                NewStyleTextWaterMark newStyleTextWaterMark = new NewStyleTextWaterMark(str, i3, i4, i7, SnapshotRender.this.mDeviceWaterMarkParam.isCinematicAspectRatio());
                drawWaterMark(newStyleTextWaterMark, i, i2, i7, z);
            }
            Bitmap bitmap = null;
            if (SnapshotRender.this.mFrontCameraWaterMarkBitmap != null && SnapshotRender.this.mDeviceWaterMarkParam.isFrontWatermarkEnable()) {
                bitmap = SnapshotRender.this.mFrontCameraWaterMarkBitmap;
            } else if (SnapshotRender.this.mDualCameraWaterMarkBitmap != null && SnapshotRender.this.mDeviceWaterMarkParam.isDualWatermarkEnable()) {
                if (!SnapshotRender.this.mDeviceWaterMarkParam.isCinematicAspectRatio() || i6 == 0 || i6 == 180) {
                    SnapshotRender snapshotRender = SnapshotRender.this;
                    snapshotRender.mDualCameraWaterMarkBitmap = snapshotRender.loadCameraWatermark(CameraAppImpl.getAndroidContext());
                    bitmap = SnapshotRender.this.mDualCameraWaterMarkBitmap;
                } else {
                    SnapshotRender snapshotRender2 = SnapshotRender.this;
                    snapshotRender2.mCinematicRatioWaterMarkBitmap = snapshotRender2.loadCinematicRatioWatermark(CameraAppImpl.getAndroidContext());
                    bitmap = SnapshotRender.this.mCinematicRatioWaterMarkBitmap;
                }
            }
            Bitmap bitmap2 = bitmap;
            if (bitmap2 != null) {
                ImageWaterMark imageWaterMark = new ImageWaterMark(bitmap2, i3, i4, i5, SnapshotRender.this.mDeviceWaterMarkParam.getSize(), SnapshotRender.this.mDeviceWaterMarkParam.getPaddingX(), SnapshotRender.this.mDeviceWaterMarkParam.getPaddingY(), SnapshotRender.this.mDeviceWaterMarkParam.isCinematicAspectRatio());
                drawWaterMark(imageWaterMark, i, i2, i5, z);
            }
        }

        public void handleMessage(Message message) {
            int i = message.what;
            if (i == 0) {
                initEGL(null, false);
                this.mGLCanvas = new SnapshotCanvas();
            } else if (i == 1) {
                drawImage((DrawYuvAttribute) message.obj, false);
                this.mGLCanvas.recycledResources();
                if (SnapshotRender.this.mReleasePending && !hasMessages(1)) {
                    release();
                }
                synchronized (SnapshotRender.this.mLock) {
                    SnapshotRender.this.mImageQueueSize = SnapshotRender.this.mImageQueueSize - 1;
                }
            } else if (i == 2) {
                YuvAttributeWrapper yuvAttributeWrapper = (YuvAttributeWrapper) message.obj;
                DrawYuvAttribute drawYuvAttribute = yuvAttributeWrapper.mAttribute;
                ConditionVariable conditionVariable = yuvAttributeWrapper.mBlocker;
                if (drawYuvAttribute.mPictureSize.getWidth() == 0 || drawYuvAttribute.mPictureSize.getHeight() == 0) {
                    Log.e(SnapshotRender.TAG, String.format(Locale.ENGLISH, "yuv image is broken width %d height %d", new Object[]{Integer.valueOf(drawYuvAttribute.mPictureSize.getWidth()), Integer.valueOf(drawYuvAttribute.mPictureSize.getHeight())}));
                    if (conditionVariable == null) {
                        return;
                    }
                } else {
                    this.mGLCanvas.setSize(drawYuvAttribute.mPictureSize.getWidth(), drawYuvAttribute.mPictureSize.getHeight());
                    int access$500 = SnapshotRender.this.calEachBlockHeight(drawYuvAttribute.mPictureSize.getWidth(), drawYuvAttribute.mPictureSize.getHeight());
                    SnapshotRender.this.mBlockWidth = drawYuvAttribute.mPictureSize.getWidth();
                    IntBuffer allocate = IntBuffer.allocate(2);
                    GLES20.glGetIntegerv(3379, allocate);
                    boolean z = false;
                    while (SnapshotRender.this.mBlockWidth > allocate.get(0)) {
                        SnapshotRender snapshotRender = SnapshotRender.this;
                        snapshotRender.mBlockWidth = snapshotRender.mBlockWidth / 2;
                        access$500 /= 2;
                        if ((drawYuvAttribute.mJpegRotation + m.cQ) % 180 == 0) {
                            z = true;
                        }
                    }
                    SnapshotRender.this.mBlockHeight = drawYuvAttribute.mPictureSize.getHeight() / access$500;
                    SnapshotRender snapshotRender2 = SnapshotRender.this;
                    snapshotRender2.mAdjWidth = snapshotRender2.mBlockWidth;
                    SnapshotRender snapshotRender3 = SnapshotRender.this;
                    snapshotRender3.mAdjHeight = snapshotRender3.mBlockHeight;
                    if (SnapshotRender.this.mBlockHeight % 4 != 0) {
                        SnapshotRender snapshotRender4 = SnapshotRender.this;
                        SnapshotRender.access$712(snapshotRender4, 4 - (snapshotRender4.mBlockHeight % 4));
                    }
                    drawImage(drawYuvAttribute, z);
                    this.mGLCanvas.recycledResources();
                    if (conditionVariable == null) {
                        return;
                    }
                }
                conditionVariable.open();
            } else if (i == 5) {
                release();
            } else if (i == 6) {
                this.mGLCanvas.prepareEffectRenders(false, message.arg1);
            }
        }
    }

    class RenderHolder {
        /* access modifiers changed from: private */
        public static SnapshotRender render = new SnapshotRender();

        private RenderHolder() {
        }
    }

    private static /* synthetic */ void $closeResource(Throwable th, AutoCloseable autoCloseable) {
        if (th != null) {
            try {
                autoCloseable.close();
            } catch (Throwable th2) {
                th.addSuppressed(th2);
            }
        } else {
            autoCloseable.close();
        }
    }

    private SnapshotRender() {
        this.mQuality = 97;
        this.mImageQueueSize = 0;
        this.mLock = new Object();
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("SnapshotRender created ");
        sb.append(this);
        Log.d(str, sb.toString());
        this.mEglThread = new HandlerThread(TAG);
        this.mEglThread.start();
        this.mEglHandler = new EGLHandler(this.mEglThread.getLooper());
        if (this.mMemImage == null) {
            this.mMemImage = new MemYuvImage();
        }
        this.mFrameCounter = new CounterUtil();
        this.mTotalCounter = new CounterUtil();
        this.mSplitter = new Splitter();
        this.mEglHandler.sendEmptyMessage(0);
        this.mRelease = false;
    }

    static /* synthetic */ int access$712(SnapshotRender snapshotRender, int i) {
        int i2 = snapshotRender.mBlockHeight + i;
        snapshotRender.mBlockHeight = i2;
        return i2;
    }

    /* access modifiers changed from: private */
    public int calEachBlockHeight(int i, int i2) {
        int i3 = 1;
        while (i * i2 > 6000000) {
            i2 >>= 1;
            i3 <<= 1;
        }
        return i3;
    }

    /* access modifiers changed from: private */
    public void destroy() {
        this.mRelease = true;
        this.mReleasePending = false;
        PbufferSurface pbufferSurface = this.mRenderSurface;
        if (pbufferSurface != null) {
            pbufferSurface.release();
            this.mRenderSurface = null;
        }
        EglCore eglCore = this.mEglCore;
        if (eglCore != null) {
            eglCore.release();
            this.mEglCore = null;
        }
        this.mEglThread.quit();
        Bitmap bitmap = this.mDualCameraWaterMarkBitmap;
        if (bitmap != null && !bitmap.isRecycled()) {
            this.mDualCameraWaterMarkBitmap.recycle();
            this.mDualCameraWaterMarkBitmap = null;
        }
        Bitmap bitmap2 = this.mFrontCameraWaterMarkBitmap;
        if (bitmap2 != null && !bitmap2.isRecycled()) {
            this.mFrontCameraWaterMarkBitmap.recycle();
            this.mFrontCameraWaterMarkBitmap = null;
        }
        Bitmap bitmap3 = this.mCinematicRatioWaterMarkBitmap;
        if (bitmap3 != null && !bitmap3.isRecycled()) {
            this.mCinematicRatioWaterMarkBitmap.recycle();
            this.mCinematicRatioWaterMarkBitmap = null;
        }
        System.gc();
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("SnapshotRender released ");
        sb.append(this);
        Log.d(str, sb.toString());
    }

    public static SnapshotRender getRender() {
        return RenderHolder.render;
    }

    /* access modifiers changed from: private */
    /* JADX WARNING: Code restructure failed: missing block: B:20:0x0051, code lost:
        r0 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:22:?, code lost:
        $closeResource(r2, r1);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:23:0x0055, code lost:
        throw r0;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public Bitmap loadCameraWatermark(Context context) {
        Options options = new Options();
        options.inScaled = false;
        options.inPurgeable = true;
        options.inPremultiplied = false;
        if (!C0122O00000o.instance().OOo0ooO() && !C0122O00000o.instance().OOoOo0O()) {
            return BitmapFactory.decodeFile(CameraSettings.getDualCameraWaterMarkFilePathVendor(), options);
        }
        File file = new File(context.getFilesDir(), Util.getDefaultWatermarkFileName());
        if (!file.exists()) {
            Util.generateMainWatermark2File();
        }
        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            Bitmap decodeStream = BitmapFactory.decodeStream(fileInputStream, null, options);
            $closeResource(null, fileInputStream);
            return decodeStream;
        } catch (Exception e) {
            Log.d(TAG, "Failed to load app camera watermark ", (Throwable) e);
            return null;
        }
    }

    /* access modifiers changed from: private */
    /* JADX WARNING: Code restructure failed: missing block: B:18:0x0047, code lost:
        r0 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:20:?, code lost:
        $closeResource(r3, r4);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:21:0x004b, code lost:
        throw r0;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public Bitmap loadCinematicRatioWatermark(Context context) {
        Options options = new Options();
        options.inScaled = false;
        options.inPurgeable = true;
        options.inPremultiplied = false;
        if (C0122O00000o.instance().OOo0ooO() || C0122O00000o.instance().OOoOo0O()) {
            File file = new File(context.getFilesDir(), Util.WATERMARK_CINEMATIC_RATIO_FILE_NAME);
            if (!file.exists()) {
                return Util.generateCinematicRatioWatermark2File();
            }
            try {
                FileInputStream fileInputStream = new FileInputStream(file);
                Bitmap decodeStream = BitmapFactory.decodeStream(fileInputStream, null, options);
                $closeResource(null, fileInputStream);
                return decodeStream;
            } catch (Exception e) {
                Log.d(TAG, "Failed to load app camera watermark ", (Throwable) e);
            }
        }
        return null;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:17:0x0045, code lost:
        r0 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:19:?, code lost:
        $closeResource(r3, r4);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:20:0x0049, code lost:
        throw r0;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private Bitmap loadFrontCameraWatermark(Context context) {
        Options options = new Options();
        options.inScaled = false;
        options.inPurgeable = true;
        options.inPremultiplied = false;
        if (C0122O00000o.instance().OOo0ooO() || C0122O00000o.instance().OOoOo0O()) {
            File file = new File(context.getFilesDir(), Util.WATERMARK_FRONT_FILE_NAME);
            if (!file.exists()) {
                Util.generateFrontWatermark2File();
            }
            try {
                FileInputStream fileInputStream = new FileInputStream(file);
                Bitmap decodeStream = BitmapFactory.decodeStream(fileInputStream, null, options);
                $closeResource(null, fileInputStream);
                return decodeStream;
            } catch (Exception e) {
                Log.d(TAG, "Failed to load front camera watermark ", (Throwable) e);
            }
        }
        return null;
    }

    public boolean isRelease() {
        return this.mReleasePending || this.mRelease;
    }

    public void prepareEffectRender(DeviceWatermarkParam deviceWatermarkParam, int i) {
        this.mDeviceWaterMarkParam = deviceWatermarkParam;
        if (deviceWatermarkParam.isDualWatermarkEnable() && this.mDualCameraWaterMarkBitmap == null) {
            this.mDualCameraWaterMarkBitmap = loadCameraWatermark(CameraAppImpl.getAndroidContext());
        } else if (deviceWatermarkParam.isFrontWatermarkEnable() && this.mFrontCameraWaterMarkBitmap == null) {
            this.mFrontCameraWaterMarkBitmap = loadFrontCameraWatermark(CameraAppImpl.getAndroidContext());
        }
        if (i != FilterInfo.FILTER_ID_NONE) {
            this.mEglHandler.obtainMessage(6, i, 0).sendToTarget();
        }
    }

    public boolean processImageAsync(DrawYuvAttribute drawYuvAttribute) {
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("queueSize=");
        sb.append(this.mImageQueueSize);
        Log.d(str, sb.toString());
        if (this.mImageQueueSize >= 7) {
            Log.d(TAG, "queueSize is full");
            return false;
        }
        synchronized (this.mLock) {
            this.mImageQueueSize++;
        }
        this.mEglHandler.obtainMessage(1, drawYuvAttribute).sendToTarget();
        return true;
    }

    public void processImageSync(YuvAttributeWrapper yuvAttributeWrapper) {
        this.mEglHandler.obtainMessage(2, yuvAttributeWrapper).sendToTarget();
    }

    public void release() {
        if (this.mEglHandler.hasMessages(1)) {
            Log.d(TAG, "release: try to release but message is not null, so pending it");
            this.mReleasePending = true;
            return;
        }
        this.mEglHandler.sendEmptyMessage(5);
    }
}
