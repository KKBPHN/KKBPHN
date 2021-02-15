package miui.hybrid;

import android.app.Activity;
import com.miui.internal.hybrid.HybridManager;

public class NativeInterface {
    private HybridManager mManager;

    public NativeInterface(HybridManager hybridManager) {
        this.mManager = hybridManager;
    }

    public void addLifecycleListener(LifecycleListener lifecycleListener) {
        this.mManager.addLifecycleListener(lifecycleListener);
    }

    public Activity getActivity() {
        return this.mManager.getActivity();
    }

    public void removeLifecycleListener(LifecycleListener lifecycleListener) {
        this.mManager.removeLifecycleListener(lifecycleListener);
    }
}
