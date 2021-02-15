package com.android.camera;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.os.SystemProperties;
import android.util.SparseArray;
import com.android.camera.constant.DurationConstant;
import com.android.camera.log.Log;

public class SensorStateManager {
    public static final int ACCELEROMETER = 8;
    private static final double ACCELEROMETER_GAP_TOLERANCE = 0.5d;
    private static final int ACCELEROMETER_THRESHOLD = 16;
    private static final int CAPTURE_POSTURE_DEGREE = SystemProperties.getInt("capture_degree", 45);
    private static final boolean DEBUG = Util.isDebugOsBuild();
    private static final long EVENT_PROCESS_INTERVAL = 100000000;
    private static final long EVENT_TIME_OUT = 1000000000;
    public static final int GAME_ROTATION = 64;
    public static final int GRAVITY = 32;
    public static final int GYROSCOPE = 2;
    private static final double GYROSCOPE_FOCUS_THRESHOLD = 1.0471975511965976d;
    private static final double GYROSCOPE_IGNORE_THRESHOLD = 0.05000000074505806d;
    /* access modifiers changed from: private */
    public static final double GYROSCOPE_MOVING_THRESHOLD = ((double) (((float) SystemProperties.getInt("camera_moving_threshold", 15)) / 10.0f));
    private static final double GYROSCOPE_STABLE_THRESHOLD = ((double) (((float) SystemProperties.getInt("camera_stable_threshold", 9)) / 10.0f));
    public static final int GYROSCOPE_UNCALIBRATED = 128;
    public static final int LEFT_CAPTURE_POSTURE = 1;
    public static final int LINEAR_ACCELEROMETER = 1;
    private static final double LINERACCELEROMETER_GAP_TOLERANCE = 0.8999999761581421d;
    private static final int LYING_HYSTERESIS = 5;
    public static final int MAGNETIC_FIELD_UNCALIBRATED = 256;
    private static final int MAX_LYING_BOUND = 153;
    private static final int MAX_SENSOR_SIZE = 11;
    private static final int MIN_LYING_BOUND = 26;
    private static final int MSG_DEVICE_BECOME_STABLE = 1;
    private static final int MSG_UPDATE = 2;
    private static final float NS2S = 1.0E-9f;
    public static final int ORIENTATION = 4;
    public static final int ORIENTATION_ACCELEROMETER = 512;
    public static final int ORIENTATION_MAGNETIC_FIELD = 1024;
    public static final int ORIENTATION_UNKNOWN = -1;
    public static final int PORTRAIT_CAPTURE_POSTURE = 0;
    public static final int RIGHT_CAPTURE_POSTURE = 2;
    public static final int ROTATION_VECTOR = 16;
    public static final int SENSOR_ALL = 2047;
    /* access modifiers changed from: private */
    public final String TAG;
    /* access modifiers changed from: private */
    public boolean isNeedFullGyroscope;
    private final Sensor mAccelerometerSensor;
    private SensorEventListener mAccelerometerSensorEventListenerImpl;
    /* access modifiers changed from: private */
    public long mAccelerometerTimeStamp;
    /* access modifiers changed from: private */
    public float[] mAccelerometerValues;
    /* access modifiers changed from: private */
    public double[] mAngleSpeed;
    /* access modifiers changed from: private */
    public int mAngleSpeedIndex;
    /* access modifiers changed from: private */
    public double mAngleTotal = 0.0d;
    private int mCapturePosture = 0;
    private boolean mFocusSensorEnabled;
    private final Sensor mGameRotationSensor;
    private SensorEventListener mGameRotationSensorListener;
    /* access modifiers changed from: private */
    public boolean mGradienterEnabled;
    private final Sensor mGravitySensor;
    private SensorEventListener mGravitySensorListener;
    private final Sensor mGyroscope;
    private boolean mGyroscopeEnabled;
    private SensorEventListener mGyroscopeListener;
    /* access modifiers changed from: private */
    public long mGyroscopeTimeStamp;
    private final Sensor mGyroscopeUncalibrated;
    private SensorEventListener mGyroscopeUncalibratedListener;
    private Handler mHandler;
    /* access modifiers changed from: private */
    public boolean mIsLyingForGradienter = false;
    private boolean mIsLyingForLie = false;
    /* access modifiers changed from: private */
    public boolean mLieFlagEnabled;
    private SensorEventListener mLinearAccelerationListener;
    private final Sensor mLinearAccelerometer;
    private final Sensor mMagneticFieldUncalibrated;
    private boolean mMagneticFieldUncalibratedEnable;
    private final SensorEventListener mMagneticFieldUncalibratedListener;
    /* access modifiers changed from: private */
    public float[] mMagneticFieldValues;
    /* access modifiers changed from: private */
    public float mOrientation = -1.0f;
    private final Sensor mOrientationAccelerometerSensor;
    private final Sensor mOrientationMagneticFieldSensor;
    private final Sensor mOrientationSensor;
    private SensorEventListener mOrientationSensorEventListener;
    private int mRate;
    private SensorEventListener mRoatationSensorListener;
    private final Sensor mRotationVecotrSensor;
    private boolean mRotationVectorFlagEnabled;
    private SparseArray mSensorEventListenerMap;
    private HandlerThread mSensorListenerThread;
    private final SensorManager mSensorManager;
    private SparseArray mSensorMap;
    private int mSensorRegistered;
    private SensorStateListener mSensorStateListener;
    private boolean mTTARFlagEnabled;
    private Handler mThreadHandler;

