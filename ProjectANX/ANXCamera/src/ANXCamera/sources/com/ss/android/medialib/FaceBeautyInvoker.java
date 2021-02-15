package com.ss.android.medialib;

import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.Image.Plane;
import android.opengl.EGLContext;
import android.os.Build;
import android.os.Build.VERSION;
import android.text.TextUtils;
import android.util.Pair;
import android.view.Surface;
import androidx.annotation.Keep;
import androidx.annotation.RestrictTo;
import androidx.annotation.RestrictTo.Scope;
import com.bef.effectsdk.message.MessageCenter.Listener;
import com.ss.android.medialib.camera.ImageFrame;
import com.ss.android.medialib.common.Common.IGetTimestampCallback;
import com.ss.android.medialib.common.Common.IOnOpenGLCallback;
import com.ss.android.medialib.common.Common.IShotScreenCallback;
import com.ss.android.medialib.common.LogUtil;
import com.ss.android.medialib.listener.FaceDetectListener;
import com.ss.android.medialib.listener.NativeInitListener;
import com.ss.android.medialib.listener.SlamDetectListener;
import com.ss.android.medialib.listener.TextureTimeListener;
import com.ss.android.medialib.log.VEMonitorUtils;
import com.ss.android.medialib.model.EnigmaResult;
import com.ss.android.medialib.model.FaceAttributeInfo;
import com.ss.android.medialib.model.FaceDetectInfo;
import com.ss.android.medialib.presenter.IStickerRequestCallback;
import com.ss.android.medialib.qr.ScanSettings;
import com.ss.android.medialib.utils.Utils;
import com.ss.android.ttve.monitor.TEMonitor;
import com.ss.android.ttve.monitor.TEMonitorNewKeys;
import com.ss.android.ttve.nativePort.TENativeLibsLoader;
import com.ss.android.vesdk.VELogUtil;
import com.ss.android.vesdk.model.BefTextLayout;
import com.ss.android.vesdk.model.BefTextLayoutResult;
import java.lang.reflect.Array;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

@Keep
public class FaceBeautyInvoker implements Listener {
    public static final int INVALID_ENV = -100001;
    public static final int INVALID_HANDLE = -100000;
    private static final String TAG = "FaceBeautyInvoker";
    static AVCEncoderInterface mEncoderCaller;
    private static RecordStopCallback mRecordStopCallback;
    @Deprecated
    private static Runnable sDuetCompleteRunable = null;
    @Deprecated
    private static FaceDetectListener sFaceDetectListener;
    private static Listener sMessageListener;
    @Deprecated
    private static NativeInitListener sNativeInitListener;
    private static List sSlamDetectListeners = new ArrayList();
    /* access modifiers changed from: private */
    public AVCEncoder mAVCEncoder;
    private AVCEncoderInterface mAVCEncoderInterface = new AVCEncoderInterface() {
        public int getProfile() {
            return FaceBeautyInvoker.this.mAVCEncoder.getProfile();
        }

        public int onEncoderData(int i, int i2, int i3, boolean z) {
            VELogUtil.d(FaceBeautyInvoker.TAG, "onEncoderData: ...");
            if (FaceBeautyInvoker.this.mAVCEncoder != null) {
                return FaceBeautyInvoker.this.mAVCEncoder.encode(i, i2, i3, z);
            }
            return 0;
        }

        public void onEncoderData(ByteBuffer byteBuffer, int i, boolean z) {
            VELogUtil.d(FaceBeautyInvoker.TAG, "onEncoderData: ...");
        }

        public void onEncoderData(byte[] bArr, int i, boolean z) {
            String str = FaceBeautyInvoker.TAG;
            VELogUtil.d(str, "FaceBeautyManager onEncoderData == enter");
            if (FaceBeautyInvoker.this.mAVCEncoder != null) {
                FaceBeautyInvoker.this.mAVCEncoder.encode(bArr, i, z);
            }
            VELogUtil.d(str, "FaceBeautyManager onEncoderData == exit");
        }

        public Surface onInitHardEncoder(int i, int i2, int i3, int i4, int i5, int i6, boolean z) {
            String str = FaceBeautyInvoker.TAG;
            VELogUtil.i(str, "FaceBeautyManager onInitHardEncoder == enter");
            StringBuilder sb = new StringBuilder();
            sb.append("width = ");
            int i7 = i;
            sb.append(i);
            sb.append("\theight = ");
            int i8 = i2;
            sb.append(i2);
            VELogUtil.i(str, sb.toString());
            if (FaceBeautyInvoker.this.mAVCEncoder == null) {
                FaceBeautyInvoker.this.mAVCEncoder = new AVCEncoder();
            }
            FaceBeautyInvoker.this.mAVCEncoder.setEncoderCaller(this);
            Surface initAVCEncoder = FaceBeautyInvoker.this.mAVCEncoder.initAVCEncoder(i, i2, i3, i4, i5, i6, z);
            if (initAVCEncoder == null) {
                FaceBeautyInvoker.this.mAVCEncoder.uninitAVCEncoder();
                FaceBeautyInvoker.this.mAVCEncoder = null;
                FaceBeautyInvoker.this.setHardEncoderStatus(false);
                return null;
            }
            VELogUtil.e(str, "====== initAVCEncoder succeed ======");
            FaceBeautyInvoker.this.setHardEncoderStatus(true);
            VELogUtil.i(str, "FaceBeautyManager onInitHardEncoder == exit");
            return initAVCEncoder;
        }

        public Surface onInitHardEncoder(int i, int i2, int i3, int i4, boolean z) {
            String str = FaceBeautyInvoker.TAG;
            VELogUtil.i(str, "FaceBeautyManager onInitHardEncoder == enter");
            StringBuilder sb = new StringBuilder();
            sb.append("width = ");
            sb.append(i);
            sb.append("\theight = ");
            sb.append(i2);
            VELogUtil.i(str, sb.toString());
            if (FaceBeautyInvoker.this.mAVCEncoder == null) {
                FaceBeautyInvoker.this.mAVCEncoder = new AVCEncoder();
            }
            FaceBeautyInvoker.this.mAVCEncoder.setEncoderCaller(this);
            Surface initAVCEncoder = FaceBeautyInvoker.this.mAVCEncoder.initAVCEncoder(i, i2, i3, i4, z);
            if (initAVCEncoder == null) {
                FaceBeautyInvoker.this.mAVCEncoder.uninitAVCEncoder();
                FaceBeautyInvoker.this.mAVCEncoder = null;
                FaceBeautyInvoker.this.setHardEncoderStatus(false);
                return null;
            }
            VELogUtil.i(str, "====== initAVCEncoder succeed ======");
            FaceBeautyInvoker.this.setHardEncoderStatus(true);
            VELogUtil.i(str, "FaceBeautyManager onInitHardEncoder == exit");
            return initAVCEncoder;
        }

        public void onSetCodecConfig(ByteBuffer byteBuffer) {
            StringBuilder sb = new StringBuilder();
            sb.append("onSetCodecConfig: data size = ");
            sb.append(byteBuffer.remaining());
            VELogUtil.d(FaceBeautyInvoker.TAG, sb.toString());
            if (FaceBeautyInvoker.this.mHandler != 0) {
                FaceBeautyInvoker faceBeautyInvoker = FaceBeautyInvoker.this;
                faceBeautyInvoker.nativeSetCodecConfig(faceBeautyInvoker.mHandler, byteBuffer, byteBuffer.remaining());
            }
        }

        public void onSwapGlBuffers() {
            if (FaceBeautyInvoker.this.mHandler != 0) {
                FaceBeautyInvoker faceBeautyInvoker = FaceBeautyInvoker.this;
                faceBeautyInvoker.nativeOnSwapGlBuffers(faceBeautyInvoker.mHandler);
            }
        }

        public void onUninitHardEncoder() {
            String str = FaceBeautyInvoker.TAG;
            VELogUtil.i(str, "FaceBeautyManager onUninitHardEncoder == enter");
            if (FaceBeautyInvoker.this.mAVCEncoder != null) {
                FaceBeautyInvoker.this.mAVCEncoder.uninitAVCEncoder();
                FaceBeautyInvoker.this.mAVCEncoder = null;
                VELogUtil.i(str, "====== uninitAVCEncoder ======");
            }
            VELogUtil.i(str, "FaceBeautyManager onUninitHardEncoder == exit");
        }

        public void onWriteFile(ByteBuffer byteBuffer, int i, int i2, int i3) {
            if (FaceBeautyInvoker.this.mHandler != 0) {
                FaceBeautyInvoker faceBeautyInvoker = FaceBeautyInvoker.this;
                faceBeautyInvoker.nativeWriteFile(faceBeautyInvoker.mHandler, byteBuffer, byteBuffer.remaining(), i, i3);
            }
        }

        public void onWriteFile(ByteBuffer byteBuffer, long j, long j2, int i, int i2) {
            if (FaceBeautyInvoker.this.mHandler != 0) {
                FaceBeautyInvoker faceBeautyInvoker = FaceBeautyInvoker.this;
                faceBeautyInvoker.nativeWriteFile2(faceBeautyInvoker.mHandler, byteBuffer, byteBuffer.remaining(), j, j2, i2);
            }
        }

        public void setColorFormat(int i) {
            if (FaceBeautyInvoker.this.mHandler != 0) {
                FaceBeautyInvoker faceBeautyInvoker = FaceBeautyInvoker.this;
                faceBeautyInvoker.nativeSetColorFormat(faceBeautyInvoker.mHandler, i);
            }
        }
    };
    private Runnable mDuetCompleteRunable;
    private FaceDetectListener mFaceDetectListener;
    private IGetTimestampCallback mGetTimestampCallback = null;
    /* access modifiers changed from: private */
    public long mHandler = 0;
    private boolean mIsDuringScreenshot = false;
    private boolean mIsRenderRady = false;
    private NativeInitListener mNativeInitListener;
    private IOnOpenGLCallback mOpenGLCallback = null;
    private IShotScreenCallback mShotScreenCallback = null;
    private TextureTimeListener mTextureTimeListener;

