package defpackage;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;

/* renamed from: b reason: default package */
public class b extends Binder implements IInterface {
    protected b(String str) {
        attachInterface(this, str);
    }

    /* access modifiers changed from: protected */
    public boolean O000000o(int i, Parcel parcel) {
        throw null;
    }

    public final IBinder asBinder() {
        return this;
    }

    public final boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) {
        if (i <= 16777215) {
            parcel.enforceInterface(getInterfaceDescriptor());
        } else if (super.onTransact(i, parcel, parcel2, i2)) {
            return true;
        }
        return O000000o(i, parcel);
    }
}
