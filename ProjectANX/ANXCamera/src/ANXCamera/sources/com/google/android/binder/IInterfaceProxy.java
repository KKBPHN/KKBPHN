package com.google.android.binder;

import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import androidx.annotation.RestrictTo;
import androidx.annotation.RestrictTo.Scope;

@RestrictTo({Scope.LIBRARY_GROUP})
public class IInterfaceProxy implements IInterface {
    private final String mDescriptor;
    private final IBinder mRemote;

    protected IInterfaceProxy(IBinder iBinder, String str) {
        this.mRemote = iBinder;
        this.mDescriptor = str;
    }

    public IBinder asBinder() {
        return this.mRemote;
    }

    /* access modifiers changed from: protected */
    public final Parcel obtainData() {
        Parcel obtain = Parcel.obtain();
        obtain.writeInterfaceToken(this.mDescriptor);
        return obtain;
    }

    /* access modifiers changed from: protected */
    public final void transact(int i, Parcel parcel) {
        Parcel obtain = Parcel.obtain();
        try {
            this.mRemote.transact(i, parcel, obtain, 1);
            obtain.readException();
        } finally {
            obtain.recycle();
            parcel.recycle();
        }
    }
}