    class MainHandler extends Handler {
        public MainHandler(Looper looper) {
            super(looper);
        }

        public void handleMessage(Message message) {
            int i = message.what;
            boolean z = true;
            if (i == 1) {
                SensorStateManager.this.deviceBecomeStable();
            } else if (i == 2) {
                SensorStateManager sensorStateManager = SensorStateManager.this;
                int i2 = message.arg1;
                if (message.arg2 != 1) {
                    z = false;
                }
                sensorStateManager.update(i2, z);
            }
        }
    }

    public class SensorStateAdapter implements SensorStateListener {
        public boolean isWorking() {
            return false;
        }

        public void notifyDevicePostureChanged() {
        }

        public void onDeviceBecomeStable() {
        }

        public void onDeviceBeginMoving() {
        }

        public void onDeviceKeepMoving(double d) {
        }

        public void onDeviceKeepStable() {
        }

        public void onDeviceLieChanged(boolean z) {
        }

        public void onDeviceOrientationChanged(float f, boolean z) {
        }

        public void onDeviceRotationChanged(float[] fArr) {
        }

        public void onSensorChanged(SensorEvent sensorEvent) {
        }
    }

    public interface SensorStateListener {
        boolean isWorking();

        void notifyDevicePostureChanged();

        void onDeviceBecomeStable();

        void onDeviceBeginMoving();

        void onDeviceKeepMoving(double d);

        void onDeviceKeepStable();

        void onDeviceLieChanged(boolean z);

        void onDeviceOrientationChanged(float f, boolean z);

        void onDeviceRotationChanged(float[] fArr);

        void onSensorChanged(SensorEvent sensorEvent);
    }

