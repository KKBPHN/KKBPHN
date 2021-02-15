package defpackage;

import com.google.lens.sdk.LensApi;
import com.google.lens.sdk.LensApi.LensAvailabilityCallback;

/* renamed from: bn reason: default package */
public final /* synthetic */ class bn implements ap {
    private final LensAvailabilityCallback a;

    public bn(LensAvailabilityCallback lensAvailabilityCallback) {
        this.a = lensAvailabilityCallback;
    }

    public final void a(int i) {
        LensAvailabilityCallback lensAvailabilityCallback = this.a;
        int i2 = i - 2;
        int i3 = LensApi.b;
        if (i != 0) {
            lensAvailabilityCallback.onAvailabilityStatusFetched(i2);
            return;
        }
        throw null;
    }
}
