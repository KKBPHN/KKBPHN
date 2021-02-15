package com.android.camera2.vendortag;

import O00000Oo.O00000oO.O000000o.C0122O00000o;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraCharacteristics.Key;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.CaptureRequest.Builder;
import android.hardware.camera2.CaptureResult;
import android.util.Log;

public class VendorTagHelper {
    private static final int RETHROW = 51966;
    private static final int SWALLOW = 57005;
    private static final String TAG = "VendorTagHelper";
    private static final int WARNING = 47806;
    private static int sPreferredAction = -1;

    private static int getPreferredAction() {
        if (sPreferredAction == -1) {
            int i = (Log.isLoggable("VENDOR_TAG_NFE_RETHROW", 3) || !C0122O00000o.instance().OO0o00()) ? 47806 : 51966;
            sPreferredAction = i;
        }
        return sPreferredAction;
    }

    public static Object getValue(CameraCharacteristics cameraCharacteristics, VendorTag vendorTag) {
        return tryGetValue(cameraCharacteristics, vendorTag, getPreferredAction());
    }

    public static Object getValue(Builder builder, VendorTag vendorTag) {
        return tryGetValue(builder, vendorTag, getPreferredAction());
    }

    public static Object getValue(CaptureRequest captureRequest, VendorTag vendorTag) {
        return tryGetValue(captureRequest, vendorTag, getPreferredAction());
    }

    public static Object getValue(CaptureResult captureResult, VendorTag vendorTag) {
        return tryGetValue(captureResult, vendorTag, getPreferredAction());
    }

    public static Object getValueQuietly(CameraCharacteristics cameraCharacteristics, VendorTag vendorTag) {
        return tryGetValue(cameraCharacteristics, vendorTag, 57005);
    }

    public static Object getValueQuietly(Builder builder, VendorTag vendorTag, Object obj) {
        return tryGetValue(builder, vendorTag, 57005);
    }

    public static Object getValueQuietly(CaptureResult captureResult, VendorTag vendorTag) {
        return tryGetValue(captureResult, vendorTag, 57005);
    }

    public static Object getValueSafely(CameraCharacteristics cameraCharacteristics, VendorTag vendorTag) {
        return tryGetValue(cameraCharacteristics, vendorTag, 47806);
    }

    public static Object getValueSafely(Builder builder, VendorTag vendorTag) {
        return tryGetValue(builder, vendorTag, 47806);
    }

    public static Object getValueSafely(CaptureRequest captureRequest, VendorTag vendorTag) {
        return tryGetValue(captureRequest, vendorTag, 47806);
    }

    public static Object getValueSafely(CaptureResult captureResult, VendorTag vendorTag) {
        return tryGetValue(captureResult, vendorTag, 47806);
    }

    public static void setValue(Builder builder, VendorTag vendorTag, Object obj) {
        trySetValue(builder, vendorTag, obj, getPreferredAction());
    }

    public static void setValueQuietly(Builder builder, VendorTag vendorTag, Object obj) {
        trySetValue(builder, vendorTag, obj, 57005);
    }

    public static void setValueSafely(Builder builder, VendorTag vendorTag, Object obj) {
        trySetValue(builder, vendorTag, obj, 47806);
    }

    private static Object tryGetValue(CameraCharacteristics cameraCharacteristics, VendorTag vendorTag, int i) {
        String str = TAG;
        if (cameraCharacteristics == null || vendorTag == null || vendorTag.getKey() == null) {
            com.android.camera.log.Log.w(str, "caution: failed to try get value from camera characteristics: <NULL>");
            return null;
        }
        try {
            return cameraCharacteristics.get((Key) vendorTag.getKey());
        } catch (Exception e) {
            if (i != 51966) {
                if (i == 47806) {
                    StringBuilder sb = new StringBuilder();
                    sb.append("caution: failed to try get value from camera characteristics: <VTNF>, ");
                    sb.append(vendorTag.getName());
                    com.android.camera.log.Log.w(str, sb.toString());
                }
                return null;
            }
            throw new VendorTagNotFoundException((Throwable) e);
        }
    }

    private static Object tryGetValue(Builder builder, VendorTag vendorTag, int i) {
        String str = TAG;
        if (builder == null || vendorTag == null || vendorTag.getKey() == null) {
            com.android.camera.log.Log.w(str, "caution: failed to try get value from capture request: <NULL>");
            return null;
        }
        try {
            return builder.get((CaptureRequest.Key) vendorTag.getKey());
        } catch (Exception e) {
            if (i != 51966) {
                if (i == 47806) {
                    StringBuilder sb = new StringBuilder();
                    sb.append("caution: failed to try get value from capture request: <VTNF>, ");
                    sb.append(vendorTag.getName());
                    com.android.camera.log.Log.w(str, sb.toString());
                }
                return null;
            }
            throw new VendorTagNotFoundException((Throwable) e);
        }
    }

    private static Object tryGetValue(CaptureRequest captureRequest, VendorTag vendorTag, int i) {
        String str = TAG;
        if (captureRequest == null || vendorTag == null || vendorTag.getKey() == null) {
            com.android.camera.log.Log.w(str, "caution: failed to try get value from capture request: <NULL>");
            return null;
        }
        try {
            return captureRequest.get((CaptureRequest.Key) vendorTag.getKey());
        } catch (Exception e) {
            if (i != 51966) {
                if (i == 47806) {
                    StringBuilder sb = new StringBuilder();
                    sb.append("caution: failed to try get value from capture request: <VTNF>, ");
                    sb.append(vendorTag.getName());
                    com.android.camera.log.Log.w(str, sb.toString());
                }
                return null;
            }
            throw new VendorTagNotFoundException((Throwable) e);
        }
    }

    private static Object tryGetValue(CaptureResult captureResult, VendorTag vendorTag, int i) {
        String str = TAG;
        if (captureResult == null || vendorTag == null || vendorTag.getKey() == null) {
            com.android.camera.log.Log.w(str, "caution: failed to try get value from capture result: <NULL>");
            return null;
        }
        try {
            return captureResult.get((CaptureResult.Key) vendorTag.getKey());
        } catch (Exception e) {
            if (i != 51966) {
                if (i == 47806) {
                    StringBuilder sb = new StringBuilder();
                    sb.append("caution: failed to try get value from capture result: <VTNF>, ");
                    sb.append(vendorTag.getName());
                    com.android.camera.log.Log.w(str, sb.toString());
                }
                return null;
            }
            throw new VendorTagNotFoundException((Throwable) e);
        }
    }

    private static void trySetValue(Builder builder, VendorTag vendorTag, Object obj, int i) {
        String str = TAG;
        if (builder == null || vendorTag == null || vendorTag.getKey() == null) {
            com.android.camera.log.Log.w(str, "caution: failed to update capture request: <NULL>");
            return;
        }
        try {
            builder.set((CaptureRequest.Key) vendorTag.getKey(), obj);
        } catch (Exception e) {
            if (i == 51966) {
                throw new VendorTagNotFoundException((Throwable) e);
            } else if (i == 47806) {
                StringBuilder sb = new StringBuilder();
                sb.append("caution: failed to update capture request: <VTNF>, ");
                sb.append(vendorTag.getName());
                com.android.camera.log.Log.w(str, sb.toString());
            }
        }
    }
}