    @Keep
    public interface FaceInfoCallback {
        void onResult(FaceAttributeInfo faceAttributeInfo, FaceDetectInfo faceDetectInfo);
    }

    @Keep
    public interface OnARTextBitmapCallback {
        BefTextLayoutResult onResult(String str, BefTextLayout befTextLayout);
    }

    @Keep
    public interface OnARTextContentCallback {
        void onResult(String[] strArr);
    }

    @Keep
    public interface OnARTextCountCallback {
        void onResult(int i);
    }

    @Keep
    public interface OnCherEffectParmaCallback {
        void onCherEffect(String[] strArr, double[] dArr, boolean[] zArr);
    }

    @Keep
    public interface OnFrameCallback {
        void onFrame(int i, double d);

        void onInit(EGLContext eGLContext, int i, int i2, int i3, long j);
    }

    @Keep
    public interface OnHandDetectCallback {
        void onResult(int[] iArr);
    }

    @Keep
    public interface OnPictureCallback {
        void onResult(int[] iArr, int i, int i2);
    }

    @Keep
    public interface OnPictureCallbackV2 {
        void onImage(int[] iArr, int i, int i2);

        void onResult(int i, int i2);
    }

    @Keep
    public interface OnRunningErrorCallback {
        public static final int FRAG_HW_ENCODER_ERR = -42001;
        public static final int INIT_FRAG_OUTPUT_ERR = -42000;

        void onError(int i);
    }

    public interface RecordStopCallback {
        void onStop();
    }

    static {
        TENativeLibsLoader.loadLibrary();
    }

    public FaceBeautyInvoker() {
        mEncoderCaller = this.mAVCEncoderInterface;
    }

    public static synchronized void addSlamDetectListener(SlamDetectListener slamDetectListener) {
        synchronized (FaceBeautyInvoker.class) {
            if (slamDetectListener != null) {
                sSlamDetectListeners.add(slamDetectListener);
            }
        }
    }

    public static synchronized void clearSlamDetectListener() {
        synchronized (FaceBeautyInvoker.class) {
            sSlamDetectListeners.clear();
        }
    }

    public static NativeInitListener getNativeInitListener() {
        return sNativeInitListener;
    }

    private boolean isRenderReady() {
        return this.mIsRenderRady;
    }

    private native int nativeAddPCMData(long j, byte[] bArr, int i);

    private native int nativeBindEffectAudioProcessor(long j, int i, int i2, boolean z);

    private native void nativeCancelAll(long j);

    private native int nativeChangeMusicPath(long j, String str);

    private native void nativeChangeOutputVideoSize(long j, int i, int i2);

    private native int nativeChangeSurface(long j, Surface surface);

    private native void nativeChooseSlamFace(long j, int i);

    private native int nativeClearFragFile(long j);

    private native int nativeCloseWavFile(long j, boolean z);

    private native int nativeConcat(long j, String str, String str2, int i, String str3, String str4);

    private native long nativeCreate();

    private native int nativeDeleteLastFrag(long j);

    private native void nativeEnableAbandonFirstFrame(long j, boolean z);

    private native int nativeEnableBlindWaterMark(long j, boolean z);

    private native void nativeEnableEffect(long j, boolean z);

    private native void nativeEnableEffectBGM(long j, boolean z);

    private native void nativeEnableScan(long j, boolean z, long j2);

    private native void nativeEnableSceneRecognition(long j, boolean z);

    private native int nativeEnableTTFaceDetect(long j, boolean z);

    private native void nativeEnableUse16BitAlign(long j, boolean z);

    private native int nativeExpandPreviewAndRecordInterval(long j, boolean z);

    private native long nativeGetAudioEndTime(long j);

    private native long nativeGetEndFrameTime(long j);

    private native EnigmaResult nativeGetEnigmaResult(long j);

    private native float nativeGetReactionCamRotation(long j);

    private native int[] nativeGetReactionCameraPosInRecordPixel(long j);

    private native int[] nativeGetReactionCameraPosInViewPixel(long j);

    private native int[] nativeGetReactionPosMarginInViewPixel(long j);

    private native int nativeGetSlamFaceCount(long j);

    private native int nativeHideSlamKeyBoard(long j, boolean z);

    private native int nativeInitAudioConfig(long j, int i, int i2);

    private native int nativeInitAudioPlayer(long j, String str, int i, int i2, long j2, boolean z);

    private native int nativeInitDuet(long j, String str, float f, float f2, float f3, boolean z);

    private native int nativeInitFaceBeautyPlay(long j, int i, int i2, String str, int i3, int i4, String str2, int i5, boolean z, boolean z2);

    private native int nativeInitFaceBeautyPlayOnlyPreview(long j, ScanSettings scanSettings);

    private native int nativeInitImageDrawer(long j, int i);

    private native int nativeInitMediaCodecSurface(long j, Surface surface);

    private native int nativeInitReaction(long j, String str);

    private native int nativeInitWavFile(long j, int i, int i2, double d);

    private native boolean nativeIsQualcomm(long j);

    private native boolean nativeIsStickerEnabled(long j);

    private native int nativeMarkPlayDone(long j);

    private native int nativeOnAudioCallback(long j, byte[] bArr, int i);

    private native int nativeOnDrawFrameBuffer(long j, byte[] bArr, int i, int i2, int i3);

    private native int nativeOnDrawFrameBuffer2(long j, ByteBuffer byteBuffer, int[] iArr, ByteBuffer byteBuffer2, int[] iArr2, ByteBuffer byteBuffer3, int[] iArr3, int i, int i2, int i3);

    private native int nativeOnFrameAvailable(long j, int i, float[] fArr);

    private native int nativeOnFrameTime(long j, double d);

    public native void nativeOnSwapGlBuffers(long j);

    private native int nativePauseEffectAudio(long j, boolean z);

    private native boolean nativePosInReactionRegion(long j, int i, int i2);

    private native int nativeProcessTouchEvent(long j, float f, float f2);

    private native void nativeRecoverCherEffect(long j, String[] strArr, double[] dArr, boolean[] zArr);

    private native void nativeRegisterCherEffectParamCallback(long j, OnCherEffectParmaCallback onCherEffectParmaCallback);

    private native void nativeRegisterFaceInfoUpload(long j, boolean z, FaceInfoCallback faceInfoCallback);

    private native void nativeRegisterHandDetectCallback(long j, int[] iArr, OnHandDetectCallback onHandDetectCallback);

    private native int nativeRenderPicture(long j, byte[] bArr, int i, int i2, int i3, OnPictureCallbackV2 onPictureCallbackV2);

    private native int nativeRenderPicture2(long j, ByteBuffer byteBuffer, int[] iArr, ByteBuffer byteBuffer2, int[] iArr2, ByteBuffer byteBuffer3, int[] iArr3, int i, int i2, int i3, int i4, int i5, OnPictureCallbackV2 onPictureCallbackV2);

    private native int nativeRenderPicture3(long j, Bitmap bitmap, int i, int i2, OnPictureCallbackV2 onPictureCallbackV2);

    private native void nativeResetPerfStats(long j);

    private native int nativeResetStartTime(long j, long j2, long j3);

    private native float nativeRotateReactionWindow(long j, float f);

    private native int nativeSave(long j);

    private native int[] nativeScaleReactionWindow(long j, float f);

    private native void nativeSendEffectMonitor(long j);

    private native void nativeSendEffectMsg(long j, int i, long j2, long j3, String str);

    private native void nativeSetAlgorithmChangeMsg(long j, int i, boolean z);

    private native int nativeSetBGMVolume(long j, float f);

    private native int nativeSetBeautyFace(long j, int i, String str);

    private native int nativeSetBeautyFaceIntensity(long j, float f, float f2);

    private native int nativeSetBlindWaterMarkDiffKeys(long j, int i, int i2);

    private native int nativeSetBlindWaterMarkPosition(long j, int i, int i2);

    private native void nativeSetCameraFirstFrameOptimize(long j, boolean z);

    public native int nativeSetCodecConfig(long j, ByteBuffer byteBuffer, int i);

    public native int nativeSetColorFormat(long j, int i);

    private native int nativeSetDLEEnable(long j, boolean z);

    private native void nativeSetDetectInterval(long j, int i);

    private native void nativeSetDetectionMode(long j, boolean z);

    private native int nativeSetDeviceRotation(long j, float[] fArr);

    private native void nativeSetDuetCameraPaused(long j, boolean z);

    private native void nativeSetEffectBuildChainType(long j, int i);

    private native int nativeSetFaceMakeUp(long j, String str, float f, float f2);

    private native int nativeSetFaceMakeUp2(long j, String str);

    private native int nativeSetFilter(long j, String str, String str2, float f);

    private native int nativeSetFilterIntensity(long j, float f);

    private native int nativeSetFilterPos(long j, float f);

    private native int nativeSetFrameCallback(long j, OnFrameCallback onFrameCallback, boolean z);

