package com.android.camera.dualvideo.util;

import com.android.camera.dualvideo.util.DualVideoConfigManager.ConfigItem;
import java.util.function.Function;

/* compiled from: lambda */
/* renamed from: com.android.camera.dualvideo.util.O0000oO0 reason: case insensitive filesystem */
public final /* synthetic */ class C0244O0000oO0 implements Function {
    public static final /* synthetic */ C0244O0000oO0 INSTANCE = new C0244O0000oO0();

    private /* synthetic */ C0244O0000oO0() {
    }

    public final Object apply(Object obj) {
        return Float.valueOf(((ConfigItem) obj).mPresentZoom);
    }
}
