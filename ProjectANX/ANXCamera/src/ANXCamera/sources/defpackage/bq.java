package defpackage;

import com.google.lens.sdk.LensApi.LensAvailabilityCallback;
import com.google.lens.sdk.LensApi.LensFeature;

/* renamed from: bq reason: default package */
public final class bq implements ai {
    private final LensAvailabilityCallback a;
    @LensFeature
    private final int b;

    public bq(LensAvailabilityCallback lensAvailabilityCallback, @LensFeature int i) {
        this.a = lensAvailabilityCallback;
        this.b = i;
    }

    public final void O000000o(bi biVar) {
        int i;
        if (this.b != 0) {
            int a2 = bh.a(biVar.e);
            if (a2 == 0) {
                a2 = bh.a;
            }
            i = a2 - 2;
            if (a2 == 0) {
                throw null;
            }
        } else {
            int a3 = bh.a(biVar.d);
            if (a3 == 0) {
                a3 = bh.a;
            }
            i = a3 - 2;
            if (a3 == 0) {
                throw null;
            }
        }
        this.a.onAvailabilityStatusFetched(i);
    }
}
