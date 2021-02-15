package com.android.camera.dualvideo.render;

import android.content.res.Resources;
import com.android.camera.dualvideo.util.DualVideoConfigManager.ConfigItem;
import java.util.function.Consumer;

/* compiled from: lambda */
/* renamed from: com.android.camera.dualvideo.render.O000OOoO reason: case insensitive filesystem */
public final /* synthetic */ class C0193O000OOoO implements Consumer {
    private final /* synthetic */ MiscTextureManager O0OOoO0;
    private final /* synthetic */ Resources O0OOoOO;

    public /* synthetic */ C0193O000OOoO(MiscTextureManager miscTextureManager, Resources resources) {
        this.O0OOoO0 = miscTextureManager;
        this.O0OOoOO = resources;
    }

    public final void accept(Object obj) {
        this.O0OOoO0.O000000o(this.O0OOoOO, (ConfigItem) obj);
    }
}
