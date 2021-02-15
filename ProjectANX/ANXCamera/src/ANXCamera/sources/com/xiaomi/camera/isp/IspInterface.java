package com.xiaomi.camera.isp;

import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Surface;
import com.xiaomi.camera.imagecodec.FeatureSetting;
import com.xiaomi.camera.imagecodec.QueryFeatureSettingParameter;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Locale;

public class IspInterface {
    private static boolean DEBUG = false;
    private static final String TAG = "IspInterface";
    private final Object mContextLock = new Object();
    private ArrayList mInputStreamList;
    private long mNativeContext;
    private Surface mPicOutputSurface;
    private Parcelable mSettings;
    private Surface mTuningOutputSurface;
    private Surface mYuvOutputSurface;

    static {
        String str = "1";
        DEBUG = str.equals(System.getProperty("camera.debug.enable", str));
        try {
            System.loadLibrary("jni_ispinterface");
        } catch (UnsatisfiedLinkError e) {
            Log.e(TAG, "load library jni_ispinterface.so failed", e);
        }
        nativeClassInit();
    }

    private IspInterface(ArrayList arrayList, Surface surface, Surface surface2, Surface surface3, int i, Parcelable parcelable) {
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("ctor: E ");
        sb.append(hashCode());
        Log.d(str, sb.toString());
        this.mInputStreamList = arrayList;
        this.mPicOutputSurface = surface;
        this.mYuvOutputSurface = surface2;
        this.mTuningOutputSurface = surface3;
        this.mSettings = parcelable;
        nativeCreate(new WeakReference(this), this.mInputStreamList, this.mPicOutputSurface, this.mYuvOutputSurface, this.mTuningOutputSurface, i, this.mSettings);
        String str2 = TAG;
        StringBuilder sb2 = new StringBuilder();
        sb2.append("ctor: X ");
        sb2.append(hashCode());
        Log.d(str2, sb2.toString());
    }

    public static IspInterface create(@NonNull ArrayList arrayList, @Nullable Surface surface, @NonNull Surface surface2, @NonNull Surface surface3, int i, @NonNull Parcelable parcelable) {
        if (arrayList.size() != 0) {
            IspInterface ispInterface = new IspInterface(arrayList, surface, surface2, surface3, i, parcelable);
            return ispInterface;
        }
        throw new IllegalArgumentException("The inputStreamList is empty");
    }

    private static native void nativeClassInit();

    private native void nativeCreate(Object obj, ArrayList arrayList, Surface surface, Surface surface2, Surface surface3, int i, Parcelable parcelable);

    private native void nativeDestroy();

    private native FeatureSetting nativeQueryFeatureSetting(QueryFeatureSettingParameter queryFeatureSettingParameter);

    private native void nativeRawToJpeg(IspRequest ispRequest);

    private native void nativeRawToYuv(IspRequest ispRequest);

    private native void nativeSetFlipFlag(int i);

    private native void nativeYuvToJpeg(IspRequest ispRequest);

    private native void nativeYuvToYuv(IspRequest ispRequest);

    private static void postEventFromNative(Object obj) {
        Log.d(TAG, "postEventFromNative: E");
        if (((IspInterface) ((WeakReference) obj).get()) != null) {
            Log.d(TAG, "postEventFromNative: X");
        }
    }

    public long getInputStreamId(int i, int i2, int i3) {
        ArrayList arrayList = this.mInputStreamList;
        if (arrayList != null) {
            Iterator it = arrayList.iterator();
            while (it.hasNext()) {
                IspStream ispStream = (IspStream) it.next();
                if (i == ispStream.width && i2 == ispStream.height && i3 == ispStream.format) {
                    return ispStream.streamId;
                }
            }
        }
        if (this.mInputStreamList == null) {
            String str = "getInputStreamId: input stream has not been initialized";
            Log.e(TAG, str);
            if (DEBUG) {
                throw new RuntimeException(str);
            }
        } else {
            String format = String.format(Locale.ENGLISH, "getInputStreamId: no stream found with [width=%d height=%d format=%d]", new Object[]{Integer.valueOf(i), Integer.valueOf(i2), Integer.valueOf(i3)});
            Log.e(TAG, format);
            if (DEBUG) {
                throw new RuntimeException(format);
            }
        }
        return 0;
    }

    public FeatureSetting queryFeatureSetting(QueryFeatureSettingParameter queryFeatureSettingParameter) {
        return nativeQueryFeatureSetting(queryFeatureSettingParameter);
    }

    public void rawToJpeg(IspRequest ispRequest) {
        nativeRawToJpeg(ispRequest);
    }

    public void rawToYuv(IspRequest ispRequest) {
        nativeRawToYuv(ispRequest);
    }

    public void release() {
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("release: E ");
        sb.append(hashCode());
        Log.d(str, sb.toString());
        synchronized (this.mContextLock) {
            nativeDestroy();
        }
        String str2 = TAG;
        StringBuilder sb2 = new StringBuilder();
        sb2.append("release: X ");
        sb2.append(hashCode());
        Log.d(str2, sb2.toString());
    }

    public void setFlip(boolean z) {
        synchronized (this.mContextLock) {
            nativeSetFlipFlag(z ? 1 : 0);
        }
    }

    public void yuvToJpeg(IspRequest ispRequest) {
        nativeYuvToJpeg(ispRequest);
    }

    public void yuvToYuv(IspRequest ispRequest) {
        nativeYuvToYuv(ispRequest);
    }
}
