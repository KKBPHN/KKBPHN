package com.xiaomi.camera.device.callable;

import androidx.annotation.NonNull;
import com.android.camera.log.Log;
import java.util.function.Consumer;

public class ShotBoostCallable extends CameraCallable {
    public static final String TAG = "ShotBoost";
    private Consumer mParam;
    private boolean mSuccess;

    public ShotBoostCallable(String str, boolean z, Consumer consumer) {
        super(str, null);
        this.mSuccess = z;
        this.mParam = consumer;
    }

    @NonNull
    public CallableReturn call() {
        return null;
    }

    /* access modifiers changed from: protected */
    public String getTag() {
        return TAG;
    }

    public void run() {
        if (this.mParam != null) {
            Log.d(TAG, "shotboost enable UI");
            this.mParam.accept(Boolean.valueOf(this.mSuccess));
        }
    }
}