    public SensorStateManager(Context context, Looper looper) {
        StringBuilder sb = new StringBuilder();
        sb.append("SensorSM@");
        sb.append(hashCode());
        this.TAG = sb.toString();
        double d = GYROSCOPE_STABLE_THRESHOLD;
        this.mAngleSpeed = new double[]{d, d, d, d, d};
        this.mAngleSpeedIndex = -1;
        this.mAccelerometerTimeStamp = 0;
        this.mGyroscopeTimeStamp = 0;
        this.mAccelerometerValues = new float[3];
        this.mMagneticFieldValues = new float[3];
        this.mGyroscopeListener = new SensorEventListener() {
            public void onAccuracyChanged(Sensor sensor, int i) {
            }

            public void onSensorChanged(SensorEvent sensorEvent) {
                SensorStateListener access$000 = SensorStateManager.this.getSensorStateListener();
                if (access$000 != null && access$000.isWorking()) {
                    long abs = Math.abs(sensorEvent.timestamp - SensorStateManager.this.mGyroscopeTimeStamp);
                    if (!SensorStateManager.this.isNeedFullGyroscope && abs < SensorStateManager.EVENT_PROCESS_INTERVAL) {
                        return;
                    }
                    if (SensorStateManager.this.mGyroscopeTimeStamp == 0 || abs > 1000000000) {
                        SensorStateManager.this.mGyroscopeTimeStamp = sensorEvent.timestamp;
                    } else {
                        float f = ((float) abs) * SensorStateManager.NS2S;
                        float[] fArr = sensorEvent.values;
                        double sqrt = Math.sqrt((double) ((fArr[0] * fArr[0]) + (fArr[1] * fArr[1]) + (fArr[2] * fArr[2])));
                        SensorStateManager.this.mGyroscopeTimeStamp = sensorEvent.timestamp;
                        if (SensorStateManager.GYROSCOPE_MOVING_THRESHOLD < sqrt) {
                            SensorStateManager.this.deviceBeginMoving();
                        }
                        SensorStateManager sensorStateManager = SensorStateManager.this;
                        sensorStateManager.mAngleSpeedIndex = SensorStateManager.access$504(sensorStateManager) % SensorStateManager.this.mAngleSpeed.length;
                        SensorStateManager.this.mAngleSpeed[SensorStateManager.this.mAngleSpeedIndex] = sqrt;
                        if (SensorStateManager.this.isNeedFullGyroscope || sqrt >= SensorStateManager.GYROSCOPE_IGNORE_THRESHOLD) {
                            SensorStateManager.access$718(SensorStateManager.this, sqrt * ((double) f));
                            if (SensorStateManager.this.mAngleTotal > SensorStateManager.GYROSCOPE_FOCUS_THRESHOLD) {
                                SensorStateManager.this.mAngleTotal = 0.0d;
                                SensorStateManager.this.deviceKeepMoving(10000.0d);
                            }
                            if (access$000.isWorking()) {
                                access$000.onSensorChanged(sensorEvent);
                            }
                        }
                    }
                }
            }
        };
        this.mLinearAccelerationListener = new SensorEventListener() {
            public void onAccuracyChanged(Sensor sensor, int i) {
            }

            public void onSensorChanged(SensorEvent sensorEvent) {
                SensorStateListener access$000 = SensorStateManager.this.getSensorStateListener();
                if (access$000 != null && access$000.isWorking()) {
                    long abs = Math.abs(sensorEvent.timestamp - SensorStateManager.this.mAccelerometerTimeStamp);
                    if (abs >= SensorStateManager.EVENT_PROCESS_INTERVAL) {
                        if (SensorStateManager.this.mAccelerometerTimeStamp == 0 || abs > 1000000000) {
                            SensorStateManager.this.mAccelerometerTimeStamp = sensorEvent.timestamp;
                        } else {
                            float[] fArr = sensorEvent.values;
                            double sqrt = Math.sqrt((double) ((fArr[0] * fArr[0]) + (fArr[1] * fArr[1]) + (fArr[2] * fArr[2])));
                            SensorStateManager.this.mAccelerometerTimeStamp = sensorEvent.timestamp;
                            if (sqrt > SensorStateManager.LINERACCELEROMETER_GAP_TOLERANCE) {
                                SensorStateManager.this.deviceKeepMoving(sqrt);
                            }
                        }
                    }
                }
            }
        };
        this.mOrientationSensorEventListener = new SensorEventListener() {
            public void onAccuracyChanged(Sensor sensor, int i) {
            }

            public void onSensorChanged(SensorEvent sensorEvent) {
                if (SensorStateManager.this.mLieFlagEnabled || SensorStateManager.this.mGradienterEnabled) {
                    if (sensorEvent.sensor.getType() == 3) {
                        SensorStateManager sensorStateManager = SensorStateManager.this;
                        float[] fArr = sensorEvent.values;
                        sensorStateManager.calculateOrientation(fArr[1], fArr[2]);
                        return;
                    }
                    if (sensorEvent.sensor.getType() == 1) {
                        SensorStateManager.this.mAccelerometerValues = sensorEvent.values;
                    }
                    if (sensorEvent.sensor.getType() == 2) {
                        SensorStateManager.this.mMagneticFieldValues = sensorEvent.values;
                    }
                    float[] fArr2 = new float[3];
                    float[] fArr3 = new float[9];
                    SensorManager.getRotationMatrix(fArr3, null, SensorStateManager.this.mAccelerometerValues, SensorStateManager.this.mMagneticFieldValues);
                    SensorManager.getOrientation(fArr3, fArr2);
                    fArr2[1] = (float) Math.toDegrees((double) fArr2[1]);
                    fArr2[2] = (float) Math.toDegrees((double) fArr2[2]);
                    SensorStateManager.this.calculateOrientation(fArr2[1], fArr2[2]);
                }
            }
        };
        this.mAccelerometerSensorEventListenerImpl = new SensorEventListener() {
            private static final float CLEAR_FILTER_THRESHOLD = 3.0f;
            private static final int _DATA_X = 0;
            private static final int _DATA_Y = 1;
            private static final int _DATA_Z = 2;
            private static final float finalAlpha = 0.7f;
            private static final float firstAlpha = 0.8f;
            private float[] finalFilter = new float[3];
            private float[] firstFilter = new float[3];

            private void clearFilter() {
                int i = 0;
                while (true) {
                    float[] fArr = this.firstFilter;
                    if (i < fArr.length) {
                        fArr[i] = 0.0f;
                        this.finalFilter[i] = 0.0f;
                        i++;
                    } else {
                        return;
                    }
                }
            }

            public void onAccuracyChanged(Sensor sensor, int i) {
                String access$1500 = SensorStateManager.this.TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("onAccuracyChanged accuracy=");
                sb.append(i);
                Log.v(access$1500, sb.toString());
            }

            public void onSensorChanged(SensorEvent sensorEvent) {
                SensorStateListener access$000 = SensorStateManager.this.getSensorStateListener();
                if (access$000 != null) {
                    float[] fArr = this.firstFilter;
                    float f = fArr[0] * 0.8f;
                    float[] fArr2 = sensorEvent.values;
                    fArr[0] = f + (fArr2[0] * 0.19999999f);
                    fArr[1] = (fArr[1] * 0.8f) + (fArr2[1] * 0.19999999f);
                    fArr[2] = (fArr[2] * 0.8f) + (fArr2[2] * 0.19999999f);
                    float[] fArr3 = this.finalFilter;
                    fArr3[0] = (fArr3[0] * 0.7f) + (fArr[0] * 0.3f);
                    fArr3[1] = (fArr3[1] * 0.7f) + (fArr[1] * 0.3f);
                    fArr3[2] = (fArr3[2] * 0.7f) + (fArr[2] * 0.3f);
                    String access$1500 = SensorStateManager.this.TAG;
                    StringBuilder sb = new StringBuilder();
                    sb.append("finalFilter=");
                    sb.append(this.finalFilter[0]);
                    String str = " ";
                    sb.append(str);
                    sb.append(this.finalFilter[1]);
                    sb.append(str);
                    sb.append(this.finalFilter[2]);
                    sb.append(" event.values=");
                    sb.append(sensorEvent.values[0]);
                    sb.append(str);
                    sb.append(sensorEvent.values[1]);
                    sb.append(str);
                    sb.append(sensorEvent.values[2]);
                    Log.v(access$1500, sb.toString());
                    float f2 = -1.0f;
                    float[] fArr4 = this.finalFilter;
                    float f3 = -fArr4[0];
                    float f4 = -fArr4[1];
                    float f5 = -fArr4[2];
                    if (((f3 * f3) + (f4 * f4)) * 4.0f >= f5 * f5) {
                        f2 = SensorStateManager.this.normalizeDegree((float) (90 - Math.round(((float) Math.atan2((double) (-f4), (double) f3)) * 57.29578f)));
                    }
                    if (f2 != SensorStateManager.this.mOrientation) {
                        if (Math.abs(SensorStateManager.this.mOrientation - f2) > 3.0f) {
                            clearFilter();
                        }
                        SensorStateManager.this.mOrientation = f2;
                        String access$15002 = SensorStateManager.this.TAG;
                        StringBuilder sb2 = new StringBuilder();
                        sb2.append("SensorEventListenerImpl TYPE_ACCELEROMETER mOrientation=");
                        sb2.append(SensorStateManager.this.mOrientation);
                        sb2.append(" mIsLyingForGradienter=");
                        sb2.append(SensorStateManager.this.mIsLyingForGradienter);
                        Log.v(access$15002, sb2.toString());
                        access$000.onDeviceOrientationChanged(SensorStateManager.this.mOrientation, SensorStateManager.this.mIsLyingForGradienter);
                    }
                    if (access$000.isWorking()) {
                        access$000.onSensorChanged(sensorEvent);
                    }
                    if (((double) Math.abs(sensorEvent.values[0] - this.finalFilter[0])) > 0.5d || ((double) Math.abs(sensorEvent.values[1] - this.finalFilter[1])) > 0.5d || ((double) Math.abs(sensorEvent.values[2] - this.finalFilter[2])) > 0.5d) {
                        SensorStateManager.this.deviceKeepMoving(0.5d);
                    }
                }
            }
        };
        this.mRoatationSensorListener = new SensorEventListener() {
            public void onAccuracyChanged(Sensor sensor, int i) {
            }

            public void onSensorChanged(SensorEvent sensorEvent) {
                SensorStateListener access$000 = SensorStateManager.this.getSensorStateListener();
                if (access$000 != null && access$000.isWorking()) {
                    float[] fArr = sensorEvent.values;
                    if (fArr != null && fArr.length >= 4) {
                        access$000.onDeviceRotationChanged(new float[]{fArr[0], fArr[1], fArr[2], fArr[3]});
                    }
                }
            }
        };
        this.mGravitySensorListener = new SensorEventListener() {
            public void onAccuracyChanged(Sensor sensor, int i) {
            }

            public void onSensorChanged(SensorEvent sensorEvent) {
                SensorStateListener access$000 = SensorStateManager.this.getSensorStateListener();
                if (access$000 != null && access$000.isWorking()) {
                    access$000.onSensorChanged(sensorEvent);
                }
            }
        };
        this.mGameRotationSensorListener = new SensorEventListener() {
            public void onAccuracyChanged(Sensor sensor, int i) {
            }

            public void onSensorChanged(SensorEvent sensorEvent) {
                SensorStateListener access$000 = SensorStateManager.this.getSensorStateListener();
                if (access$000 != null && access$000.isWorking()) {
                    access$000.onSensorChanged(sensorEvent);
                }
            }
        };
        this.mGyroscopeUncalibratedListener = new SensorEventListener() {
            public void onAccuracyChanged(Sensor sensor, int i) {
            }

            public void onSensorChanged(SensorEvent sensorEvent) {
                SensorStateListener access$000 = SensorStateManager.this.getSensorStateListener();
                if (access$000 != null && access$000.isWorking()) {
                    access$000.onSensorChanged(sensorEvent);
                }
            }
        };
        this.mMagneticFieldUncalibratedListener = new SensorEventListener() {
            public void onAccuracyChanged(Sensor sensor, int i) {
            }

            public void onSensorChanged(SensorEvent sensorEvent) {
                SensorStateListener access$000 = SensorStateManager.this.getSensorStateListener();
                if (access$000 != null && access$000.isWorking()) {
                    access$000.onSensorChanged(sensorEvent);
                }
            }
        };
        this.mSensorManager = (SensorManager) context.getApplicationContext().getSystemService("sensor");
        this.mGyroscope = this.mSensorManager.getDefaultSensor(4);
        this.mLinearAccelerometer = this.mSensorManager.getDefaultSensor(10);
        this.mAccelerometerSensor = this.mSensorManager.getDefaultSensor(1);
        this.mOrientationSensor = this.mSensorManager.getDefaultSensor(3);
        this.mOrientationAccelerometerSensor = this.mSensorManager.getDefaultSensor(1);
        this.mOrientationMagneticFieldSensor = this.mSensorManager.getDefaultSensor(2);
        this.mRotationVecotrSensor = this.mSensorManager.getDefaultSensor(11);
        this.mGravitySensor = this.mSensorManager.getDefaultSensor(9);
        this.mGameRotationSensor = this.mSensorManager.getDefaultSensor(15);
        this.mGyroscopeUncalibrated = this.mSensorManager.getDefaultSensor(16);
        this.mMagneticFieldUncalibrated = this.mSensorManager.getDefaultSensor(14);
        this.mHandler = new MainHandler(looper);
        this.mSensorEventListenerMap = new SparseArray();
        this.mSensorEventListenerMap.put(2, this.mGyroscopeListener);
        this.mSensorEventListenerMap.put(1, this.mLinearAccelerationListener);
        if (this.mOrientationSensor == null) {
            this.mSensorEventListenerMap.put(1024, this.mOrientationSensorEventListener);
            this.mSensorEventListenerMap.put(512, this.mOrientationSensorEventListener);
        } else {
            this.mSensorEventListenerMap.put(4, this.mOrientationSensorEventListener);
        }
        this.mSensorEventListenerMap.put(8, this.mAccelerometerSensorEventListenerImpl);
        this.mSensorEventListenerMap.put(16, this.mRoatationSensorListener);
        this.mSensorEventListenerMap.put(32, this.mGravitySensorListener);
        this.mSensorEventListenerMap.put(64, this.mGameRotationSensorListener);
        this.mSensorEventListenerMap.put(128, this.mGyroscopeUncalibratedListener);
        if (this.mMagneticFieldUncalibrated != null) {
            this.mSensorEventListenerMap.put(256, this.mMagneticFieldUncalibratedListener);
        }
        this.mSensorMap = new SparseArray();
        this.mSensorMap.put(2, this.mGyroscope);
        this.mSensorMap.put(1, this.mLinearAccelerometer);
        Sensor sensor = this.mOrientationSensor;
        if (sensor == null) {
            this.mSensorMap.put(1024, this.mOrientationMagneticFieldSensor);
            this.mSensorMap.put(512, this.mOrientationAccelerometerSensor);
        } else {
            this.mSensorMap.put(4, sensor);
        }
        this.mSensorMap.put(8, this.mAccelerometerSensor);
        this.mSensorMap.put(16, this.mRotationVecotrSensor);
        this.mSensorMap.put(32, this.mGravitySensor);
        this.mSensorMap.put(64, this.mGameRotationSensor);
        this.mSensorMap.put(128, this.mGyroscopeUncalibrated);
        Sensor sensor2 = this.mMagneticFieldUncalibrated;
        if (sensor2 != null) {
            this.mSensorMap.put(256, sensor2);
        }
        this.mRate = DurationConstant.DURATION_VIDEO_BOKEH_RECORDING;
        this.mSensorListenerThread = new HandlerThread("SensorListenerThread");
        this.mSensorListenerThread.start();
    }

