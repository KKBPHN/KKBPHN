package com.android.camera.data.data.runing;

import com.android.camera.dualvideo.util.UserSelectData;
import java.util.function.Function;

/* compiled from: lambda */
public final /* synthetic */ class O000000o implements Function {
    public static final /* synthetic */ O000000o INSTANCE = new O000000o();

    private /* synthetic */ O000000o() {
    }

    public final Object apply(Object obj) {
        return Integer.valueOf(((UserSelectData) obj).getCameraId());
    }
}
