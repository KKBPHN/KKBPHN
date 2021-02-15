package com.android.camera2;

import android.hardware.camera2.params.StreamConfiguration;
import com.android.camera.CameraSize;
import java.util.List;
import java.util.function.Consumer;

/* compiled from: lambda */
public final /* synthetic */ class O000000o implements Consumer {
    private final /* synthetic */ List O0OOoO0;

    public /* synthetic */ O000000o(List list) {
        this.O0OOoO0 = list;
    }

    public final void accept(Object obj) {
        this.O0OOoO0.add(new CameraSize(((StreamConfiguration) obj).getSize()));
    }
}
