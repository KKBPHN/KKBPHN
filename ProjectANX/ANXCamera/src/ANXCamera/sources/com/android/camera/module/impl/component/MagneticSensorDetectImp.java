package com.android.camera.module.impl.component;

import android.hardware.SensorEvent;
import android.hardware.camera2.CaptureResult;
import com.android.camera.log.Log;
import com.android.camera.protocol.ModeCoordinatorImpl;
import com.android.camera.protocol.ModeProtocol.BaseProtocol;
import com.android.camera.protocol.ModeProtocol.MagneticSensorDetect;
import com.android.camera2.CaptureResultParser;

public class MagneticSensorDetectImp implements MagneticSensorDetect {
    private static final boolean DEBUG = true;
    private static final int MAGNETIC_DELTA = 15;
    private static final int MAGNETIC_DETECT_TIME_OUT = 60000;
    private static final String TAG = "MagneticSensorDetectImp";
    private float mAECLux;
    private long mCostTime = 0;
    private long mLastCaptureTime = 0;
    private boolean mMagneticChanged = true;
    private float[] mMagneticValues = new float[3];
    private float[] mPostMagneticValues = new float[3];

    public MagneticSensorDetectImp() {
        resetMagneticInfo();
        Log.d(TAG, "init MagneticSensorDetectImp");
    }

    public static BaseProtocol create() {
        return new MagneticSensorDetectImp();
    }

    private void printMagnetInfo() {
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("capture info timestamp:");
        sb.append(this.mLastCaptureTime);
        String str2 = ", [";
        sb.append(str2);
        sb.append(this.mPostMagneticValues[0]);
        String str3 = ",";
        sb.append(str3);
        sb.append(this.mPostMagneticValues[1]);
        sb.append(str3);
        sb.append(this.mPostMagneticValues[2]);
        String str4 = "]";
        sb.append(str4);
        Log.d(str, sb.toString());
        String str5 = TAG;
        StringBuilder sb2 = new StringBuilder();
        sb2.append("preview info timestamp:");
        sb2.append(System.currentTimeMillis());
        sb2.append(str2);
        sb2.append(this.mMagneticValues[0]);
        sb2.append(str3);
        sb2.append(this.mMagneticValues[1]);
        sb2.append(str3);
        sb2.append(this.mMagneticValues[2]);
        sb2.append(str4);
        Log.d(str5, sb2.toString());
    }

    public boolean isLockHDRChecker(String str) {
        boolean z = !this.mMagneticChanged;
        String str2 = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("(");
        sb.append(str);
        sb.append(")is_lock_hdr:");
        sb.append(z);
        Log.d(str2, sb.toString());
        return z;
    }

    public void onMagneticSensorChanged(SensorEvent sensorEvent) {
        for (int i = 0; i < 3; i++) {
            this.mMagneticValues[i] = sensorEvent.values[i];
        }
    }

    public void recordMagneticInfo() {
        System.arraycopy(this.mMagneticValues, 0, this.mPostMagneticValues, 0, 3);
        this.mLastCaptureTime = System.currentTimeMillis();
        this.mCostTime = this.mLastCaptureTime;
        this.mMagneticChanged = false;
    }

    public void registerProtocol() {
        ModeCoordinatorImpl.getInstance().attachProtocol(2576, this);
    }

    public void resetMagneticInfo() {
        if (this.mLastCaptureTime > 0) {
            for (int i = 0; i < 3; i++) {
                this.mPostMagneticValues[i] = 0.0f;
            }
            this.mLastCaptureTime = 0;
            this.mCostTime = 0;
            this.mMagneticChanged = true;
        }
    }

    public void unRegisterProtocol() {
        ModeCoordinatorImpl.getInstance().detachProtocol(2576, this);
    }

    public void updateMagneticDetection() {
        boolean z;
        printMagnetInfo();
        if (this.mLastCaptureTime > 0) {
            long currentTimeMillis = System.currentTimeMillis();
            float f = this.mAECLux;
            if (f < 1.0f || f > 1000.0f) {
                Log.d(TAG, "AECLux, no trigger");
                z = false;
            } else {
                z = true;
            }
            if (Math.abs(currentTimeMillis - this.mLastCaptureTime) > 60000) {
                Log.d(TAG, "Timeout, no trigger");
                z = false;
            }
            if (z) {
                float abs = Math.abs(this.mPostMagneticValues[0] - this.mMagneticValues[0]);
                float abs2 = Math.abs(this.mPostMagneticValues[1] - this.mMagneticValues[1]);
                float abs3 = Math.abs(this.mPostMagneticValues[2] - this.mMagneticValues[2]);
                float f2 = abs + abs2 + abs3;
                if (currentTimeMillis >= this.mCostTime || f2 >= 15.0f) {
                    String str = TAG;
                    StringBuilder sb = new StringBuilder();
                    sb.append("Mag diff:");
                    sb.append(f2);
                    sb.append(",time diff:");
                    sb.append((currentTimeMillis - this.mLastCaptureTime) / 1000);
                    sb.append(", sensor diff: [");
                    sb.append(abs);
                    String str2 = ",";
                    sb.append(str2);
                    sb.append(abs2);
                    sb.append(str2);
                    sb.append(abs3);
                    sb.append("], base[");
                    sb.append(this.mPostMagneticValues[0]);
                    sb.append(str2);
                    sb.append(this.mPostMagneticValues[1]);
                    sb.append(str2);
                    sb.append(this.mPostMagneticValues[2]);
                    sb.append("],current[");
                    sb.append(this.mMagneticValues[0]);
                    sb.append(str2);
                    sb.append(this.mMagneticValues[1]);
                    sb.append(str2);
                    sb.append(this.mMagneticValues[2]);
                    sb.append("]");
                    Log.d(str, sb.toString());
                    this.mCostTime += 500;
                }
                if (f2 < 15.0f) {
                    Log.d(TAG, "Magnetic no changed");
                    this.mMagneticChanged = false;
                    return;
                }
                Log.d(TAG, "Magnetic changed");
            }
            resetMagneticInfo();
        }
    }

    public void updatePreview(CaptureResult captureResult) {
        if (this.mLastCaptureTime > 0) {
            this.mAECLux = CaptureResultParser.getAecLux(captureResult);
        }
    }
}
