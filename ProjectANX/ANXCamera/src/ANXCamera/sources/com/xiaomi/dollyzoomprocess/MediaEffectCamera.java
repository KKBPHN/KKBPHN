package com.xiaomi.dollyzoomprocess;

import android.media.Image;
import android.media.Image.Plane;
import com.android.camera.log.Log;
import java.nio.ByteBuffer;
import miui.text.ExtraTextUtils;

public class MediaEffectCamera {
    public static final int ENCODER_DEVICE_K2 = 1;
    public static final int ENCODER_DEVICE_OTHERS = 0;
    public static final int ENCODER_FLAG_H264 = 0;
    public static final int ENCODER_FLAG_H265 = 1;
    public static final int ENCODER_FLAG_RGB = 0;
    public static final int ENCODER_FLAG_YUV = 1;
    public static final int ENCODER_FLAG_YUV_NV12 = 2;
    public static final int ENCODER_FLAG_YUV_NV21 = 3;
    public static final int ENC_STATE_FINISHED_FAILED = 2;
    public static final int ENC_STATE_FINISHED_NORMAL = 1;
    public static final int ENC_STATE_RECORDING = 0;
    public static final int FLAG_FILM_SIZE_OFF = 0;
    public static final int FLAG_FILM_SIZE_ON = 1;
    public static final int FLAG_RUN = 1;
    public static final int FLAG_STOP = 0;
    public static final int RUN_STATE_BAD_ALGO_RESLUT = 4;
    public static final int RUN_STATE_EARLY_STOP_BY_ALGO = 7;
    public static final int RUN_STATE_FAILED = -2;
    public static final int RUN_STATE_INITIALIZED = 1;
    public static final int RUN_STATE_MOVE_OUT_FRAME = 5;
    public static final int RUN_STATE_MOVE_OUT_ZOOM = 6;
    public static final int RUN_STATE_NORMAL_END = 3;
    public static final int RUN_STATE_NOT_READY = -1;
    public static final int RUN_STATE_RUNNING = 2;
    public static final int RUN_STATE_WAITING = 0;
    private static String TAG = "DollyZoomCamera";
    private long mDollyZoomBlock;
    private long mMediaFilterCamera;
    private long mRender;
    private int m_device_type = 0;
    private int m_flag_encode_type = 0;
    private int m_flag_film_state = 0;
    private int m_flag_run_state = 0;
    private int m_input_h = 2160;
    private int m_input_w = 3840;
    private boolean m_is_initialized = false;
    private double m_land_ref_b_B = 0.87d;
    private double m_land_ref_b_L = 0.4d;
    private double m_land_ref_b_R = 0.6d;
    private double m_land_ref_b_T = 0.17d;
    private int m_now_state = -2;
    private double m_port_ref_b_B = 0.9d;
    private double m_port_ref_b_L = 0.23d;
    private double m_port_ref_b_R = 0.78d;
    private double m_port_ref_b_T = 0.2d;
    private double m_ref_box_B = 0.8d;
    private double m_ref_box_L = 0.15d;
    private double m_ref_box_R = 0.75d;
    private double m_ref_box_T = 0.15d;
    private int m_rotate_angle = 0;
    private String m_save_video_path = "/sdcard/default_dz_video.mp4";
    private int m_yuv_data_type = 3;

    static {
        System.loadLibrary("DollyZoom");
    }

    public MediaEffectCamera() {
        Log.d(TAG, "construct MediaEffectCamera");
        this.mMediaFilterCamera = 0;
        this.mDollyZoomBlock = 0;
        this.mRender = 0;
    }

    private static native long ConstructRenderAndPipelineJni(int i, int i2, double d, double d2, double d3, double d4, int i3, int i4);

    private static native long ConstructRenderJni();

    private static native void DestructDollyZoomBlockJni(long j);

    private static native void DestructRenderJni(long j);

    private static native long DoSomethingAtStopJni(long j);

    private static native int GetNowEncoderStateJni(long j);

    private static native double GetNowScaleJni(long j);

    private static native int GetNowStateJni(long j);

    private static native void InitRenderJni(long j, int i, int i2, int i3, int i4);

    private static native long PausePipelineJni(long j);

    private static native long PushExtraYAndUVFrameJni(long j, ByteBuffer byteBuffer, ByteBuffer byteBuffer2, int i, int i2, long j2, int i3);

    private static native long RecoverPipelineJni(long j);

    private void RefreshReferenceBox() {
        double d;
        int i = this.m_rotate_angle;
        if (i == 0 || i == 180) {
            this.m_ref_box_L = this.m_land_ref_b_L;
            this.m_ref_box_R = this.m_land_ref_b_R;
            this.m_ref_box_T = this.m_land_ref_b_T;
            d = this.m_land_ref_b_B;
        } else if (i == 90 || i == 270) {
            this.m_ref_box_L = this.m_port_ref_b_L;
            this.m_ref_box_R = this.m_port_ref_b_R;
            this.m_ref_box_T = this.m_port_ref_b_T;
            d = this.m_port_ref_b_B;
        } else {
            return;
        }
        this.m_ref_box_B = d;
    }

    private static native void RenderFrameJni(long j);

    private static native long ResetDollyZoomStateJni(long j);

    private static native long SetDollyZoomOrientationJni(long j, int i, double d, double d2, double d3, double d4);

