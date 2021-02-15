package com.android.camera.panorama;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import com.android.camera.log.Log;
import com.android.camera.panorama.MorphoSensorFusion.SensorData;
import java.util.ArrayList;
import java.util.Locale;

public class SensorFusion implements SensorEventListener {
    public static final int GYRO_CALIBRATED = 0;
    public static final int GYRO_UNCALIBRATED = 1;
    private static final String LOG_TAG = "SensorFusion";
    private static final int MAX_DATA_NUM = 512;
    public static final int MODE_USE_ACCELEROMETER_AND_MAGNETIC_FIELD = 3;
    public static final int MODE_USE_ALL_SENSORS = 0;
    public static final int MODE_USE_GYROSCOPE = 1;
    public static final int MODE_USE_GYROSCOPE_AND_ROTATION_VECTOR = 4;
    public static final int MODE_USE_GYROSCOPE_WITH_ACCELEROMETER = 2;
    public static final int OFFSET_MODE_DYNAMIC = 1;
    public static final int OFFSET_MODE_STATIC = 0;
    public static final int ROTATE_0 = 0;
    public static final int ROTATE_180 = 2;
    public static final int ROTATE_270 = 3;
    public static final int ROTATE_90 = 1;
    public static final int SENSOR_TYPE_ACCELEROMETER = 1;
    public static final int SENSOR_TYPE_GYROSCOPE = 0;
    public static final int SENSOR_TYPE_MAGNETIC_FIELD = 2;
    public static final int SENSOR_TYPE_NUM = 4;
    public static final int SENSOR_TYPE_ROTATION_VECTOR = 3;
    public static final int STATE_CALC_OFFSET = 0;
    public static final int STATE_PROCESS = 1;
    public static final Object SensorSynchronizedObject = new Object();
    private int camera_rotation = 1;
    private ArrayList mAllValueList;
    private int mGyroCalibratedMode = 0;
    private int mMode;
    private MorphoSensorFusion mMorphoSensorFusion;
    private final ArrayList mPartOfAccelerometerList = new ArrayList();
    private final ArrayList mPartOfGyroscopeList = new ArrayList();
    private final ArrayList mPartOfGyroscopeUncalibratedList = new ArrayList();
    private final ArrayList mPartOfMagneticFieldList = new ArrayList();
    private final ArrayList mPartOfRotationVectorList = new ArrayList();
    private final double[][] mSensorMatrix;
    private final boolean mStock;

    public SensorFusion(boolean z) {
        this.mStock = z;
        if (this.mStock) {
            this.mAllValueList = new ArrayList();
            for (int i = 0; i < 4; i++) {
                this.mAllValueList.add(new ArrayList());
            }
        }
        this.mSensorMatrix = new double[4][];
        int i2 = 0;
        while (true) {
            double[][] dArr = this.mSensorMatrix;
            if (i2 >= dArr.length) {
                break;
            }
            dArr[i2] = createMatrix();
            i2++;
        }
        this.mMorphoSensorFusion = new MorphoSensorFusion();
        int initialize = this.mMorphoSensorFusion.initialize();
        if (initialize != 0) {
            Log.e(LOG_TAG, String.format(Locale.US, "MorphoSensorFusion.initialize error ret:0x%08X", new Object[]{Integer.valueOf(initialize)}));
        }
    }

    private void calcRotationMatrix(double[] dArr, double d, double d2, double d3) {
        double[] createMatrix = createMatrix();
        double[] createMatrix2 = createMatrix();
        double[] createMatrix3 = createMatrix();
        double[] createMatrix4 = createMatrix();
        createMatrix[4] = Math.cos(d2);
        createMatrix[5] = -Math.sin(d2);
        createMatrix[7] = Math.sin(d2);
        createMatrix[8] = Math.cos(d2);
        createMatrix2[0] = Math.cos(d);
        createMatrix2[2] = Math.sin(d);
        createMatrix2[6] = -Math.sin(d);
        createMatrix2[8] = Math.cos(d);
        createMatrix3[0] = Math.cos(d3);
        createMatrix3[1] = -Math.sin(d3);
        createMatrix3[3] = Math.sin(d3);
        createMatrix3[4] = Math.cos(d3);
        multMatrix(createMatrix4, createMatrix, createMatrix2);
        multMatrix(dArr, createMatrix4, createMatrix3);
    }