    static /* synthetic */ int access$504(SensorStateManager sensorStateManager) {
        int i = sensorStateManager.mAngleSpeedIndex + 1;
        sensorStateManager.mAngleSpeedIndex = i;
        return i;
    }

    static /* synthetic */ double access$718(SensorStateManager sensorStateManager, double d) {
        double d2 = sensorStateManager.mAngleTotal + d;
        sensorStateManager.mAngleTotal = d2;
        return d2;
    }

    /* access modifiers changed from: private */
    public void calculateOrientation(float f, float f2) {
        SensorStateListener sensorStateListener = getSensorStateListener();
        if (sensorStateListener != null) {
            float abs = Math.abs(f);
            float abs2 = Math.abs(f2);
            int i = 5;
            if (this.mLieFlagEnabled) {
                int i2 = this.mIsLyingForLie ? 5 : 0;
                int i3 = i2 + 10;
                int i4 = 170 - i2;
                float f3 = (float) i3;
                boolean z = (abs <= f3 || abs >= ((float) i4)) && (abs2 <= f3 || abs2 >= ((float) i4));
                if (z != this.mIsLyingForLie) {
                    this.mIsLyingForLie = z;
                    String str = this.TAG;
                    StringBuilder sb = new StringBuilder();
                    sb.append("SensorEventListenerImpl TYPE_ORIENTATION mIsLyingForLie=");
                    sb.append(this.mIsLyingForLie);
                    Log.v(str, sb.toString());
                    sensorStateListener.onDeviceLieChanged(this.mIsLyingForLie);
                }
            }
            if (this.mGradienterEnabled) {
                if (!this.mIsLyingForGradienter) {
                    i = 0;
                }
                int i5 = i + 26;
                int i6 = 153 - i;
                float f4 = (float) i5;
                boolean z2 = (abs <= f4 || abs >= ((float) i6)) && (abs2 <= f4 || abs2 >= ((float) i6));
                if (z2 != this.mIsLyingForGradienter) {
                    this.mIsLyingForGradienter = z2;
                    String str2 = this.TAG;
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append("SensorEventListenerImpl TYPE_ORIENTATION mIsLyingForGradienter=");
                    sb2.append(this.mIsLyingForGradienter);
                    sb2.append("mOrientation=");
                    sb2.append(this.mOrientation);
                    Log.v(str2, sb2.toString());
                    sensorStateListener.onDeviceOrientationChanged(this.mOrientation, this.mIsLyingForGradienter);
                }
            }
            if (Math.abs(abs2 - 90.0f) < ((float) CAPTURE_POSTURE_DEGREE)) {
                int i7 = 2;
                if (this.mOrientationSensor == null ? f2 > 0.0f : f2 < 0.0f) {
                    i7 = 1;
                }
                changeCapturePosture(i7);
            } else {
                changeCapturePosture(0);
            }
        }
    }

