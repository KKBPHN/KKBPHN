package com.android.camera.dualvideo.view;

import android.graphics.Rect;
import com.android.camera.protocol.ModeProtocol.DualVideoRenderProtocol;
import java.util.function.Consumer;

/* compiled from: lambda */
/* renamed from: com.android.camera.dualvideo.view.O0000OoO reason: case insensitive filesystem */
public final /* synthetic */ class C0247O0000OoO implements Consumer {
    private final /* synthetic */ int O0OOoO0;
    private final /* synthetic */ Rect O0OOoOO;

    public /* synthetic */ C0247O0000OoO(int i, Rect rect) {
        this.O0OOoO0 = i;
        this.O0OOoOO = rect;
    }

    public final void accept(Object obj) {
        ((DualVideoRenderProtocol) obj).getRenderManager().getCameraItemManager().getVisibleRenderList().stream().filter(new C0248O0000Ooo(this.O0OOoO0)).findAny().ifPresent(new O0000o00(this.O0OOoOO));
    }
}
