package com.miui.extravideo.interpolation;

import android.graphics.Bitmap;
import android.util.Log;
import com.miui.extravideo.BuildConfig;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class VideoInterpolator {
    private static final String TAG = "VideoInterpolator";

    public static void doDecodeAndEncodeAsync(int i, int i2, String str, String str2, boolean z, boolean z2, EncodeListener encodeListener) {
        doDecodeAndEncodeAsyncWithWatermark(i, i2, str, str2, z, null, null, z2, encodeListener);
    }

    public static void doDecodeAndEncodeAsync(String str, String str2, boolean z, boolean z2, EncodeListener encodeListener) {
        doDecodeAndEncodeAsyncWithWatermark(str, str2, z, null, null, z2, encodeListener);
    }

    public static void doDecodeAndEncodeAsyncWithWatermark(int i, int i2, String str, String str2, boolean z, Bitmap bitmap, float[] fArr, boolean z2, EncodeListener encodeListener) {
        Log.d(TAG, String.format("start doDecodeAndEncode async mode sdk version : %s", new Object[]{BuildConfig.VERSION_NAME}));
        VideoInterpolatorAsyncImp videoInterpolatorAsyncImp = new VideoInterpolatorAsyncImp(i, i2, str, str2, z, null, fArr, z2);
        videoInterpolatorAsyncImp.setEncodeListener(encodeListener);
        videoInterpolatorAsyncImp.doDecodeAndEncode();
    }

    public static void doDecodeAndEncodeAsyncWithWatermark(String str, String str2, boolean z, Bitmap bitmap, float[] fArr, boolean z2, EncodeListener encodeListener) {
        Log.d(TAG, String.format("start doDecodeAndEncode async mode sdk version : %s", new Object[]{BuildConfig.VERSION_NAME}));
        VideoInterpolatorAsyncImp videoInterpolatorAsyncImp = new VideoInterpolatorAsyncImp(str, str2, z, null, fArr, z2);
        videoInterpolatorAsyncImp.setEncodeListener(encodeListener);
        videoInterpolatorAsyncImp.doDecodeAndEncode();
    }

    public static boolean doDecodeAndEncodeSync(int i, int i2, String str, String str2, boolean z, boolean z2) {
        return doDecodeAndEncodeSyncWithWatermark(i, i2, str, str2, z, null, null, z2);
    }

    public static boolean doDecodeAndEncodeSync(String str, String str2) {
        return doDecodeAndEncodeSync(str, str2, true, true);
    }

    public static boolean doDecodeAndEncodeSync(String str, String str2, boolean z, boolean z2) {
        return doDecodeAndEncodeSyncWithWatermark(str, str2, z, null, null, z2);
    }

    public static boolean doDecodeAndEncodeSyncWithWatermark(int i, int i2, String str, String str2, boolean z, Bitmap bitmap, float[] fArr, boolean z2) {
        Log.d(TAG, String.format("start doDecodeAndEncode sync mode sdk version : %s", new Object[]{BuildConfig.VERSION_NAME}));
        final boolean[] zArr = new boolean[1];
        final ReentrantLock reentrantLock = new ReentrantLock();
        final Condition newCondition = reentrantLock.newCondition();
        VideoInterpolatorAsyncImp videoInterpolatorAsyncImp = new VideoInterpolatorAsyncImp(i, i2, str, str2, z, bitmap, fArr, z2);
        videoInterpolatorAsyncImp.setEncodeListener(new EncodeListener() {
            public void onEncodeFinish() {
                reentrantLock.lock();
                zArr[0] = true;
                try {
                    newCondition.signal();
                } finally {
                    reentrantLock.unlock();
                }
            }

            public void onError() {
                reentrantLock.lock();
                zArr[0] = false;
                try {
                    newCondition.signal();
                } finally {
                    reentrantLock.unlock();
                }
            }
        });
        reentrantLock.lock();
        try {
            videoInterpolatorAsyncImp.doDecodeAndEncode();
            newCondition.await();
        } catch (Exception e) {
            e.printStackTrace();
            zArr[0] = false;
        } catch (Throwable th) {
            reentrantLock.unlock();
            throw th;
        }
        reentrantLock.unlock();
        return zArr[0];
    }

    public static boolean doDecodeAndEncodeSyncWithWatermark(String str, String str2, boolean z, Bitmap bitmap, float[] fArr, boolean z2) {
        Log.d(TAG, String.format("start doDecodeAndEncode sync mode sdk version : %s", new Object[]{BuildConfig.VERSION_NAME}));
        final boolean[] zArr = new boolean[1];
        final ReentrantLock reentrantLock = new ReentrantLock();
        final Condition newCondition = reentrantLock.newCondition();
        VideoInterpolatorAsyncImp videoInterpolatorAsyncImp = new VideoInterpolatorAsyncImp(str, str2, z, bitmap, fArr, z2);
        videoInterpolatorAsyncImp.setEncodeListener(new EncodeListener() {
            public void onEncodeFinish() {
                reentrantLock.lock();
                zArr[0] = true;
                try {
                    newCondition.signal();
                } finally {
                    reentrantLock.unlock();
                }
            }

            public void onError() {
                reentrantLock.lock();
                zArr[0] = false;
                try {
                    newCondition.signal();
                } finally {
                    reentrantLock.unlock();
                }
            }
        });
        reentrantLock.lock();
        try {
            videoInterpolatorAsyncImp.doDecodeAndEncode();
            newCondition.await();
        } catch (Exception e) {
            e.printStackTrace();
            zArr[0] = false;
        } catch (Throwable th) {
            reentrantLock.unlock();
            throw th;
        }
        reentrantLock.unlock();
        return zArr[0];
    }
}
