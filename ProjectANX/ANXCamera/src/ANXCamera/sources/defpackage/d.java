package defpackage;

import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;

/* renamed from: d reason: default package */
public final class d extends a implements e {
    public d(IBinder iBinder) {
        super(iBinder, "com.google.android.apps.gsa.publicsearch.IPublicSearchService");
    }

    public final g O000000o(String str, i iVar) {
        Parcel a = a();
        a.writeString(str);
        c.O000000o(a, (IInterface) iVar);
        g gVar = null;
        a.writeByteArray(null);
        a = Parcel.obtain();
        try {
            this.a.transact(1, a, a, 0);
            a.readException();
            a.recycle();
            IBinder readStrongBinder = a.readStrongBinder();
            if (readStrongBinder != null) {
                IInterface queryLocalInterface = readStrongBinder.queryLocalInterface("com.google.android.apps.gsa.publicsearch.IPublicSearchServiceSession");
                gVar = queryLocalInterface instanceof g ? (g) queryLocalInterface : new f(readStrongBinder);
            }
            return gVar;
        } catch (RuntimeException e) {
            throw e;
        } finally {
            a.recycle();
        }
    }
}
