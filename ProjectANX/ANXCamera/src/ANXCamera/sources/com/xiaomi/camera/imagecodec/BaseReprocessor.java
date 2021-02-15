package com.xiaomi.camera.imagecodec;

import android.media.Image;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.util.Log;
import com.xiaomi.camera.isp.IspInterfaceIO;
import java.util.HashMap;

public abstract class BaseReprocessor implements Reprocessor {
    protected boolean mIsMFNRSupported;

    public void customize(HashMap hashMap) {
        Log.w(getClass().getSimpleName(), "customize");
    }

    public boolean isMFNRSupported() {
        return this.mIsMFNRSupported;
    }

    public FeatureSetting queryFeatureSetting(@NonNull IspInterfaceIO ispInterfaceIO, @NonNull Parcelable parcelable, @NonNull QueryFeatureSettingParameter queryFeatureSettingParameter, boolean z) {
        Log.w(getClass().getSimpleName(), "queryFeatureSetting");
        return null;
    }

    /* access modifiers changed from: protected */
    public Image queueImageToPool(Image image) {
        long timestamp = image.getTimestamp();
        ImagePool.getInstance().queueImage(image);
        Image image2 = ImagePool.getInstance().getImage(timestamp);
        ImagePool.getInstance().holdImage(image2);
        String simpleName = getClass().getSimpleName();
        StringBuilder sb = new StringBuilder();
        sb.append("queueImageToPool: ");
        sb.append(image2);
        sb.append(" | ");
        sb.append(timestamp);
        Log.d(simpleName, sb.toString());
        return image2;
    }

    public void setVirtualCameraIds(@NonNull String str, @NonNull String str2) {
    }
}
