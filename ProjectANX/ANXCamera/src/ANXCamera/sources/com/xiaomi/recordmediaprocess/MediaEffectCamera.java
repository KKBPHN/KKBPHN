package com.xiaomi.recordmediaprocess;

import android.media.Image;
import android.media.Image.Plane;
import android.util.Log;
import java.nio.ByteBuffer;
import javax.microedition.khronos.egl.EGLContext;
import miui.text.ExtraTextUtils;

public class MediaEffectCamera {
    private static String TAG = "MediaEffectCamera";
    private long mMediaFilterCamera = 0;
    EffectCameraNotifier mMediaFilterCameraNotify = null;

    public MediaEffectCamera() {
        Log.d(TAG, "construct MediaEffectCamera");
    }

    private static native void CancelRecordingJni(long j);

    private static native long ConstructMediaEffectCameraJni(EGLContext eGLContext, int i, int i2, int i3, int i4, EffectCameraNotifier effectCameraNotifier);

    private static native void DestructMediaEffectCameraJni(long j);

    private static native int GetRecordingStatusJni(long j);

    private static native void NeedProcessTextureJni(long j, long j2, int i, int i2);

    private static native void PauseRecordingJni(long j);

    private static native void PushExtraYAndUVFrameJni(long j, ByteBuffer byteBuffer, ByteBuffer byteBuffer2, int i, int i2, long j2);

    private static native void ResumeRecordingJni(long j);

    private static native void SetOrientationJni(long j, int i);

    private static native void StartRecordingJni(long j, int i, String str, String str2, String str3, String str4, long j2, float f, long j3);

    private static native void StopRecordingJni(long j);

    public static String Version() {
        return VersionJni();
    }

    private static native String VersionJni();

    public void CancelRecording() {
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("Cancel MediaFilterCamera: ");
        sb.append(this.mMediaFilterCamera);
        Log.d(str, sb.toString());
        CancelRecordingJni(this.mMediaFilterCamera);
    }

    public void ConstructMediaEffectCamera(int i, int i2, int i3, int i4, EffectCameraNotifier effectCameraNotifier) {
        this.mMediaFilterCameraNotify = effectCameraNotifier;
        this.mMediaFilterCamera = ConstructMediaEffectCameraJni(null, i, i2, i3, i4, this.mMediaFilterCameraNotify);
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("construct MediaFilterCamera: ");
        sb.append(this.mMediaFilterCamera);
        Log.d(str, sb.toString());
    }

    public void DestructMediaEffectCamera() {
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("destruct MediaEffectCamera: ");
        sb.append(this.mMediaFilterCamera);
        Log.d(str, sb.toString());
        DestructMediaEffectCameraJni(this.mMediaFilterCamera);
        this.mMediaFilterCamera = 0;
        this.mMediaFilterCameraNotify = null;
    }

    public RecordingStatus GetRecordingStatus() {
        Log.d(TAG, "GetRecordingStatus ");
        return RecordingStatus.int2enum(GetRecordingStatusJni(this.mMediaFilterCamera));
    }

    public void NeedProcessTexture(long j, int i, int i2) {
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("NeedProcessTexture: ");
        sb.append(this.mMediaFilterCamera);
        Log.d(str, sb.toString());
        NeedProcessTextureJni(this.mMediaFilterCamera, j, i, i2);
    }

    public void PauseRecording() {
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("Pause MediaFilterCamera: ");
        sb.append(this.mMediaFilterCamera);
        Log.d(str, sb.toString());
        PauseRecordingJni(this.mMediaFilterCamera);
    }

    public void PushExtraYAndUVFrame(Image image) {
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("PushExtraYUVFrame MediaFilterCamera: ");
        sb.append(this.mMediaFilterCamera);
        Log.d(str, sb.toString());
        Plane[] planes = image.getPlanes();
        PushExtraYAndUVFrameJni(this.mMediaFilterCamera, planes[0].getBuffer(), planes[2].getBuffer(), planes[0].getRowStride(), image.getHeight(), image.getTimestamp() / ExtraTextUtils.MB);
    }

    public void ResumeRecording() {
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("Resume MediaFilterCamera: ");
        sb.append(this.mMediaFilterCamera);
        Log.d(str, sb.toString());
        ResumeRecordingJni(this.mMediaFilterCamera);
    }

    public void SetOrientation(int i) {
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("SetOrientation MediaFilterCamera: ");
        sb.append(i);
        Log.d(str, sb.toString());
        SetOrientationJni(this.mMediaFilterCamera, i);
    }

    public void StartRecording(int i, String str, String str2, String str3, String str4, long j, float f) {
        String str5 = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("Start MediaFilterCamera: ");
        sb.append(this.mMediaFilterCamera);
        sb.append(" filePath: ");
        sb.append(str);
        Log.d(str5, sb.toString());
        StartRecordingJni(this.mMediaFilterCamera, i, str, str2, str3, str4, j, f, 15000);
    }

    public void StartRecording(String str, String str2, String str3, long j, float f, long j2) {
        String str4 = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("Start MediaFilterCamera: ");
        sb.append(this.mMediaFilterCamera);
        sb.append(" filePath: ");
        sb.append(str);
        sb.append("record_video_max_duration:");
        long j3 = j2;
        sb.append(j3);
        Log.d(str4, sb.toString());
        StartRecordingJni(this.mMediaFilterCamera, 0, str, "", str2, str3, j, f, j3);
    }

    public void StopRecording() {
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("Stop MediaFilterCamera: ");
        sb.append(this.mMediaFilterCamera);
        Log.d(str, sb.toString());
        StopRecordingJni(this.mMediaFilterCamera);
    }
}