    private native int nativeSetHandDetectLowpower(long j, boolean z);

    private native int nativeSetHardEncoderStatus(long j, boolean z);

    private native int nativeSetIntensityByType(long j, int i, float f);

    private native void nativeSetModeChangeState(long j, int i);

    private native int nativeSetMusicNodes(long j, String str);

    private native int nativeSetMusicTime(long j, long j2, long j3);

    private native void nativeSetNativeLibraryDir(String str);

    private native int nativeSetPlayLength(long j, long j2);

    private native void nativeSetPreviewSizeRatio(long j, float f);

    private native void nativeSetReactionBorderParam(long j, int i, int i2);

    private native boolean nativeSetReactionMaskImage(long j, String str, boolean z);

    private native void nativeSetReactionPosMargin(long j, int i, int i2, int i3, int i4);

    private native void nativeSetRenderCacheTexture(long j, String str, String str2);

    private native int nativeSetReshape(long j, String str, float f, float f2);

    private native int nativeSetRunningErrorCallback(long j, OnRunningErrorCallback onRunningErrorCallback);

    private native boolean nativeSetSharedTextureStatus(boolean z);

    private native int nativeSetSkinTone(long j, String str);

    private native int nativeSetSlamFace(long j, Bitmap bitmap);

    private native int nativeSetSlamInputText(long j, String str, int i, int i2, String str2);

    private native int nativeSetSticker(long j, Bitmap bitmap, int i, int i2);

    private native int nativeSetStickerPath(long j, String str, int i, int i2, boolean z);

    private native int nativeSetSwapDuetRegion(long j, boolean z);

    private native int nativeSetUseMusic(long j, int i);

    private native int nativeSetVideoQuality(long j, int i, int i2);

    private native int nativeShotScreen(long j, String str, int[] iArr, boolean z, int i, OnPictureCallback onPictureCallback);

    private native int nativeSlamDeviceConfig(long j, boolean z, boolean z2, boolean z3, boolean z4);

    private native int nativeSlamGetTextBitmap(long j, OnARTextBitmapCallback onARTextBitmapCallback);

    private native int nativeSlamGetTextLimitCount(long j, OnARTextCountCallback onARTextCountCallback);

    private native int nativeSlamGetTextParagraphContent(long j, OnARTextContentCallback onARTextContentCallback);

    private native int nativeSlamProcessDoubleClickEvent(long j, float f, float f2);

    private native int nativeSlamProcessIngestAcc(long j, double d, double d2, double d3, double d4);

    private native int nativeSlamProcessIngestGra(long j, double d, double d2, double d3, double d4);

    private native int nativeSlamProcessIngestGyr(long j, double d, double d2, double d3, double d4);

    private native int nativeSlamProcessIngestOri(long j, double[] dArr, double d);

    private native int nativeSlamProcessPanEvent(long j, float f, float f2, float f3, float f4, float f5);

    private native int nativeSlamProcessRotationEvent(long j, float f, float f2);

    private native int nativeSlamProcessScaleEvent(long j, float f, float f2);

    private native int nativeSlamProcessTouchEvent(long j, float f, float f2);

    private native int nativeSlamProcessTouchEventByType(long j, int i, float f, float f2, int i2);

    private native int nativeSlamSetLanguge(long j, String str);

    private native int nativeSlamSetTextBitmapResult(long j, Bitmap bitmap, int i, int i2, int i3);

    private native int nativeStartPlay(long j, Surface surface, int i, int i2, String str);

    private native int nativeStartPlay2(long j, int i, int i2, int i3, int i4, String str);

    private native int nativeStartRecord(long j, double d, boolean z, int i, int i2, int i3);

    private native int nativeStopPlay(long j);

    private native int nativeStopRecord(long j, boolean z);

    private native int nativeTryRestore(long j, int i, String str);

    private native void nativeUnRegisterFaceInfoUpload(long j);

    private native int nativeUninitAudioPlayer(long j);

    private native int nativeUninitFaceBeautyPlay(long j);

    private native void nativeUpdateReactionBGAlpha(long j, float f);

    private native int[] nativeUpdateReactionCameraPos(long j, int i, int i2, int i3, int i4);

    private native int[] nativeUpdateReactionCameraPosWithRotation(long j, int i, int i2, int i3, int i4, float f);

    private native void nativeUpdateRotation(long j, float f, float f2, float f3);

    private native void nativeUpdateRotationAndFront(long j, int i, boolean z);

    private native void nativeUseLargeMattingModel(long j, boolean z);

    public native int nativeWriteFile(long j, ByteBuffer byteBuffer, int i, int i2, int i3);

    public native int nativeWriteFile2(long j, ByteBuffer byteBuffer, int i, long j2, long j3, int i2);

    private int onNativeCallback_GetHardEncoderProfile() {
        if (mEncoderCaller == null) {
            return -1;
        }
        VELogUtil.i(TAG, "GetHardEncoderProfile");
        return mEncoderCaller.getProfile();
    }

    private void onNativeCallback_Init(int i) {
        StringBuilder sb;
        String str = TAG;
        if (i < 0) {
            sb = new StringBuilder();
            sb.append("onNativeCallback_Init error = ");
            sb.append(i);
            VELogUtil.e(str, sb.toString());
        } else {
            sb = new StringBuilder();
            sb.append("onNativeCallback_Init success = ");
            sb.append(i);
            VELogUtil.i(str, sb.toString());
            this.mIsRenderRady = true;
        }
        NativeInitListener nativeInitListener = this.mNativeInitListener;
        if (nativeInitListener != null) {
            nativeInitListener.onNativeInitCallBack(i);
        }
        NativeInitListener nativeInitListener2 = sNativeInitListener;
        if (nativeInitListener2 != null) {
            nativeInitListener2.onNativeInitCallBack(i);
        }
    }

    public static void onNativeCallback_onMonitorLogFloat(String str, String str2, float f) {
        TEMonitor.monitorTELog(str, str2, f);
    }

    public static void onNativeCallback_onMonitorLogInt(String str, String str2, int i) {
        TEMonitor.monitorTELog(str, str2, (long) i);
    }

    public static synchronized void removeSlamDetectListener(SlamDetectListener slamDetectListener) {
        synchronized (FaceBeautyInvoker.class) {
            sSlamDetectListeners.remove(slamDetectListener);
        }
    }

    private native void setCaptureMirror(long j, boolean z);

    @Deprecated
    public static void setDuetVideoCompleteCallback(Runnable runnable) {
        sDuetCompleteRunable = runnable;
    }

    @Deprecated
    public static void setFaceDetectListener(FaceDetectListener faceDetectListener) {
        sFaceDetectListener = faceDetectListener;
    }

    public static synchronized void setMessageListener(Listener listener) {
        synchronized (FaceBeautyInvoker.class) {
            sMessageListener = listener;
        }
    }

    @Deprecated
    public static void setNativeInitListener(NativeInitListener nativeInitListener) {
        sNativeInitListener = nativeInitListener;
    }

    public static void setRecordStopCallback(RecordStopCallback recordStopCallback) {
        mRecordStopCallback = recordStopCallback;
    }

    @Deprecated
    public static synchronized void setSlamDetectListener(SlamDetectListener slamDetectListener) {
        synchronized (FaceBeautyInvoker.class) {
            addSlamDetectListener(slamDetectListener);
        }
    }

    @RestrictTo({Scope.LIBRARY})
    public int addPCMData(byte[] bArr, int i) {
        long j = this.mHandler;
        return j == 0 ? INVALID_HANDLE : nativeAddPCMData(j, bArr, i);
    }

    public int bindEffectAudioProcessor(int i, int i2, boolean z) {
        long j = this.mHandler;
        if (j != 0) {
            return nativeBindEffectAudioProcessor(j, i, i2, z);
        }
        VELogUtil.e(TAG, "invalid handle");
        return INVALID_HANDLE;
    }

    public void cancelAll() {
        long j = this.mHandler;
        if (j != 0) {
            nativeCancelAll(j);
        }
    }

    public synchronized int changeMusicPath(String str) {
        if (this.mHandler == 0) {
            return INVALID_HANDLE;
        }
        return nativeChangeMusicPath(this.mHandler, str);
    }

    public void changeOutputVideoSize(int i, int i2) {
        long j = this.mHandler;
        if (j != 0) {
            nativeChangeOutputVideoSize(j, i, i2);
        }
    }

    public synchronized int changeSurface(Surface surface) {
        if (this.mHandler == 0) {
            return INVALID_HANDLE;
        }
        return nativeChangeSurface(this.mHandler, surface);
    }

    public void chooseSlamFace(int i) {
        if (isRenderReady()) {
            long j = this.mHandler;
            if (j == 0) {
                VELogUtil.e(TAG, "invalid handle");
            } else {
                nativeChooseSlamFace(j, i);
            }
        }
    }

    public int clearFragFile() {
        long j = this.mHandler;
        return j == 0 ? INVALID_HANDLE : nativeClearFragFile(j);
    }

    @RestrictTo({Scope.LIBRARY})
    public int closeWavFile(boolean z) {
        long j = this.mHandler;
        if (j == 0) {
            return INVALID_HANDLE;
        }
        int nativeCloseWavFile = nativeCloseWavFile(j, z);
        save();
        return nativeCloseWavFile;
    }

    public int concat(String str, String str2, int i, String str3, String str4) {
        synchronized (this) {
            if (this.mHandler == 0) {
                return INVALID_HANDLE;
            }
            return nativeConcat(this.mHandler, str, str2, i, str3, str4);
        }
    }

