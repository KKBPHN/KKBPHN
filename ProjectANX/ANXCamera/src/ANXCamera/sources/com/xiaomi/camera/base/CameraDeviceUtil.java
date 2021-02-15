package com.xiaomi.camera.base;

import android.content.Context;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraCharacteristics.Key;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.CaptureResult;
import android.os.Parcelable;
import com.android.camera.log.Log;
import com.xiaomi.protocol.ICustomCaptureResult;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public final class CameraDeviceUtil {
    private static final String TAG = "CameraDeviceUtil";

    private CameraDeviceUtil() {
    }

    public static int getCameraCombinationMode(int i) {
        if (i == 0) {
            return 1;
        }
        if (i == 1) {
            return 17;
        }
        if (i != 2) {
            if (i != 3) {
                if (i != 4) {
                    if (i == 40) {
                        return 18;
                    }
                    if (i == 80) {
                        return 515;
                    }
                    if (i == 81) {
                        return 771;
                    }
                    switch (i) {
                        case 20:
                            break;
                        case 21:
                            return 3;
                        case 22:
                        case 24:
                            return 4;
                        case 23:
                            return 5;
                        default:
                            switch (i) {
                                case 60:
                                    break;
                                case 61:
                                    break;
                                case 62:
                                    return 514;
                                case 63:
                                    return 770;
                                default:
                                    return 0;
                            }
                    }
                }
                return 769;
            }
            return 513;
        }
        return 2;
    }

    public static ICustomCaptureResult getCustomCaptureResult(CaptureResult captureResult) {
        Parcelable nativeMetadata = getNativeMetadata(captureResult);
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("getCustomCaptureResult: resultCameraMetadataNative =");
        sb.append(nativeMetadata);
        Log.d(str, sb.toString());
        try {
            Method method = CaptureRequest.class.getMethod("getNativeCopy", new Class[0]);
            method.setAccessible(true);
            Parcelable parcelable = (Parcelable) method.invoke(captureResult.getRequest(), new Object[0]);
            String str2 = TAG;
            StringBuilder sb2 = new StringBuilder();
            sb2.append("getCustomCaptureResult: requestNativeMetadata =");
            sb2.append(parcelable);
            Log.d(str2, sb2.toString());
            ICustomCaptureResult iCustomCaptureResult = new ICustomCaptureResult();
            iCustomCaptureResult.setFrameNumber(captureResult.getFrameNumber());
            iCustomCaptureResult.setRequest(captureResult.getRequest());
            iCustomCaptureResult.setParcelRequest(parcelable);
            iCustomCaptureResult.setSequenceId(captureResult.getSequenceId());
            iCustomCaptureResult.setResults(nativeMetadata);
            Long l = (Long) captureResult.get(CaptureResult.SENSOR_TIMESTAMP);
            if (l != null) {
                iCustomCaptureResult.setTimeStamp(l.longValue());
            }
            String str3 = TAG;
            StringBuilder sb3 = new StringBuilder();
            sb3.append("getCustomCaptureResult: ");
            sb3.append(iCustomCaptureResult);
            Log.d(str3, sb3.toString());
            return iCustomCaptureResult;
        } catch (Exception e) {
            Log.e(TAG, "getCustomCaptureResult: getCustomCaptureResult", (Throwable) e);
            return null;
        }
    }

    public static Parcelable getNativeMetadata(CaptureResult captureResult) {
        try {
            Method method = CaptureResult.class.getMethod("getNativeCopy", new Class[0]);
            method.setAccessible(true);
            return (Parcelable) method.invoke(captureResult, new Object[0]);
        } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            Log.e(TAG, e.getMessage());
            return null;
        }
    }

    public static void prepareCalibrationDataForAlgo(Context context, String str) {
        try {
            CameraCharacteristics cameraCharacteristics = ((CameraManager) context.getSystemService("camera")).getCameraCharacteristics(str);
            Integer num = (Integer) cameraCharacteristics.get(CameraCharacteristics.LENS_FACING);
            if (num != null) {
                boolean z = num.intValue() == 0;
                try {
                    byte[] bArr = (byte[]) cameraCharacteristics.get((Key) Class.forName("android.hardware.camera2.CameraCharacteristics$Key").getDeclaredConstructor(new Class[]{String.class, Class.class}).newInstance(new Object[]{"com.xiaomi.camera.algoup.dualCalibrationData", byte[].class}));
                    if (bArr != null) {
                        CommonUtil.saveCameraCalibrationToFile(context, bArr, z);
                    }
                } catch (ClassNotFoundException | IllegalAccessException | InstantiationException | NoSuchMethodException | InvocationTargetException e) {
                    Log.e(TAG, "prepareCalibrationDataForAlgo: call reflect method failed!", e);
                    throw new RuntimeException("getCameraCharacteristics's dualCalibrationData failed");
                }
            }
        } catch (CameraAccessException e2) {
            Log.e(TAG, "prepareCalibrationDataForAlgo: get getCameraCharacteristics failed!", (Throwable) e2);
        }
    }
}