    private void changeCapturePosture(int i) {
        if (this.mCapturePosture != i) {
            this.mCapturePosture = i;
            SensorStateListener sensorStateListener = getSensorStateListener();
            if (sensorStateListener != null) {
                sensorStateListener.notifyDevicePostureChanged();
            }
        }
    }

    /* access modifiers changed from: private */
    public void deviceBecomeStable() {
        if (this.mFocusSensorEnabled) {
            SensorStateListener sensorStateListener = getSensorStateListener();
            if (sensorStateListener != null) {
                sensorStateListener.onDeviceBecomeStable();
            }
        }
    }

    /* access modifiers changed from: private */
    public void deviceBeginMoving() {
        SensorStateListener sensorStateListener = getSensorStateListener();
        if (sensorStateListener != null) {
            sensorStateListener.onDeviceBeginMoving();
        }
    }

    /* access modifiers changed from: private */
    public void deviceKeepMoving(double d) {
        if (this.mFocusSensorEnabled) {
            SensorStateListener sensorStateListener = getSensorStateListener();
            if (sensorStateListener != null) {
                sensorStateListener.onDeviceKeepMoving(d);
            }
        }
    }

    private void deviceKeepStable() {
        SensorStateListener sensorStateListener = getSensorStateListener();
        if (sensorStateListener != null) {
            sensorStateListener.onDeviceKeepStable();
        }
    }

