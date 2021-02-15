package com.android.camera.effect.renders;

import O00000Oo.O00000oO.O000000o.C0122O00000o;
import O00000Oo.O00000oO.O000000o.C0124O00000oO;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.RectF;
import android.location.Location;
import android.net.Uri;
import android.opengl.EGLContext;
import android.opengl.GLES20;
import android.os.ConditionVariable;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.util.TypedValue;
import com.android.camera.CameraAppImpl;
import com.android.camera.CameraSettings;
import com.android.camera.Display;
import com.android.camera.EncodingQuality;
import com.android.camera.R;
import com.android.camera.Thumbnail;
import com.android.camera.Util;
import com.android.camera.data.DataRepository;
import com.android.camera.data.data.runing.ComponentRunningTiltValue;
import com.android.camera.effect.FilterInfo;
import com.android.camera.effect.FrameBuffer;
import com.android.camera.effect.ShaderNativeUtil;
import com.android.camera.effect.SnapshotCanvas;
import com.android.camera.effect.draw_mode.DrawBasicTexAttribute;
import com.android.camera.effect.draw_mode.DrawIntTexAttribute;
import com.android.camera.effect.draw_mode.DrawJPEGAttribute;
import com.android.camera.effect.framework.gles.EglCore;
import com.android.camera.effect.framework.gles.OpenGlUtils;
import com.android.camera.effect.framework.gles.PbufferSurface;
import com.android.camera.effect.framework.graphics.Block;
import com.android.camera.effect.framework.graphics.GraphicBuffer;
import com.android.camera.effect.framework.graphics.Splitter;
import com.android.camera.effect.framework.image.MemImage;
import com.android.camera.effect.framework.utils.CounterUtil;
import com.android.camera.log.Log;
import com.android.camera.storage.ImageSaver;
import com.android.camera.storage.ImageSaver.ImageSaverCallback;
import com.android.camera.storage.Storage;
import com.android.camera.watermark.WaterMarkBitmap;
import com.android.camera.watermark.WaterMarkData;
import com.android.gallery3d.exif.ExifInterface;
import com.android.gallery3d.ui.BaseGLCanvas;
import com.arcsoft.supernight.SuperNightProcess;
import com.xiaomi.camera.core.PictureInfo;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.ref.WeakReference;
import java.nio.IntBuffer;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class SnapshotEffectRender {
    private static final int DEFAULT_BLOCK_HEIGHT = 1500;
    private static final int DEFAULT_BLOCK_WIDTH = 4000;
    private static final int QUEUE_LIMIT = 7;
    /* access modifiers changed from: private */
    public static final String TAG = "SnapshotEffectRender";
    /* access modifiers changed from: private */
    public int mBlockHeight;
    /* access modifiers changed from: private */
    public int mBlockWidth;
    /* access modifiers changed from: private */
    public Bitmap mCinematicRatioWaterMarkBitmap;
    /* access modifiers changed from: private */
    public Context mContext;
    /* access modifiers changed from: private */
    public String mCurrentCustomWaterMarkText;
    /* access modifiers changed from: private */
    public Bitmap mDualCameraWaterMarkBitmap;
    /* access modifiers changed from: private */
    public float mDualCameraWaterMarkPaddingXRatio;
    /* access modifiers changed from: private */
    public float mDualCameraWaterMarkPaddingYRatio;
    /* access modifiers changed from: private */
    public float mDualCameraWaterMarkSizeRatio;
    /* access modifiers changed from: private */
    public EglCore mEglCore;
    private EGLHandler mEglHandler;
    private HandlerThread mEglThread;
    /* access modifiers changed from: private */
    public ConditionVariable mEglThreadBlockVar = new ConditionVariable();
    private boolean mExifNeeded = true;
    /* access modifiers changed from: private */
    public FrameBuffer mFrameBuffer;
    /* access modifiers changed from: private */
    public CounterUtil mFrameCounter;
    /* access modifiers changed from: private */
    public Bitmap mFrontCameraWaterMarkBitmap;
    /* access modifiers changed from: private */
    public float mFrontCameraWaterMarkPaddingXRatio;
    /* access modifiers changed from: private */
    public float mFrontCameraWaterMarkPaddingYRatio;
    /* access modifiers changed from: private */
    public float mFrontCameraWaterMarkSizeRatio;
    /* access modifiers changed from: private */
    public SnapshotCanvas mGLCanvas;
    /* access modifiers changed from: private */
    public GraphicBuffer mGraphicBuffer;
    /* access modifiers changed from: private */
    public ImageSaver mImageSaver;
    /* access modifiers changed from: private */
    public boolean mInitGraphicBuffer;
    private boolean mIsImageCaptureIntent;
    /* access modifiers changed from: private */
    public volatile int mJpegQueueSize = 0;
    /* access modifiers changed from: private */
    public final Object mLock = new Object();
    /* access modifiers changed from: private */
    public MemImage mMemImage;
    /* access modifiers changed from: private */
    public int mQuality = 97;
    private boolean mRelease;
    /* access modifiers changed from: private */
    public boolean mReleasePending;
    /* access modifiers changed from: private */
    public CounterUtil mRenderCounter;
    /* access modifiers changed from: private */
    public PbufferSurface mRenderSurface;
    /* access modifiers changed from: private */
    public WeakReference mSaverCallback;
    /* access modifiers changed from: private */
    public Splitter mSplitter;
    /* access modifiers changed from: private */
    public int mSquareModeExtraMargin;
    /* access modifiers changed from: private */
    public int mTextureId;
    /* access modifiers changed from: private */
    public Map mTitleMap = new HashMap(7);
    /* access modifiers changed from: private */
    public CounterUtil mTotalCounter;

    class EGLHandler extends Handler {
        public static final int MSG_DRAW_MAIN_ASYNC = 1;
        public static final int MSG_DRAW_MAIN_SYNC = 2;
        public static final int MSG_DRAW_THUMB = 4;
        public static final int MSG_GET_DRAW_THUMB = 3;
        public static final int MSG_INIT_EGL_SYNC = 0;
        public static final int MSG_PREPARE_EFFECT_RENDER = 6;
        public static final int MSG_RELEASE = 5;

        public EGLHandler(Looper looper) {
            super(looper);
        }

        /* JADX WARNING: type inference failed for: r8v5, types: [int, boolean] */
        /* JADX WARNING: type inference failed for: r8v6 */
        /* JADX WARNING: type inference failed for: r8v7 */
        /* JADX WARNING: type inference failed for: r8v8 */
        /* JADX WARNING: type inference failed for: r8v9 */
        /* JADX WARNING: type inference failed for: r8v16 */
        /* JADX WARNING: type inference failed for: r8v17 */
        /* JADX WARNING: type inference failed for: r8v18 */
        /* JADX WARNING: type inference failed for: r8v19 */
        /* JADX WARNING: Multi-variable type inference failed. Error: jadx.core.utils.exceptions.JadxRuntimeException: No candidate types for var: r8v5, types: [int, boolean]
  assigns: []
  uses: [?[int, short, byte, char], int, boolean]
  mth insns count: 308
        	at jadx.core.dex.visitors.typeinference.TypeSearch.fillTypeCandidates(TypeSearch.java:237)
        	at java.util.ArrayList.forEach(ArrayList.java:1259)
        	at jadx.core.dex.visitors.typeinference.TypeSearch.run(TypeSearch.java:53)
        	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.runMultiVariableSearch(TypeInferenceVisitor.java:99)
        	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.visit(TypeInferenceVisitor.java:92)
        	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:27)
        	at jadx.core.dex.visitors.DepthTraversal.lambda$visit$1(DepthTraversal.java:14)
        	at java.util.ArrayList.forEach(ArrayList.java:1259)
        	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
        	at jadx.core.dex.visitors.DepthTraversal.lambda$visit$0(DepthTraversal.java:13)
        	at java.util.ArrayList.forEach(ArrayList.java:1259)
        	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:13)
        	at jadx.core.ProcessClass.process(ProcessClass.java:30)
        	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:311)
        	at jadx.api.JavaClass.decompile(JavaClass.java:62)
        	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:217)
         */
        /* JADX WARNING: Unknown variable types count: 4 */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        private byte[] applyEffect(DrawJPEGAttribute drawJPEGAttribute, int i, boolean z, Size size, Size size2) {
            int i2;
            int i3;
            int i4;
            int i5;
            int i6;
            int i7;
            int i8;
            EGLHandler eGLHandler;
            int i9;
            ? r8;
            int[] iArr;
            byte[] bArr;
            byte[] bArr2;
            int[] iArr2;
            ? r82;
            int i10;
            DrawJPEGAttribute drawJPEGAttribute2 = drawJPEGAttribute;
            boolean z2 = z;
            Size size3 = size;
            Size size4 = size2;
            SnapshotEffectRender.this.mRenderSurface.makeCurrent();
            String access$1000 = SnapshotEffectRender.TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("applyEffect: applyToThumb = ");
            sb.append(z2);
            Log.d(access$1000, sb.toString());
            byte[] thumbnailBytes = z2 ? drawJPEGAttribute2.mExif.getThumbnailBytes() : drawJPEGAttribute2.mData;
            if (thumbnailBytes == null) {
                String access$10002 = SnapshotEffectRender.TAG;
                StringBuilder sb2 = new StringBuilder();
                sb2.append("Null ");
                sb2.append(z2 ? "thumb!" : "jpeg!");
                Log.w(access$10002, sb2.toString());
                return null;
            } else if (!z2 && drawJPEGAttribute2.mEffectIndex == FilterInfo.FILTER_ID_NONE && !CameraSettings.isAgeGenderAndMagicMirrorWaterOpen() && !CameraSettings.isTiltShiftOn()) {
                return applyEffectOnlyWatermarkRange(drawJPEGAttribute2, size3, size4);
            } else {
                CounterUtil counterUtil = new CounterUtil();
                String str = "init Texture";
                counterUtil.reset(str);
                int[] iArr3 = new int[1];
                GLES20.glGenTextures(1, iArr3, 0);
                int[] initTexture = ShaderNativeUtil.initTexture(thumbnailBytes, iArr3[0], i);
                GLES20.glFlush();
                counterUtil.tick(str);
                int i11 = initTexture[0];
                int i12 = initTexture[1];
                int i13 = z2 ? initTexture[0] : drawJPEGAttribute2.mPreviewWidth;
                int i14 = z2 ? initTexture[1] : drawJPEGAttribute2.mPreviewHeight;
                Render effectRender = getEffectRender(drawJPEGAttribute2.mEffectIndex);
                if (effectRender == null) {
                    Log.w(SnapshotEffectRender.TAG, "init render failed");
                    return thumbnailBytes;
                }
                if (effectRender instanceof PipeRender) {
                    ((PipeRender) effectRender).setFrameBufferSize(i11, i12);
                }
                effectRender.setPreviewSize(i13, i14);
                effectRender.setEffectRangeAttribute(drawJPEGAttribute2.mAttribute);
                effectRender.setMirror(drawJPEGAttribute2.mMirror);
                if (z2) {
                    effectRender.setSnapshotSize(i11, i12);
                } else {
                    effectRender.setSnapshotSize(size4.width, size4.height);
                }
                effectRender.setOrientation(drawJPEGAttribute2.mOrientation);
                effectRender.setShootRotation(drawJPEGAttribute2.mShootRotation);
                effectRender.setJpegOrientation(drawJPEGAttribute2.mJpegOrientation);
                checkFrameBuffer(i11, i12);
                SnapshotEffectRender.this.mGLCanvas.beginBindFrameBuffer(SnapshotEffectRender.this.mFrameBuffer);
                effectRender.setParentFrameBufferId(SnapshotEffectRender.this.mFrameBuffer.getId());
                DrawIntTexAttribute drawIntTexAttribute = new DrawIntTexAttribute(iArr3[0], 0, 0, i11, i12, true);
                effectRender.draw(drawIntTexAttribute);
                effectRender.deleteBuffer();
                if (drawJPEGAttribute2.mRequestModuleIdx != 165) {
                    i5 = i11;
                    i2 = i12;
                    i4 = 0;
                    i3 = 0;
                } else if (i11 > i12) {
                    i3 = ((i11 - i12) / 2) - ((SnapshotEffectRender.this.mSquareModeExtraMargin * i12) / Display.getWindowWidth());
                    i5 = i12;
                    i2 = i5;
                    i4 = 0;
                } else {
                    i4 = ((i12 - i11) / 2) - ((SnapshotEffectRender.this.mSquareModeExtraMargin * i11) / Display.getWindowWidth());
                    i5 = i11;
                    i2 = i5;
                    i3 = 0;
                }
                int i15 = i5;
                int i16 = i4;
                int[] iArr4 = iArr3;
                int i17 = i3;
                CounterUtil counterUtil2 = counterUtil;
                drawAgeGenderAndMagicMirrorWater(drawJPEGAttribute2.mWaterInfos, i3, i16, i11, i12, i13, i14, drawJPEGAttribute2.mJpegOrientation, drawJPEGAttribute2.mIsPortraitRawData);
                if (!z2) {
                    drawJPEGAttribute2.mWidth = i15;
                    drawJPEGAttribute2.mHeight = i2;
                } else if (size3 != null) {
                    size3.width = i15;
                    size3.height = i2;
                    String access$10003 = SnapshotEffectRender.TAG;
                    StringBuilder sb3 = new StringBuilder();
                    sb3.append("thumbSize=");
                    sb3.append(size3.width);
                    sb3.append("*");
                    sb3.append(size3.height);
                    Log.d(access$10003, sb3.toString());
                }
                if (drawJPEGAttribute2.mApplyWaterMark) {
                    if (!z2) {
                        int[] watermarkRange = Util.getWatermarkRange(drawJPEGAttribute2.mWidth, drawJPEGAttribute2.mHeight, drawJPEGAttribute2.mJpegOrientation, drawJPEGAttribute2.mHasDualWaterMark, drawJPEGAttribute2.mHasFrontWaterMark, drawJPEGAttribute2.mTimeWaterMarkText, drawJPEGAttribute2.mDeviceWatermarkParam);
                        String access$10004 = SnapshotEffectRender.TAG;
                        StringBuilder sb4 = new StringBuilder();
                        sb4.append("applyEffect: rotation = ");
                        sb4.append(drawJPEGAttribute2.mJpegOrientation);
                        sb4.append(", watermarkRange = ");
                        sb4.append(Arrays.toString(watermarkRange));
                        Log.d(access$10004, sb4.toString());
                        i9 = i17;
                        i8 = 1;
                        i7 = i16;
                        i10 = i15;
                        eGLHandler = this;
                        iArr2 = watermarkRange;
                        bArr2 = ShaderNativeUtil.getPicture(watermarkRange[0] + i9, watermarkRange[1] + i7, watermarkRange[2], watermarkRange[3], SnapshotEffectRender.this.mQuality);
                        r82 = 0;
                    } else {
                        i7 = i16;
                        i10 = i15;
                        i9 = i17;
                        i8 = 1;
                        eGLHandler = this;
                        iArr2 = null;
                        bArr2 = null;
                        r82 = 0;
                    }
                    i6 = i10;
                    drawTimeWaterMark(drawJPEGAttribute, i9, i7, i10, i2, drawJPEGAttribute2.mJpegOrientation, null);
                    if (C0122O00000o.instance().OOo0OO()) {
                        PictureInfo pictureInfo = drawJPEGAttribute2.mInfo;
                        if (pictureInfo != null && pictureInfo.isFrontCamera()) {
                            drawFrontCameraWaterMark(drawJPEGAttribute, i9, i7, i6, i2, drawJPEGAttribute2.mJpegOrientation, null);
                            iArr = iArr2;
                            bArr = bArr2;
                            r8 = r82;
                        }
                    }
                    drawDoubleShotWaterMark(drawJPEGAttribute, i9, i7, i6, i2, drawJPEGAttribute2.mJpegOrientation, null);
                    iArr = iArr2;
                    bArr = bArr2;
                    r8 = r82;
                } else {
                    i7 = i16;
                    i6 = i15;
                    i9 = i17;
                    i8 = 1;
                    eGLHandler = this;
                    bArr = null;
                    iArr = null;
                    r8 = 0;
                }
                CounterUtil counterUtil3 = counterUtil2;
                counterUtil3.tick("draw");
                GLES20.glPixelStorei(SuperNightProcess.ASVL_PAF_RAW12_RGGB_12B, i8);
                int access$2900 = SnapshotEffectRender.this.mQuality;
                if (z2) {
                    access$2900 = Math.min(SnapshotEffectRender.this.mQuality, EncodingQuality.NORMAL.toInteger(r8));
                }
                if (drawJPEGAttribute2.isMiMovieOpen && !z2) {
                    Size size5 = size2;
                    Util.drawMiMovieBlackBridgeEGL(SnapshotEffectRender.this.mGLCanvas, size5.width, size5.height);
                }
                byte[] picture = ShaderNativeUtil.getPicture(i9, i7, i6, i2, access$2900);
                counterUtil3.tick("readpixels");
                int[] iArr5 = iArr4;
                if (GLES20.glIsTexture(iArr5[r8])) {
                    GLES20.glDeleteTextures(i8, iArr5, r8);
                }
                SnapshotEffectRender.this.mGLCanvas.endBindFrameBuffer();
                SnapshotEffectRender.this.mRenderSurface.makeNothingCurrent();
                if (drawJPEGAttribute2.mApplyWaterMark) {
                    drawJPEGAttribute2.mDataOfTheRegionUnderWatermarks = bArr;
                    drawJPEGAttribute2.mCoordinatesOfTheRegionUnderWatermarks = iArr;
                }
                return picture;
            }
        }

        /* JADX WARNING: Removed duplicated region for block: B:34:0x025c  */
        /* JADX WARNING: Removed duplicated region for block: B:35:0x0271  */
        /* JADX WARNING: Removed duplicated region for block: B:38:0x02b4  */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        private byte[] applyEffectOnlyWatermarkRange(DrawJPEGAttribute drawJPEGAttribute, Size size, Size size2) {
            int i;
            int i2;
            int i3;
            int i4;
            int i5;
            GraphicBuffer graphicBuffer;
            DrawJPEGAttribute drawJPEGAttribute2 = drawJPEGAttribute;
            Size size3 = size2;
            if (!drawJPEGAttribute2.mApplyWaterMark && drawJPEGAttribute2.mRequestModuleIdx != 165) {
                return drawJPEGAttribute2.mData;
            }
            long currentTimeMillis = System.currentTimeMillis();
            int[] decompressPicture = ShaderNativeUtil.decompressPicture(drawJPEGAttribute2.mData, 1);
            String access$1000 = SnapshotEffectRender.TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("jpeg decompress total time =");
            sb.append(System.currentTimeMillis() - currentTimeMillis);
            Log.d(access$1000, sb.toString());
            int i6 = drawJPEGAttribute2.mPreviewWidth;
            int i7 = drawJPEGAttribute2.mPreviewHeight;
            int i8 = decompressPicture[0];
            int i9 = decompressPicture[1];
            if (drawJPEGAttribute2.mRequestModuleIdx != 165) {
                i2 = i9;
                i3 = i8;
                i4 = 0;
                i = 0;
            } else if (i8 > i9) {
                i3 = i9;
                i2 = i3;
                i = ((i8 - i9) / 2) - ((SnapshotEffectRender.this.mSquareModeExtraMargin * i9) / Display.getWindowWidth());
                i4 = 0;
            } else {
                i4 = ((i9 - i8) / 2) - ((SnapshotEffectRender.this.mSquareModeExtraMargin * i8) / Display.getWindowWidth());
                i3 = i8;
                i2 = i3;
                i = 0;
            }
            drawJPEGAttribute2.mWidth = i3;
            drawJPEGAttribute2.mHeight = i2;
            if (drawJPEGAttribute2.mRequestModuleIdx == 165 && !drawJPEGAttribute2.mDeviceWaterMarkEnabled && drawJPEGAttribute2.mTimeWaterMarkText == null) {
                ShaderNativeUtil.getCenterSquareImage(i, i4);
                return ShaderNativeUtil.compressPicture(i3, i2, SnapshotEffectRender.this.mQuality);
            }
            int i10 = i2;
            int[] watermarkRange = Util.getWatermarkRange(drawJPEGAttribute2.mWidth, drawJPEGAttribute2.mHeight, drawJPEGAttribute2.mJpegOrientation, drawJPEGAttribute2.mHasDualWaterMark, drawJPEGAttribute2.mHasFrontWaterMark, drawJPEGAttribute2.mTimeWaterMarkText, drawJPEGAttribute2.mDeviceWatermarkParam);
            String access$10002 = SnapshotEffectRender.TAG;
            StringBuilder sb2 = new StringBuilder();
            sb2.append("applyEffectOnlyWatermarkRange: rotation = ");
            sb2.append(drawJPEGAttribute2.mJpegOrientation);
            sb2.append(", watermarkRange = ");
            sb2.append(Arrays.toString(watermarkRange));
            Log.d(access$10002, sb2.toString());
            int i11 = watermarkRange[2];
            int i12 = watermarkRange[3];
            SnapshotEffectRender.this.mRenderSurface.makeCurrent();
            SnapshotEffectRender.this.mGraphicBuffer.reszieBuffer(i11, i12);
            SnapshotEffectRender.this.mBlockWidth = i11;
            SnapshotEffectRender.this.mBlockHeight = i12;
            long currentTimeMillis2 = System.currentTimeMillis();
            int[] iArr = {OpenGlUtils.genTexture()};
            int i13 = i3;
            int i14 = i6;
            int i15 = i7;
            byte[] jpegFromMemImage = ShaderNativeUtil.getJpegFromMemImage(watermarkRange[0] + i, watermarkRange[1] + i4, watermarkRange[2], watermarkRange[3], SnapshotEffectRender.this.mQuality);
            int i16 = (((watermarkRange[1] + i4) * i8) + watermarkRange[0] + i) * 3;
            ShaderNativeUtil.updateTextureWidthStride(iArr[0], i11, i12, i8, i16);
            String access$10003 = SnapshotEffectRender.TAG;
            StringBuilder sb3 = new StringBuilder();
            sb3.append("get pixel and upload total time =");
            sb3.append(System.currentTimeMillis() - currentTimeMillis2);
            Log.d(access$10003, sb3.toString());
            Render effectRender = getEffectRender(drawJPEGAttribute2.mEffectIndex);
            if (effectRender == null) {
                Log.w(SnapshotEffectRender.TAG, "init render failed");
                return drawJPEGAttribute2.mData;
            }
            effectRender.setPreviewSize(i14, i15);
            effectRender.setEffectRangeAttribute(drawJPEGAttribute2.mAttribute);
            effectRender.setMirror(drawJPEGAttribute2.mMirror);
            Size size4 = size2;
            effectRender.setSnapshotSize(size4.width, size4.height);
            effectRender.setOrientation(drawJPEGAttribute2.mOrientation);
            effectRender.setShootRotation(drawJPEGAttribute2.mShootRotation);
            effectRender.setJpegOrientation(drawJPEGAttribute2.mJpegOrientation);
            SnapshotEffectRender.this.mGLCanvas.beginBindFrameBuffer(SnapshotEffectRender.this.mGraphicBuffer.getFrameBufferId(), i11, i12);
            long currentTimeMillis3 = System.currentTimeMillis();
            PipeRender pipeRender = (PipeRender) effectRender;
            DrawIntTexAttribute drawIntTexAttribute = new DrawIntTexAttribute(iArr[0], 0, 0, i11, i12, true);
            pipeRender.drawOnExtraFrameBufferOnce(drawIntTexAttribute);
            effectRender.deleteBuffer();
            int i17 = -watermarkRange[0];
            int i18 = -watermarkRange[1];
            int i19 = i16;
            int i20 = i18;
            int i21 = i17;
            Render render = effectRender;
            int i22 = drawJPEGAttribute2.mJpegOrientation;
            byte[] bArr = jpegFromMemImage;
            int i23 = i4;
            drawTimeWaterMark(drawJPEGAttribute, i17, i18, i13, i10, i22, null);
            if (C0122O00000o.instance().OOo0OO()) {
                PictureInfo pictureInfo = drawJPEGAttribute2.mInfo;
                if (pictureInfo != null && pictureInfo.isFrontCamera()) {
                    drawFrontCameraWaterMark(drawJPEGAttribute, i21, i20, i13, i10, drawJPEGAttribute2.mJpegOrientation, null);
                    String access$10004 = SnapshotEffectRender.TAG;
                    StringBuilder sb4 = new StringBuilder();
                    sb4.append("drawTime=");
                    sb4.append(System.currentTimeMillis() - currentTimeMillis3);
                    Log.d(access$10004, sb4.toString());
                    render.deleteBuffer();
                    GLES20.glFinish();
                    if (drawJPEGAttribute2.mRequestModuleIdx != 165) {
                        ShaderNativeUtil.getCenterSquareImage(i, i23);
                        i5 = ((watermarkRange[1] * i13) + watermarkRange[0]) * 3;
                        graphicBuffer = SnapshotEffectRender.this.mGraphicBuffer;
                    } else {
                        graphicBuffer = SnapshotEffectRender.this.mGraphicBuffer;
                        i5 = i19;
                    }
                    graphicBuffer.readBuffer(i11, i12, i5);
                    long currentTimeMillis4 = System.currentTimeMillis();
                    int i24 = i13;
                    byte[] compressPicture = ShaderNativeUtil.compressPicture(i24, i10, SnapshotEffectRender.this.mQuality);
                    String access$10005 = SnapshotEffectRender.TAG;
                    StringBuilder sb5 = new StringBuilder();
                    sb5.append("compress=");
                    sb5.append(System.currentTimeMillis() - currentTimeMillis4);
                    Log.d(access$10005, sb5.toString());
                    if (GLES20.glIsTexture(iArr[0])) {
                        GLES20.glDeleteTextures(1, iArr, 0);
                    }
                    SnapshotEffectRender.this.mGLCanvas.endBindFrameBuffer();
                    SnapshotEffectRender.this.mRenderSurface.makeNothingCurrent();
                    drawJPEGAttribute2.mDataOfTheRegionUnderWatermarks = bArr;
                    drawJPEGAttribute2.mCoordinatesOfTheRegionUnderWatermarks = watermarkRange;
                    return compressPicture;
                }
            }
            drawDoubleShotWaterMark(drawJPEGAttribute, i21, i20, i13, i10, drawJPEGAttribute2.mJpegOrientation, null);
            String access$100042 = SnapshotEffectRender.TAG;
            StringBuilder sb42 = new StringBuilder();
            sb42.append("drawTime=");
            sb42.append(System.currentTimeMillis() - currentTimeMillis3);
            Log.d(access$100042, sb42.toString());
            render.deleteBuffer();
            GLES20.glFinish();
            if (drawJPEGAttribute2.mRequestModuleIdx != 165) {
            }
            graphicBuffer.readBuffer(i11, i12, i5);
            long currentTimeMillis42 = System.currentTimeMillis();
            int i242 = i13;
            byte[] compressPicture2 = ShaderNativeUtil.compressPicture(i242, i10, SnapshotEffectRender.this.mQuality);
            String access$100052 = SnapshotEffectRender.TAG;
            StringBuilder sb52 = new StringBuilder();
            sb52.append("compress=");
            sb52.append(System.currentTimeMillis() - currentTimeMillis42);
            Log.d(access$100052, sb52.toString());
            if (GLES20.glIsTexture(iArr[0])) {
            }
            SnapshotEffectRender.this.mGLCanvas.endBindFrameBuffer();
            SnapshotEffectRender.this.mRenderSurface.makeNothingCurrent();
            drawJPEGAttribute2.mDataOfTheRegionUnderWatermarks = bArr;
            drawJPEGAttribute2.mCoordinatesOfTheRegionUnderWatermarks = watermarkRange;
            return compressPicture2;
        }

        private byte[] applyMiMovieBlackBridge(DrawJPEGAttribute drawJPEGAttribute, Size size) {
            DrawJPEGAttribute drawJPEGAttribute2 = drawJPEGAttribute;
            Size size2 = size;
            int[] decompressPicture = ShaderNativeUtil.decompressPicture(drawJPEGAttribute2.mData, 1);
            int i = drawJPEGAttribute2.mPreviewWidth;
            int i2 = drawJPEGAttribute2.mPreviewHeight;
            int i3 = decompressPicture[0];
            int i4 = decompressPicture[1];
            drawJPEGAttribute2.mWidth = i3;
            drawJPEGAttribute2.mHeight = i4;
            SnapshotEffectRender.this.mRenderSurface.makeCurrent();
            SnapshotEffectRender.this.mGraphicBuffer.reszieBuffer(i3, i4);
            int[] iArr = {OpenGlUtils.genTexture()};
            ShaderNativeUtil.updateTextureWidthStride(iArr[0], i3, i4, i3, 0);
            Render effectRender = getEffectRender(FilterInfo.FILTER_ID_NONE);
            if (effectRender == null) {
                Log.w(SnapshotEffectRender.TAG, "init render failed");
                return null;
            }
            effectRender.setPreviewSize(i, i2);
            effectRender.setEffectRangeAttribute(drawJPEGAttribute2.mAttribute);
            effectRender.setMirror(drawJPEGAttribute2.mMirror);
            effectRender.setSnapshotSize(size2.width, size2.height);
            effectRender.setOrientation(drawJPEGAttribute2.mOrientation);
            effectRender.setShootRotation(drawJPEGAttribute2.mShootRotation);
            effectRender.setJpegOrientation(drawJPEGAttribute2.mJpegOrientation);
            SnapshotEffectRender.this.mGLCanvas.beginBindFrameBuffer(SnapshotEffectRender.this.mGraphicBuffer.getFrameBufferId(), i3, i4);
            PipeRender pipeRender = (PipeRender) effectRender;
            Render render = effectRender;
            int[] iArr2 = iArr;
            DrawIntTexAttribute drawIntTexAttribute = new DrawIntTexAttribute(iArr[0], 0, 0, i3, i4, true);
            pipeRender.drawOnExtraFrameBufferOnce(drawIntTexAttribute);
            Util.drawMiMovieBlackBridgeEGL(SnapshotEffectRender.this.mGLCanvas, size2.width, size2.height);
            render.deleteBuffer();
            GLES20.glFinish();
            SnapshotEffectRender.this.mGraphicBuffer.readBuffer(i3, i4, 0);
            byte[] compressPicture = ShaderNativeUtil.compressPicture(i3, i4, SnapshotEffectRender.this.mQuality);
            if (GLES20.glIsTexture(iArr2[0])) {
                GLES20.glDeleteTextures(1, iArr2, 0);
            }
            SnapshotEffectRender.this.mGLCanvas.endBindFrameBuffer();
            SnapshotEffectRender.this.mRenderSurface.makeNothingCurrent();
            return compressPicture;
        }

        /* JADX WARNING: Removed duplicated region for block: B:28:0x0182  */
        /* JADX WARNING: Removed duplicated region for block: B:29:0x0204  */
        /* JADX WARNING: Removed duplicated region for block: B:33:0x021d  */
        /* JADX WARNING: Removed duplicated region for block: B:49:0x04b7  */
        /* JADX WARNING: Removed duplicated region for block: B:52:0x04cb  */
        /* JADX WARNING: Removed duplicated region for block: B:55:0x04ec  */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        private void blockSplitApplyEffect(DrawJPEGAttribute drawJPEGAttribute, int i, boolean z, Size size, Size size2) {
            int i2;
            int i3;
            int i4;
            int i5;
            int i6;
            int i7;
            int i8;
            RectF rectF;
            int[] iArr;
            int i9;
            RectF rectF2;
            DrawJPEGAttribute drawJPEGAttribute2 = drawJPEGAttribute;
            boolean z2 = z;
            Size size3 = size2;
            String access$1000 = SnapshotEffectRender.TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("applyEffect: applyToThumb = ");
            sb.append(z2);
            Log.d(access$1000, sb.toString());
            byte[] thumbnailBytes = z2 ? drawJPEGAttribute2.mExif.getThumbnailBytes() : drawJPEGAttribute2.mData;
            if (thumbnailBytes == null) {
                String access$10002 = SnapshotEffectRender.TAG;
                StringBuilder sb2 = new StringBuilder();
                sb2.append("Null ");
                sb2.append(z2 ? "thumb!" : "jpeg!");
                Log.w(access$10002, sb2.toString());
                return;
            }
            SnapshotEffectRender.this.mRenderSurface.makeCurrent();
            SnapshotEffectRender.this.mTextureId = OpenGlUtils.genTexture();
            SnapshotEffectRender.this.mMemImage.getPixelsFromJpg(thumbnailBytes);
            int i10 = SnapshotEffectRender.this.mMemImage.mWidth;
            int i11 = SnapshotEffectRender.this.mMemImage.mHeight;
            int i12 = drawJPEGAttribute2.mPreviewWidth;
            int i13 = drawJPEGAttribute2.mPreviewHeight;
            SnapshotEffectRender.this.mRenderCounter.reset("[NewEffectFramework]start");
            CounterUtil counterUtil = new CounterUtil();
            counterUtil.reset("local start");
            List split = SnapshotEffectRender.this.mSplitter.split(SnapshotEffectRender.this.mMemImage.mWidth, SnapshotEffectRender.this.mMemImage.mHeight, SnapshotEffectRender.this.mBlockWidth, SnapshotEffectRender.this.mBlockHeight, SnapshotEffectRender.this.mBlockWidth, SnapshotEffectRender.this.mBlockHeight);
            counterUtil.tick("local start setImageSettings");
            Render effectRender = getEffectRender(drawJPEGAttribute2.mEffectIndex);
            if (effectRender == null) {
                Log.w(SnapshotEffectRender.TAG, "init render failed");
                return;
            }
            effectRender.setPreviewSize(i12, i13);
            effectRender.setEffectRangeAttribute(drawJPEGAttribute2.mAttribute);
            effectRender.setMirror(drawJPEGAttribute2.mMirror);
            if (z2) {
                effectRender.setSnapshotSize(i10, i11);
            } else {
                effectRender.setSnapshotSize(size3.width, size3.height);
            }
            effectRender.setOrientation(drawJPEGAttribute2.mOrientation);
            effectRender.setShootRotation(drawJPEGAttribute2.mShootRotation);
            effectRender.setJpegOrientation(drawJPEGAttribute2.mJpegOrientation);
            counterUtil.tick("local start render");
            SnapshotEffectRender.this.mGLCanvas.beginBindFrameBuffer(SnapshotEffectRender.this.mGraphicBuffer.getFrameBufferId(), SnapshotEffectRender.this.mBlockWidth, SnapshotEffectRender.this.mBlockHeight);
            String str = "local beginBindFrameBuffer";
            counterUtil.tick(str);
            SnapshotEffectRender.this.mGLCanvas.getState().pushState();
            counterUtil.tick(str);
            if (drawJPEGAttribute2.mRequestModuleIdx != 165) {
                i2 = i10;
                i3 = i11;
                i5 = 0;
            } else if (i10 > i11) {
                i4 = ((i10 - i11) / 2) - ((SnapshotEffectRender.this.mSquareModeExtraMargin * i11) / Display.getWindowWidth());
                i3 = i11;
                i2 = i3;
                i5 = 0;
                drawJPEGAttribute2.mWidth = i2;
                drawJPEGAttribute2.mHeight = i3;
                WaterMark waterMark = null;
                if (!drawJPEGAttribute2.mApplyWaterMark) {
                    i8 = i3;
                    i7 = i2;
                    i6 = i13;
                    int[] watermarkRange = Util.getWatermarkRange(drawJPEGAttribute2.mWidth, drawJPEGAttribute2.mHeight, drawJPEGAttribute2.mJpegOrientation, drawJPEGAttribute2.mHasDualWaterMark, drawJPEGAttribute2.mHasFrontWaterMark, drawJPEGAttribute2.mTimeWaterMarkText, drawJPEGAttribute2.mDeviceWatermarkParam);
                    String access$10003 = SnapshotEffectRender.TAG;
                    StringBuilder sb3 = new StringBuilder();
                    sb3.append("blockSplitApplyEffect: rotation = ");
                    sb3.append(drawJPEGAttribute2.mJpegOrientation);
                    sb3.append(", watermarkRange = ");
                    sb3.append(Arrays.toString(watermarkRange));
                    Log.d(access$10003, sb3.toString());
                    RectF rectF3 = new RectF((float) (watermarkRange[0] + i4), (float) (watermarkRange[1] + i5), (float) (watermarkRange[0] + i4 + watermarkRange[2]), (float) (watermarkRange[1] + i5 + watermarkRange[3]));
                    ShaderNativeUtil.genWaterMarkRange(watermarkRange[0] + i4, watermarkRange[1] + i5, watermarkRange[2], watermarkRange[3], 3);
                    iArr = watermarkRange;
                    rectF = rectF3;
                } else {
                    i8 = i3;
                    i7 = i2;
                    i6 = i13;
                    iArr = null;
                    rectF = null;
                }
                RectF rectF4 = new RectF();
                WaterMark waterMark2 = null;
                WaterMark waterMark3 = null;
                i9 = 0;
                while (i9 < split.size()) {
                    SnapshotEffectRender.this.mFrameCounter.reset(String.format("[loop%d/%d]begin", new Object[]{Integer.valueOf(i9), Integer.valueOf(split.size())}));
                    Block block = (Block) split.get(i9);
                    int i14 = block.mWidth;
                    int i15 = block.mHeight;
                    int[] iArr2 = iArr;
                    int i16 = block.mRowStride;
                    RectF rectF5 = rectF;
                    int i17 = block.mOffset;
                    int i18 = i12;
                    int i19 = SnapshotEffectRender.this.mMemImage.mChannels;
                    GLES20.glViewport(0, 0, i14, i15);
                    int i20 = i5;
                    GLES20.glClearColor(1.0f, 0.0f, 0.0f, 1.0f);
                    GLES20.glClear(16640);
                    SnapshotEffectRender.this.mFrameCounter.tick(String.format("[loop%d/%d]gl predraw", new Object[]{Integer.valueOf(i9), Integer.valueOf(split.size())}));
                    int i21 = i17 * i19;
                    SnapshotEffectRender.this.mMemImage.textureWithStride(SnapshotEffectRender.this.mTextureId, i14, i15, i16, i21);
                    SnapshotEffectRender.this.mFrameCounter.tick(String.format("[loop%d/%d]gl uploadtexture textureId %d", new Object[]{Integer.valueOf(i9), Integer.valueOf(split.size()), Integer.valueOf(SnapshotEffectRender.this.mTextureId)}));
                    PipeRender pipeRender = (PipeRender) effectRender;
                    DrawIntTexAttribute drawIntTexAttribute = new DrawIntTexAttribute(SnapshotEffectRender.this.mTextureId, 0, 0, i14, i15, true);
                    pipeRender.drawOnExtraFrameBufferOnce(drawIntTexAttribute);
                    int[] offset = block.getOffset(i10, i11);
                    rectF4.left = (float) offset[0];
                    rectF4.top = (float) offset[1];
                    rectF4.right = (float) (offset[0] + i14);
                    rectF4.bottom = (float) (offset[1] + i15);
                    int i22 = i4 - offset[0];
                    int i23 = i20 - offset[1];
                    SnapshotEffectRender.this.mGLCanvas.getState().pushState();
                    DrawJPEGAttribute drawJPEGAttribute3 = drawJPEGAttribute;
                    int i24 = i21;
                    int i25 = i20;
                    drawJPEGAttribute2 = drawJPEGAttribute3;
                    int i26 = i4;
                    int i27 = i15;
                    int i28 = i8;
                    int i29 = i7;
                    int i30 = i9;
                    int i31 = i10;
                    RectF rectF6 = rectF4;
                    int i32 = i11;
                    int i33 = i14;
                    int[] iArr3 = iArr2;
                    Render render = effectRender;
                    List list = split;
                    drawAgeGenderAndMagicMirrorWater(drawJPEGAttribute3.mWaterInfos, -offset[0], -offset[1], i10, i11, i18, i6, drawJPEGAttribute3.mJpegOrientation, drawJPEGAttribute3.mIsPortraitRawData);
                    if (drawJPEGAttribute2.mApplyWaterMark) {
                        rectF2 = rectF5;
                        if (rectangle_collision(rectF6.left, rectF6.top, rectF6.width(), rectF6.height(), rectF2.left, rectF2.top, rectF2.width(), rectF2.height())) {
                            float[] intersectRect = getIntersectRect(rectF6.left, rectF6.top, rectF6.right, rectF6.bottom, rectF2.left, rectF2.top, rectF2.right, rectF2.bottom);
                            ShaderNativeUtil.mergeWaterMarkRange((int) intersectRect[0], (int) intersectRect[1], (int) (intersectRect[2] - intersectRect[0]), (int) (intersectRect[3] - intersectRect[1]), offset[0], offset[1], 3);
                            waterMark = drawTimeWaterMark(drawJPEGAttribute, i22, i23, i29, i28, drawJPEGAttribute2.mJpegOrientation, waterMark);
                            if (C0122O00000o.instance().OOo0OO()) {
                                PictureInfo pictureInfo = drawJPEGAttribute2.mInfo;
                                if (pictureInfo != null && pictureInfo.isFrontCamera()) {
                                    waterMark3 = drawFrontCameraWaterMark(drawJPEGAttribute, i22, i23, i29, i28, drawJPEGAttribute2.mJpegOrientation, waterMark3);
                                }
                            }
                            waterMark2 = drawDoubleShotWaterMark(drawJPEGAttribute, i22, i23, i29, i28, drawJPEGAttribute2.mJpegOrientation, waterMark2);
                        }
                    } else {
                        rectF2 = rectF5;
                    }
                    SnapshotEffectRender.this.mGLCanvas.getState().popState();
                    SnapshotEffectRender.this.mFrameCounter.tick(String.format("[loop%d/%d]gl gldraw", new Object[]{Integer.valueOf(i30), Integer.valueOf(list.size())}));
                    GLES20.glFinish();
                    SnapshotEffectRender.this.mGraphicBuffer.readBuffer(i33, i27, i24);
                    SnapshotEffectRender.this.mFrameCounter.tick(String.format("[loop%d/%d]gl readPixelAndMerge", new Object[]{Integer.valueOf(i30), Integer.valueOf(list.size())}));
                    i9 = i30 + 1;
                    rectF4 = rectF6;
                    i8 = i28;
                    effectRender = render;
                    i12 = i18;
                    i11 = i32;
                    i10 = i31;
                    i5 = i25;
                    i4 = i26;
                    iArr = iArr3;
                    rectF = rectF2;
                    i7 = i29;
                    split = list;
                }
                int i34 = i5;
                int i35 = i4;
                int[] iArr4 = iArr;
                if (GLES20.glIsTexture(SnapshotEffectRender.this.mTextureId)) {
                    GLES20.glDeleteTextures(1, new int[]{SnapshotEffectRender.this.mTextureId}, 0);
                }
                if (drawJPEGAttribute2.mRequestModuleIdx == 165) {
                    ShaderNativeUtil.getCenterSquareImage(i35, i34);
                }
                SnapshotEffectRender.this.mGLCanvas.getState().popState();
                SnapshotEffectRender.this.mRenderSurface.makeNothingCurrent();
                if (drawJPEGAttribute2.mApplyWaterMark) {
                    drawJPEGAttribute2.mDataOfTheRegionUnderWatermarks = ShaderNativeUtil.getWaterMarkRange(SnapshotEffectRender.this.mQuality, 3);
                    drawJPEGAttribute2.mCoordinatesOfTheRegionUnderWatermarks = iArr4;
                }
            } else {
                i5 = ((i11 - i10) / 2) - ((SnapshotEffectRender.this.mSquareModeExtraMargin * i10) / Display.getWindowWidth());
                i3 = i10;
                i2 = i3;
            }
            i4 = 0;
            drawJPEGAttribute2.mWidth = i2;
            drawJPEGAttribute2.mHeight = i3;
            WaterMark waterMark4 = null;
            if (!drawJPEGAttribute2.mApplyWaterMark) {
            }
            RectF rectF42 = new RectF();
            WaterMark waterMark22 = null;
            WaterMark waterMark32 = null;
            i9 = 0;
            while (i9 < split.size()) {
            }
            int i342 = i5;
            int i352 = i4;
            int[] iArr42 = iArr;
            if (GLES20.glIsTexture(SnapshotEffectRender.this.mTextureId)) {
            }
            if (drawJPEGAttribute2.mRequestModuleIdx == 165) {
            }
            SnapshotEffectRender.this.mGLCanvas.getState().popState();
            SnapshotEffectRender.this.mRenderSurface.makeNothingCurrent();
            if (drawJPEGAttribute2.mApplyWaterMark) {
            }
        }

        private void checkFrameBuffer(int i, int i2) {
            if (SnapshotEffectRender.this.mFrameBuffer == null || SnapshotEffectRender.this.mFrameBuffer.getWidth() < i || SnapshotEffectRender.this.mFrameBuffer.getHeight() < i2) {
                SnapshotEffectRender.this.mFrameBuffer = null;
                SnapshotEffectRender snapshotEffectRender = SnapshotEffectRender.this;
                snapshotEffectRender.mFrameBuffer = new FrameBuffer(snapshotEffectRender.mGLCanvas, i, i2, 0);
            }
        }

        private void drawAgeGenderAndMagicMirrorWater(List list, int i, int i2, int i3, int i4, int i5, int i6, int i7, boolean z) {
            if (C0124O00000oO.OOoo0oo() && !z && CameraSettings.isAgeGenderAndMagicMirrorWaterOpen()) {
                List list2 = list;
                WaterMarkBitmap waterMarkBitmap = new WaterMarkBitmap(list);
                WaterMarkData waterMarkData = waterMarkBitmap.getWaterMarkData();
                if (waterMarkData != null) {
                    AgeGenderAndMagicMirrorWaterMark ageGenderAndMagicMirrorWaterMark = new AgeGenderAndMagicMirrorWaterMark(waterMarkData.getImage(), i3, i4, i7, i5, i6, 0.0f, 0.0f);
                    int i8 = i;
                    int i9 = i2;
                    drawWaterMark(ageGenderAndMagicMirrorWaterMark, i, i2, i7 - waterMarkData.getOrientation());
                }
                waterMarkBitmap.releaseBitmap();
                Log.d(WaterMarkBitmap.class.getSimpleName(), "Draw age_gender_and_magic_mirror water mark");
            }
        }

        /* JADX WARNING: Code restructure failed: missing block: B:25:0x007c, code lost:
            if (com.android.camera.effect.renders.SnapshotEffectRender.access$1900(r0.this$0) == null) goto L_0x0068;
         */
        /* JADX WARNING: Removed duplicated region for block: B:32:0x0092 A[RETURN] */
        /* JADX WARNING: Removed duplicated region for block: B:33:0x0093  */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        private WaterMark drawDoubleShotWaterMark(DrawJPEGAttribute drawJPEGAttribute, int i, int i2, int i3, int i4, int i5, WaterMark waterMark) {
            Bitmap bitmap;
            Bitmap bitmap2;
            DrawJPEGAttribute drawJPEGAttribute2 = drawJPEGAttribute;
            int i6 = i;
            int i7 = i2;
            int i8 = i5;
            WaterMark waterMark2 = waterMark;
            if (!C0124O00000oO.OOoo0oo()) {
                return null;
            }
            if (waterMark2 != null) {
                drawWaterMark(waterMark2, i6, i7, i8);
                return waterMark2;
            } else if (!drawJPEGAttribute2.mDeviceWaterMarkEnabled) {
                return null;
            } else {
                if (drawJPEGAttribute2.isMiMovieOpen) {
                    int i9 = drawJPEGAttribute2.mJpegOrientation;
                    if (i9 == 90 || i9 == 270) {
                        if (SnapshotEffectRender.this.mCinematicRatioWaterMarkBitmap == null) {
                            SnapshotEffectRender snapshotEffectRender = SnapshotEffectRender.this;
                            snapshotEffectRender.mCinematicRatioWaterMarkBitmap = snapshotEffectRender.loadCinematicRatioWatermark(snapshotEffectRender.mContext);
                        }
                        if (SnapshotEffectRender.this.mCinematicRatioWaterMarkBitmap != null) {
                            bitmap2 = SnapshotEffectRender.this.mCinematicRatioWaterMarkBitmap;
                            bitmap = bitmap2;
                            if (bitmap != null) {
                                return null;
                            }
                            ImageWaterMark imageWaterMark = new ImageWaterMark(bitmap, i3, i4, i5, SnapshotEffectRender.this.mDualCameraWaterMarkSizeRatio, SnapshotEffectRender.this.mDualCameraWaterMarkPaddingXRatio, SnapshotEffectRender.this.mDualCameraWaterMarkPaddingYRatio, drawJPEGAttribute2.isMiMovieOpen);
                            drawWaterMark(imageWaterMark, i6, i7, i8);
                            return imageWaterMark;
                        }
                        bitmap = null;
                        if (bitmap != null) {
                        }
                    }
                }
                if (!SnapshotEffectRender.this.mCurrentCustomWaterMarkText.equals(CameraSettings.getCustomWatermark())) {
                    SnapshotEffectRender.this.mCurrentCustomWaterMarkText = CameraSettings.getCustomWatermark();
                }
                SnapshotEffectRender snapshotEffectRender2 = SnapshotEffectRender.this;
                snapshotEffectRender2.mDualCameraWaterMarkBitmap = snapshotEffectRender2.loadCameraWatermark(snapshotEffectRender2.mContext);
                if (SnapshotEffectRender.this.mDualCameraWaterMarkBitmap != null) {
                    bitmap2 = SnapshotEffectRender.this.mDualCameraWaterMarkBitmap;
                    bitmap = bitmap2;
                    if (bitmap != null) {
                    }
                }
                bitmap = null;
                if (bitmap != null) {
                }
            }
        }

        private WaterMark drawFrontCameraWaterMark(DrawJPEGAttribute drawJPEGAttribute, int i, int i2, int i3, int i4, int i5, WaterMark waterMark) {
            if (!C0124O00000oO.OOoo0oo()) {
                return null;
            }
            if (waterMark != null) {
                drawWaterMark(waterMark, i, i2, i5);
                return waterMark;
            } else if (!drawJPEGAttribute.mDeviceWaterMarkEnabled || SnapshotEffectRender.this.mFrontCameraWaterMarkBitmap == null) {
                return null;
            } else {
                MimojiImageWaterMark mimojiImageWaterMark = new MimojiImageWaterMark(SnapshotEffectRender.this.mFrontCameraWaterMarkBitmap, i3, i4, i5, SnapshotEffectRender.this.mFrontCameraWaterMarkSizeRatio, SnapshotEffectRender.this.mFrontCameraWaterMarkPaddingXRatio, SnapshotEffectRender.this.mFrontCameraWaterMarkPaddingYRatio);
                drawWaterMark(mimojiImageWaterMark, i, i2, i5);
                return mimojiImageWaterMark;
            }
        }

        private boolean drawMainJpeg(DrawJPEGAttribute drawJPEGAttribute, boolean z, boolean z2) {
            DrawJPEGAttribute drawJPEGAttribute2 = drawJPEGAttribute;
            boolean z3 = z;
            boolean z4 = z2;
            Size size = new Size(drawJPEGAttribute2.mWidth, drawJPEGAttribute2.mHeight);
            int i = 1;
            while (true) {
                int i2 = drawJPEGAttribute2.mWidth;
                int i3 = BaseGLCanvas.sMaxTextureSize;
                if (i2 <= i3 && drawJPEGAttribute2.mHeight <= i3) {
                    break;
                }
                drawJPEGAttribute2.mWidth /= 2;
                drawJPEGAttribute2.mHeight /= 2;
                i *= 2;
            }
            Log.d(SnapshotEffectRender.TAG, String.format(Locale.US, "downScale: %d width: %d %d parallel: %b", new Object[]{Integer.valueOf(i), Integer.valueOf(drawJPEGAttribute2.mWidth), Integer.valueOf(drawJPEGAttribute2.mPreviewWidth), Boolean.valueOf(z2)}));
            boolean z5 = drawJPEGAttribute2.isMiMovieOpen && !z4;
            if (drawJPEGAttribute2.mEffectIndex == FilterInfo.FILTER_ID_NONE && !CameraSettings.isAgeGenderAndMagicMirrorWaterOpen() && !CameraSettings.isTiltShiftOn() && !z5) {
                return saveMainJpeg(drawJPEGAttribute2, z3, z4, applyEffectOnlyWatermarkRange(drawJPEGAttribute2, null, size));
            }
            if (CameraSettings.isTiltShiftOn()) {
                saveMainJpeg(drawJPEGAttribute2, false, z4, applyEffect(drawJPEGAttribute, i, false, null, size));
            } else {
                blockSplitApplyEffect(drawJPEGAttribute, i, false, null, size);
                SnapshotEffectRender.this.mRenderCounter.tick("[NewEffectFramework] done");
                byte[] encodeJpeg = SnapshotEffectRender.this.mMemImage.encodeJpeg(SnapshotEffectRender.this.mQuality, drawJPEGAttribute2.mWidth, drawJPEGAttribute2.mHeight);
                if (z5) {
                    DrawJPEGAttribute drawJPEGAttribute3 = new DrawJPEGAttribute(encodeJpeg, drawJPEGAttribute2.mNeedThumbnail, drawJPEGAttribute2.mPreviewWidth, drawJPEGAttribute2.mPreviewHeight, drawJPEGAttribute2.mWidth, drawJPEGAttribute2.mHeight, drawJPEGAttribute2.mEffectIndex, drawJPEGAttribute2.mAttribute, drawJPEGAttribute2.mLoc, drawJPEGAttribute2.mTitle, drawJPEGAttribute2.mDate, drawJPEGAttribute2.mOrientation, drawJPEGAttribute2.mJpegOrientation, drawJPEGAttribute2.mShootRotation, drawJPEGAttribute2.mMirror, drawJPEGAttribute2.mAlgorithmName, drawJPEGAttribute2.mApplyWaterMark, drawJPEGAttribute2.mInfo, drawJPEGAttribute2.mWaterInfos, drawJPEGAttribute2.mDeviceWaterMarkEnabled, drawJPEGAttribute2.mIsUltraPixelWatermarkEnabled, drawJPEGAttribute2.mTimeWaterMarkText, drawJPEGAttribute2.mHasDualWaterMark, drawJPEGAttribute2.mHasFrontWaterMark, drawJPEGAttribute2.mDeviceWatermarkParam, drawJPEGAttribute2.mIsPortraitRawData, drawJPEGAttribute2.mRequestModuleIdx, drawJPEGAttribute2.mPreviewThumbnailHash, drawJPEGAttribute2.isMiMovieOpen);
                    saveMainJpeg(drawJPEGAttribute2, z3, z4, applyMiMovieBlackBridge(drawJPEGAttribute3, size));
                } else {
                    saveMainJpeg(drawJPEGAttribute2, false, z4, encodeJpeg);
                }
            }
            SnapshotEffectRender.this.mTotalCounter.tick("TOTAL finish");
            return true;
        }

        private boolean drawThumbJpeg(DrawJPEGAttribute drawJPEGAttribute, boolean z) {
            DrawJPEGAttribute drawJPEGAttribute2 = drawJPEGAttribute;
            if (drawJPEGAttribute2.mExif == null) {
                drawJPEGAttribute2.mExif = SnapshotEffectRender.this.getExif(drawJPEGAttribute2);
                if (!TextUtils.isEmpty(drawJPEGAttribute2.mAlgorithmName)) {
                    drawJPEGAttribute2.mExif.addAlgorithmComment(drawJPEGAttribute2.mAlgorithmName);
                }
            }
            Size size = new Size();
            byte[] applyEffect = applyEffect(drawJPEGAttribute, 1, true, size, null);
            String access$1000 = SnapshotEffectRender.TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("drawThumbJpeg: thumbLen=");
            sb.append(applyEffect == null ? "null" : Integer.valueOf(applyEffect.length));
            Log.d(access$1000, sb.toString());
            if (applyEffect != null) {
                drawJPEGAttribute2.mExif.setCompressedThumbnail(applyEffect);
            }
            boolean z2 = drawJPEGAttribute2.mJpegOrientation != 0;
            if (z && drawJPEGAttribute2.mExif.getThumbnailBytes() != null) {
                drawJPEGAttribute2.mUri = Storage.addImage(SnapshotEffectRender.this.mContext, drawJPEGAttribute2.mTitle, drawJPEGAttribute2.mDate, drawJPEGAttribute2.mLoc, drawJPEGAttribute2.mJpegOrientation, drawJPEGAttribute2.mExif.getThumbnailBytes(), false, size.width, size.height, false, false, false, z2, false, drawJPEGAttribute2.mAlgorithmName, null);
                ImageSaverCallback imageSaverCallback = (ImageSaverCallback) SnapshotEffectRender.this.mSaverCallback.get();
                Uri uri = drawJPEGAttribute2.mUri;
                if (!(uri == null || imageSaverCallback == null)) {
                    imageSaverCallback.onNewUriArrived(uri, drawJPEGAttribute2.mTitle);
                }
            }
            return true;
        }

        /* JADX WARNING: type inference failed for: r15v3, types: [com.android.camera.effect.renders.TextWaterMark] */
        /* JADX WARNING: Multi-variable type inference failed */
        /* JADX WARNING: Unknown variable types count: 1 */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        private WaterMark drawTimeWaterMark(DrawJPEGAttribute drawJPEGAttribute, int i, int i2, int i3, int i4, int i5, WaterMark waterMark) {
            NewStyleTextWaterMark newStyleTextWaterMark;
            if (!C0124O00000oO.OOoo0oo()) {
                return null;
            }
            if (waterMark != null) {
                drawWaterMark(waterMark, i, i2, i5);
                return waterMark;
            }
            String str = drawJPEGAttribute.mTimeWaterMarkText;
            if (str == null || waterMark != null) {
                return null;
            }
            if (C0124O00000oO.Oo00o0o()) {
                NewStyleTextWaterMark newStyleTextWaterMark2 = new NewStyleTextWaterMark(str, i3, i4, i5, drawJPEGAttribute.isMiMovieOpen);
                newStyleTextWaterMark = newStyleTextWaterMark2;
            } else {
                newStyleTextWaterMark = new TextWaterMark(str, i3, i4, i5);
            }
            drawWaterMark(newStyleTextWaterMark, i, i2, i5);
            return newStyleTextWaterMark;
        }

        private void drawWaterMark(WaterMark waterMark, int i, int i2, int i3) {
            SnapshotEffectRender.this.mGLCanvas.getState().pushState();
            if (i3 != 0) {
                SnapshotEffectRender.this.mGLCanvas.getState().translate((float) (waterMark.getCenterX() + i), (float) (waterMark.getCenterY() + i2));
                SnapshotEffectRender.this.mGLCanvas.getState().rotate((float) (-i3), 0.0f, 0.0f, 1.0f);
                SnapshotEffectRender.this.mGLCanvas.getState().translate((float) ((-i) - waterMark.getCenterX()), (float) ((-i2) - waterMark.getCenterY()));
            }
            int left = i + waterMark.getLeft();
            int top = i2 + waterMark.getTop();
            BasicRender basicRender = SnapshotEffectRender.this.mGLCanvas.getBasicRender();
            DrawBasicTexAttribute drawBasicTexAttribute = new DrawBasicTexAttribute(waterMark.getTexture(), left, top, waterMark.getWidth(), waterMark.getHeight());
            basicRender.draw(drawBasicTexAttribute);
            SnapshotEffectRender.this.mGLCanvas.getState().popState();
        }

        private Render fetchRender(int i) {
            RenderGroup effectRenderGroup = SnapshotEffectRender.this.mGLCanvas.getEffectRenderGroup();
            Render render = effectRenderGroup.getRender(i);
            if (render != null) {
                return render;
            }
            SnapshotEffectRender.this.mGLCanvas.prepareEffectRenders(false, i);
            return effectRenderGroup.getRender(i);
        }

        /* JADX WARNING: Removed duplicated region for block: B:15:0x0049  */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        private Render getEffectRender(int i) {
            int i2;
            PipeRender pipeRender = new PipeRender(SnapshotEffectRender.this.mGLCanvas);
            if (i != FilterInfo.FILTER_ID_NONE) {
                Render fetchRender = fetchRender(i);
                if (fetchRender != null) {
                    pipeRender.addRender(fetchRender);
                }
            }
            if (CameraSettings.isTiltShiftOn()) {
                Render render = null;
                String componentValue = DataRepository.dataItemRunning().getComponentRunningTiltValue().getComponentValue(160);
                if (ComponentRunningTiltValue.TILT_CIRCLE.equals(componentValue)) {
                    i2 = FilterInfo.FILTER_ID_GAUSSIAN;
                } else {
                    if (ComponentRunningTiltValue.TILT_PARALLEL.equals(componentValue)) {
                        i2 = FilterInfo.FILTER_ID_TILTSHIFT;
                    }
                    if (render != null) {
                        pipeRender.addRender(render);
                    }
                }
                render = fetchRender(i2);
                if (render != null) {
                }
            }
            if (pipeRender.getRenderSize() == 0) {
                pipeRender.addRender(fetchRender(i));
            }
            return pipeRender;
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

        private void initEGL(EGLContext eGLContext, boolean z) {
            if (SnapshotEffectRender.this.mEglCore == null) {
                SnapshotEffectRender.this.mEglCore = new EglCore(eGLContext, 2);
            }
            if (z && SnapshotEffectRender.this.mRenderSurface != null) {
                SnapshotEffectRender.this.mRenderSurface.release();
                SnapshotEffectRender.this.mRenderSurface = null;
            }
            if (SnapshotEffectRender.this.mRenderSurface == null) {
                SnapshotEffectRender snapshotEffectRender = SnapshotEffectRender.this;
                snapshotEffectRender.mRenderSurface = new PbufferSurface(snapshotEffectRender.mEglCore, 1, 1);
            }
            SnapshotEffectRender.this.mRenderSurface.makeCurrent();
        }

        private boolean rectangle_collision(float f, float f2, float f3, float f4, float f5, float f6, float f7, float f8) {
            return f <= f7 + f5 && f + f3 >= f5 && f2 <= f8 + f6 && f2 + f4 >= f6;
        }

        private void release() {
            if (SnapshotEffectRender.this.mFrameBuffer != null) {
                SnapshotEffectRender.this.mFrameBuffer.release();
            }
            SnapshotEffectRender.this.mFrameBuffer = null;
            SnapshotEffectRender.this.mGLCanvas.recycledResources();
            SnapshotEffectRender.this.mGLCanvas = null;
            SnapshotEffectRender.this.destroy();
        }

        private boolean saveMainJpeg(DrawJPEGAttribute drawJPEGAttribute, boolean z, boolean z2, byte[] bArr) {
            String str;
            DrawJPEGAttribute drawJPEGAttribute2 = drawJPEGAttribute;
            byte[] bArr2 = bArr;
            String access$1000 = SnapshotEffectRender.TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("mainLen=");
            sb.append(bArr2 == null ? "null" : Integer.valueOf(bArr2.length));
            Log.d(access$1000, sb.toString());
            if (bArr2 != null) {
                drawJPEGAttribute2.mData = bArr2;
            }
            if (z) {
                synchronized (SnapshotEffectRender.this) {
                    str = (String) SnapshotEffectRender.this.mTitleMap.get(drawJPEGAttribute2.mTitle);
                    SnapshotEffectRender.this.mTitleMap.remove(drawJPEGAttribute2.mTitle);
                }
                String str2 = null;
                if (SnapshotEffectRender.this.mImageSaver != null) {
                    ImageSaver access$3700 = SnapshotEffectRender.this.mImageSaver;
                    byte[] bArr3 = drawJPEGAttribute2.mData;
                    boolean z3 = drawJPEGAttribute2.mNeedThumbnail;
                    String str3 = str == null ? drawJPEGAttribute2.mTitle : str;
                    if (str != null) {
                        str2 = drawJPEGAttribute2.mTitle;
                    }
                    access$3700.addImage(bArr3, z3, str3, str2, drawJPEGAttribute2.mDate, drawJPEGAttribute2.mUri, drawJPEGAttribute2.mLoc, drawJPEGAttribute2.mWidth, drawJPEGAttribute2.mHeight, drawJPEGAttribute2.mExif, drawJPEGAttribute2.mJpegOrientation, false, false, str == null ? drawJPEGAttribute2.mFinalImage : false, false, z2, drawJPEGAttribute2.mAlgorithmName, drawJPEGAttribute2.mInfo, drawJPEGAttribute2.mPreviewThumbnailHash, null);
                } else if (drawJPEGAttribute2.mUri == null) {
                    Log.d(SnapshotEffectRender.TAG, "addImageForEffect");
                    Context access$1600 = SnapshotEffectRender.this.mContext;
                    if (str == null) {
                        str = drawJPEGAttribute2.mTitle;
                    }
                    Storage.addImageForEffect(access$1600, str, drawJPEGAttribute2.mDate, drawJPEGAttribute2.mLoc, drawJPEGAttribute2.mJpegOrientation, drawJPEGAttribute2.mData, drawJPEGAttribute2.mWidth, drawJPEGAttribute2.mHeight, false, z2, drawJPEGAttribute2.mAlgorithmName, drawJPEGAttribute2.mInfo);
                } else {
                    String access$10002 = SnapshotEffectRender.TAG;
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append("updateImage: uri=");
                    sb2.append(drawJPEGAttribute2.mUri);
                    Log.d(access$10002, sb2.toString());
                    Context access$16002 = SnapshotEffectRender.this.mContext;
                    byte[] bArr4 = drawJPEGAttribute2.mData;
                    ExifInterface exifInterface = drawJPEGAttribute2.mExif;
                    Uri uri = drawJPEGAttribute2.mUri;
                    String str4 = str == null ? drawJPEGAttribute2.mTitle : str;
                    Location location = drawJPEGAttribute2.mLoc;
                    int i = drawJPEGAttribute2.mJpegOrientation;
                    int i2 = drawJPEGAttribute2.mWidth;
                    int i3 = drawJPEGAttribute2.mHeight;
                    if (str != null) {
                        str2 = drawJPEGAttribute2.mTitle;
                    }
                    Storage.updateImage(access$16002, bArr4, false, exifInterface, uri, str4, location, i, i2, i3, str2);
                }
            } else if (drawJPEGAttribute2.mExif != null) {
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                try {
                    drawJPEGAttribute2.mExif.writeExif(drawJPEGAttribute2.mData, (OutputStream) byteArrayOutputStream);
                    byte[] byteArray = byteArrayOutputStream.toByteArray();
                    if (byteArray != null) {
                        drawJPEGAttribute2.mData = byteArray;
                    }
                    byteArrayOutputStream.close();
                } catch (IOException e) {
                    Log.e(SnapshotEffectRender.TAG, e.getMessage(), (Throwable) e);
                }
            }
            return true;
        }

        /* JADX WARNING: type inference failed for: r2v0 */
        /* JADX WARNING: type inference failed for: r2v1, types: [boolean] */
        /* JADX WARNING: type inference failed for: r2v2 */
        /* JADX WARNING: type inference failed for: r2v3, types: [int] */
        /* JADX WARNING: type inference failed for: r2v4, types: [int] */
        /* JADX WARNING: type inference failed for: r2v5 */
        /* JADX WARNING: Multi-variable type inference failed. Error: jadx.core.utils.exceptions.JadxRuntimeException: No candidate types for var: r2v2
  assigns: [?[int, float, boolean, short, byte, char, OBJECT, ARRAY], int]
  uses: [boolean, int]
  mth insns count: 188
        	at jadx.core.dex.visitors.typeinference.TypeSearch.fillTypeCandidates(TypeSearch.java:237)
        	at java.util.ArrayList.forEach(ArrayList.java:1259)
        	at jadx.core.dex.visitors.typeinference.TypeSearch.run(TypeSearch.java:53)
        	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.runMultiVariableSearch(TypeInferenceVisitor.java:99)
        	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.visit(TypeInferenceVisitor.java:92)
        	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:27)
        	at jadx.core.dex.visitors.DepthTraversal.lambda$visit$1(DepthTraversal.java:14)
        	at java.util.ArrayList.forEach(ArrayList.java:1259)
        	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
        	at jadx.core.dex.visitors.DepthTraversal.lambda$visit$0(DepthTraversal.java:13)
        	at java.util.ArrayList.forEach(ArrayList.java:1259)
        	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:13)
        	at jadx.core.ProcessClass.process(ProcessClass.java:30)
        	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:311)
        	at jadx.api.JavaClass.decompile(JavaClass.java:62)
        	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:217)
         */
        /* JADX WARNING: Unknown variable types count: 2 */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void handleMessage(Message message) {
            ? r2;
            if (SnapshotEffectRender.this.mSaverCallback.get() != null) {
                ? r22 = 0;
                switch (message.what) {
                    case 0:
                        initEGL(null, false);
                        SnapshotEffectRender.this.mGLCanvas = new SnapshotCanvas();
                        SnapshotEffectRender.this.mGraphicBuffer = new GraphicBuffer();
                        ImageSaverCallback imageSaverCallback = (ImageSaverCallback) SnapshotEffectRender.this.mSaverCallback.get();
                        if (imageSaverCallback != null) {
                            SnapshotEffectRender.this.mGLCanvas.setSize(imageSaverCallback.getCameraScreenNail().getWidth(), imageSaverCallback.getCameraScreenNail().getHeight());
                            break;
                        }
                        break;
                    case 1:
                        DrawJPEGAttribute drawJPEGAttribute = (DrawJPEGAttribute) message.obj;
                        if (message.arg1 > 0) {
                            r2 = 1;
                        }
                        drawMainJpeg(drawJPEGAttribute, true, r2);
                        SnapshotEffectRender.this.mGLCanvas.recycledResources();
                        if (SnapshotEffectRender.this.mReleasePending && !hasMessages(1)) {
                            release();
                        }
                        synchronized (SnapshotEffectRender.this.mLock) {
                            SnapshotEffectRender.this.mJpegQueueSize = SnapshotEffectRender.this.mJpegQueueSize - 1;
                        }
                        break;
                    case 2:
                        DrawJPEGAttribute drawJPEGAttribute2 = (DrawJPEGAttribute) message.obj;
                        boolean z = message.arg1 > 0;
                        boolean z2 = message.arg2 > 0;
                        int access$700 = SnapshotEffectRender.this.mBlockWidth;
                        int access$800 = SnapshotEffectRender.this.mBlockHeight;
                        int access$900 = SnapshotEffectRender.this.calEachBlockHeight(drawJPEGAttribute2.mWidth, drawJPEGAttribute2.mHeight);
                        int i = drawJPEGAttribute2.mWidth;
                        if (i != 0 && drawJPEGAttribute2.mHeight != 0) {
                            SnapshotEffectRender.this.mBlockWidth = i;
                            SnapshotEffectRender.this.mBlockHeight = drawJPEGAttribute2.mHeight / access$900;
                            SnapshotEffectRender.this.mRenderSurface.makeCurrent();
                            IntBuffer allocate = IntBuffer.allocate(2);
                            GLES20.glGetIntegerv(3379, allocate);
                            SnapshotEffectRender.this.mRenderSurface.makeNothingCurrent();
                            int i2 = drawJPEGAttribute2.mWidth;
                            while (SnapshotEffectRender.this.mBlockWidth > allocate.get(0)) {
                                SnapshotEffectRender.this.mBlockWidth = i2 / 2;
                            }
                            if (!(access$700 == SnapshotEffectRender.this.mBlockWidth && access$800 == SnapshotEffectRender.this.mBlockHeight) && SnapshotEffectRender.this.mInitGraphicBuffer) {
                                SnapshotEffectRender.this.mRenderSurface.makeCurrent();
                                SnapshotEffectRender.this.mGraphicBuffer.reszieBuffer(SnapshotEffectRender.this.mBlockWidth, SnapshotEffectRender.this.mBlockHeight);
                                SnapshotEffectRender.this.mRenderSurface.makeNothingCurrent();
                            } else {
                                SnapshotEffectRender.this.mRenderSurface.makeCurrent();
                                SnapshotEffectRender.this.mGraphicBuffer.initBuffer(SnapshotEffectRender.this.mBlockWidth, SnapshotEffectRender.this.mBlockHeight);
                                SnapshotEffectRender.this.mRenderSurface.makeNothingCurrent();
                                SnapshotEffectRender.this.mInitGraphicBuffer = true;
                            }
                            if (z && !z2) {
                                drawThumbJpeg(drawJPEGAttribute2, false);
                            }
                            SnapshotEffectRender.this.mTotalCounter.reset("TOTAL");
                            drawMainJpeg(drawJPEGAttribute2, false, z2);
                            SnapshotEffectRender.this.mRenderSurface.makeCurrent();
                            SnapshotEffectRender.this.mGLCanvas.recycledResources();
                            SnapshotEffectRender.this.mGraphicBuffer.release();
                            SnapshotEffectRender.this.mRenderSurface.makeNothingCurrent();
                            SnapshotEffectRender.this.mTotalCounter.tick("TOTAL");
                            break;
                        } else {
                            Object[] objArr = new Object[3];
                            objArr[0] = Integer.valueOf(drawJPEGAttribute2.mWidth);
                            objArr[1] = Integer.valueOf(drawJPEGAttribute2.mHeight);
                            byte[] bArr = drawJPEGAttribute2.mData;
                            if (bArr != null) {
                                r22 = bArr.length;
                            }
                            objArr[2] = Integer.valueOf(r22);
                            Log.e(SnapshotEffectRender.TAG, String.format("jpeg data is broken width %d height %d length %d", objArr));
                            break;
                        }
                        break;
                    case 3:
                        drawThumbJpeg((DrawJPEGAttribute) message.obj, true);
                        break;
                    case 4:
                        drawThumbJpeg((DrawJPEGAttribute) message.obj, true);
                        break;
                    case 5:
                        release();
                        break;
                    case 6:
                        SnapshotEffectRender.this.mGLCanvas.prepareEffectRenders(false, message.arg1);
                        break;
                }
                SnapshotEffectRender.this.mEglThreadBlockVar.open();
            }
        }

        public void sendMessageSync(int i) {
            SnapshotEffectRender.this.mEglThreadBlockVar.close();
            sendEmptyMessage(i);
            SnapshotEffectRender.this.mEglThreadBlockVar.block();
        }
    }

    class Size {
        public int height;
        public int width;

        public Size() {
        }

        Size(int i, int i2) {
            this.width = i;
            this.height = i2;
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

    public SnapshotEffectRender(ImageSaverCallback imageSaverCallback, boolean z) {
        Log.d(TAG, "SnapshotEffectRender: has been created!!!");
        this.mSaverCallback = new WeakReference(imageSaverCallback);
        this.mContext = CameraAppImpl.getAndroidContext();
        this.mIsImageCaptureIntent = z;
        if (this.mMemImage == null) {
            this.mMemImage = new MemImage();
        }
        this.mFrameCounter = new CounterUtil();
        this.mTotalCounter = new CounterUtil();
        this.mRenderCounter = new CounterUtil();
        this.mEglThread = new HandlerThread("SnapshotEffectProcessor");
        this.mEglThread.start();
        this.mSplitter = new Splitter();
        this.mBlockWidth = DEFAULT_BLOCK_WIDTH;
        this.mBlockHeight = 1500;
        this.mEglHandler = new EGLHandler(this.mEglThread.getLooper());
        this.mEglHandler.sendMessageSync(0);
        this.mRelease = false;
        this.mInitGraphicBuffer = false;
        if (CameraSettings.isSupportedDualCameraWaterMark()) {
            this.mDualCameraWaterMarkBitmap = loadCameraWatermark(this.mContext);
            this.mCurrentCustomWaterMarkText = CameraSettings.getCustomWatermark();
            this.mDualCameraWaterMarkSizeRatio = getResourceFloat(R.dimen.dualcamera_watermark_size_ratio, 0.0f);
            this.mDualCameraWaterMarkPaddingXRatio = getResourceFloat(R.dimen.dualcamera_watermark_padding_x_ratio, 0.0f);
            this.mDualCameraWaterMarkPaddingYRatio = getResourceFloat(R.dimen.dualcamera_watermark_padding_y_ratio, 0.0f);
        }
        if (C0122O00000o.instance().OOo0OO()) {
            this.mFrontCameraWaterMarkBitmap = loadFrontCameraWatermark(this.mContext);
            this.mFrontCameraWaterMarkSizeRatio = getResourceFloat(R.dimen.frontcamera_watermark_size_ratio, 0.0f);
            this.mFrontCameraWaterMarkPaddingXRatio = getResourceFloat(R.dimen.frontcamera_watermark_padding_x_ratio, 0.0f);
            this.mFrontCameraWaterMarkPaddingYRatio = getResourceFloat(R.dimen.frontcamera_watermark_padding_y_ratio, 0.0f);
        }
        this.mSquareModeExtraMargin = this.mContext.getResources().getDimensionPixelSize(R.dimen.square_mode_bottom_cover_extra_margin);
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
        this.mImageSaver = null;
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
        Log.d(TAG, "SnapshotEffectRender: has been released!!!");
    }

    /* access modifiers changed from: private */
    public ExifInterface getExif(DrawJPEGAttribute drawJPEGAttribute) {
        ExifInterface exifInterface = new ExifInterface();
        try {
            exifInterface.readExif(drawJPEGAttribute.mData);
            if (drawJPEGAttribute.mInfo != null) {
                exifInterface.addXiaomiComment(drawJPEGAttribute.mInfo.toString());
            }
        } catch (IOException e) {
            Log.d(TAG, e.getMessage());
        }
        return exifInterface;
    }

    /* access modifiers changed from: private */
    /* JADX WARNING: Code restructure failed: missing block: B:21:0x0053, code lost:
        r0 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:23:?, code lost:
        $closeResource(r2, r1);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:24:0x0057, code lost:
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
            return Util.generateMainWatermark2File();
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

    private void processorThumAsync(DrawJPEGAttribute drawJPEGAttribute) {
        if (this.mExifNeeded) {
            this.mEglHandler.obtainMessage(3, drawJPEGAttribute).sendToTarget();
        } else {
            drawJPEGAttribute.mUri = Storage.newImage(this.mContext, drawJPEGAttribute.mTitle, drawJPEGAttribute.mDate, drawJPEGAttribute.mJpegOrientation, drawJPEGAttribute.mPreviewWidth, drawJPEGAttribute.mPreviewHeight, false);
        }
    }

    private boolean processorThumSync(DrawJPEGAttribute drawJPEGAttribute) {
        if (this.mExifNeeded) {
            drawJPEGAttribute.mExif = getExif(drawJPEGAttribute);
            if (!TextUtils.isEmpty(drawJPEGAttribute.mAlgorithmName)) {
                drawJPEGAttribute.mExif.addAlgorithmComment(drawJPEGAttribute.mAlgorithmName);
            }
            if (drawJPEGAttribute.mExif.getThumbnailBytes() != null) {
                this.mEglThreadBlockVar.close();
                this.mEglHandler.obtainMessage(4, drawJPEGAttribute).sendToTarget();
                this.mEglThreadBlockVar.block();
                return true;
            }
        }
        drawJPEGAttribute.mUri = Storage.newImage(this.mContext, drawJPEGAttribute.mTitle, drawJPEGAttribute.mDate, drawJPEGAttribute.mJpegOrientation, drawJPEGAttribute.mPreviewWidth, drawJPEGAttribute.mPreviewHeight, false);
        return false;
    }

    public void changeJpegTitle(String str, String str2) {
        if (str2 != null && str2.length() != 0) {
            synchronized (this) {
                this.mTitleMap.put(str2, str);
            }
        }
    }

    public float getResourceFloat(int i, float f) {
        TypedValue typedValue = new TypedValue();
        try {
            this.mContext.getResources().getValue(i, typedValue, true);
            return typedValue.getFloat();
        } catch (Exception unused) {
            String str = TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("Missing resource ");
            sb.append(Integer.toHexString(i));
            Log.e(str, sb.toString());
            return f;
        }
    }

    public boolean isRelease() {
        return this.mReleasePending || this.mRelease;
    }

    public void prepareEffectRender(int i) {
        this.mEglHandler.obtainMessage(6, i, 0).sendToTarget();
    }

    public boolean processorJpegAsync(DrawJPEGAttribute drawJPEGAttribute, boolean z) {
        boolean z2;
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("queueSize=");
        sb.append(this.mJpegQueueSize);
        Log.d(str, sb.toString());
        if (z || this.mJpegQueueSize < 7) {
            if (!z) {
                boolean z3 = this.mJpegQueueSize == 0;
                if (z3) {
                    z2 = processorThumSync(drawJPEGAttribute);
                } else {
                    processorThumAsync(drawJPEGAttribute);
                    z2 = false;
                }
                if (!this.mIsImageCaptureIntent && z3 && this.mExifNeeded && z2) {
                    if (!drawJPEGAttribute.mNeedThumbnail) {
                        ImageSaverCallback imageSaverCallback = (ImageSaverCallback) this.mSaverCallback.get();
                        if (imageSaverCallback != null) {
                            imageSaverCallback.getThumbnailUpdater().updatePreviewThumbnailUri(drawJPEGAttribute.mUri);
                        }
                    } else {
                        Bitmap thumbnailBitmap = drawJPEGAttribute.mExif.getThumbnailBitmap();
                        if (thumbnailBitmap != null) {
                            Uri uri = drawJPEGAttribute.mUri;
                            if (uri != null) {
                                drawJPEGAttribute.mFinalImage = false;
                                Thumbnail createThumbnail = Thumbnail.createThumbnail(uri, thumbnailBitmap, drawJPEGAttribute.mJpegOrientation, false);
                                ImageSaverCallback imageSaverCallback2 = (ImageSaverCallback) this.mSaverCallback.get();
                                if (imageSaverCallback2 != null) {
                                    imageSaverCallback2.getThumbnailUpdater().setThumbnail(createThumbnail, true, true);
                                }
                            }
                        }
                    }
                }
            }
            synchronized (this.mLock) {
                this.mJpegQueueSize++;
            }
            this.mEglHandler.obtainMessage(1, z ? 1 : 0, 0, drawJPEGAttribute).sendToTarget();
            return true;
        }
        String str2 = TAG;
        StringBuilder sb2 = new StringBuilder();
        sb2.append("queueSize is full, drop it ");
        sb2.append(drawJPEGAttribute.mTitle);
        Log.d(str2, sb2.toString());
        return false;
    }

    public void processorJpegSync(DrawJPEGAttribute drawJPEGAttribute, boolean z) {
        this.mEglThreadBlockVar.close();
        EGLHandler eGLHandler = this.mEglHandler;
        boolean z2 = this.mExifNeeded;
        eGLHandler.obtainMessage(2, z2 ? 1 : 0, z ? 1 : 0, drawJPEGAttribute).sendToTarget();
        this.mEglThreadBlockVar.block();
    }

    public void releaseIfNeeded() {
        if (this.mEglHandler.hasMessages(1)) {
            this.mReleasePending = true;
        } else {
            this.mEglHandler.sendEmptyMessage(5);
        }
    }

    public void setExifNeed(boolean z) {
        this.mExifNeeded = z;
    }

    public void setImageSaver(ImageSaver imageSaver) {
        this.mImageSaver = imageSaver;
    }

    public void setQuality(int i) {
        if (i >= 0 && i <= 97) {
            this.mQuality = i;
        }
    }
}
