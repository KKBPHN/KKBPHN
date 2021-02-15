package com.android.camera.module.loader;

import com.android.camera.module.Camera2Module;
import io.reactivex.functions.Predicate;
import java.lang.ref.WeakReference;

public class PredicateFilterAiScene implements Predicate {
    private static final String TAG = "PredicateFilterAiScene";
    private int mCurrentDetectedScene;
    private boolean mIsSupportIDCardMode;
    private long mLastChangeSceneTime = 0;
    private WeakReference mModuleWeakReference;

    public PredicateFilterAiScene(Camera2Module camera2Module, boolean z) {
        this.mModuleWeakReference = new WeakReference(camera2Module);
        this.mIsSupportIDCardMode = z;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:20:0x0059, code lost:
        if ((java.lang.System.currentTimeMillis() - r8.mLastChangeSceneTime) > 300) goto L_0x0038;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean test(Integer num) {
        Camera2Module camera2Module = (Camera2Module) this.mModuleWeakReference.get();
        if (camera2Module != null && !camera2Module.isDoingAction() && camera2Module.isAlive()) {
            if (this.mCurrentDetectedScene == num.intValue() || this.mCurrentDetectedScene != 38 || !this.mIsSupportIDCardMode) {
                if (this.mCurrentDetectedScene != num.intValue()) {
                }
            } else if (System.currentTimeMillis() - this.mLastChangeSceneTime <= 3000) {
                return false;
            }
            this.mCurrentDetectedScene = num.intValue();
            this.mLastChangeSceneTime = System.currentTimeMillis();
            return true;
        }
        return false;
    }
}