    public int concat(String str, String str2, String str3, String str4) {
        long j = this.mHandler;
        return j == 0 ? INVALID_HANDLE : nativeConcat(j, str, str2, 0, str3, str4);
    }

    public void createEncoder() {
        if (this.mAVCEncoder == null) {
            this.mAVCEncoder = new AVCEncoder();
        }
        this.mAVCEncoder.createEncoder();
    }

    public int deleteLastFrag() {
        long j = this.mHandler;
        return j == 0 ? INVALID_HANDLE : nativeDeleteLastFrag(j);
    }

    public void destroyMessageCenter() {
        MessageCenterWrapper.getInstance().destroy();
        MessageCenterWrapper.getInstance().removeListener(this);
        synchronized (FaceBeautyInvoker.class) {
            sMessageListener = null;
        }
    }

    public void enableAbandonFirstFrame(boolean z) {
        long j = this.mHandler;
        if (j == 0) {
            LogUtil.e(TAG, "invalid handle");
        } else {
            nativeEnableAbandonFirstFrame(j, z);
        }
    }

    public int enableBlindWaterMark(boolean z) {
        long j = this.mHandler;
        return j == 0 ? INVALID_HANDLE : nativeEnableBlindWaterMark(j, z);
    }

    public void enableEffect(boolean z) {
        if (isRenderReady()) {
            long j = this.mHandler;
            if (j == 0) {
                VELogUtil.e(TAG, "invalid handle");
            } else {
                nativeEnableEffect(j, z);
            }
        }
    }

    public void enableEffectBGM(boolean z) {
        if (isRenderReady()) {
            long j = this.mHandler;
            if (j == 0) {
                VELogUtil.e(TAG, "invalid handle");
            } else {
                nativeEnableEffectBGM(j, z);
            }
        }
    }

    public void enableScan(boolean z, long j) {
        long j2 = this.mHandler;
        if (j2 == 0) {
            VELogUtil.e(TAG, "invalid handle");
        } else {
            nativeEnableScan(j2, z, j);
        }
    }

    public void enableSceneRecognition(boolean z) {
        if (isRenderReady()) {
            nativeEnableSceneRecognition(this.mHandler, z);
        }
    }

    public int enableTTFaceDetect(boolean z) {
        long j = this.mHandler;
        return j == 0 ? INVALID_HANDLE : nativeEnableTTFaceDetect(j, z);
    }

    public long getAudioEndTime() {
        long j = this.mHandler;
        if (j == 0) {
            return -100000;
        }
        return nativeGetAudioEndTime(j);
    }

    public long getEndFrameTime() {
        long j = this.mHandler;
        if (j == 0) {
            return -100000;
        }
        return nativeGetEndFrameTime(j);
    }

    public EnigmaResult getEnigmaResult() {
        if (!isRenderReady()) {
            return null;
        }
        long j = this.mHandler;
        if (j != 0) {
            return nativeGetEnigmaResult(j);
        }
        VELogUtil.e(TAG, "invalid handle");
        return null;
    }

    public float getReactionCamRotation() {
        long j = this.mHandler;
        if (j == 0) {
            return -100000.0f;
        }
        return nativeGetReactionCamRotation(j);
    }

    public int[] getReactionCameraPosInRecordPixel() {
        long j = this.mHandler;
        if (j == 0) {
            return null;
        }
        return nativeGetReactionCameraPosInRecordPixel(j);
    }

    public int[] getReactionCameraPosInViewPixel() {
        long j = this.mHandler;
        if (j == 0) {
            return null;
        }
        return nativeGetReactionCameraPosInViewPixel(j);
    }

    public int[] getReactionPosMarginInViewPixel() {
        long j = this.mHandler;
        if (j == 0) {
            return null;
        }
        return nativeGetReactionPosMarginInViewPixel(j);
    }

    public int getSlamFaceCount() {
        if (!isRenderReady()) {
            return INVALID_ENV;
        }
        long j = this.mHandler;
        if (j != 0) {
            return nativeGetSlamFaceCount(j);
        }
        VELogUtil.e(TAG, "invalid handle");
        return INVALID_HANDLE;
    }

    public long getTextureDeltaTime(boolean z) {
        TextureTimeListener textureTimeListener = this.mTextureTimeListener;
        if (textureTimeListener != null) {
            return textureTimeListener.getTextureDeltaTime(z);
        }
        return 0;
    }

    @RestrictTo({Scope.LIBRARY})
    public int initAudioConfig(int i, int i2) {
        long j = this.mHandler;
        return j == 0 ? INVALID_HANDLE : nativeInitAudioConfig(j, i, i2);
    }

    public int initAudioPlayer(Context context, String str, long j) {
        return initAudioPlayer(context, str, j, false, false);
    }

    public int initAudioPlayer(Context context, String str, long j, boolean z, boolean z2) {
        PackageManager packageManager = context.getPackageManager();
        boolean z3 = false;
        Integer valueOf = Integer.valueOf(0);
        if (packageManager != null) {
            z3 = packageManager.hasSystemFeature("android.hardware.audio.low_latency");
        }
        StringBuilder sb = new StringBuilder();
        sb.append("has low latency ? ");
        sb.append(z3);
        VELogUtil.d(TAG, sb.toString());
        Pair pair = z2 ? new Pair(valueOf, valueOf) : Utils.getSystemAudioInfo(context);
        StringBuilder sb2 = new StringBuilder();
        sb2.append("nativeSampleRate ? ");
        sb2.append(pair.first);
        sb2.append(" nativeSamleBufferSize? ");
        sb2.append(pair.second);
        VELogUtil.d(TAG, sb2.toString());
        synchronized (this) {
            if (this.mHandler == 0) {
                return INVALID_HANDLE;
            }
            int nativeInitAudioPlayer = nativeInitAudioPlayer(this.mHandler, str, ((Integer) pair.first).intValue(), ((Integer) pair.second).intValue(), j, z);
            return nativeInitAudioPlayer;
        }
    }

    public void initDuet(String str, float f, float f2, float f3, boolean z) {
        long j = this.mHandler;
        if (j != 0) {
            nativeInitDuet(j, str, f, f2, f3, z);
        }
    }

