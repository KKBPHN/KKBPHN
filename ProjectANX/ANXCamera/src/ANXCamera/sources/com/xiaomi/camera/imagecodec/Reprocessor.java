package com.xiaomi.camera.imagecodec;

import android.content.Context;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import com.xiaomi.camera.isp.IspInterfaceIO;
import java.util.HashMap;

public interface Reprocessor {
    public static final int KEY_MAX_JPEG_SIZE = 102;
    public static final int KEY_MFNR_SUPPORTED = 101;
    public static final int KEY_YUV_TUNING_BUFFER_SIZE = 103;

    public abstract class Singleton {
        private Object mInstance;

        public abstract Object create();

        public final Object get() {
            Object obj;
            synchronized (this) {
                if (this.mInstance == null) {
                    this.mInstance = create();
                }
                obj = this.mInstance;
            }
            return obj;
        }
    }

    void customize(HashMap hashMap);

    void deInit();

    void init(Context context);

    boolean isMFNRSupported();

    FeatureSetting queryFeatureSetting(@NonNull IspInterfaceIO ispInterfaceIO, @NonNull Parcelable parcelable, @NonNull QueryFeatureSettingParameter queryFeatureSettingParameter, boolean z);

    void setOutputPictureSpec(int i, int i2, int i3);

    void setVirtualCameraIds(String str, String str2);

    void submit(ReprocessData reprocessData);
}
