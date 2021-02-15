package defpackage;

import android.os.IBinder;
import android.os.Parcel;
import android.os.Parcelable;

/* renamed from: f reason: default package */
public final class f extends a implements g {
    public f(IBinder iBinder) {
        super(iBinder, "com.google.android.apps.gsa.publicsearch.IPublicSearchServiceSession");
    }

    public final void O000000o(byte[] bArr, k kVar) {
        Parcel a = a();
        a.writeByteArray(bArr);
        c.O000000o(a, (Parcelable) kVar);
        O000000o(2, a);
    }

    public final void a(byte[] bArr) {
        Parcel a = a();
        a.writeByteArray(bArr);
        O000000o(1, a);
    }
}