    private static native long SetDollyZoomOutputConfigJni(long j, String str, int i, int i2);

    public void ConstructMediaEffectCamera(int i, int i2) {
        this.m_input_w = i;
        this.m_input_h = i2;
        if (!this.m_is_initialized) {
            RefreshReferenceBox();
            this.mDollyZoomBlock = ConstructRenderAndPipelineJni(this.m_input_w, this.m_input_h, this.m_ref_box_L, this.m_ref_box_R, this.m_ref_box_T, this.m_ref_box_B, this.m_yuv_data_type, this.m_device_type);
            String str = TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("construct mDollyZoomBlock: ");
            sb.append(this.mDollyZoomBlock);
            Log.d(str, sb.toString());
            this.m_is_initialized = true;
        }
    }

    public void ConstructMediaEffectCamera(int i, int i2, int i3) {
        this.m_input_w = i;
        this.m_input_h = i2;
        this.m_device_type = i3;
        if (!this.m_is_initialized) {
            RefreshReferenceBox();
            this.mDollyZoomBlock = ConstructRenderAndPipelineJni(this.m_input_w, this.m_input_h, this.m_ref_box_L, this.m_ref_box_R, this.m_ref_box_T, this.m_ref_box_B, this.m_yuv_data_type, this.m_device_type);
            String str = TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("construct mDollyZoomBlock: ");
            sb.append(this.mDollyZoomBlock);
            Log.d(str, sb.toString());
            this.m_is_initialized = true;
        }
    }

    public void ConstructRender() {
        Log.d(TAG, "construct ConstructRender");
        this.mRender = ConstructRenderJni();
    }

    public void DestructMediaEffectCamera() {
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("destruct mDollyZoomBlock: ");
        sb.append(this.mDollyZoomBlock);
        Log.d(str, sb.toString());
        if (this.m_is_initialized) {
            DestructDollyZoomBlockJni(this.mDollyZoomBlock);
            this.mDollyZoomBlock = 0;
            this.m_is_initialized = false;
        }
    }

    public void DestructRender() {
        Log.d(TAG, "construct DestructRender");
        DestructRenderJni(this.mRender);
        this.mRender = 0;
    }

    public int GetEncoderState() {
        return GetNowEncoderStateJni(this.mDollyZoomBlock);
    }

    public double GetNowScale() {
        return GetNowScaleJni(this.mDollyZoomBlock);
    }

    public int GetNowState() {
        return GetNowStateJni(this.mDollyZoomBlock);
    }

    public void InitRender(int i, int i2, int i3, int i4) {
        Log.d(TAG, "construct InitRender");
        InitRenderJni(this.mRender, i, i2, i3, i4);
    }

    public void PauseRunning() {
        this.mDollyZoomBlock = PausePipelineJni(this.mDollyZoomBlock);
    }

    public void PushExtraYAndUVFrame(Image image) {
        Plane[] planes = image.getPlanes();
        int rowStride = planes[0].getRowStride();
        if (this.m_is_initialized) {
            this.mDollyZoomBlock = PushExtraYAndUVFrameJni(this.mDollyZoomBlock, planes[0].getBuffer(), planes[2].getBuffer(), rowStride, image.getHeight(), image.getTimestamp() / ExtraTextUtils.MB, this.m_flag_run_state);
        }
    }

    public void RecoverRunning() {
        this.mDollyZoomBlock = RecoverPipelineJni(this.mDollyZoomBlock);
    }

    public void RenderFrame() {
        RenderFrameJni(this.mRender);
    }

    public boolean SetEncodeType(int i) {
        this.m_flag_encode_type = i;
        this.mDollyZoomBlock = SetDollyZoomOutputConfigJni(this.mDollyZoomBlock, this.m_save_video_path, this.m_flag_encode_type, this.m_flag_film_state);
        return true;
    }

    public boolean SetFilmSizeState(int i) {
        this.m_flag_film_state = i;
        this.mDollyZoomBlock = SetDollyZoomOutputConfigJni(this.mDollyZoomBlock, this.m_save_video_path, this.m_flag_encode_type, this.m_flag_film_state);
        return true;
    }

    public boolean SetRotation(int i) {
        this.m_rotate_angle = i;
        RefreshReferenceBox();
        this.mDollyZoomBlock = SetDollyZoomOrientationJni(this.mDollyZoomBlock, this.m_rotate_angle, this.m_ref_box_L, this.m_ref_box_R, this.m_ref_box_T, this.m_ref_box_B);
        return true;
    }

    public boolean SetSavePath(String str) {
        this.m_save_video_path = str;
        this.mDollyZoomBlock = SetDollyZoomOutputConfigJni(this.mDollyZoomBlock, this.m_save_video_path, this.m_flag_encode_type, this.m_flag_film_state);
        return true;
    }

    public void StartRecording() {
        this.mDollyZoomBlock = ResetDollyZoomStateJni(this.mDollyZoomBlock);
        this.m_flag_run_state = 1;
        Log.d(TAG, "StartRecording");
    }

    public void StopRecording() {
        this.mDollyZoomBlock = DoSomethingAtStopJni(this.mDollyZoomBlock);
        this.m_flag_run_state = 0;
        Log.d(TAG, "StopRecording");
    }
}
