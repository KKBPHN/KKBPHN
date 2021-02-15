package com.ss.android.vesdk;

import androidx.annotation.Keep;
import com.ss.android.vesdk.VEListener.VEEditorEffectListener;

@Keep
public class TEEffectCallback {
    private VEEditorEffectListener listener;

    public void onDone(int i, boolean z) {
        this.listener.onDone(i, z);
    }

    public void setListener(Object obj) {
        this.listener = (VEEditorEffectListener) obj;
    }
}
