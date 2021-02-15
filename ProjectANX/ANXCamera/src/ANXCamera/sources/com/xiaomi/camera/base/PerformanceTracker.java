package com.xiaomi.camera.base;

import com.android.camera.log.Log;

public final class PerformanceTracker {
    public static final int END = 1;
    private static final String END_TAG = "[  END]";
    private static final String EVENT_ALGORITHM_PROCESS = "[AlgorithmProcess]";
    private static final String EVENT_CLEAR_SHOT = "[       ClearShot]";
    private static final String EVENT_IMAGE_SAVER = "[      ImageSaver]";
    private static final String EVENT_JPEG_REPROCESS = "[   JpegReprocess]";
    private static final String EVENT_PICTURE_CAPTURE = "[    PictureTaken]";
    public static final int START = 0;
    private static final String START_TAG = "[START]";
    private static final String TAG = "[PERFORMANCE]";
    private static final boolean isEnable = true;
    private static long mCaptureStartTimestamp;

    public static synchronized void calCaptureDuration(int i) {
        synchronized (PerformanceTracker.class) {
            if (i == 0) {
                try {
                    mCaptureStartTimestamp = System.currentTimeMillis();
                } catch (Throwable th) {
                    throw th;
                }
            } else if (i == 1) {
                String str = TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("[    PictureTaken] duration[");
                sb.append(System.currentTimeMillis() - mCaptureStartTimestamp);
                sb.append("]");
                Log.i(str, sb.toString());
            }
        }
    }

    public static synchronized void trackAlgorithmProcess(String str, int i) {
        String str2;
        String sb;
        synchronized (PerformanceTracker.class) {
            if (i == 0) {
                str2 = TAG;
                try {
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append("[AlgorithmProcess][START]");
                    sb2.append(str);
                    sb2.append("[");
                    sb2.append(System.currentTimeMillis());
                    sb2.append("]");
                    sb = sb2.toString();
                } catch (Throwable th) {
                    throw th;
                }
            } else if (i == 1) {
                str2 = TAG;
                StringBuilder sb3 = new StringBuilder();
                sb3.append("[AlgorithmProcess][  END]");
                sb3.append(str);
                sb3.append("[");
                sb3.append(System.currentTimeMillis());
                sb3.append("]");
                sb = sb3.toString();
            }
            Log.i(str2, sb);
        }
    }

    public static synchronized void trackClearShotProcess(String str, int i) {
        String str2;
        String sb;
        synchronized (PerformanceTracker.class) {
            if (i == 0) {
                str2 = TAG;
                try {
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append("[       ClearShot][START]");
                    sb2.append(str);
                    sb2.append("[");
                    sb2.append(System.currentTimeMillis());
                    sb2.append("]");
                    sb = sb2.toString();
                } catch (Throwable th) {
                    throw th;
                }
            } else if (i == 1) {
                str2 = TAG;
                StringBuilder sb3 = new StringBuilder();
                sb3.append("[       ClearShot][  END]");
                sb3.append(str);
                sb3.append("[");
                sb3.append(System.currentTimeMillis());
                sb3.append("]");
                sb = sb3.toString();
            }
            Log.i(str2, sb);
        }
    }

    public static synchronized void trackImageSaver(Object obj, int i) {
        String str;
        String sb;
        synchronized (PerformanceTracker.class) {
            if (i == 0) {
                str = TAG;
                try {
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append("[      ImageSaver][START][");
                    sb2.append(obj);
                    sb2.append("][");
                    sb2.append(System.currentTimeMillis());
                    sb2.append("]");
                    sb = sb2.toString();
                } catch (Throwable th) {
                    throw th;
                }
            } else if (i == 1) {
                str = TAG;
                StringBuilder sb3 = new StringBuilder();
                sb3.append("[      ImageSaver][  END][");
                sb3.append(obj);
                sb3.append("][");
                sb3.append(System.currentTimeMillis());
                sb3.append("]");
                sb = sb3.toString();
            }
            Log.i(str, sb);
        }
    }

    public static synchronized void trackJpegReprocess(int i, int i2) {
        String str;
        String sb;
        synchronized (PerformanceTracker.class) {
            String str2 = null;
            if (i == 0) {
                str2 = "[EFFECT]";
            } else if (i == 1) {
                str2 = "[   RAW]";
            } else if (i == 2) {
                str2 = "[ DEPTH]";
            }
            if (i2 == 0) {
                str = TAG;
                StringBuilder sb2 = new StringBuilder();
                sb2.append("[   JpegReprocess][START]");
                sb2.append(str2);
                sb2.append("[");
                sb2.append(System.currentTimeMillis());
                sb2.append("]");
                sb = sb2.toString();
            } else if (i2 == 1) {
                str = TAG;
                StringBuilder sb3 = new StringBuilder();
                sb3.append("[   JpegReprocess][  END]");
                sb3.append(str2);
                sb3.append("[");
                sb3.append(System.currentTimeMillis());
                sb3.append("]");
                sb = sb3.toString();
            }
            Log.i(str, sb);
        }
    }

    public static synchronized void trackPictureCapture(int i) {
        String str;
        String sb;
        synchronized (PerformanceTracker.class) {
            if (i == 0) {
                str = TAG;
                try {
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append("[    PictureTaken][START][");
                    sb2.append(System.currentTimeMillis());
                    sb2.append("]");
                    sb = sb2.toString();
                } catch (Throwable th) {
                    throw th;
                }
            } else if (i == 1) {
                str = TAG;
                StringBuilder sb3 = new StringBuilder();
                sb3.append("[    PictureTaken][  END][");
                sb3.append(System.currentTimeMillis());
                sb3.append("]");
                sb = sb3.toString();
            }
            Log.i(str, sb);
        }
    }
}