    private double[] createMatrix() {
        return new double[]{1.0d, 0.0d, 0.0d, 0.0d, 1.0d, 0.0d, 0.0d, 0.0d, 1.0d};
    }

    private Object[] getSensorDataArray(ArrayList arrayList) {
        int size = arrayList.size();
        Object[] objArr = new Object[size];
        for (int i = 0; i < size; i++) {
            objArr[i] = new SensorData(((SensorData) arrayList.get(i)).mTimeStamp, ((SensorData) arrayList.get(i)).mValues);
        }
        arrayList.clear();
        return objArr;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:22:0x002c, code lost:
        return r2;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:29:0x003f, code lost:
        return r2;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:36:0x0052, code lost:
        return r2;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:45:0x0069, code lost:
        return r2;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:52:0x007c, code lost:
        return r2;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:59:0x008b, code lost:
        return r2;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:64:0x0096, code lost:
        return r2;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:73:0x00b1, code lost:
        return r2;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private boolean isUpdateSensorMatrix() {
        synchronized (SensorSynchronizedObject) {
            int i = this.mMode;
            boolean z = false;
            if (i != 0) {
                if (i != 1) {
                    if (i != 2) {
                        if (i != 3) {
                            if (i != 4) {
                                return false;
                            }
                            if (this.mGyroCalibratedMode == 0) {
                                if (!this.mPartOfGyroscopeList.isEmpty() && !this.mPartOfRotationVectorList.isEmpty()) {
                                    z = true;
                                }
                            } else if (!this.mPartOfGyroscopeUncalibratedList.isEmpty() && !this.mPartOfRotationVectorList.isEmpty()) {
                                z = true;
                            }
                        } else if (!this.mPartOfAccelerometerList.isEmpty() && !this.mPartOfMagneticFieldList.isEmpty()) {
                            boolean z2 = true;
                        }
                    } else if (this.mGyroCalibratedMode == 0) {
                        if (!this.mPartOfGyroscopeList.isEmpty() && !this.mPartOfAccelerometerList.isEmpty()) {
                            boolean z3 = true;
                        }
                    } else if (!this.mPartOfGyroscopeUncalibratedList.isEmpty() && !this.mPartOfAccelerometerList.isEmpty()) {
                        boolean z4 = true;
                    }
                } else if (this.mGyroCalibratedMode == 0) {
                    if (!this.mPartOfGyroscopeList.isEmpty()) {
                        boolean z5 = true;
                    }
                } else if (!this.mPartOfGyroscopeUncalibratedList.isEmpty()) {
                    boolean z6 = true;
                }
            } else if (!this.mPartOfGyroscopeList.isEmpty() && !this.mPartOfAccelerometerList.isEmpty() && !this.mPartOfMagneticFieldList.isEmpty()) {
                boolean z7 = true;
            }
        }
    }

    private void multMatrix(double[] dArr, double[] dArr2, double[] dArr3) {
        double[] dArr4 = new double[9];
        for (int i = 0; i < 3; i++) {
            for (int i2 = 0; i2 < 3; i2++) {
                double d = 0.0d;
                for (int i3 = 0; i3 < 3; i3++) {
                    d += dArr2[(i * 3) + i3] * dArr3[(i3 * 3) + i2];
                }
                dArr4[(i * 3) + i2] = d;
            }
        }
        System.arraycopy(dArr4, 0, dArr, 0, dArr4.length);
    }

    private int setInputSensorData(Object[] objArr, int i) {
        if (objArr == null) {
            return -2147483647;
        }
        return this.mMorphoSensorFusion.setSensorData(objArr, i);
    }

    /* JADX WARNING: Removed duplicated region for block: B:28:0x00d5  */
    /* JADX WARNING: Removed duplicated region for block: B:33:0x00fa  */
    /* JADX WARNING: Removed duplicated region for block: B:38:0x011f  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private int updateSensorMatrix() {
        ArrayList arrayList;
        ArrayList arrayList2;
        ArrayList arrayList3;
        ArrayList arrayList4;
        ArrayList arrayList5;
        int i;
        Locale locale;
        Object[] objArr;
        synchronized (SensorSynchronizedObject) {
            arrayList = (ArrayList) this.mPartOfGyroscopeList.clone();
            arrayList2 = (ArrayList) this.mPartOfGyroscopeUncalibratedList.clone();
            arrayList3 = (ArrayList) this.mPartOfAccelerometerList.clone();
            arrayList4 = (ArrayList) this.mPartOfMagneticFieldList.clone();
            arrayList5 = (ArrayList) this.mPartOfRotationVectorList.clone();
            this.mPartOfGyroscopeList.clear();
            this.mPartOfGyroscopeUncalibratedList.clear();
            this.mPartOfAccelerometerList.clear();
            this.mPartOfMagneticFieldList.clear();
            this.mPartOfRotationVectorList.clear();
        }
        if (this.mStock) {
            if (this.mGyroCalibratedMode == 0) {
                ((ArrayList) this.mAllValueList.get(0)).addAll(arrayList);
            } else {
                ((ArrayList) this.mAllValueList.get(0)).addAll(arrayList2);
            }
            ((ArrayList) this.mAllValueList.get(1)).addAll(arrayList3);
            ((ArrayList) this.mAllValueList.get(2)).addAll(arrayList4);
            ((ArrayList) this.mAllValueList.get(3)).addAll(arrayList5);
        }
        if (this.mGyroCalibratedMode == 0) {
            if (!arrayList.isEmpty()) {
                i = setInputSensorData(getSensorDataArray(arrayList), 0);
                if (i != 0) {
                    locale = Locale.US;
                    objArr = new Object[]{Integer.valueOf(i)};
                }
                if (!arrayList3.isEmpty()) {
                    i = setInputSensorData(getSensorDataArray(arrayList3), 1);
                    if (i != 0) {
                        Log.e(LOG_TAG, String.format(Locale.US, "SensorFusion.setSensorData(SENSOR_TYPE_ACCELEROMETER) error ret:0x%08X", new Object[]{Integer.valueOf(i)}));
                    }
                }
                if (!arrayList4.isEmpty()) {
                    i = setInputSensorData(getSensorDataArray(arrayList4), 2);
                    if (i != 0) {
                        Log.e(LOG_TAG, String.format(Locale.US, "SensorFusion.setSensorData(SENSOR_TYPE_MAGNETIC_FIELD) error ret:0x%08X", new Object[]{Integer.valueOf(i)}));
                    }
                }
                if (!arrayList5.isEmpty()) {
                    i = setInputSensorData(getSensorDataArray(arrayList5), 3);
                    if (i != 0) {
                        Log.e(LOG_TAG, String.format(Locale.US, "SensorFusion.setSensorData(SENSOR_TYPE_ROTATION_VECTOR) error ret:0x%08X", new Object[]{Integer.valueOf(i)}));
                    }
                }
                return this.mMorphoSensorFusion.outputRotationMatrix3x3(3, this.mSensorMatrix[3]) | i | this.mMorphoSensorFusion.calc() | this.mMorphoSensorFusion.outputRotationMatrix3x3(1, this.mSensorMatrix[1]) | this.mMorphoSensorFusion.outputRotationMatrix3x3(0, this.mSensorMatrix[0]);
            }
            i = 0;
            if (!arrayList3.isEmpty()) {
            }
            if (!arrayList4.isEmpty()) {
            }
            if (!arrayList5.isEmpty()) {
            }
            return this.mMorphoSensorFusion.outputRotationMatrix3x3(3, this.mSensorMatrix[3]) | i | this.mMorphoSensorFusion.calc() | this.mMorphoSensorFusion.outputRotationMatrix3x3(1, this.mSensorMatrix[1]) | this.mMorphoSensorFusion.outputRotationMatrix3x3(0, this.mSensorMatrix[0]);
        }
        if (!arrayList2.isEmpty()) {
            i = setInputSensorData(getSensorDataArray(arrayList2), 0);
            if (i != 0) {
                locale = Locale.US;
                objArr = new Object[]{Integer.valueOf(i)};
            }
            if (!arrayList3.isEmpty()) {
            }
            if (!arrayList4.isEmpty()) {
            }
            if (!arrayList5.isEmpty()) {
            }
            return this.mMorphoSensorFusion.outputRotationMatrix3x3(3, this.mSensorMatrix[3]) | i | this.mMorphoSensorFusion.calc() | this.mMorphoSensorFusion.outputRotationMatrix3x3(1, this.mSensorMatrix[1]) | this.mMorphoSensorFusion.outputRotationMatrix3x3(0, this.mSensorMatrix[0]);
        }
        i = 0;
        if (!arrayList3.isEmpty()) {
        }
        if (!arrayList4.isEmpty()) {
        }
        if (!arrayList5.isEmpty()) {
        }
        return this.mMorphoSensorFusion.outputRotationMatrix3x3(3, this.mSensorMatrix[3]) | i | this.mMorphoSensorFusion.calc() | this.mMorphoSensorFusion.outputRotationMatrix3x3(1, this.mSensorMatrix[1]) | this.mMorphoSensorFusion.outputRotationMatrix3x3(0, this.mSensorMatrix[0]);
        Log.e(LOG_TAG, String.format(locale, "SensorFusion.setSensorData(SENSOR_TYPE_GYROSCOPE) error ret:0x%08X", objArr));
        if (!arrayList3.isEmpty()) {
        }
        if (!arrayList4.isEmpty()) {
        }
        if (!arrayList5.isEmpty()) {
        }
        return this.mMorphoSensorFusion.outputRotationMatrix3x3(3, this.mSensorMatrix[3]) | i | this.mMorphoSensorFusion.calc() | this.mMorphoSensorFusion.outputRotationMatrix3x3(1, this.mSensorMatrix[1]) | this.mMorphoSensorFusion.outputRotationMatrix3x3(0, this.mSensorMatrix[0]);
    }

    public void clearStockData() {
        synchronized (this) {
            if (this.mStock) {
                for (int i = 0; i < this.mAllValueList.size(); i++) {
                    ((ArrayList) this.mAllValueList.get(i)).clear();
                }
            }
        }
    }

    public int getSensorMatrix(double[] dArr, double[] dArr2, double[] dArr3, int[] iArr) {
        int updateSensorMatrix;
        synchronized (this) {
            updateSensorMatrix = isUpdateSensorMatrix() ? updateSensorMatrix() | 0 : 0;
            if (dArr != null) {
                System.arraycopy(this.mSensorMatrix[0], 0, dArr, 0, this.mSensorMatrix[0].length);
            }
            if (dArr2 != null) {
                System.arraycopy(this.mSensorMatrix[3], 0, dArr2, 0, this.mSensorMatrix[3].length);
            }
            if (dArr3 != null) {
                System.arraycopy(this.mSensorMatrix[1], 0, dArr3, 0, this.mSensorMatrix[1].length);
            }
            if (this.mStock && iArr != null && iArr.length == this.mAllValueList.size()) {
                for (int i = 0; i < this.mAllValueList.size(); i++) {
                    iArr[i] = ((ArrayList) this.mAllValueList.get(i)).size() - 1;
                }
            }
        }
        return updateSensorMatrix;
    }

    public ArrayList getStockData() {
        ArrayList arrayList;
        if (!this.mStock) {
            return new ArrayList();
        }
        synchronized (this) {
            arrayList = this.mAllValueList;
        }
        return arrayList;
    }

    public void onAccuracyChanged(Sensor sensor, int i) {
    }

    /* JADX WARNING: Removed duplicated region for block: B:28:0x006e A[LOOP:0: B:26:0x0064->B:28:0x006e, LOOP_END] */
    /* JADX WARNING: Removed duplicated region for block: B:31:0x007c A[LOOP:1: B:29:0x0074->B:31:0x007c, LOOP_END] */
    /* JADX WARNING: Removed duplicated region for block: B:34:0x008a A[LOOP:2: B:32:0x0082->B:34:0x008a, LOOP_END] */
    /* JADX WARNING: Removed duplicated region for block: B:37:0x0098 A[LOOP:3: B:35:0x0090->B:37:0x0098, LOOP_END] */
    /* JADX WARNING: Removed duplicated region for block: B:40:0x00a6 A[LOOP:4: B:38:0x009e->B:40:0x00a6, LOOP_END] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void onSensorChanged(SensorEvent sensorEvent) {
        ArrayList arrayList;
        synchronized (SensorSynchronizedObject) {
            SensorData sensorData = new SensorData(sensorEvent.timestamp, sensorEvent.values);
            int type = sensorEvent.sensor.getType();
            if (type == 1) {
                arrayList = this.mPartOfAccelerometerList;
            } else if (type == 2) {
                arrayList = this.mPartOfMagneticFieldList;
            } else if (type == 4) {
                if (this.camera_rotation == 3) {
                    sensorData.mValues[0] = -sensorData.mValues[0];
                    sensorData.mValues[1] = -sensorData.mValues[1];
                }
                arrayList = this.mPartOfGyroscopeList;
            } else if (type == 15) {
                arrayList = this.mPartOfRotationVectorList;
            } else if (type != 16) {
                while (this.mPartOfGyroscopeList.size() > 512) {
                    this.mPartOfGyroscopeList.remove(0);
                }
                while (this.mPartOfGyroscopeUncalibratedList.size() > 512) {
                    this.mPartOfGyroscopeUncalibratedList.remove(0);
                }
                while (this.mPartOfAccelerometerList.size() > 512) {
                    this.mPartOfAccelerometerList.remove(0);
                }
                while (this.mPartOfMagneticFieldList.size() > 512) {
                    this.mPartOfMagneticFieldList.remove(0);
                }
                while (this.mPartOfRotationVectorList.size() > 512) {
                    this.mPartOfRotationVectorList.remove(0);
                }
            } else {
                if (this.camera_rotation == 3) {
                    sensorData.mValues[0] = -sensorData.mValues[0];
                    sensorData.mValues[1] = -sensorData.mValues[1];
                }
                arrayList = this.mPartOfGyroscopeUncalibratedList;
            }
            arrayList.add(sensorData);
            while (this.mPartOfGyroscopeList.size() > 512) {
            }
            while (this.mPartOfGyroscopeUncalibratedList.size() > 512) {
            }
            while (this.mPartOfAccelerometerList.size() > 512) {
            }
            while (this.mPartOfMagneticFieldList.size() > 512) {
            }
            while (this.mPartOfRotationVectorList.size() > 512) {
            }
        }
    }

    public void release() {
        synchronized (this) {
            int finish = this.mMorphoSensorFusion.finish();
            if (finish != 0) {
                Log.e(LOG_TAG, String.format(Locale.US, "MorphoSensorFusion.finish error ret:0x%08X", new Object[]{Integer.valueOf(finish)}));
            }
            this.mMorphoSensorFusion = null;
        }
    }

    public void resetOffsetValue() {
        synchronized (this) {
            this.mMorphoSensorFusion.setAppState(1);
            this.mMorphoSensorFusion.calc();
        }
    }

    public int setAppState(int i) {
        int appState;
        synchronized (this) {
            appState = this.mMorphoSensorFusion.setAppState(i) | 0;
        }
        return appState;
    }

    public void setInitialOrientation(int i) {
        double radians = Math.toRadians((double) i);
        calcRotationMatrix(this.mSensorMatrix[0], 0.0d, 0.0d, radians);
        calcRotationMatrix(this.mSensorMatrix[3], 0.0d, 0.0d, radians);
        calcRotationMatrix(this.mSensorMatrix[1], 0.0d, 0.0d, radians);
    }

    public int setMode(int i) {
        int mode;
        synchronized (this) {
            this.mMode = i;
            mode = this.mMorphoSensorFusion.setMode(i) | 0;
        }
        return mode;
    }

    public int setOffset(SensorData sensorData, int i) {
        int offset;
        synchronized (this) {
            offset = this.mMode == 4 ? this.mMorphoSensorFusion.setOffset(sensorData, i) | 0 : -2147483646;
        }
        return offset;
    }

    public int setOffsetMode(int i) {
        int offsetMode;
        synchronized (this) {
            offsetMode = this.mMorphoSensorFusion.setOffsetMode(i) | 0;
        }
        return offsetMode;
    }

    public int setRotation(int i) {
        int rotation;
        this.camera_rotation = i;
        synchronized (this) {
            rotation = this.mMorphoSensorFusion.setRotation(i) | 0;
        }
        return rotation;
    }

    public int setUncalibratedMode() {
        synchronized (this) {
            this.mGyroCalibratedMode = 1;
        }
        return 0;
    }
}
