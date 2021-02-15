package com.xiaomi.fenshen;

import android.app.Activity;
import android.media.Image;
import android.text.TextUtils;
import android.util.Log;
import com.android.camera.data.DataRepository;
import com.android.camera.module.impl.component.MultiFeatureManagerImpl;
import com.xiaomi.camera.util.SystemProperties;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

public class FenShenCam {
    public static boolean DEBUG = false;
    private static boolean IS_MTK_PLATFORM = true;
    public static final int MAX_PHOTO_SUBJECT_COUNT = 4;
    public static final int MAX_VIDEO_SUBJECT_COUNT = 2;
    private static final String TAG = "CloneSDK";
    private static Listener listener;
    public static boolean sIsEdit;
    private static volatile boolean sReleased;

    public interface Listener {
        void onMessage(Message message);

        void onPhotoResult(byte[] bArr);

        void onStartPreview();

        void onStopPreview();

        void onStopRecord();

        void onSubjectCount(int i);

        void onVideoSaved(int i);

        void requestRender();
    }

    public enum Message {
        START,
        PREVIEW_NO_PERSON,
        ALIGN_OK,
        ALIGN_WARNING,
        ALIGN_TOO_LARGE_OR_FAILED,
        NO_PERSON,
        MOVE_OUTSIDE,
        DYNAMIC_SCENE,
        ERROR_INIT,
        ERROR_RUNTIME,
        PREVIEW_PERSON,
        SAVE_VIDEO_SUCCESS,
        EDIT_DONE,
        PAUSED,
        NOPERSON_INCLICKPOS
    }

    public enum Mode {
        PHOTO,
        VIDEO,
        MCOPY,
        TIMEFREEZE
    }

    public enum TEventType {
        CLICK_UP,
        CLICK_DOWN,
        GENERIC_UP,
        DRAG,
        SCALE
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

    static {
        Runtime runtime;
        String str;
        String str2 = TAG;
        DEBUG = Log.isLoggable(str2, 3);
        try {
            String str3 = SystemProperties.get("ro.board.platform");
            StringBuilder sb = new StringBuilder();
            sb.append("platform is ");
            sb.append(str3);
            com.android.camera.log.Log.i(str2, sb.toString());
            if (!TextUtils.isEmpty(str3) && str3.startsWith("mt")) {
            }
            Runtime.getRuntime().loadLibrary("clone_c++_shared");
            if (IS_MTK_PLATFORM) {
                runtime = Runtime.getRuntime();
                str = "fenshen_apu";
            } else {
                runtime = Runtime.getRuntime();
                str = "fenshen_snpe";
            }
            runtime.loadLibrary(str);
        } catch (Throwable th) {
            StringBuilder sb2 = new StringBuilder();
            sb2.append("Load native library failed: ");
            sb2.append(th.toString());
            com.android.camera.log.Log.e(str2, sb2.toString());
        }
    }

