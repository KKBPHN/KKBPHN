package defpackage;

import android.app.KeyguardManager.KeyguardDismissCallback;
import android.util.Log;
import com.google.lens.sdk.LensApi.LensLaunchStatusCallback;

/* renamed from: bp reason: default package */
public final class bp extends KeyguardDismissCallback {
    final /* synthetic */ Runnable a;
    final /* synthetic */ LensLaunchStatusCallback b;

    public bp(Runnable runnable, LensLaunchStatusCallback lensLaunchStatusCallback) {
        this.a = runnable;
        this.b = lensLaunchStatusCallback;
    }

    public final void onDismissCancelled() {
        LensLaunchStatusCallback lensLaunchStatusCallback = this.b;
        if (lensLaunchStatusCallback != null) {
            lensLaunchStatusCallback.onLaunchStatusFetched(1);
        }
    }

    public final void onDismissError() {
        LensLaunchStatusCallback lensLaunchStatusCallback = this.b;
        if (lensLaunchStatusCallback != null) {
            lensLaunchStatusCallback.onLaunchStatusFetched(1);
        }
        Log.e("LensApi", "Error dismissing keyguard");
    }

    public final void onDismissSucceeded() {
        this.a.run();
        LensLaunchStatusCallback lensLaunchStatusCallback = this.b;
        if (lensLaunchStatusCallback != null) {
            lensLaunchStatusCallback.onLaunchStatusFetched(0);
        }
    }
}
