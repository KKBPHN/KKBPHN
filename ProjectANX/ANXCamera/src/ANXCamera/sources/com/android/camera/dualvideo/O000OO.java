package com.android.camera.dualvideo;

import android.view.MotionEvent;
import com.android.camera.dualvideo.render.RenderManager;
import java.util.function.Function;

/* compiled from: lambda */
public final /* synthetic */ class O000OO implements Function {
    private final /* synthetic */ MotionEvent O0OOoO0;

    public /* synthetic */ O000OO(MotionEvent motionEvent) {
        this.O0OOoO0 = motionEvent;
    }

    public final Object apply(Object obj) {
        return Boolean.valueOf(((RenderManager) obj).dispatchTouchEvent(this.O0OOoO0));
    }
}