    private int getOrientationType() {
        return this.mOrientationSensor == null ? 1536 : 4;
    }

    /* access modifiers changed from: private */
    public synchronized SensorStateListener getSensorStateListener() {
        return this.mSensorStateListener;
    }

    private static boolean isContains(int i, int i2) {
        return (i & i2) == i2;
    }

    private static boolean isPartialContains(int i, int i2) {
        return (i & i2) != 0;
    }

    /* access modifiers changed from: private */
    public float normalizeDegree(float f) {
        while (f >= 360.0f) {
            f -= 360.0f;
        }
        while (f < 0.0f) {
            f += 360.0f;
        }
        return f;
    }

    /* access modifiers changed from: private */
    public void update(int i, boolean z) {
        if (!z && isPartialContains(this.mSensorRegistered, i)) {
            unregister(i);
        } else if (!z || isContains(this.mSensorRegistered, i)) {
            Log.d(this.TAG, "update sensor %d, enable ", Integer.valueOf(i), Boolean.valueOf(z));
        } else {
            register(i, false);
        }
    }

    public int getCapturePosture() {
        return this.mCapturePosture;
    }

    public boolean isDeviceLying() {
        return this.mIsLyingForGradienter;
    }

    public boolean isMagneticFieldUncalibratedEnable() {
        return this.mMagneticFieldUncalibratedEnable;
    }