    public static void addPhoto(Image image) {
        int nativeGetCurrentSubjectCount = nativeGetCurrentSubjectCount();
        String str = TAG;
        if (nativeGetCurrentSubjectCount >= 4) {
            com.android.camera.log.Log.i(str, "AddPhoto: reached max subjects count, ignore");
        } else {
            StringBuilder sb = new StringBuilder();
            sb.append("addPhoto ");
            sb.append(image);
            sb.append(", ");
            sb.append(image.getWidth());
            sb.append("x");
            sb.append(image.getHeight());
            com.android.camera.log.Log.d(str, sb.toString());
            long timestamp = image.getTimestamp();
            if (image.getFormat() == 35) {
                nativeAddPhoto(timestamp, image.getPlanes()[0].getBuffer(), image.getPlanes()[0].getRowStride(), image.getPlanes()[1].getBuffer(), image.getPlanes()[1].getRowStride(), image.getPlanes()[2].getBuffer(), image.getPlanes()[2].getRowStride(), image.getWidth(), image.getHeight());
            } else {
                throw new RuntimeException("illegal image format, expect YUV image");
            }
        }
        Listener listener2 = listener;
        if (listener2 != null) {
            listener2.requestRender();
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:19:0x00aa, code lost:
        return;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static synchronized void addPreviewFrame(Image image) {
        synchronized (FenShenCam.class) {
            if (sReleased) {
                com.android.camera.log.Log.i(TAG, "ignore render, released");
                return;
            }
            if (DEBUG) {
                String str = TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("addPreviewFrame ");
                sb.append(image);
                sb.append(", ");
                sb.append(image.getWidth());
                sb.append("x");
                sb.append(image.getHeight());
                com.android.camera.log.Log.d(str, sb.toString());
            }
            long timestamp = image.getTimestamp();
            if (image.getFormat() == 35) {
                nativeAddPreviewFrame(timestamp, image.getPlanes()[0].getBuffer(), image.getPlanes()[0].getRowStride(), image.getPlanes()[1].getBuffer(), image.getPlanes()[1].getRowStride(), image.getPlanes()[2].getBuffer(), image.getPlanes()[2].getRowStride(), image.getWidth(), image.getHeight());
                if (listener != null) {
                    listener.requestRender();
                } else {
                    com.android.camera.log.Log.w(TAG, "addPreviewFrame, can't requestRender since listener is null");
                }
            } else {
                throw new RuntimeException("illegal image format, expect YUV image");
            }
        }
    }

    public static void cancelEdit() {
        com.android.camera.log.Log.d(TAG, "cancelEdit");
        nativeCancelEdit();
        Listener listener2 = listener;
        if (listener2 != null) {
            listener2.requestRender();
        }
        sIsEdit = false;
    }

    public static void cancelPhoto() {
        com.android.camera.log.Log.d(TAG, "cancelPhoto");
        nativeCancelPhoto();
        Listener listener2 = listener;
        if (listener2 != null) {
            listener2.requestRender();
        }
    }

    public static void cancelVideo() {
        com.android.camera.log.Log.d(TAG, "cancelVideo");
        nativeCancelVideo();
        Listener listener2 = listener;
        if (listener2 != null) {
            listener2.requestRender();
        }
    }

    public static void editMultiCopy() {
        com.android.camera.log.Log.d(TAG, "editMultiCopy");
        nativeEditMultiCopy();
        sIsEdit = true;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:28:0x0065, code lost:
        r0 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:30:?, code lost:
        $closeResource(r6, r5);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:31:0x0069, code lost:
        throw r0;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:35:0x006c, code lost:
        r6 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:36:0x006d, code lost:
        if (r4 != null) goto L_0x006f;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:38:?, code lost:
        $closeResource(r5, r4);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:39:0x0072, code lost:
        throw r6;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static void extractRawTo(Activity activity, String str, File file) {
        String cloneModelVersion = CloneUtil.getCloneModelVersion(IS_MTK_PLATFORM);
        boolean exists = file.exists();
        String str2 = TAG;
        if (!exists || !DataRepository.dataItemGlobal().matchCloneModelVersion(cloneModelVersion)) {
            StringBuilder sb = new StringBuilder();
            sb.append("extractRawTo ");
            sb.append(file.getAbsolutePath());
            com.android.camera.log.Log.d(str2, sb.toString());
            try {
                InputStream open = MultiFeatureManagerImpl.getAssetManager(activity).open(str);
                FileOutputStream fileOutputStream = new FileOutputStream(file);
                byte[] bArr = new byte[10240];
                while (true) {
                    int read = open.read(bArr);
                    if (read <= 0) {
                        break;
                    }
                    fileOutputStream.write(bArr, 0, read);
                }
                DataRepository.dataItemGlobal().updateCloneModelVersion(cloneModelVersion);
                $closeResource(null, fileOutputStream);
                if (open != null) {
                    $closeResource(null, open);
                }
            } catch (IOException e) {
                throw new RuntimeException("extract raw file failed", e);
            }
        } else {
            com.android.camera.log.Log.d(str2, "ignore extractRawTo, file exists and matched");
        }
    }

    public static void finishPhoto() {
        nativeFinishPhoto();
    }

    public static int getCurrentSubjectCount() {
        return nativeGetCurrentSubjectCount();
    }

    public static void init(int i, int i2, String str, String str2) {
        sReleased = false;
        sIsEdit = false;
        StringBuilder sb = new StringBuilder();
        sb.append("init previewWidth ");
        sb.append(i);
        sb.append(", previewHeight = ");
        sb.append(i2);
        com.android.camera.log.Log.d(TAG, sb.toString());
        nativeInit(i, i2, str, str2);
    }

    public static void initResources(Activity activity) {
        String str = TAG;
        com.android.camera.log.Log.d(str, "initResources E");
        File filesDir = activity.getFilesDir();
        filesDir.mkdirs();
        if (IS_MTK_PLATFORM) {
            String str2 = "photo_b384_384_version1_apu_20200508.bin";
            extractRawTo(activity, str2, new File(filesDir, str2));
        } else {
            extractRawTo(activity, "xseg2_dlc", new File(filesDir, "xseg2.dlc"));
        }
        com.android.camera.log.Log.d(str, "initResources X");
    }

    public static boolean isPlaying() {
        return nativeIsPlaying();
    }

    private static native void nativeAddPhoto(long j, ByteBuffer byteBuffer, int i, ByteBuffer byteBuffer2, int i2, ByteBuffer byteBuffer3, int i3, int i4, int i5);

    private static native void nativeAddPreviewFrame(long j, ByteBuffer byteBuffer, int i, ByteBuffer byteBuffer2, int i2, ByteBuffer byteBuffer3, int i3, int i4, int i5);

    private static native void nativeCancelEdit();

    private static native void nativeCancelPhoto();

    private static native void nativeCancelVideo();

    private static native void nativeEditMultiCopy();

    private static native void nativeFinishPhoto();

    private static native int nativeGetCurrentSubjectCount();

    private static native byte[] nativeGetResultJpeg();

    private static native int nativeInit(int i, int i2, String str, String str2);

    private static native boolean nativeIsPlaying();

    private static native void nativePausePlayEffect();

    private static native void nativePlayPreview();

    private static native String nativePullCmd();

    private static native void nativeRelease();

    private static native int nativeRender();

    private static native int nativeRenderInit(int i, int i2, int i3, int i4, int i5, int i6);

    private static native void nativeResetEdit();

    private static native void nativeResumePlayEffect();

    private static native void nativeSaveEdit();

    private static native void nativeSaveVideo(String str);

    public static native void nativeSendTouchEvent(int i, float f, float f2, float f3, float f4, float f5);

    private static native void nativeSetFilmFormat(boolean z);

    private static native void nativeSetMode(int i);

    private static native void nativeSetPhotoQuality(int i);

    private static native void nativeSetVideoCodec(String str);

    private static native void nativeStart();

    private static native void nativeStartRecordVideo();

    private static native void nativeStartTimeFreeze();

    private static native void nativeStopRecordVideo();

    private static native void nativeStopTimeFreeze();

    public static void pausePlayEffect() {
        nativePausePlayEffect();
    }

    public static void playPreview() {
        nativePlayPreview();
        Listener listener2 = listener;
        if (listener2 != null) {
            listener2.requestRender();
        }
    }

    public static synchronized void release() {
        synchronized (FenShenCam.class) {
            if (sReleased) {
                com.android.camera.log.Log.w(TAG, "ignore release twice");
                return;
            }
            com.android.camera.log.Log.d(TAG, "release");
            sReleased = true;
            nativeRelease();
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:27:0x005a, code lost:
        return;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static synchronized void render() {
        Listener listener2;
        Message message;
        synchronized (FenShenCam.class) {
            if (sReleased) {
                com.android.camera.log.Log.i(TAG, "ignore render, released");
                return;
            }
            nativeRender();
            if (DEBUG) {
                com.android.camera.log.Log.d(TAG, "nativeRender ");
            }
            if (listener != null) {
                listener.onSubjectCount(nativeGetCurrentSubjectCount());
            }
            while (true) {
                String nativePullCmd = nativePullCmd();
                if (nativePullCmd == null) {
                    break;
                }
                if (DEBUG) {
                    String str = TAG;
                    StringBuilder sb = new StringBuilder();
                    sb.append("render cmd ");
                    sb.append(nativePullCmd);
                    com.android.camera.log.Log.d(str, sb.toString());
                }
                if (listener != null) {
                    if (nativePullCmd.equals("dexcam_nullptr")) {
                        break;
                    } else if (nativePullCmd.equals("request_render")) {
                        listener.requestRender();
                    } else if (nativePullCmd.equals("start_preview")) {
                        listener.onStartPreview();
                    } else if (nativePullCmd.equals("stop_preview")) {
                        listener.onStopPreview();
                    } else if (nativePullCmd.equals("stop_record")) {
                        listener.onStopRecord();
                    } else if (nativePullCmd.equals("jpg_available")) {
                        listener.onPhotoResult(nativeGetResultJpeg());
                    } else if (nativePullCmd.startsWith("video_saved ")) {
                        listener.onVideoSaved(Integer.parseInt(nativePullCmd.substring(12)));
                    } else {
                        if (nativePullCmd.equals("msg_start")) {
                            listener2 = listener;
                            message = Message.START;
                        } else if (nativePullCmd.equals("align_ok")) {
                            listener2 = listener;
                            message = Message.ALIGN_OK;
                        } else if (nativePullCmd.equals("align_warning")) {
                            listener2 = listener;
                            message = Message.ALIGN_WARNING;
                        } else if (nativePullCmd.equals("align_fail")) {
                            listener2 = listener;
                            message = Message.ALIGN_TOO_LARGE_OR_FAILED;
                        } else if (nativePullCmd.equals("preview_no_person")) {
                            listener2 = listener;
                            message = Message.PREVIEW_NO_PERSON;
                        } else if (nativePullCmd.equals("no_person")) {
                            listener2 = listener;
                            message = Message.NO_PERSON;
                        } else if (nativePullCmd.equals("move_outside")) {
                            listener2 = listener;
                            message = Message.MOVE_OUTSIDE;
                        } else if (nativePullCmd.equals("dynamic_scene")) {
                            listener2 = listener;
                            message = Message.DYNAMIC_SCENE;
                        } else if (nativePullCmd.equals("init_error")) {
                            listener2 = listener;
                            message = Message.ERROR_INIT;
                        } else if (nativePullCmd.equals("runtime_error")) {
                            listener2 = listener;
                            message = Message.ERROR_RUNTIME;
                        } else if (nativePullCmd.equals("success_target_detect")) {
                            listener2 = listener;
                            message = Message.PREVIEW_PERSON;
                        } else if (nativePullCmd.equals("failed_target_detect")) {
                            listener2 = listener;
                            message = Message.PREVIEW_NO_PERSON;
                        } else if (nativePullCmd.equals("save_video_success")) {
                            listener2 = listener;
                            message = Message.SAVE_VIDEO_SUCCESS;
                        } else if (nativePullCmd.equals("edit_done")) {
                            listener2 = listener;
                            message = Message.EDIT_DONE;
                        } else if (nativePullCmd.equals("paused")) {
                            listener2 = listener;
                            message = Message.PAUSED;
                        } else if (nativePullCmd.equals("noperson_inclickpos")) {
                            listener2 = listener;
                            message = Message.NOPERSON_INCLICKPOS;
                        }
                        listener2.onMessage(message);
                    }
                }
            }
        }
    }

    public static void renderInit(int i, int i2, int i3, int i4, int i5, int i6) {
        StringBuilder sb = new StringBuilder();
        sb.append("renderInit screenW ");
        sb.append(i);
        sb.append(", screenH ");
        sb.append(i2);
        sb.append(", drawX = ");
        sb.append(i3);
        sb.append(", drawY = ");
        sb.append(i4);
        sb.append(", drawW = ");
        sb.append(i5);
        sb.append(", drawH = ");
        sb.append(i6);
        com.android.camera.log.Log.d(TAG, sb.toString());
        nativeRenderInit(i, i2, i3, i4, i5, i6);
    }

    public static void resetEdit() {
        com.android.camera.log.Log.d(TAG, "resetEdit");
        nativeResetEdit();
        Listener listener2 = listener;
        if (listener2 != null) {
            listener2.requestRender();
        }
    }

    public static void resumePlayEffect() {
        nativeResumePlayEffect();
    }

    public static void saveEdit() {
        com.android.camera.log.Log.d(TAG, "saveEdit");
        nativeSaveEdit();
        Listener listener2 = listener;
        if (listener2 != null) {
            listener2.requestRender();
        }
        sIsEdit = false;
    }

    public static void saveVideo(String str) {
        com.android.camera.log.Log.d(TAG, "saveVideo");
        nativeSaveVideo(str);
        Listener listener2 = listener;
        if (listener2 != null) {
            listener2.requestRender();
        }
    }

    public static void sendTouchEvent(TEventType tEventType, float f, float f2, float f3, float f4, float f5) {
        nativeSendTouchEvent(tEventType.ordinal(), f, f2, f3, f4, f5);
    }

    public static void setFilmFormat(boolean z) {
        nativeSetFilmFormat(z);
    }

    public static void setListener(Listener listener2) {
        StringBuilder sb = new StringBuilder();
        sb.append("setListener ");
        sb.append(listener2);
        com.android.camera.log.Log.d(TAG, sb.toString());
        listener = listener2;
    }

    public static void setMode(Mode mode) {
        StringBuilder sb = new StringBuilder();
        sb.append("setMode ");
        sb.append(mode.name());
        com.android.camera.log.Log.d(TAG, sb.toString());
        nativeSetMode(mode.ordinal());
    }

    public static void setPhotoQuality(int i) {
        nativeSetPhotoQuality(i);
    }

    public static void setVideoCodec(String str) {
        nativeSetVideoCodec(str);
    }

    public static void start() {
        com.android.camera.log.Log.d(TAG, "start");
        nativeStart();
    }

    public static void startRecordVideo() {
        com.android.camera.log.Log.d(TAG, "startRecordVideo");
        nativeStartRecordVideo();
    }

    public static void startTimeFreeze() {
        nativeStartTimeFreeze();
    }

    public static void stopRecordVideo() {
        com.android.camera.log.Log.d(TAG, "stopRecordVideo");
        nativeStopRecordVideo();
    }

    public static void stopTimeFreeze() {
        nativeStopTimeFreeze();
    }
}