    public int initFaceBeautyPlay(int i, int i2, String str, int i3, int i4, String str2, int i5) {
        return initFaceBeautyPlay(i, i2, str, i3, i4, str2, i5, false);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:14:0x0047, code lost:
        return r0;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public int initFaceBeautyPlay(int i, int i2, String str, int i3, int i4, String str2, int i5, boolean z) {
        this.mHandler = nativeCreate();
        initMessageCenter();
        AVCEncoder.setDrainWaitTimeout(5000);
        synchronized (this) {
            if (this.mHandler == 0) {
                return INVALID_HANDLE;
            }
            int nativeInitFaceBeautyPlay = nativeInitFaceBeautyPlay(this.mHandler, i, i2, str, i3, i4, str2, i5, false, z);
            if (nativeInitFaceBeautyPlay == 0 && Build.MODEL.contains("OPPO R7")) {
                nativeExpandPreviewAndRecordInterval(this.mHandler, true);
            }
        }
    }

    public int initFaceBeautyPlayOnlyPreview(ScanSettings scanSettings) {
        this.mHandler = nativeCreate();
        initMessageCenter();
        long j = this.mHandler;
        return j == 0 ? INVALID_HANDLE : nativeInitFaceBeautyPlayOnlyPreview(j, scanSettings);
    }

    public void initHardEncoderInAdvance() {
        if (this.mAVCEncoder == null) {
            this.mAVCEncoder = new AVCEncoder();
        }
    }

    public int initImageDrawer(int i) {
        long j = this.mHandler;
        if (j != 0) {
            return nativeInitImageDrawer(j, i);
        }
        VELogUtil.e(TAG, "invalid handle");
        return INVALID_HANDLE;
    }

    public int initMediaCodecSurface(Surface surface) {
        long j = this.mHandler;
        if (j != 0) {
            return nativeInitMediaCodecSurface(j, surface);
        }
        VELogUtil.e(TAG, "invalid handle");
        return INVALID_HANDLE;
    }

    public void initMessageCenter() {
        MessageCenterWrapper.getInstance().init();
        MessageCenterWrapper.getInstance().addListener(this);
    }

    public void initReaction(Context context, String str, String str2) {
        long j = this.mHandler;
        if (j != 0) {
            nativeInitReaction(j, str);
        }
    }

    @RestrictTo({Scope.LIBRARY})
    public int initWavFile(int i, int i2, double d) {
        long j = this.mHandler;
        return j == 0 ? INVALID_HANDLE : nativeInitWavFile(j, i, i2, d);
    }

    public boolean isStickerEnabled() {
        long j = this.mHandler;
        if (j != 0) {
            return nativeIsStickerEnabled(j);
        }
        VELogUtil.e(TAG, "invalid handle");
        return false;
    }

    public int markPlayDone() {
        long j = this.mHandler;
        return j == 0 ? INVALID_HANDLE : nativeMarkPlayDone(j);
    }

    public native boolean nativePreviewDuetVideo(long j);

    public native void nativeSetForceAlgorithmCnt(long j, int i);

    public native void nativeSetPreviewDuetVideoPaused(long j, boolean z);

    public native int nativeSetStickerRequestCallback(long j, IStickerRequestCallback iStickerRequestCallback);

    public void onAudioCallback(byte[] bArr, int i) {
        long j = this.mHandler;
        if (j != 0) {
            nativeOnAudioCallback(j, bArr, i);
        }
    }

    public int onDrawFrame(int i, float[] fArr) {
        long j = this.mHandler;
        return j == 0 ? INVALID_HANDLE : nativeOnFrameAvailable(j, i, fArr);
    }

    public int onDrawFrame(ImageFrame imageFrame) {
        if (this.mHandler == 0) {
            return INVALID_HANDLE;
        }
        if (imageFrame.getBuf() != null) {
            nativeOnDrawFrameBuffer(this.mHandler, imageFrame.getBuf(), imageFrame.getBuf().length, imageFrame.getWidth(), imageFrame.getHeight());
        } else if (VERSION.SDK_INT >= 19) {
            Plane[] plans = imageFrame.getPlans();
            if (plans == null) {
                return -1;
            }
            int[][] iArr = (int[][]) Array.newInstance(int.class, new int[]{3, 3});
            ByteBuffer[] byteBufferArr = new ByteBuffer[3];
            for (int i = 0; i < plans.length; i++) {
                byteBufferArr[i] = plans[i].getBuffer();
                iArr[i] = new int[]{byteBufferArr[i].remaining(), plans[i].getRowStride(), plans[i].getPixelStride()};
            }
            return nativeOnDrawFrameBuffer2(this.mHandler, byteBufferArr[0], iArr[0], byteBufferArr[1], iArr[1], byteBufferArr[2], iArr[2], imageFrame.getFormat(), imageFrame.getWidth(), imageFrame.getHeight());
        }
        return -1;
    }

    public int onDrawFrameTime(double d) {
        long j = this.mHandler;
        return j == 0 ? INVALID_HANDLE : nativeOnFrameTime(j, d);
    }

    public void onDuetVideoComplete() {
        Runnable runnable = this.mDuetCompleteRunable;
        if (runnable != null) {
            runnable.run();
        }
        Runnable runnable2 = sDuetCompleteRunable;
        if (runnable2 != null) {
            runnable2.run();
        }
    }

    public void onFirstFrameRenderInfo(int i, double d) {
        StringBuilder sb;
        long j;
        String str = TAG;
        if (i == 0) {
            TEMonitor.perfDouble(0, TEMonitorNewKeys.TE_PREVIEW_FIRST_FRAME_SCREEN_TIME, d - ((double) VEMonitorUtils.sbeforeCameraOpenTimeStamp));
            sb = new StringBuilder();
            sb.append("Camera Preview First Frame Cost: ");
            j = VEMonitorUtils.sbeforeCameraOpenTimeStamp;
        } else if (i == 1) {
            TEMonitor.perfDouble(0, TEMonitorNewKeys.TE_PREVIEW_SWITCH_CAMERA_SCREEN_TIME, d - ((double) VEMonitorUtils.sbeforeSwitchCameraTimeStamp));
            sb = new StringBuilder();
            sb.append("Camera Change Cost: ");
            j = VEMonitorUtils.sbeforeSwitchCameraTimeStamp;
        } else {
            return;
        }
        sb.append(d - ((double) j));
        LogUtil.w(str, sb.toString());
    }

    public void onMessageReceived(int i, int i2, int i3, String str) {
        synchronized (FaceBeautyInvoker.class) {
            if (sMessageListener != null) {
                sMessageListener.onMessageReceived(i, i2, i3, str);
            }
        }
    }

    public Surface onNativeCallback_InitHardEncoder(int i, int i2, int i3, int i4, int i5, int i6, boolean z) {
        if (mEncoderCaller == null) {
            return null;
        }
        VELogUtil.i(TAG, "InitHardEncoder");
        return mEncoderCaller.onInitHardEncoder(i, i2, i3, i4, i5, i6, z);
    }

    public void onNativeCallback_InitHardEncoderRet(int i, int i2) {
        String str = TAG;
        VELogUtil.i(str, "onInitHardEncoderRet");
        StringBuilder sb = new StringBuilder();
        sb.append("isCPUEncode = ");
        sb.append(i);
        VELogUtil.i(str, sb.toString());
        NativeInitListener nativeInitListener = this.mNativeInitListener;
        if (nativeInitListener != null) {
            nativeInitListener.onNativeInitHardEncoderRetCallback(i, i2);
        }
        NativeInitListener nativeInitListener2 = sNativeInitListener;
        if (nativeInitListener2 != null) {
            nativeInitListener2.onNativeInitHardEncoderRetCallback(i, i2);
        }
    }

    public void onNativeCallback_UninitHardEncoder() {
        String str = TAG;
        VELogUtil.i(str, " onUninitHardEncoder == enter");
        AVCEncoderInterface aVCEncoderInterface = mEncoderCaller;
        if (aVCEncoderInterface != null) {
            aVCEncoderInterface.onUninitHardEncoder();
        }
        VELogUtil.i(str, " onUninitHardEncoder == exit");
    }

    public void onNativeCallback_encodeData(byte[] bArr, int i, boolean z) {
        AVCEncoderInterface aVCEncoderInterface = mEncoderCaller;
        if (aVCEncoderInterface != null) {
            aVCEncoderInterface.onEncoderData(bArr, i, z);
        }
    }

    public int onNativeCallback_encodeTexture(int i, int i2, boolean z) {
        AVCEncoderInterface aVCEncoderInterface = mEncoderCaller;
        if (aVCEncoderInterface != null) {
            return aVCEncoderInterface.onEncoderData(i, i2, 0, z);
        }
        return 0;
    }

    public void onNativeCallback_onFaceDetect(int i, int i2) {
        StringBuilder sb = new StringBuilder();
        sb.append("FaceBeautyInvoker onFaceDetect ");
        sb.append(i2);
        VELogUtil.d(TAG, sb.toString());
        FaceDetectListener faceDetectListener = sFaceDetectListener;
        if (faceDetectListener != null) {
            faceDetectListener.onResult(i, i2);
        }
        FaceDetectListener faceDetectListener2 = this.mFaceDetectListener;
        if (faceDetectListener2 != null) {
            faceDetectListener2.onResult(i, i2);
        }
    }

    public void onNativeCallback_onOpenGLCreate() {
        VELogUtil.i(TAG, "onNativeCallback_onOpenGLCreate");
        IOnOpenGLCallback iOnOpenGLCallback = this.mOpenGLCallback;
        if (iOnOpenGLCallback != null) {
            iOnOpenGLCallback.onOpenGLCreate();
        }
    }

    public void onNativeCallback_onOpenGLDestroy() {
        VELogUtil.i(TAG, "onNativeCallback_onOpenGLDestroy");
        IOnOpenGLCallback iOnOpenGLCallback = this.mOpenGLCallback;
        if (iOnOpenGLCallback != null) {
            iOnOpenGLCallback.onOpenGLDestroy();
        }
    }

    public int onNativeCallback_onOpenGLRunning(double d) {
        StringBuilder sb = new StringBuilder();
        sb.append("onNativeCallback_onOpenGLRunning, timestamp = ");
        sb.append(d);
        VELogUtil.d(TAG, sb.toString());
        IOnOpenGLCallback iOnOpenGLCallback = this.mOpenGLCallback;
        int onOpenGLRunning = iOnOpenGLCallback != null ? iOnOpenGLCallback.onOpenGLRunning() : 0;
        IGetTimestampCallback iGetTimestampCallback = this.mGetTimestampCallback;
        if (iGetTimestampCallback != null) {
            iGetTimestampCallback.getTimestamp(d);
        }
        return onOpenGLRunning;
    }

    public synchronized void onNativeCallback_onShotScreen(int i) {
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("onNativeCallback_onShotScreen: ret = ");
        sb.append(i);
        VELogUtil.i(str, sb.toString());
        this.mIsDuringScreenshot = false;
        if (this.mShotScreenCallback != null) {
            this.mShotScreenCallback.onShotScreen(i);
        }
    }

    public void onNativeCallback_onSlamDetect(boolean z) {
        ArrayList<SlamDetectListener> arrayList = new ArrayList<>();
        synchronized (FaceBeautyInvoker.class) {
            for (SlamDetectListener add : sSlamDetectListeners) {
                arrayList.add(add);
            }
        }
        for (SlamDetectListener slamDetectListener : arrayList) {
            if (slamDetectListener != null) {
                slamDetectListener.onSlam(z);
            }
        }
    }

    public void onNativeRecordStop() {
        RecordStopCallback recordStopCallback = mRecordStopCallback;
        if (recordStopCallback != null) {
            recordStopCallback.onStop();
        }
    }

    public void pauseEffectAudio(boolean z) {
        if (isRenderReady()) {
            long j = this.mHandler;
            if (j == 0) {
                VELogUtil.e(TAG, "invalid handle");
            } else {
                nativePauseEffectAudio(j, z);
            }
        }
    }

    public boolean posInReactionRegion(int i, int i2) {
        long j = this.mHandler;
        if (j == 0) {
            return false;
        }
        return nativePosInReactionRegion(j, i, i2);
    }

    public boolean previewDuetVideo() {
        long j = this.mHandler;
        if (j == 0) {
            return false;
        }
        return nativePreviewDuetVideo(j);
    }

    public int processTouchEvent(float f, float f2) {
        if (!isRenderReady()) {
            return INVALID_ENV;
        }
        long j = this.mHandler;
        if (j != 0) {
            return nativeProcessTouchEvent(j, f, f2);
        }
        VELogUtil.e(TAG, "invalid handle");
        return INVALID_HANDLE;
    }

    public void recoverCherEffect(String[] strArr, double[] dArr, boolean[] zArr) {
        if (isRenderReady()) {
            long j = this.mHandler;
            if (j == 0) {
                VELogUtil.e(TAG, "invalid handle");
            } else {
                nativeRecoverCherEffect(j, strArr, dArr, zArr);
            }
        }
    }

    public void registerCherEffectParamCallback(OnCherEffectParmaCallback onCherEffectParmaCallback) {
        long j = this.mHandler;
        if (j == 0) {
            VELogUtil.e(TAG, "invalid handle");
        } else {
            nativeRegisterCherEffectParamCallback(j, onCherEffectParmaCallback);
        }
    }

    public void registerFaceInfoUpload(boolean z, FaceInfoCallback faceInfoCallback) {
        long j = this.mHandler;
        if (j == 0) {
            VELogUtil.e(TAG, "invalid handle");
        } else {
            nativeRegisterFaceInfoUpload(j, z, faceInfoCallback);
        }
    }

    public void registerHandDetectCallback(int[] iArr, OnHandDetectCallback onHandDetectCallback) {
        long j = this.mHandler;
        if (j == 0) {
            VELogUtil.e(TAG, "invalid handle");
        } else {
            nativeRegisterHandDetectCallback(j, iArr, onHandDetectCallback);
        }
    }

    public void releaseEncoder() {
        AVCEncoder aVCEncoder = this.mAVCEncoder;
        if (aVCEncoder != null) {
            aVCEncoder.releaseEncoder();
        }
    }

    public int renderPicture(ImageFrame imageFrame, int i, int i2, OnPictureCallbackV2 onPictureCallbackV2) {
        if (this.mHandler == 0) {
            return INVALID_HANDLE;
        }
        if (imageFrame.getBuf() != null) {
            return nativeRenderPicture(this.mHandler, imageFrame.getBuf(), imageFrame.getBuf().length, i, i2, onPictureCallbackV2);
        } else if (VERSION.SDK_INT >= 19 && imageFrame.getPlans() != null) {
            Plane[] plans = imageFrame.getPlans();
            if (plans == null) {
                return -1;
            }
            int[][] iArr = (int[][]) Array.newInstance(int.class, new int[]{3, 3});
            ByteBuffer[] byteBufferArr = new ByteBuffer[3];
            for (int i3 = 0; i3 < plans.length; i3++) {
                byteBufferArr[i3] = plans[i3].getBuffer();
                iArr[i3] = new int[]{byteBufferArr[i3].remaining(), plans[i3].getRowStride(), plans[i3].getPixelStride()};
            }
            return nativeRenderPicture2(this.mHandler, byteBufferArr[0], iArr[0], byteBufferArr[1], iArr[1], byteBufferArr[2], iArr[2], imageFrame.getFormat(), imageFrame.getWidth(), imageFrame.getHeight(), i, i2, onPictureCallbackV2);
        } else if (imageFrame.getBitmap() != null) {
            return nativeRenderPicture3(this.mHandler, imageFrame.getBitmap(), i, i2, onPictureCallbackV2);
        } else {
            return nativeRenderPicture(this.mHandler, null, 0, 0, 0, null);
        }
    }

    public void resetPerfStats() {
        long j = this.mHandler;
        if (j != 0) {
            nativeResetPerfStats(j);
        }
    }

    public int resetStartTime(long j, long j2) {
        long j3 = this.mHandler;
        return j3 == 0 ? INVALID_HANDLE : nativeResetStartTime(j3, j, j2);
    }

    public float rotateReactionWindow(float f) {
        long j = this.mHandler;
        if (j == 0) {
            return -100000.0f;
        }
        return nativeRotateReactionWindow(j, f);
    }

    public int save() {
        long j = this.mHandler;
        return j == 0 ? INVALID_HANDLE : nativeSave(j);
    }

    public int[] scaleReactionWindow(float f) {
        long j = this.mHandler;
        if (j == 0) {
            return null;
        }
        return nativeScaleReactionWindow(j, f);
    }

    public void sendEffectMsg(int i, long j, long j2, String str) {
        if (isRenderReady()) {
            long j3 = this.mHandler;
            if (j3 == 0) {
                VELogUtil.e(TAG, "invalid handle");
            } else {
                nativeSendEffectMsg(j3, i, j, j2, str);
            }
        }
    }

    public void setAlgorithmChangeMsg(int i, boolean z) {
        if (isRenderReady()) {
            long j = this.mHandler;
            if (j == 0) {
                VELogUtil.e(TAG, "invalid handle");
            } else {
                nativeSetAlgorithmChangeMsg(j, i, z);
            }
        }
    }

    public int setBGMVolume(float f) {
        long j = this.mHandler;
        return j == 0 ? INVALID_HANDLE : nativeSetBGMVolume(j, f);
    }

    public int setBeautyFace(int i, String str) {
        if (!isRenderReady()) {
            return INVALID_ENV;
        }
        long j = this.mHandler;
        if (j != 0) {
            return nativeSetBeautyFace(j, i, str);
        }
        VELogUtil.e(TAG, "invalid handle");
        return INVALID_HANDLE;
    }

    public void setBeautyFace(int i, String str, float f, float f2) {
        if (isRenderReady()) {
            StringBuilder sb = new StringBuilder();
            sb.append("nativeSetBeautyFace: ");
            sb.append(i);
            String sb2 = sb.toString();
            String str2 = TAG;
            VELogUtil.d(str2, sb2);
            if (this.mHandler == 0) {
                VELogUtil.e(str2, "invalid handle");
                return;
            }
            if (i != 3 || FileUtils.checkFileExists(str)) {
                nativeSetBeautyFace(this.mHandler, i, str);
                nativeSetBeautyFaceIntensity(this.mHandler, f, f2);
            } else {
                nativeSetBeautyFace(this.mHandler, 0, "");
                nativeSetBeautyFaceIntensity(this.mHandler, 0.0f, 0.0f);
            }
        }
    }

    public int setBeautyFaceIntensity(float f, float f2) {
        if (!isRenderReady()) {
            return INVALID_ENV;
        }
        long j = this.mHandler;
        if (j != 0) {
            return nativeSetBeautyFaceIntensity(j, f, f2);
        }
        VELogUtil.e(TAG, "invalid handle");
        return INVALID_HANDLE;
    }

    public int setBlindWaterMarkDiffKeys(int i, int i2) {
        long j = this.mHandler;
        return j == 0 ? INVALID_HANDLE : nativeSetBlindWaterMarkDiffKeys(j, i, i2);
    }

    public int setBlindWaterMarkPosition(int i, int i2) {
        long j = this.mHandler;
        return j == 0 ? INVALID_HANDLE : nativeSetBlindWaterMarkPosition(j, i, i2);
    }

    public void setCameraFirstFrameOptimize(boolean z) {
        if (this.mHandler == 0) {
            LogUtil.e(TAG, "invalid handle");
        }
        nativeSetCameraFirstFrameOptimize(this.mHandler, z);
    }

    public void setCaptureMirror(boolean z) {
        setCaptureMirror(this.mHandler, z);
    }

    public void setDLEEnable(boolean z) {
        long j = this.mHandler;
        if (j == 0) {
            LogUtil.e(TAG, "invalid handle");
        } else {
            nativeSetDLEEnable(j, z);
        }
    }

    public void setDetectInterval(int i) {
        if (isRenderReady()) {
            long j = this.mHandler;
            if (j == 0) {
                VELogUtil.e(TAG, "invalid handle");
            } else {
                nativeSetDetectInterval(j, i);
            }
        }
    }

    public void setDetectionMode(boolean z) {
        long j = this.mHandler;
        if (j != 0) {
            nativeSetDetectionMode(j, z);
        }
    }

    public void setDeviceRotation(float[] fArr) {
        if (isRenderReady()) {
            long j = this.mHandler;
            if (j != 0) {
                nativeSetDeviceRotation(j, fArr);
            }
        }
    }

    public void setDuetCameraPaused(boolean z) {
        long j = this.mHandler;
        if (j != 0) {
            nativeSetDuetCameraPaused(j, z);
        }
    }

    public void setDuetVideoCompleteCallback2(Runnable runnable) {
        this.mDuetCompleteRunable = runnable;
    }

    public void setEffectBuildChainType(int i) {
        long j = this.mHandler;
        if (j != 0) {
            nativeSetEffectBuildChainType(j, i);
        }
    }

    public void setFaceDetectListener2(FaceDetectListener faceDetectListener) {
        this.mFaceDetectListener = faceDetectListener;
    }

    public int setFaceMakeUp(String str) {
        if (!isRenderReady()) {
            return INVALID_ENV;
        }
        long j = this.mHandler;
        if (j != 0) {
            return nativeSetFaceMakeUp2(j, str);
        }
        VELogUtil.e(TAG, "invalid handle");
        return INVALID_HANDLE;
    }

    public int setFaceMakeUp(String str, float f, float f2) {
        if (!isRenderReady()) {
            return INVALID_ENV;
        }
        long j = this.mHandler;
        if (j != 0) {
            return nativeSetFaceMakeUp(j, str, f, f2);
        }
        VELogUtil.e(TAG, "invalid handle");
        return INVALID_HANDLE;
    }

    public int setFilter(String str) {
        if (!isRenderReady()) {
            return INVALID_ENV;
        }
        long j = this.mHandler;
        return j == 0 ? INVALID_HANDLE : nativeSetFilter(j, str, str, 1.0f);
    }

    public int setFilter(String str, String str2, float f) {
        if (!isRenderReady()) {
            return INVALID_ENV;
        }
        long j = this.mHandler;
        return j == 0 ? INVALID_HANDLE : nativeSetFilter(j, str, str2, f);
    }

    public int setFilterIntensity(float f) {
        if (!isRenderReady()) {
            return INVALID_ENV;
        }
        long j = this.mHandler;
        return j == 0 ? INVALID_HANDLE : nativeSetFilterIntensity(j, f);
    }

    @Deprecated
    public int setFilterPos(float f) {
        if (!isRenderReady()) {
            return INVALID_ENV;
        }
        long j = this.mHandler;
        return j == 0 ? INVALID_HANDLE : nativeSetFilterPos(j, f);
    }

    public void setForceAlgorithmCnt(int i) {
        long j = this.mHandler;
        if (j == 0) {
            VELogUtil.e(TAG, "invalid handle");
        } else {
            nativeSetForceAlgorithmCnt(j, i);
        }
    }

    public int setFrameCallback(OnFrameCallback onFrameCallback, boolean z) {
        long j = this.mHandler;
        return j == 0 ? INVALID_HANDLE : nativeSetFrameCallback(j, onFrameCallback, z);
    }

    public void setGetTimestampCallback(IGetTimestampCallback iGetTimestampCallback) {
        this.mGetTimestampCallback = iGetTimestampCallback;
    }

    public int setHandDetectLowpower(boolean z) {
        if (!isRenderReady()) {
            return INVALID_ENV;
        }
        long j = this.mHandler;
        if (j != 0) {
            return nativeSetHandDetectLowpower(j, z);
        }
        VELogUtil.e(TAG, "invalid handle");
        return INVALID_HANDLE;
    }

    public int setHardEncoderStatus(boolean z) {
        long j = this.mHandler;
        return j == 0 ? INVALID_HANDLE : nativeSetHardEncoderStatus(j, z);
    }

    public int setIntensityByType(int i, float f) {
        if (!isRenderReady()) {
            return INVALID_ENV;
        }
        long j = this.mHandler;
        if (j != 0) {
            return nativeSetIntensityByType(j, i, f);
        }
        VELogUtil.e(TAG, "invalid handle");
        return INVALID_HANDLE;
    }

    public synchronized void setModeChangeState(int i) {
        if (this.mHandler != 0) {
            nativeSetModeChangeState(this.mHandler, i);
        }
    }

    public int setMusicNodes(String str) {
        if (!isRenderReady()) {
            return INVALID_ENV;
        }
        long j = this.mHandler;
        if (j != 0) {
            return nativeSetMusicNodes(j, str);
        }
        VELogUtil.e(TAG, "invalid handle");
        return INVALID_HANDLE;
    }

    public synchronized int setMusicTime(long j, long j2) {
        if (this.mHandler == 0) {
            return INVALID_HANDLE;
        }
        return nativeSetMusicTime(this.mHandler, j, j2);
    }

    public void setNativeInitListener2(NativeInitListener nativeInitListener) {
        this.mNativeInitListener = nativeInitListener;
    }

    public void setNativeLibraryDir(String str) {
        if (!TextUtils.isEmpty(str)) {
            nativeSetNativeLibraryDir(str);
        }
    }

    public void setOnOpenGLCallback(IOnOpenGLCallback iOnOpenGLCallback) {
        this.mOpenGLCallback = iOnOpenGLCallback;
    }

    public int setPlayLength(long j) {
        long j2 = this.mHandler;
        return j2 == 0 ? INVALID_HANDLE : nativeSetPlayLength(j2, j);
    }

    public void setPreviewDuetVideoPaused(boolean z) {
        long j = this.mHandler;
        if (j != 0) {
            nativeSetPreviewDuetVideoPaused(j, z);
        }
    }

    public void setPreviewSizeRatio(float f) {
        long j = this.mHandler;
        if (j != 0) {
            nativeSetPreviewSizeRatio(j, f);
        }
    }

    public void setReactionBorderParam(int i, int i2) {
        long j = this.mHandler;
        if (j != 0) {
            nativeSetReactionBorderParam(j, i, i2);
        }
    }

    public boolean setReactionMaskImage(String str, boolean z) {
        long j = this.mHandler;
        if (j == 0) {
            return false;
        }
        return nativeSetReactionMaskImage(j, str, z);
    }

    public void setReactionPosMargin(int i, int i2, int i3, int i4) {
        long j = this.mHandler;
        if (j != 0) {
            nativeSetReactionPosMargin(j, i, i2, i3, i4);
        }
    }

    public void setRenderCacheTexture(String str, String str2) {
        long j = this.mHandler;
        if (j == 0) {
            VELogUtil.e(TAG, "invalid handle");
        } else {
            nativeSetRenderCacheTexture(j, str, str2);
        }
    }

    public int setReshape(String str, float f, float f2) {
        if (!isRenderReady()) {
            return INVALID_ENV;
        }
        long j = this.mHandler;
        if (j != 0) {
            return nativeSetReshape(j, str, f, f2);
        }
        VELogUtil.e(TAG, "invalid handle");
        return INVALID_HANDLE;
    }

    public void setRunningErrorCallback(OnRunningErrorCallback onRunningErrorCallback) {
        long j = this.mHandler;
        if (j != 0) {
            nativeSetRunningErrorCallback(j, onRunningErrorCallback);
        }
    }

    public boolean setSharedTextureStatus(boolean z) {
        if (!isRenderReady()) {
            return false;
        }
        if (this.mHandler != 0) {
            return nativeSetSharedTextureStatus(z);
        }
        VELogUtil.e(TAG, "invalid handle");
        return false;
    }

    public int setSkinTone(String str) {
        if (!isRenderReady()) {
            return INVALID_ENV;
        }
        long j = this.mHandler;
        if (j != 0) {
            return nativeSetSkinTone(j, str);
        }
        VELogUtil.e(TAG, "invalid handle");
        return INVALID_HANDLE;
    }

    public int setSlamFace(Bitmap bitmap) {
        if (!isRenderReady()) {
            return INVALID_ENV;
        }
        long j = this.mHandler;
        if (j != 0) {
            return nativeSetSlamFace(j, bitmap);
        }
        VELogUtil.e(TAG, "invalid handle");
        return INVALID_HANDLE;
    }

    public int setSticker(Bitmap bitmap, int i, int i2) {
        long j = this.mHandler;
        return j == 0 ? INVALID_HANDLE : nativeSetSticker(j, bitmap, i, i2);
    }

    public int setStickerPath(String str, int i, int i2, boolean z) {
        if (!isRenderReady()) {
            return INVALID_ENV;
        }
        long j = this.mHandler;
        if (j != 0) {
            return nativeSetStickerPath(j, str, i, i2, z);
        }
        VELogUtil.e(TAG, "invalid handle");
        return INVALID_HANDLE;
    }

    public int setStickerRequestCallback(IStickerRequestCallback iStickerRequestCallback) {
        long j = this.mHandler;
        return j == 0 ? INVALID_HANDLE : nativeSetStickerRequestCallback(j, iStickerRequestCallback);
    }

    public void setSwapDuetRegion(boolean z) {
        long j = this.mHandler;
        if (j != 0) {
            nativeSetSwapDuetRegion(j, z);
        }
    }

    public void setTextureTimeListener(TextureTimeListener textureTimeListener) {
        this.mTextureTimeListener = textureTimeListener;
    }

    public synchronized void setUseMusic(int i) {
        if (this.mHandler != 0) {
            nativeSetUseMusic(this.mHandler, i);
        }
    }

    public int setVideoQuality(int i, int i2) {
        long j = this.mHandler;
        return j == 0 ? INVALID_HANDLE : nativeSetVideoQuality(j, i, i2);
    }

    public synchronized int shotScreen(String str, int[] iArr, boolean z, int i, OnPictureCallback onPictureCallback, IShotScreenCallback iShotScreenCallback) {
        if (!this.mIsDuringScreenshot) {
            this.mIsDuringScreenshot = true;
            this.mShotScreenCallback = iShotScreenCallback;
            if (this.mHandler == 0) {
                return INVALID_HANDLE;
            }
            return nativeShotScreen(this.mHandler, str, iArr, z, i, onPictureCallback);
        }
        VELogUtil.w(TAG, "Last screenshot not complete");
        iShotScreenCallback.onShotScreen(-1);
        return -1;
    }

    public int slamDeviceConfig(boolean z, boolean z2, boolean z3, boolean z4) {
        if (!isRenderReady()) {
            return INVALID_ENV;
        }
        long j = this.mHandler;
        if (j != 0) {
            return nativeSlamDeviceConfig(j, z, z2, z3, z4);
        }
        VELogUtil.e(TAG, "invalid handle");
        return INVALID_HANDLE;
    }

    public int slamGetTextBitmap(OnARTextBitmapCallback onARTextBitmapCallback) {
        if (!isRenderReady()) {
            return INVALID_ENV;
        }
        long j = this.mHandler;
        if (j != 0) {
            return nativeSlamGetTextBitmap(j, onARTextBitmapCallback);
        }
        LogUtil.e(TAG, "invalid handle");
        return INVALID_HANDLE;
    }

    public int slamGetTextLimitCount(OnARTextCountCallback onARTextCountCallback) {
        if (!isRenderReady()) {
            return INVALID_ENV;
        }
        long j = this.mHandler;
        if (j != 0) {
            return nativeSlamGetTextLimitCount(j, onARTextCountCallback);
        }
        VELogUtil.e(TAG, "invalid handle");
        return INVALID_HANDLE;
    }

    public int slamGetTextParagraphContent(OnARTextContentCallback onARTextContentCallback) {
        if (!isRenderReady()) {
            return INVALID_ENV;
        }
        long j = this.mHandler;
        if (j != 0) {
            return nativeSlamGetTextParagraphContent(j, onARTextContentCallback);
        }
        VELogUtil.e(TAG, "invalid handle");
        return INVALID_HANDLE;
    }

    public int slamNotifyHideKeyBoard(boolean z) {
        if (!isRenderReady()) {
            return INVALID_ENV;
        }
        long j = this.mHandler;
        if (j != 0) {
            return nativeHideSlamKeyBoard(j, z);
        }
        VELogUtil.e(TAG, "invalid handle");
        return INVALID_HANDLE;
    }

    public int slamProcessDoubleClickEvent(float f, float f2) {
        if (!isRenderReady()) {
            return INVALID_ENV;
        }
        long j = this.mHandler;
        if (j != 0) {
            return nativeSlamProcessDoubleClickEvent(j, f, f2);
        }
        VELogUtil.e(TAG, "invalid handle");
        return INVALID_HANDLE;
    }

    public int slamProcessIngestAcc(double d, double d2, double d3, double d4) {
        if (!isRenderReady()) {
            return INVALID_ENV;
        }
        long j = this.mHandler;
        if (j != 0) {
            return nativeSlamProcessIngestAcc(j, d, d2, d3, d4);
        }
        VELogUtil.e(TAG, "invalid handle");
        return INVALID_HANDLE;
    }

    public int slamProcessIngestGra(double d, double d2, double d3, double d4) {
        if (!isRenderReady()) {
            return INVALID_ENV;
        }
        long j = this.mHandler;
        if (j != 0) {
            return nativeSlamProcessIngestGra(j, d, d2, d3, d4);
        }
        VELogUtil.e(TAG, "invalid handle");
        return INVALID_HANDLE;
    }

    public int slamProcessIngestGyr(double d, double d2, double d3, double d4) {
        if (!isRenderReady()) {
            return INVALID_ENV;
        }
        long j = this.mHandler;
        if (j != 0) {
            return nativeSlamProcessIngestGyr(j, d, d2, d3, d4);
        }
        VELogUtil.e(TAG, "invalid handle");
        return INVALID_HANDLE;
    }

    public int slamProcessIngestOri(double[] dArr, double d) {
        if (!isRenderReady()) {
            return INVALID_ENV;
        }
        long j = this.mHandler;
        if (j != 0) {
            return nativeSlamProcessIngestOri(j, dArr, d);
        }
        VELogUtil.e(TAG, "invalid handle");
        return INVALID_HANDLE;
    }

    public int slamProcessPanEvent(float f, float f2, float f3, float f4, float f5) {
        if (!isRenderReady()) {
            return INVALID_ENV;
        }
        long j = this.mHandler;
        if (j != 0) {
            return nativeSlamProcessPanEvent(j, f, f2, f3, f4, f5);
        }
        VELogUtil.e(TAG, "invalid handle");
        return INVALID_HANDLE;
    }

    public int slamProcessRotationEvent(float f, float f2) {
        if (!isRenderReady()) {
            return INVALID_ENV;
        }
        long j = this.mHandler;
        if (j != 0) {
            return nativeSlamProcessRotationEvent(j, f, f2);
        }
        VELogUtil.e(TAG, "invalid handle");
        return INVALID_HANDLE;
    }

    public int slamProcessScaleEvent(float f, float f2) {
        if (!isRenderReady()) {
            return INVALID_ENV;
        }
        long j = this.mHandler;
        if (j != 0) {
            return nativeSlamProcessScaleEvent(j, f, f2);
        }
        VELogUtil.e(TAG, "invalid handle");
        return INVALID_HANDLE;
    }

    public int slamProcessTouchEvent(float f, float f2) {
        if (!isRenderReady()) {
            return INVALID_ENV;
        }
        long j = this.mHandler;
        if (j != 0) {
            return nativeSlamProcessTouchEvent(j, f, f2);
        }
        VELogUtil.e(TAG, "invalid handle");
        return INVALID_HANDLE;
    }

    public int slamProcessTouchEventByType(int i, float f, float f2, int i2) {
        if (!isRenderReady()) {
            return INVALID_ENV;
        }
        long j = this.mHandler;
        if (j != 0) {
            return nativeSlamProcessTouchEventByType(j, i, f, f2, i2);
        }
        VELogUtil.e(TAG, "invalid handle");
        return INVALID_HANDLE;
    }

    public int slamSetInputText(String str, int i, int i2, String str2) {
        if (!isRenderReady()) {
            return INVALID_ENV;
        }
        long j = this.mHandler;
        if (j != 0) {
            return nativeSetSlamInputText(j, str, i, i2, str2);
        }
        VELogUtil.e(TAG, "invalid handle");
        return INVALID_HANDLE;
    }

    public int slamSetLanguge(String str) {
        if (!isRenderReady()) {
            return INVALID_ENV;
        }
        long j = this.mHandler;
        if (j != 0) {
            return nativeSlamSetLanguge(j, str);
        }
        VELogUtil.e(TAG, "invalid handle");
        return INVALID_HANDLE;
    }

    public int slamSetTextBitmapResult(Bitmap bitmap, int i, int i2, int i3) {
        if (!isRenderReady()) {
            return INVALID_ENV;
        }
        long j = this.mHandler;
        if (j != 0) {
            return nativeSlamSetTextBitmapResult(j, bitmap, i, i2, i3);
        }
        LogUtil.e(TAG, "invalid handle");
        return INVALID_HANDLE;
    }

    public int startPlay(Surface surface, String str, int i, int i2) {
        this.mIsRenderRady = false;
        long j = this.mHandler;
        return j == 0 ? INVALID_HANDLE : nativeStartPlay(j, surface, i, i2, str);
    }

    public int startRecord(double d, boolean z, float f, int i, int i2) {
        int i3;
        FaceBeautyInvoker faceBeautyInvoker;
        int i4 = (int) (((float) 4000000) * f);
        if (i4 > 12000000) {
            faceBeautyInvoker = this;
            i3 = 12000000;
        } else {
            i3 = i4;
            faceBeautyInvoker = this;
        }
        long j = faceBeautyInvoker.mHandler;
        return j == 0 ? INVALID_HANDLE : nativeStartRecord(j, d, z, i3, i, i2);
    }

    public int stopPlay() {
        this.mIsRenderRady = false;
        long j = this.mHandler;
        return j == 0 ? INVALID_HANDLE : nativeStopPlay(j);
    }

    public int stopRecord(boolean z) {
        long j = this.mHandler;
        return j == 0 ? INVALID_HANDLE : nativeStopRecord(j, z);
    }

    public int tryRestore(int i, String str) {
        long j = this.mHandler;
        return j == 0 ? INVALID_HANDLE : nativeTryRestore(j, i, str);
    }

    public void unRegisterFaceInfoUpload() {
        long j = this.mHandler;
        if (j == 0) {
            VELogUtil.e(TAG, "invalid handle");
        } else {
            nativeUnRegisterFaceInfoUpload(j);
        }
    }

    public void uninitAudioPlayer() {
        long j = this.mHandler;
        if (j != 0) {
            nativeUninitAudioPlayer(j);
        }
    }

    public int uninitFaceBeautyPlay() {
        int nativeUninitFaceBeautyPlay;
        destroyMessageCenter();
        if (this.mHandler == 0) {
            return INVALID_HANDLE;
        }
        synchronized (this) {
            long j = this.mHandler;
            this.mHandler = 0;
            this.mTextureTimeListener = null;
            this.mShotScreenCallback = null;
            mRecordStopCallback = null;
            this.mNativeInitListener = null;
            this.mFaceDetectListener = null;
            nativeUninitFaceBeautyPlay = nativeUninitFaceBeautyPlay(j);
        }
        return nativeUninitFaceBeautyPlay;
    }

    public void updateReactionBGAlpha(float f) {
        long j = this.mHandler;
        if (j != 0) {
            nativeUpdateReactionBGAlpha(j, f);
        }
    }

    public int[] updateReactionCameraPos(int i, int i2, int i3, int i4) {
        long j = this.mHandler;
        if (j == 0) {
            return null;
        }
        return nativeUpdateReactionCameraPos(j, i, i2, i3, i4);
    }

    public int[] updateReactionCameraPosWithRotation(int i, int i2, int i3, int i4, float f) {
        long j = this.mHandler;
        if (j == 0) {
            return null;
        }
        return nativeUpdateReactionCameraPosWithRotation(j, i, i2, i3, i4, f);
    }

    public void updateRotation(float f, float f2, float f3) {
        long j = this.mHandler;
        if (j != 0) {
            nativeUpdateRotation(j, f, f2, f3);
        }
    }

    public void updateRotation(int i, boolean z) {
        long j = this.mHandler;
        if (j != 0) {
            nativeUpdateRotationAndFront(j, i, z);
        }
    }

    public void useLargeMattingModel(boolean z) {
        if (isRenderReady()) {
            long j = this.mHandler;
            if (j == 0) {
                VELogUtil.e(TAG, "invalid handle");
            } else {
                nativeUseLargeMattingModel(j, z);
            }
        }
    }

    public int writeFile(ByteBuffer byteBuffer, int i, int i2, int i3) {
        long j = this.mHandler;
        return j == 0 ? INVALID_HANDLE : nativeWriteFile(j, byteBuffer, i, i2, i3);
    }
}