    public boolean isStable() {
        String str = " threshold=";
        boolean z = true;
        if (this.mAngleSpeedIndex >= 0) {
            double d = 0.0d;
            for (double d2 : this.mAngleSpeed) {
                d += d2;
            }
            double[] dArr = this.mAngleSpeed;
            double length = d / ((double) dArr.length);
            double d3 = dArr[Util.clamp(this.mAngleSpeedIndex, 0, dArr.length - 1)];
            String str2 = this.TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("isStable mAngleSpeed=");
            sb.append(length);
            sb.append(" lastSpeed=");
            sb.append(d3);
            sb.append(str);
            sb.append(GYROSCOPE_STABLE_THRESHOLD);
            Log.v(str2, sb.toString());
            double d4 = GYROSCOPE_STABLE_THRESHOLD;
            if (length > d4 || d3 > d4) {
                z = false;
            }
            return z;
        }
        String str3 = this.TAG;
        StringBuilder sb2 = new StringBuilder();
        sb2.append("isStable return true for mAngleSpeedIndex=");
        sb2.append(this.mAngleSpeedIndex);
        sb2.append(str);
        sb2.append(GYROSCOPE_STABLE_THRESHOLD);
        Log.v(str3, sb2.toString());
        return true;
    }

    public void onDestroy() {
        this.mHandler.removeCallbacksAndMessages(null);
        this.mSensorListenerThread.quit();
        int i = this.mSensorRegistered;
        if (i != 0) {
            unregister(i);
        }
    }

    public void register() {
        int i = this.mFocusSensorEnabled ? (this.mLinearAccelerometer == null && this.mGyroscope == null) ? 8 : 3 : 0;
        if (this.mGradienterEnabled) {
            i = i | 8 | getOrientationType();
        }
        if (this.mLieFlagEnabled) {
            i |= getOrientationType();
        }
        if (this.mRotationVectorFlagEnabled) {
            i |= 16;
        }
        if (this.mTTARFlagEnabled) {
            i = i | 8 | 2 | 64 | 32;
        }
        if (this.mGyroscopeEnabled) {
            i |= 2;
        }
        if (this.mMagneticFieldUncalibratedEnable) {
            i |= 256;
        }
        register(i, false);
    }

    public void register(int i, boolean z) {
        int i2 = i & SENSOR_ALL;
        this.isNeedFullGyroscope = z;
        if (!isContains(this.mSensorRegistered, i2)) {
            if (this.mThreadHandler == null && isPartialContains(i2, getOrientationType() | 8)) {
                HandlerThread handlerThread = this.mSensorListenerThread;
                if (handlerThread == null || !handlerThread.isAlive()) {
                    Log.w(this.TAG, "register: init mThreadHandler when mSensorListenerThread is quited, so return! | sensor = %d", Integer.valueOf(i2));
                } else {
                    this.mThreadHandler = new Handler(this.mSensorListenerThread.getLooper());
                }
            }
            if (this.mFocusSensorEnabled) {
                i2 = (this.mLinearAccelerometer == null && this.mGyroscope == null) ? i2 | 8 : i2 | 3;
                this.mHandler.removeMessages(2);
            }
            int i3 = this.mSensorRegistered;
            int i4 = (~i3) & i2;
            if (i4 == 0) {
                Log.w(this.TAG, "register fail, no sensor need register, mSensorRegistered = %d, registerList = %d", Integer.valueOf(i3), Integer.valueOf(i2));
                return;
            }
            if (DEBUG) {
                String str = this.TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("register ");
                sb.append(i4);
                sb.append(", ");
                sb.append(Util.getCallers(3));
                Log.d(str, sb.toString());
            }
            for (int i5 = 0; i5 < 11; i5++) {
                int i6 = 1 << i5;
                if (isContains(i4, i6)) {
                    Sensor sensor = (Sensor) this.mSensorMap.get(i6);
                    if (sensor == null) {
                        Log.w(this.TAG, "register fail, device does not have this sensor, sensorKey = %d,", Integer.valueOf(i6));
                    } else {
                        if (i6 == 2 || i6 == 1) {
                            this.mSensorManager.registerListener((SensorEventListener) this.mSensorEventListenerMap.get(i6), sensor, 2);
                        } else {
                            HandlerThread handlerThread2 = this.mSensorListenerThread;
                            if (handlerThread2 != null && handlerThread2.isAlive()) {
                                this.mSensorManager.registerListener((SensorEventListener) this.mSensorEventListenerMap.get(i6), sensor, this.mRate, this.mThreadHandler);
                            }
                        }
                        this.mSensorRegistered = i6 | this.mSensorRegistered;
                    }
                }
            }
        }
    }

