package defpackage;

import android.os.Parcel;

/* renamed from: h reason: default package */
public abstract class h extends b implements i {
    public h() {
        super("com.google.android.apps.gsa.publicsearch.IPublicSearchServiceSessionCallback");
    }

    /* access modifiers changed from: protected */
    public final boolean O000000o(int i, Parcel parcel) {
        if (i != 1) {
            return false;
        }
        O000000o(parcel.createByteArray(), (k) c.O000000o(parcel, k.CREATOR));
        return true;
    }
}