    public void reset() {
        this.mHandler.removeMessages(1);
        this.mAngleTotal = 0.0d;
    }

    public void setFocusSensorEnabled(boolean z) {
        if (this.mFocusSensorEnabled != z) {
            this.mHandler.removeMessages(2);
            int i = (this.mLinearAccelerometer == null && this.mGyroscope == null) ? 8 : 3;
            this.mFocusSensorEnabled = z;
            Handler handler = this.mHandler;
            handler.sendMessageDelayed(handler.obtainMessage(2, i, z ? 1 : 0), 1000);
        }
    }

    public void setGradienterEnabled(boolean z) {
        if (this.mGradienterEnabled != z) {
            int orientationType = getOrientationType() | 8;
            this.mGradienterEnabled = z;
            update(orientationType, this.mGradienterEnabled);
        }
    }

    public void setGyroscopeEnabled(boolean z) {
        if (this.mGyroscopeEnabled != z) {
            this.mGyroscopeEnabled = z;
            update(2, this.mGyroscopeEnabled);
        }
    }

    public void setLieIndicatorEnabled(boolean z) {
        if (this.mLieFlagEnabled != z) {
            int orientationType = getOrientationType();
            this.mLieFlagEnabled = z;
            update(orientationType, this.mLieFlagEnabled);
        }
    }

    public void setMagneticFieldUncalibratedEnable(boolean z) {
        String str = this.TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("setMagneticFieldUncalibratedEnable:");
        sb.append(z);
        Log.d(str, sb.toString());
        if (this.mMagneticFieldUncalibratedEnable != z) {
            this.mMagneticFieldUncalibratedEnable = z;
            update(256, this.mMagneticFieldUncalibratedEnable);
        }
    }

    public void setRotationVectorEnabled(boolean z) {
        if (this.mRotationVectorFlagEnabled != z) {
            this.mRotationVectorFlagEnabled = z;
            update(16, this.mRotationVectorFlagEnabled);
        }
    }

    public synchronized void setSensorStateListener(SensorStateListener sensorStateListener) {
        this.mSensorStateListener = sensorStateListener;
    }

    public void setTTARSensorEnabled(boolean z) {
        if (this.mTTARFlagEnabled != z) {
            this.mTTARFlagEnabled = z;
            update(106, this.mTTARFlagEnabled);
        }
    }

    public void unregister(int i) {
        int i2 = i & SENSOR_ALL;
        this.isNeedFullGyroscope = false;
        if (this.mSensorRegistered == 0) {
            Log.d(this.TAG, "unregister fail, no sensor registered");
            return;
        }
        if (!this.mFocusSensorEnabled || i2 == 2047) {
            if (!this.mFocusSensorEnabled && this.mHandler.hasMessages(2)) {
                i2 = (this.mLinearAccelerometer == null && this.mGyroscope == null) ? i2 | 8 : i2 | 3;
            }
            reset();
            this.mHandler.removeMessages(2);
        }
        int i3 = this.mSensorRegistered;
        int i4 = i3 & i2;
        if (i4 == 0) {
            Log.d(this.TAG, "unregister fail, no sensor need unRegister, mSensorRegistered = %d, unRegisterList = %d", Integer.valueOf(i3), Integer.valueOf(i2));
            return;
        }
        if (DEBUG) {
            String str = this.TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("unregister ");
            sb.append(i4);
            sb.append(", ");
            sb.append(Util.getCallers(3));
            Log.d(str, sb.toString());
        }
        for (int i5 = 0; i5 < 11; i5++) {
            int i6 = 1 << i5;
            if (isContains(i4, i6)) {
                this.mSensorManager.unregisterListener((SensorEventListener) this.mSensorEventListenerMap.get(i6));
                this.mSensorRegistered &= ~i6;
                if (i6 == (this.mOrientationSensor == null ? 1024 : 4)) {
                    this.mIsLyingForGradienter = false;
                    this.mIsLyingForLie = false;
                    this.mAccelerometerValues = new float[3];
                    this.mMagneticFieldValues = new float[3];
                    changeCapturePosture(0);
                }
            }
        }
    }
}
